<?php
/**
 * Simple OTP Test - Direct test without routing
 * Test URL: http://172.20.10.2/AwareHealth/api/test_otp_simple.php
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

require_once __DIR__ . '/../config.php';

$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit();
}

$conn->set_charset("utf8mb4");

// Test email and OTP
$testEmail = 'test@example.com';
$testOTP = '123456';
$expiresAt = date('Y-m-d H:i:s', strtotime('+5 minutes'));

// Check if table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'otp_verification'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    echo json_encode([
        'success' => false,
        'message' => 'Table otp_verification does not exist. Please create it first.',
        'sql' => "CREATE TABLE `otp_verification` (
          `id` INT AUTO_INCREMENT PRIMARY KEY,
          `email` VARCHAR(255) NOT NULL UNIQUE,
          `otp` VARCHAR(6) NOT NULL,
          `expires_at` DATETIME NOT NULL,
          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
    ]);
    $conn->close();
    exit();
}

// Delete old test records
$conn->query("DELETE FROM otp_verification WHERE email = '$testEmail'");

// Insert test OTP
$stmt = $conn->prepare("INSERT INTO otp_verification (email, otp, expires_at) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $testEmail, $testOTP, $expiresAt);

if ($stmt->execute()) {
    $insertedId = $conn->insert_id;
    
    // Verify it was inserted
    $check = $conn->query("SELECT * FROM otp_verification WHERE id = $insertedId");
    $row = $check->fetch_assoc();
    
    // Count all records
    $countResult = $conn->query("SELECT COUNT(*) as count FROM otp_verification");
    $countRow = $countResult->fetch_assoc();
    
    echo json_encode([
        'success' => true,
        'message' => 'OTP inserted successfully',
        'inserted_id' => $insertedId,
        'record' => $row,
        'table_count' => $countRow['count'],
        'test_url' => 'http://172.20.10.2/AwareHealth/api/test_otp_simple.php'
    ], JSON_PRETTY_PRINT);
    
    // Clean up
    $conn->query("DELETE FROM otp_verification WHERE email = '$testEmail'");
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Failed to insert: ' . $conn->error,
        'error_code' => $conn->errno,
        'table_exists' => true
    ], JSON_PRETTY_PRINT);
}

$stmt->close();
$conn->close();

