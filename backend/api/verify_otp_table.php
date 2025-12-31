<?php
/**
 * Verify OTP Table Exists
 * Quick check if otp_verification table exists
 */

require_once __DIR__ . '/../config.php';

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit();
}

// Check if table exists
$result = $conn->query("SHOW TABLES LIKE 'otp_verification'");

if ($result && $result->num_rows > 0) {
    // Get table structure
    $columns = $conn->query("DESCRIBE otp_verification");
    $columnList = [];
    while ($col = $columns->fetch_assoc()) {
        $columnList[] = [
            'field' => $col['Field'],
            'type' => $col['Type'],
            'null' => $col['Null'],
            'key' => $col['Key'],
            'default' => $col['Default'],
            'extra' => $col['Extra']
        ];
    }
    
    echo json_encode([
        'success' => true,
        'message' => 'OTP verification table exists!',
        'database' => DB_NAME,
        'table' => 'otp_verification',
        'columns' => $columnList,
        'phpmyadmin_url' => 'http://localhost/phpmyadmin/index.php?route=/database/structure&db=' . DB_NAME
    ], JSON_PRETTY_PRINT);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'OTP verification table does NOT exist',
        'action' => 'Run: http://localhost/AwareHealth/api/auto_create_otp_table.php'
    ], JSON_PRETTY_PRINT);
}

$conn->close();

