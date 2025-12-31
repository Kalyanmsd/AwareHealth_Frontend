<?php
/**
 * Verify OTP - Check if OTP is valid
 * POST: { "email": "user@example.com", "otp": "123456" }
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

// Log request for debugging
error_log("Verify OTP Request - Raw input: " . $rawInput);

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

// Validate input
if (!isset($input['email']) || empty(trim($input['email']))) {
    ob_end_clean();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Email is required'
    ]);
    exit();
}

if (!isset($input['otp']) || empty(trim($input['otp']))) {
    ob_end_clean();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'OTP is required'
    ]);
    exit();
}

// Normalize email (lowercase, trim)
$email = strtolower(trim($input['email']));
$otp = trim($input['otp']);

// Remove any non-numeric characters from OTP
$otp = preg_replace('/[^0-9]/', '', $otp);

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

// Validate OTP format (6 digits)
if (strlen($otp) !== 6 || !ctype_digit($otp)) {
    ob_end_clean();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'OTP must be exactly 6 digits'
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

// Log verification attempt
error_log("OTP Verification Attempt - Email: $email, OTP: $otp");

// Find OTP record - use exact email match (already normalized to lowercase)
$stmt = $conn->prepare("SELECT id, email, otp, expires_at, created_at FROM otp_verification WHERE email = ?");
if (!$stmt) {
    $conn->close();
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database prepare error: ' . $conn->error
    ]);
    exit();
}

$stmt->bind_param("s", $email);
if (!$stmt->execute()) {
    $error = $stmt->error;
    $stmt->close();
    $conn->close();
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query error: ' . $error
    ]);
    exit();
}

$result = $stmt->get_result();

if ($result->num_rows === 0) {
    $stmt->close();
    
    // Debug: Check if any records exist for this email (case-insensitive)
    $debugStmt = $conn->prepare("SELECT id, email, otp FROM otp_verification WHERE LOWER(email) = ?");
    $debugStmt->bind_param("s", $email);
    $debugStmt->execute();
    $debugResult = $debugStmt->get_result();
    $debugStmt->close();
    
    if ($debugResult->num_rows > 0) {
        $debugRow = $debugResult->fetch_assoc();
        error_log("Debug: Found record with different case - Stored: '" . $debugRow['email'] . "', Looking for: '$email'");
    } else {
        // Check total records in table
        $countResult = $conn->query("SELECT COUNT(*) as total FROM otp_verification");
        $countRow = $countResult->fetch_assoc();
        error_log("Debug: No OTP found for email '$email'. Total records in table: " . $countRow['total']);
    }
    
    $conn->close();
    ob_end_clean();
    
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'No OTP found for this email. Please request a new OTP.'
    ]);
    exit();
}

$row = $result->fetch_assoc();
$storedOTP = trim($row['otp']);
$expiresAt = $row['expires_at'];
$recordId = $row['id'];
$storedEmail = $row['email'];

$stmt->close();

// Log what we found
error_log("OTP Found - ID: $recordId, Email: $storedEmail, Stored OTP: $storedOTP, Entered OTP: $otp, Expires: $expiresAt");

// Check if OTP matches (exact match)
if ($storedOTP !== $otp) {
    error_log("OTP Mismatch - Stored: '$storedOTP' (length: " . strlen($storedOTP) . "), Entered: '$otp' (length: " . strlen($otp) . ")");
    $conn->close();
    ob_end_clean();
    
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid OTP. Please check and try again.'
    ]);
    exit();
}

// Check if OTP is expired
$currentTime = time();
$expiryTime = strtotime($expiresAt);

if ($currentTime > $expiryTime) {
    // Delete expired OTP
    $deleteStmt = $conn->prepare("DELETE FROM otp_verification WHERE id = ?");
    $deleteStmt->bind_param("i", $recordId);
    $deleteStmt->execute();
    $deleteStmt->close();
    $conn->close();
    
    error_log("OTP Expired - Email: $email, Expires: $expiresAt, Current: " . date('Y-m-d H:i:s', $currentTime));
    ob_end_clean();
    
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'OTP has expired. Please request a new OTP.'
    ]);
    exit();
}

// OTP is valid - delete the record
$deleteStmt = $conn->prepare("DELETE FROM otp_verification WHERE id = ?");
if (!$deleteStmt) {
    error_log("Warning: Failed to prepare delete statement: " . $conn->error);
} else {
    $deleteStmt->bind_param("i", $recordId);
    if (!$deleteStmt->execute()) {
        error_log("Warning: Failed to delete OTP after verification: " . $deleteStmt->error);
    }
    $deleteStmt->close();
}

$conn->close();

error_log("OTP Verified Successfully - Email: $email");

ob_end_clean();
http_response_code(200);
echo json_encode([
    'success' => true,
    'message' => 'OTP verified successfully'
]);
