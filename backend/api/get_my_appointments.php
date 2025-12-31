<?php
/**
 * Get My Appointments - Fetch appointments for a user
 * GET: http://172.20.10.2/AwareHealth/api/get_my_appointments.php?email=patient@example.com
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
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

// Get email from query parameter
$userEmail = isset($_GET['email']) ? strtolower(trim($_GET['email'])) : null;

if (empty($userEmail)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Email parameter is required. Use: ?email=your-email@example.com'
    ]);
    exit();
}

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

// Fetch appointments for user
$stmt = $conn->prepare("
    SELECT 
        a.id,
        a.user_email,
        a.doctor_id,
        a.appointment_date,
        a.appointment_time,
        a.status,
        a.created_at,
        d.name as doctor_name,
        d.specialization as doctor_specialization,
        d.hospital
    FROM appointments a
    JOIN doctors d ON a.doctor_id = d.id
    WHERE a.user_email = ?
    ORDER BY a.appointment_date DESC, a.appointment_time DESC
");

if (!$stmt) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

$stmt->bind_param("s", $userEmail);
$stmt->execute();
$result = $stmt->get_result();

$appointments = [];
while ($row = $result->fetch_assoc()) {
    $appointments[] = [
        'id' => (int)$row['id'],
        'user_email' => $row['user_email'],
        'doctor_id' => (int)$row['doctor_id'],
        'doctor_name' => $row['doctor_name'],
        'doctor_specialization' => $row['doctor_specialization'],
        'hospital' => $row['hospital'],
        'appointment_date' => $row['appointment_date'],
        'appointment_time' => $row['appointment_time'],
        'status' => $row['status'],
        'created_at' => $row['created_at']
    ];
}

$stmt->close();
$conn->close();

http_response_code(200);
echo json_encode([
    'success' => true,
    'message' => 'Appointments fetched successfully',
    'appointments' => $appointments,
    'count' => count($appointments),
    'user_email' => $userEmail
], JSON_PRETTY_PRINT);

