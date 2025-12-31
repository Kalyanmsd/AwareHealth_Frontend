<?php
/**
 * Get Doctors API - Fetch available doctors from database
 * 
 * Endpoint: GET /api/get_doctors.php
 * 
 * Returns: JSON array of doctors with status = 'Available'
 * 
 * For Mobile Testing:
 * - Use PC IP address: http://172.20.10.2/AwareHealth/api/get_doctors.php
 * - Do NOT use localhost (localhost only works on PC, not mobile)
 */

// Set headers for JSON response and CORS
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle preflight OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Only allow GET requests
if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use GET.'
    ]);
    exit();
}

// Include database configuration
require_once __DIR__ . '/../config.php';

// Connect to MySQL database
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

// Check connection
if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit();
}

// Set charset to UTF-8
$conn->set_charset("utf8mb4");

// Check if doctors table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Doctors table does not exist in database.'
    ]);
    exit();
}

// Check if status column exists
$statusColumnCheck = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
$hasStatusColumn = $statusColumnCheck && $statusColumnCheck->num_rows > 0;

// Prepare SQL query to fetch available doctors
if ($hasStatusColumn) {
    // Filter by status = 'Available'
    $sql = "SELECT id, name, specialization, experience, available_days, available_time 
            FROM doctors 
            WHERE status = 'Available' 
            ORDER BY name ASC";
} else {
    // If status column doesn't exist, return all doctors
    $sql = "SELECT id, name, specialization, experience, available_days, available_time 
            FROM doctors 
            ORDER BY name ASC";
}

// Execute query
$result = $conn->query($sql);

// Check if query was successful
if (!$result) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

// Fetch all doctors and build response array
$doctors = [];
while ($row = $result->fetch_assoc()) {
    $doctors[] = [
        'id' => (int)$row['id'],
        'name' => $row['name'],
        'specialization' => $row['specialization'],
        'experience' => $row['experience'],
        'available_days' => $row['available_days'],
        'available_time' => $row['available_time']
    ];
}

// Close database connection
$conn->close();

// Return JSON response
http_response_code(200);
echo json_encode([
    'success' => true,
    'message' => 'Doctors fetched successfully',
    'doctors' => $doctors,
    'count' => count($doctors)
], JSON_PRETTY_PRINT);
