<?php
/**
 * Debug OTP Endpoint
 * Use this to check what OTPs are in the database
 */

ob_start();
ini_set('display_errors', '0');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

ob_end_clean();
ob_start();

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');

try {
    $conn = getDB();
    
    $email = $_GET['email'] ?? '';
    
    if (empty($email)) {
        ob_end_clean();
        echo json_encode([
            'success' => false,
            'message' => 'Email parameter required',
            'usage' => '?email=your@email.com'
        ]);
        exit();
    }
    
    $email = strtolower(trim($email));
    
    // Check table structure
    $tableInfo = $conn->query("DESCRIBE password_reset_tokens");
    $columns = [];
    while ($row = $tableInfo->fetch_assoc()) {
        $columns[] = $row['Field'];
    }
    
    // Get all tokens for this email
    $stmt = $conn->prepare("SELECT id, user_id, email, otp, otp_expires_at, used, created_at FROM password_reset_tokens WHERE LOWER(email) = ? ORDER BY created_at DESC LIMIT 10");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $tokens = [];
    while ($row = $result->fetch_assoc()) {
        $tokens[] = [
            'id' => $row['id'],
            'email' => $row['email'],
            'otp' => $row['otp'],
            'otp_expires_at' => $row['otp_expires_at'],
            'used' => $row['used'],
            'created_at' => $row['created_at'],
            'is_expired' => strtotime($row['otp_expires_at']) < time(),
            'is_used' => $row['used'] == 1 || $row['used'] === true
        ];
    }
    
    $stmt->close();
    
    ob_end_clean();
    echo json_encode([
        'success' => true,
        'email' => $email,
        'table_columns' => $columns,
        'tokens_found' => count($tokens),
        'tokens' => $tokens,
        'current_time' => date('Y-m-d H:i:s'),
        'timestamp' => time()
    ], JSON_PRETTY_PRINT);
    
} catch (Exception $e) {
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => $e->getMessage()
    ]);
}

