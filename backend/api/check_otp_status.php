<?php
/**
 * Check OTP Status - Debug endpoint to see what's in database
 * GET: http://172.20.10.2/AwareHealth/api/check_otp_status.php?email=test@example.com
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

$email = isset($_GET['email']) ? strtolower(trim($_GET['email'])) : null;

if (empty($email)) {
    echo json_encode([
        'success' => false,
        'message' => 'Email parameter required. Use: ?email=your-email@example.com'
    ]);
    $conn->close();
    exit();
}

// Check if table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'otp_verification'");
$tableExists = ($tableCheck && $tableCheck->num_rows > 0);

$result = [
    'success' => true,
    'email' => $email,
    'table_exists' => $tableExists,
    'otp_verification' => [],
    'password_reset_tokens' => []
];

if ($tableExists) {
    // Check otp_verification table
    $stmt = $conn->prepare("SELECT id, email, otp, expires_at, created_at FROM otp_verification WHERE email = ?");
    if ($stmt) {
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $resultSet = $stmt->get_result();
        
        while ($row = $resultSet->fetch_assoc()) {
            $result['otp_verification'][] = [
                'id' => $row['id'],
                'email' => $row['email'],
                'otp' => $row['otp'],
                'expires_at' => $row['expires_at'],
                'created_at' => $row['created_at'],
                'is_expired' => (strtotime($row['expires_at']) < time())
            ];
        }
        $stmt->close();
    }
    
    // Also check all records
    $allRecords = $conn->query("SELECT id, email, otp, expires_at, created_at FROM otp_verification ORDER BY created_at DESC LIMIT 10");
    $allOtpRecords = [];
    while ($row = $allRecords->fetch_assoc()) {
        $allOtpRecords[] = $row;
    }
    $result['all_otp_records'] = $allOtpRecords;
}

// Check password_reset_tokens table
$prtStmt = $conn->prepare("SELECT id, email, otp, otp_expires_at, used, created_at FROM password_reset_tokens WHERE LOWER(email) = ? ORDER BY created_at DESC LIMIT 5");
if ($prtStmt) {
    $prtStmt->bind_param("s", $email);
    $prtStmt->execute();
    $prtResult = $prtStmt->get_result();
    
    while ($row = $prtResult->fetch_assoc()) {
        $result['password_reset_tokens'][] = [
            'id' => $row['id'],
            'email' => $row['email'],
            'otp' => $row['otp'],
            'otp_expires_at' => $row['otp_expires_at'],
            'used' => $row['used'],
            'created_at' => $row['created_at']
        ];
    }
    $prtStmt->close();
}

$conn->close();

echo json_encode($result, JSON_PRETTY_PRINT);

