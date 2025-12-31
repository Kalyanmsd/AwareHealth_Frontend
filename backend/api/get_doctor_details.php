<?php
/**
 * Get Doctor Details - Fetch single doctor by ID
 * GET: http://172.20.10.2/AwareHealth/api/get_doctor_details.php?id=1
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

// Get doctor ID from query parameter
$doctorId = isset($_GET['id']) ? (int)$_GET['id'] : null;

if (empty($doctorId)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Doctor ID is required. Use: ?id=1'
    ]);
    exit();
}

// Check if doctors table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Doctors table does not exist.'
    ]);
    exit();
}

// Fetch doctor details
$checkStatusColumn = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
$hasStatusColumn = $checkStatusColumn && $checkStatusColumn->num_rows > 0;

if ($hasStatusColumn) {
    $stmt = $conn->prepare("SELECT id, name, specialization, hospital, experience, available_days, available_time, status FROM doctors WHERE id = ?");
} else {
    $stmt = $conn->prepare("SELECT id, name, specialization, hospital, experience, available_days, available_time FROM doctors WHERE id = ?");
}

if (!$stmt) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

$stmt->bind_param("i", $doctorId);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    $stmt->close();
    $conn->close();
    http_response_code(404);
    echo json_encode([
        'success' => false,
        'message' => 'Doctor not found'
    ]);
    exit();
}

$row = $result->fetch_assoc();
$stmt->close();
$conn->close();

http_response_code(200);
echo json_encode([
    'success' => true,
    'message' => 'Doctor details fetched successfully',
    'doctor' => [
        'id' => (int)$row['id'],
        'name' => $row['name'],
        'specialization' => $row['specialization'],
        'hospital' => $row['hospital'] ?? 'Saveetha Hospital',
        'experience' => $row['experience'] ?? 'Not specified',
        'available_days' => $row['available_days'] ?? 'Not specified',
        'available_time' => $row['available_time'] ?? 'Not specified',
        'status' => $row['status'] ?? 'Available'
    ]
], JSON_PRETTY_PRINT);

