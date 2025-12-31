<?php
/**
 * Resend OTP - Regenerate and update OTP for email
 * POST: { "email": "user@example.com" }
 */

// Start output buffering
ob_start();

// Set headers
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    ob_end_clean();
    exit();
}

// Only allow POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    ob_end_clean();
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use POST.'
    ]);
    exit();
}

require_once __DIR__ . '/db.php';

// Get JSON input
$rawInput = file_get_contents('php://input');
$input = json_decode($rawInput, true);

// Validate JSON
if (json_last_error() !== JSON_ERROR_NONE) {
    ob_end_clean();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid JSON: ' . json_last_error_msg()
    ]);
    exit();
}

// Validate email
if (!isset($input['email']) || empty(trim($input['email']))) {
    ob_end_clean();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Email is required'
    ]);
    exit();
}

$email = strtolower(trim($input['email']));

// Validate email format
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    ob_end_clean();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid email format'
    ]);
    exit();
}

// Connect to database
try {
    $conn = getOTPDB();
} catch (Exception $e) {
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $e->getMessage()
    ]);
    exit();
}

// Check if table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'otp_verification'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    $conn->close();
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'OTP verification table does not exist. Please run setup_otp_table.php first.'
    ]);
    exit();
}

// Generate new 6-digit OTP
$otp = str_pad(rand(0, 999999), 6, '0', STR_PAD_LEFT);

// Set expiry to 5 minutes from now
$expiresAt = date('Y-m-d H:i:s', strtotime('+5 minutes'));

error_log("Resend OTP - Email: $email, OTP: $otp, Expires: $expiresAt");

// Check if OTP record exists
$checkStmt = $conn->prepare("SELECT id FROM otp_verification WHERE email = ?");
if (!$checkStmt) {
    $conn->close();
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database prepare error: ' . $conn->error
    ]);
    exit();
}

$checkStmt->bind_param("s", $email);
$checkStmt->execute();
$result = $checkStmt->get_result();
$checkStmt->close();

if ($result->num_rows > 0) {
    // Update existing OTP
    $updateStmt = $conn->prepare("UPDATE otp_verification SET otp = ?, expires_at = ?, created_at = CURRENT_TIMESTAMP WHERE email = ?");
    if (!$updateStmt) {
        $conn->close();
        ob_end_clean();
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Database prepare error: ' . $conn->error
        ]);
        exit();
    }
    
    $updateStmt->bind_param("sss", $otp, $expiresAt, $email);
    
    if ($updateStmt->execute()) {
        $updateStmt->close();
        $conn->close();
        
        error_log("OTP Updated - Email: $email, OTP: $otp");
        ob_end_clean();
        
        http_response_code(200);
        echo json_encode([
            'success' => true,
            'message' => 'OTP resent successfully',
            'otp' => $otp, // For testing - remove in production
            'expires_at' => $expiresAt
        ]);
    } else {
        $error = $conn->error;
        $updateStmt->close();
        $conn->close();
        
        error_log("OTP Update Failed - Email: $email, Error: $error");
        ob_end_clean();
        
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Failed to resend OTP: ' . $error
        ]);
    }
} else {
    // Insert new OTP (same as send_otp)
    $insertStmt = $conn->prepare("INSERT INTO otp_verification (email, otp, expires_at) VALUES (?, ?, ?)");
    if (!$insertStmt) {
        $conn->close();
        ob_end_clean();
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Database prepare error: ' . $conn->error
        ]);
        exit();
    }
    
    $insertStmt->bind_param("sss", $email, $otp, $expiresAt);
    
    if ($insertStmt->execute()) {
        $insertedId = $conn->insert_id;
        $insertStmt->close();
        $conn->close();
        
        error_log("OTP Inserted - Email: $email, OTP: $otp, ID: $insertedId");
        ob_end_clean();
        
        http_response_code(200);
        echo json_encode([
            'success' => true,
            'message' => 'OTP sent successfully',
            'otp' => $otp, // For testing - remove in production
            'expires_at' => $expiresAt
        ]);
    } else {
        $error = $conn->error;
        $insertStmt->close();
        $conn->close();
        
        error_log("OTP Insert Failed - Email: $email, Error: $error");
        ob_end_clean();
        
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Failed to send OTP: ' . $error
        ]);
    }
}
