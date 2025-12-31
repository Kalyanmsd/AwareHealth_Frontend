<?php
/**
 * Debug OTP Status - Check if OTP exists for an email
 * Usage: http://localhost/AwareHealth/api/debug_otp_status.php?email=user@example.com
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

ob_end_clean();
ob_start();

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

try {
    $conn = getDB();
    
    $email = isset($_GET['email']) ? strtolower(trim($_GET['email'])) : '';
    
    if (empty($email)) {
        ob_end_clean();
        echo json_encode([
            'success' => false,
            'message' => 'Email parameter required. Usage: ?email=user@example.com'
        ], JSON_PRETTY_PRINT);
        exit;
    }
    
    // Check all tokens for this email
    $stmt = $conn->prepare("
        SELECT 
            id,
            user_id,
            email,
            otp,
            otp_expires_at,
            used,
            created_at,
            TIMESTAMPDIFF(SECOND, NOW(), otp_expires_at) as seconds_until_expiry
        FROM password_reset_tokens 
        WHERE LOWER(email) = ?
        ORDER BY created_at DESC
        LIMIT 10
    ");
    
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $tokens = [];
    while ($row = $result->fetch_assoc()) {
        $tokens[] = [
            'id' => $row['id'],
            'email' => $row['email'],
            'otp' => $row['otp'] ? '***' . substr($row['otp'], -2) : null, // Show last 2 digits only
            'has_otp' => !empty($row['otp']),
            'otp_expires_at' => $row['otp_expires_at'],
            'is_expired' => $row['otp_expires_at'] ? (strtotime($row['otp_expires_at']) < time()) : false,
            'seconds_until_expiry' => $row['seconds_until_expiry'],
            'used' => (bool)$row['used'],
            'created_at' => $row['created_at']
        ];
    }
    
    $stmt->close();
    
    // Count summary
    $summaryStmt = $conn->prepare("
        SELECT 
            COUNT(*) as total,
            SUM(CASE WHEN otp IS NOT NULL AND otp != '' THEN 1 ELSE 0 END) as with_otp,
            SUM(CASE WHEN (used = 1 OR used = TRUE) THEN 1 ELSE 0 END) as used_count,
            SUM(CASE WHEN otp_expires_at IS NOT NULL AND otp_expires_at < NOW() THEN 1 ELSE 0 END) as expired_count
        FROM password_reset_tokens 
        WHERE LOWER(email) = ?
    ");
    $summaryStmt->bind_param("s", $email);
    $summaryStmt->execute();
    $summaryResult = $summaryStmt->get_result();
    $summary = $summaryResult->fetch_assoc();
    $summaryStmt->close();
    
    ob_end_clean();
    echo json_encode([
        'success' => true,
        'email' => $email,
        'summary' => [
            'total_tokens' => (int)$summary['total'],
            'tokens_with_otp' => (int)$summary['with_otp'],
            'used_tokens' => (int)$summary['used_count'],
            'expired_tokens' => (int)$summary['expired_count']
        ],
        'tokens' => $tokens,
        'message' => count($tokens) > 0 ? 'Found ' . count($tokens) . ' token(s)' : 'No tokens found for this email'
    ], JSON_PRETTY_PRINT);
    
} catch (Exception $e) {
    ob_end_clean();
    echo json_encode([
        'success' => false,
        'message' => 'Error: ' . $e->getMessage()
    ], JSON_PRETTY_PRINT);
}

