<?php
/**
 * Get Doctors API - Fetch available doctors from database
 * 
 * Endpoint: GET /backend/get_doctors.php
 * 
 * Returns: JSON array of doctors with status = 'Available'
 * 
 * Response Format:
 * {
 *   "success": true,
 *   "doctors": [
 *     {
 *       "id": 1,
 *       "name": "Dr. Name",
 *       "specialization": "General Physician",
 *       "hospital": "Saveetha Hospital",
 *       "status": "Available"
 *     }
 *   ]
 * }
 * 
 * For Mobile Testing:
 * - Use PC IP address: http://172.20.10.2/AwareHealth/backend/get_doctors.php
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
require_once __DIR__ . '/config.php';

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

// Check if specialization column exists (some tables use 'specialty')
$specializationCheck = $conn->query("SHOW COLUMNS FROM doctors LIKE 'specialization'");
$hasSpecialization = $specializationCheck && $specializationCheck->num_rows > 0;

// Check if hospital column exists (some tables use 'location')
$hospitalCheck = $conn->query("SHOW COLUMNS FROM doctors LIKE 'hospital'");
$hasHospital = $hospitalCheck && $hospitalCheck->num_rows > 0;

// Build SQL query based on available columns
if ($hasStatusColumn) {
    // Filter by status = 'Available'
    if ($hasSpecialization && $hasHospital) {
        // Use specialization and hospital columns
        $sql = "SELECT id, name, specialization, hospital, status 
                FROM doctors 
                WHERE status = 'Available' 
                ORDER BY name ASC";
    } else if ($hasSpecialization) {
        // Use specialization, check for location as hospital
        $sql = "SELECT id, name, specialization, 
                       COALESCE(location, 'Saveetha Hospital') as hospital, 
                       status 
                FROM doctors 
                WHERE status = 'Available' 
                ORDER BY name ASC";
    } else {
        // Use specialty as specialization, location as hospital
        $sql = "SELECT id, name, 
                       COALESCE(specialty, 'General Physician') as specialization,
                       COALESCE(location, 'Saveetha Hospital') as hospital,
                       status 
                FROM doctors 
                WHERE status = 'Available' 
                ORDER BY name ASC";
    }
} else {
    // If status column doesn't exist, return all doctors
    if ($hasSpecialization && $hasHospital) {
        $sql = "SELECT id, name, specialization, hospital, 'Available' as status 
                FROM doctors 
                ORDER BY name ASC";
    } else if ($hasSpecialization) {
        $sql = "SELECT id, name, specialization, 
                       COALESCE(location, 'Saveetha Hospital') as hospital,
                       'Available' as status 
                FROM doctors 
                ORDER BY name ASC";
    } else {
        $sql = "SELECT id, name, 
                       COALESCE(specialty, 'General Physician') as specialization,
                       COALESCE(location, 'Saveetha Hospital') as hospital,
                       'Available' as status 
                FROM doctors 
                ORDER BY name ASC";
    }
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
        'id' => $row['id'],
        'name' => $row['name'],
        'specialization' => $row['specialization'] ?? 'General Physician',
        'hospital' => $row['hospital'] ?? 'Saveetha Hospital',
        'status' => $row['status'] ?? 'Available'
    ];
}

// Close database connection
$conn->close();

// Check if any doctors were found
if (empty($doctors)) {
    http_response_code(200);
    echo json_encode([
        'success' => false,
        'message' => 'No doctors available'
    ]);
    exit();
}

// Return JSON response
http_response_code(200);
echo json_encode([
    'success' => true,
    'doctors' => $doctors
], JSON_PRETTY_PRINT);

