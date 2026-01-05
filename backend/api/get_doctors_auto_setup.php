<?php
/**
 * GET DOCTORS API WITH AUTO-SETUP
 * This file automatically sets up the database if needed, then returns doctors
 * Copy this file to: C:\xampp\htdocs\AwareHealth\api\get_doctors.php
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

// Connect to MySQL (without database first)
$conn = new mysqli($dbHost, $dbUser, $dbPass);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error . '. Please start XAMPP MySQL.'
    ]);
    exit();
}

// Auto-setup: Create database if needed
$conn->query("CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
$conn->select_db($dbName);
$conn->set_charset("utf8mb4");

// Auto-setup: Create table if needed
$createTable = "CREATE TABLE IF NOT EXISTS `doctors` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `specialization` VARCHAR(255) NOT NULL DEFAULT 'General Physician',
  `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
  `status` VARCHAR(50) NOT NULL DEFAULT 'Available',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
$conn->query($createTable);

// Auto-setup: Insert doctors if table is empty
$countResult = $conn->query("SELECT COUNT(*) as count FROM doctors");
$count = $countResult->fetch_assoc()['count'];

if ($count == 0) {
    $insert = "INSERT INTO doctors (name, specialization, hospital, status) VALUES
    ('Dr. Rajesh Kumar', 'General Physician', 'Saveetha Hospital', 'Available'),
    ('Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'),
    ('Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available'),
    ('Dr. Meera Singh', 'Pediatrician', 'Saveetha Hospital', 'Available'),
    ('Dr. Vikram Reddy', 'Orthopedic Surgeon', 'Saveetha Hospital', 'Available')";
    $conn->query($insert);
}

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
        'availability' => $row['status'],
        'experience' => null,
        'rating' => null
    ];
}

$conn->close();

// Return response
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

