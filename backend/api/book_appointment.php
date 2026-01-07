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

// Verify doctor exists using JOIN with users table to get doctor name
$checkStatusColumn = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
$hasStatusColumn = $checkStatusColumn && $checkStatusColumn->num_rows > 0;

// Check if doctors table has user_id
$checkUserIdColumn = $conn->query("SHOW COLUMNS FROM doctors LIKE 'user_id'");
$hasUserIdColumn = $checkUserIdColumn && $checkUserIdColumn->num_rows > 0;

// Check doctors table columns for specialty/specialization
$doctorsColumns = $conn->query("SHOW COLUMNS FROM doctors");
$hasSpecialty = false;
$hasSpecialization = false;
$hasLocation = false;
$hasHospital = false;

while ($col = $doctorsColumns->fetch_assoc()) {
    if ($col['Field'] === 'specialty') $hasSpecialty = true;
    if ($col['Field'] === 'specialization') $hasSpecialization = true;
    if ($col['Field'] === 'location') $hasLocation = true;
    if ($col['Field'] === 'hospital') $hasHospital = true;
}

// Build doctor query with JOIN to get name from users table
if ($hasUserIdColumn) {
    // Use JOIN to get name from users table
    if ($hasStatusColumn) {
        $doctorStmt = $conn->prepare("
            SELECT 
                d.id,
                COALESCE(u.name, d.name, 'Dr. Doctor') as name,
                COALESCE(d.specialization, d.specialty, 'General Physician') as specialization,
                COALESCE(d.location, d.hospital, 'Saveetha Hospital') as hospital
            FROM doctors d
            LEFT JOIN users u ON d.user_id = u.id
            WHERE d.id = ? AND d.status = 'Available'
        ");
    } else {
        $doctorStmt = $conn->prepare("
            SELECT 
                d.id,
                COALESCE(u.name, d.name, 'Dr. Doctor') as name,
                COALESCE(d.specialization, d.specialty, 'General Physician') as specialization,
                COALESCE(d.location, d.hospital, 'Saveetha Hospital') as hospital
            FROM doctors d
            LEFT JOIN users u ON d.user_id = u.id
            WHERE d.id = ?
        ");
    }
} else {
    // Fallback: doctors table doesn't have user_id
    if ($hasStatusColumn) {
        $doctorStmt = $conn->prepare("SELECT id, name, COALESCE(specialization, specialty, 'General Physician') as specialization, COALESCE(location, hospital, 'Saveetha Hospital') as hospital FROM doctors WHERE id = ? AND status = 'Available'");
    } else {
        $doctorStmt = $conn->prepare("SELECT id, name, COALESCE(specialization, specialty, 'General Physician') as specialization, COALESCE(location, hospital, 'Saveetha Hospital') as hospital FROM doctors WHERE id = ?");
    }
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

// Get doctor name from doctors table
$getDoctorStmt = $conn->prepare("SELECT COALESCE(doctor_name, name, 'Dr. Doctor') as doctor_name, COALESCE(location, hospital, 'Saveetha Hospital') as hospital FROM doctors WHERE id = ?");
$getDoctorStmt->bind_param("i", $doctorId);
$getDoctorStmt->execute();
$doctorResult = $getDoctorStmt->get_result();
$doctorData = $doctorResult->fetch_assoc();
$doctorName = $doctorData['doctor_name'] ?? 'Dr. Doctor';
$hospital = $doctorData['hospital'] ?? 'Saveetha Hospital';
$getDoctorStmt->close();

// Get patient name from users table if available
$patientName = null;
$getPatientStmt = $conn->prepare("SELECT name FROM users WHERE email = ?");
$getPatientStmt->bind_param("s", $userEmail);
if ($getPatientStmt->execute()) {
    $patientResult = $getPatientStmt->get_result();
    if ($patientResult->num_rows > 0) {
        $patientData = $patientResult->fetch_assoc();
        $patientName = $patientData['name'];
    }
}
$getPatientStmt->close();

// Check which columns exist in appointments table
$columnCheck = $conn->query("SHOW COLUMNS FROM appointments");
$columns = [];
while ($col = $columnCheck->fetch_assoc()) {
    $columns[] = $col['Field'];
}

$hasPatientName = in_array('patient_name', $columns);
$hasDoctorName = in_array('doctor_name', $columns);
$hasHospital = in_array('hospital', $columns);
$hasPatientEmail = in_array('patient_email', $columns);
$hasAppointmentDate = in_array('appointment_date', $columns);
$hasAppointmentTime = in_array('appointment_time', $columns);

// Build INSERT query based on available columns
$insertFields = ['user_email', 'doctor_id', 'appointment_date', 'appointment_time', 'status'];
$insertValues = [$userEmail, $doctorId, $appointmentDate, $appointmentTime, 'pending'];
$insertTypes = "siss";

if ($hasPatientName && $patientName) {
    $insertFields[] = 'patient_name';
    $insertValues[] = $patientName;
    $insertTypes .= "s";
}

if ($hasDoctorName) {
    $insertFields[] = 'doctor_name';
    $insertValues[] = $doctorName;
    $insertTypes .= "s";
}

if ($hasHospital) {
    $insertFields[] = 'hospital';
    $insertValues[] = $hospital;
    $insertTypes .= "s";
}

if ($hasPatientEmail) {
    $insertFields[] = 'patient_email';
    $insertValues[] = $userEmail;
    $insertTypes .= "s";
}

$fieldsStr = implode(', ', $insertFields);
$placeholders = implode(', ', array_fill(0, count($insertValues), '?'));

$insertStmt = $conn->prepare("INSERT INTO appointments ($fieldsStr) VALUES ($placeholders)");
if (!$insertStmt) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

$insertStmt->bind_param($insertTypes, ...$insertValues);

if ($insertStmt->execute()) {
    $appointmentId = $conn->insert_id;
    
    // Verify the insert and fetch appointment data
    $dateCol = $hasAppointmentDate ? 'appointment_date' : 'date';
    $timeCol = $hasAppointmentTime ? 'appointment_time' : 'time';
    
    $verifyStmt = $conn->prepare("
        SELECT 
            a.id,
            a.appointment_id,
            " . ($hasPatientName ? "COALESCE(a.patient_name, u.name, 'Patient')" : "COALESCE(u.name, 'Patient')") . " as patient_name,
            " . ($hasPatientEmail ? "COALESCE(a.patient_email, a.user_email)" : "a.user_email") . " as patient_email,
            a.user_email,
            a.doctor_id,
            " . ($hasDoctorName ? "COALESCE(a.doctor_name, d.doctor_name, d.name, 'Dr. Doctor')" : "COALESCE(d.doctor_name, d.name, 'Dr. Doctor')") . " as doctor_name,
            COALESCE(d.specialization, d.specialty, 'General Physician') as doctor_specialization,
            " . ($hasHospital ? "COALESCE(a.hospital, d.location, d.hospital, 'Saveetha Hospital')" : "COALESCE(d.location, d.hospital, 'Saveetha Hospital')") . " as location,
            a.$dateCol as appointment_date,
            a.$timeCol as appointment_time,
            a.status,
            a.created_at
        FROM appointments a
        LEFT JOIN doctors d ON a.doctor_id = d.id
        LEFT JOIN users u ON " . ($hasPatientEmail ? "a.patient_email = u.email" : "a.user_email = u.email") . "
        WHERE a.id = ?
    ");
    
    $verifyStmt->bind_param("i", $appointmentId);
    $verifyStmt->execute();
    $verifyResult = $verifyStmt->get_result();
    $verifyStmt->close();
    
    if ($verifyResult->num_rows > 0) {
        $appointment = $verifyResult->fetch_assoc();
        
        error_log("✅ Appointment booked successfully - ID: $appointmentId, Email: $userEmail, Doctor ID: $doctorId, Doctor Name: " . $appointment['doctor_name']);
        
        $insertStmt->close();
        $conn->close();
        
        // Format time to 12-hour format for display
        $time12Hour = date("g:i A", strtotime($appointment['appointment_time']));
        
        http_response_code(201);
        echo json_encode([
            'success' => true,
            'message' => 'Appointment booked successfully',
            'appointment' => [
                'id' => (int)$appointment['id'],
                'appointment_id' => $appointment['appointment_id'] ?? (string)$appointment['id'],
                'patient_name' => $appointment['patient_name'] ?? null,
                'patient_email' => $appointment['patient_email'] ?? $appointment['user_email'],
                'user_email' => $appointment['user_email'],
                'doctor_id' => (int)$appointment['doctor_id'],
                'doctor_name' => $appointment['doctor_name'],
                'doctor_specialization' => $appointment['doctor_specialization'],
                'location' => $appointment['location'],
                'hospital' => $appointment['location'],
                'appointment_date' => $appointment['appointment_date'],
                'appointment_time' => $appointment['appointment_time'],
                'appointment_time_display' => $time12Hour,
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

