<?php
/**
 * Test Get Doctors - Simple test endpoint
 * GET: http://172.20.10.2/AwareHealth/api/test_get_doctors.php
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once __DIR__ . '/../config.php';

// Test database connection
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error,
        'database' => DB_NAME
    ]);
    exit();
}

$conn->set_charset("utf8mb4");

// Check if doctors table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
$tableExists = $tableCheck && $tableCheck->num_rows > 0;

if (!$tableExists) {
    $conn->close();
    echo json_encode([
        'success' => false,
        'message' => 'Doctors table does not exist',
        'database' => DB_NAME,
        'suggestion' => 'Run: CREATE TABLE doctors (...)'
    ]);
    exit();
}

// Check if status column exists
$checkStatusColumn = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
$hasStatusColumn = $checkStatusColumn && $checkStatusColumn->num_rows > 0;

// Count total doctors
$totalCount = $conn->query("SELECT COUNT(*) as total FROM doctors");
$totalRow = $totalCount->fetch_assoc();
$totalDoctors = (int)$totalRow['total'];

// Count available doctors
if ($hasStatusColumn) {
    $availableCount = $conn->query("SELECT COUNT(*) as total FROM doctors WHERE status = 'Available'");
    $availableRow = $availableCount->fetch_assoc();
    $availableDoctors = (int)$availableRow['total'];
} else {
    $availableDoctors = $totalDoctors;
}

// Fetch doctors
if ($hasStatusColumn) {
    $stmt = $conn->prepare("SELECT id, name, specialization, hospital, experience, available_days, available_time, status FROM doctors WHERE status = 'Available' ORDER BY name ASC");
} else {
    $stmt = $conn->prepare("SELECT id, name, specialization, hospital, experience, available_days, available_time FROM doctors ORDER BY name ASC LIMIT 10");
}

$doctors = [];
if ($stmt) {
    $stmt->execute();
    $result = $stmt->get_result();
    
    while ($row = $result->fetch_assoc()) {
        $doctors[] = [
            'id' => (int)$row['id'],
            'name' => $row['name'],
            'specialization' => $row['specialization'],
            'hospital' => $row['hospital'] ?? 'Saveetha Hospital',
            'experience' => $row['experience'] ?? 'Not specified',
            'available_days' => $row['available_days'] ?? 'Not specified',
            'available_time' => $row['available_time'] ?? 'Not specified',
            'status' => $row['status'] ?? 'Available'
        ];
    }
    $stmt->close();
}

$conn->close();

echo json_encode([
    'success' => true,
    'message' => 'Test successful',
    'database' => DB_NAME,
    'table_exists' => $tableExists,
    'has_status_column' => $hasStatusColumn,
    'total_doctors' => $totalDoctors,
    'available_doctors' => $availableDoctors,
    'doctors_returned' => count($doctors),
    'doctors' => $doctors,
    'note' => $totalDoctors == 0 ? 'Doctors table is empty. Insert some doctors first.' : ($availableDoctors == 0 && $hasStatusColumn ? 'No doctors with status="Available". Update doctor status.' : '')
], JSON_PRETTY_PRINT);

