<?php
/**
 * Verify Table Exists - Check if otp_verification table is visible in phpMyAdmin
 * GET: http://localhost/AwareHealth/api/verify_table_exists.php
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

$result = [
    'success' => true,
    'database' => DB_NAME,
    'tables' => [],
    'otp_verification_exists' => false,
    'otp_verification_structure' => null,
    'phpmyadmin_url' => 'http://localhost/phpmyadmin/index.php?route=/database/structure&db=' . DB_NAME
];

// Get all tables
$tables = $conn->query("SHOW TABLES");
if ($tables) {
    while ($row = $tables->fetch_array()) {
        $tableName = $row[0];
        $result['tables'][] = $tableName;
        
        if ($tableName === 'otp_verification') {
            $result['otp_verification_exists'] = true;
            
            // Get table structure
            $structure = $conn->query("DESCRIBE otp_verification");
            $columns = [];
            while ($col = $structure->fetch_assoc()) {
                $columns[] = [
                    'field' => $col['Field'],
                    'type' => $col['Type'],
                    'null' => $col['Null'],
                    'key' => $col['Key'],
                    'default' => $col['Default'],
                    'extra' => $col['Extra']
                ];
            }
            $result['otp_verification_structure'] = $columns;
            
            // Get record count
            $countResult = $conn->query("SELECT COUNT(*) as count FROM otp_verification");
            $countRow = $countResult->fetch_assoc();
            $result['otp_verification_count'] = $countRow['count'];
        }
    }
}

$conn->close();

echo json_encode($result, JSON_PRETTY_PRINT);

