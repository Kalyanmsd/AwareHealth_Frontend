<?php
/**
 * Book Appointment - Create new appointment
 * POST: http://172.20.10.2/AwareHealth/api/book_appointment.php
 * Body: {
 *   "user_email": "patient@example.com",
 *   "doctor_id": 1,
 *   "appointment_date": "2024-12-20",
 *   "appointment_time": "10:00:00"
 * }
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Only allow POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use POST.'
    ]);
    exit();
}

require_once __DIR__ . '/../config.php';

// Connect to database
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit();
}

$conn->set_charset("utf8mb4");

// Get JSON input
$rawInput = file_get_contents('php://input');
$input = json_decode($rawInput, true);

// Log request for debugging
error_log("Book Appointment Request - Raw input: " . $rawInput);

// Validate JSON
if (json_last_error() !== JSON_ERROR_NONE) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid JSON: ' . json_last_error_msg()
    ]);
    exit();
}

// Validate required fields
$required = ['user_email', 'doctor_id', 'appointment_date', 'appointment_time'];
$missing = [];

foreach ($required as $field) {
    if (!isset($input[$field]) || empty(trim($input[$field]))) {
        $missing[] = $field;
    }
}

if (!empty($missing)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Missing required fields: ' . implode(', ', $missing)
    ]);
    exit();
}

// Sanitize and validate inputs
$userEmail = strtolower(trim($input['user_email']));
$doctorId = (int)$input['doctor_id'];
$appointmentDate = trim($input['appointment_date']);
$appointmentTime = trim($input['appointment_time']);

// Validate email format
if (!filter_var($userEmail, FILTER_VALIDATE_EMAIL)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid email format'
    ]);
    exit();
}

// Validate date format (YYYY-MM-DD)
if (!preg_match('/^\d{4}-\d{2}-\d{2}$/', $appointmentDate)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid date format. Use YYYY-MM-DD'
    ]);
    exit();
}

// Validate time format (HH:MM:SS or HH:MM)
if (!preg_match('/^\d{2}:\d{2}(:\d{2})?$/', $appointmentTime)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid time format. Use HH:MM or HH:MM:SS'
    ]);
    exit();
}

// Ensure time has seconds
if (strlen($appointmentTime) === 5) {
    $appointmentTime .= ':00';
}

// Check if appointments table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'appointments'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Appointments table does not exist. Please run the SQL script to create it.'
    ]);
    exit();
}

// Verify doctor exists (check status if column exists)
$checkStatusColumn = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
$hasStatusColumn = $checkStatusColumn && $checkStatusColumn->num_rows > 0;

if ($hasStatusColumn) {
    // Check if doctor exists and is available
    $doctorStmt = $conn->prepare("SELECT id, name, specialization, hospital FROM doctors WHERE id = ? AND status = 'Available'");
} else {
    // No status column, just check if doctor exists
    $doctorStmt = $conn->prepare("SELECT id, name, specialization, hospital FROM doctors WHERE id = ?");
}
if (!$doctorStmt) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

$doctorStmt->bind_param("i", $doctorId);
$doctorStmt->execute();
$doctorResult = $doctorStmt->get_result();

if ($doctorResult->num_rows === 0) {
    $doctorStmt->close();
    $conn->close();
    http_response_code(404);
    $message = $hasStatusColumn ? 'Doctor not found or not available' : 'Doctor not found';
    echo json_encode([
        'success' => false,
        'message' => $message
    ]);
    exit();
}

$doctor = $doctorResult->fetch_assoc();
$doctorStmt->close();

// Check if appointment already exists for same doctor, date, and time
$checkStmt = $conn->prepare("SELECT id FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status != 'Cancelled'");
$checkStmt->bind_param("iss", $doctorId, $appointmentDate, $appointmentTime);
$checkStmt->execute();
$checkResult = $checkStmt->get_result();

if ($checkResult->num_rows > 0) {
    $checkStmt->close();
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'This time slot is already booked. Please choose another time.'
    ]);
    exit();
}
$checkStmt->close();

// Insert appointment
$insertStmt = $conn->prepare("INSERT INTO appointments (user_email, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, 'Pending')");
if (!$insertStmt) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

$insertStmt->bind_param("siss", $userEmail, $doctorId, $appointmentDate, $appointmentTime);

if ($insertStmt->execute()) {
    $appointmentId = $conn->insert_id;
    
    // Verify the insert
    $verifyStmt = $conn->prepare("SELECT id, user_email, doctor_id, appointment_date, appointment_time, status, created_at FROM appointments WHERE id = ?");
    $verifyStmt->bind_param("i", $appointmentId);
    $verifyStmt->execute();
    $verifyResult = $verifyStmt->get_result();
    $verifyStmt->close();
    
    if ($verifyResult->num_rows > 0) {
        $appointment = $verifyResult->fetch_assoc();
        
        error_log("✅ Appointment booked successfully - ID: $appointmentId, Email: $userEmail, Doctor: " . $doctor['name']);
        
        $insertStmt->close();
        $conn->close();
        
        http_response_code(201);
        echo json_encode([
            'success' => true,
            'message' => 'Appointment booked successfully',
            'appointment' => [
                'id' => (int)$appointment['id'],
                'user_email' => $appointment['user_email'],
                'doctor_id' => (int)$appointment['doctor_id'],
                'doctor_name' => $doctor['name'],
                'doctor_specialization' => $doctor['specialization'],
                'appointment_date' => $appointment['appointment_date'],
                'appointment_time' => $appointment['appointment_time'],
                'status' => $appointment['status'],
                'created_at' => $appointment['created_at']
            ]
        ], JSON_PRETTY_PRINT);
    } else {
        $insertStmt->close();
        $conn->close();
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Appointment was inserted but could not be verified'
        ]);
    }
} else {
    $error = $conn->error;
    $insertStmt->close();
    $conn->close();
    
    error_log("❌ Failed to book appointment: " . $error);
    
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to book appointment: ' . $error
    ]);
}

