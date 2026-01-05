<?php
/**
 * Simple Get Doctors API
 * Direct database connection - no dependencies
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Database credentials
$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

// Connect
$conn = new mysqli($dbHost, $dbUser, $dbPass, $dbName);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit();
}

$conn->set_charset("utf8mb4");

// Query available doctors
$sql = "SELECT id, name, specialization, hospital, status 
        FROM doctors 
        WHERE status = 'Available' 
        ORDER BY name ASC";

$result = $conn->query($sql);

if (!$result) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

// Build response
$doctors = [];
while ($row = $result->fetch_assoc()) {
    $doctors[] = [
        'id' => (string)$row['id'],
        'name' => $row['name'],
        'specialization' => $row['specialization'],
        'hospital' => $row['hospital'],
        'status' => $row['status'],
        // Frontend compatibility
        'specialty' => $row['specialization'],
        'location' => $row['hospital'],
        'availability' => $row['status']
    ];
}

$conn->close();

if (empty($doctors)) {
    http_response_code(200);
    echo json_encode([
        'success' => false,
        'message' => 'No doctors available'
    ]);
    exit();
}

http_response_code(200);
echo json_encode([
    'success' => true,
    'doctors' => $doctors
], JSON_PRETTY_PRINT);

?>

