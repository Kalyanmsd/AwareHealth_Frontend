<?php
/**
 * Test Registration Endpoint
 * Use this to debug registration issues
 */

// Start output buffering
ob_start();

// Turn off error display
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

// Clear output
ob_end_clean();
ob_start();

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');

try {
    // Test database connection
    $conn = getDB();
    
    // Test table structure
    $result = $conn->query("DESCRIBE users");
    $columns = [];
    while ($row = $result->fetch_assoc()) {
        $columns[] = $row['Field'];
    }
    
    // Test insert with sample data
    $testId = 'test-' . time();
    $testEmail = 'test' . time() . '@test.com';
    $testName = 'Test User';
    $testPassword = password_hash('test123', PASSWORD_DEFAULT);
    $testUserType = 'patient';
    $testPhone = '1234567890';
    
    $stmt = $conn->prepare("INSERT INTO users (id, user_type, name, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)");
    
    if (!$stmt) {
        throw new Exception("Prepare failed: " . $conn->error);
    }
    
    $stmt->bind_param("ssssss", $testId, $testUserType, $testName, $testEmail, $testPhone, $testPassword);
    
    if (!$stmt->execute()) {
        throw new Exception("Execute failed: " . $stmt->error);
    }
    
    // Clean up test data
    $conn->query("DELETE FROM users WHERE id = '$testId'");
    $stmt->close();
    
    ob_end_clean();
    echo json_encode([
        'success' => true,
        'message' => 'Registration test passed',
        'users_table_columns' => $columns,
        'database' => DB_NAME,
        'host' => DB_HOST
    ]);
    
} catch (Exception $e) {
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Test failed',
        'error' => $e->getMessage(),
        'database' => DB_NAME,
        'host' => DB_HOST
    ]);
}

