<?php
/**
 * Send OTP - Generate and store OTP for email
 * POST: { "email": "user@example.com" }
 */

// Set headers FIRST before any output
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Debug: Log that send_otp.php was called
error_log("=== send_otp.php CALLED ===");
error_log("Request Method: " . $_SERVER['REQUEST_METHOD']);
error_log("Request URI: " . $_SERVER['REQUEST_URI']);
error_log("Remote Address: " . ($_SERVER['REMOTE_ADDR'] ?? 'unknown'));
error_log("Server Name: " . ($_SERVER['SERVER_NAME'] ?? 'unknown'));
error_log("HTTP Host: " . ($_SERVER['HTTP_HOST'] ?? 'unknown'));

// Handle OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Only allow POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
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
error_log("Send OTP Request - Raw input: " . $rawInput);

// Validate JSON
if (json_last_error() !== JSON_ERROR_NONE) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid JSON: ' . json_last_error_msg()
    ]);
    exit();
}

// Validate email
if (!isset($input['email']) || empty(trim($input['email']))) {
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
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $e->getMessage()
    ]);
    exit();
}

// Check if table exists, if not create it
$tableCheck = $conn->query("SHOW TABLES LIKE 'otp_verification'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    // Create table if it doesn't exist
    $createTable = "CREATE TABLE IF NOT EXISTS `otp_verification` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `email` VARCHAR(255) NOT NULL UNIQUE,
      `otp` VARCHAR(6) NOT NULL,
      `expires_at` DATETIME NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_email` (`email`),
      INDEX `idx_expires_at` (`expires_at`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if (!$conn->query($createTable)) {
        $conn->close();
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Failed to create OTP table: ' . $conn->error
        ]);
        exit();
    }
    error_log("OTP verification table created");
}

// Generate 6-digit OTP
$otp = str_pad(rand(0, 999999), 6, '0', STR_PAD_LEFT);

// Set expiry to 5 minutes from now
$expiresAt = date('Y-m-d H:i:s', strtotime('+5 minutes'));

// Log OTP generation
error_log("OTP Generated - Email: $email, OTP: $otp, Expires: $expiresAt");

// Delete existing OTP for this email
$deleteStmt = $conn->prepare("DELETE FROM otp_verification WHERE email = ?");
if (!$deleteStmt) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database error (delete): ' . $conn->error
    ]);
    exit();
}

$deleteStmt->bind_param("s", $email);
$deleteStmt->execute();
$deletedRows = $deleteStmt->affected_rows;
$deleteStmt->close();

error_log("Deleted $deletedRows old OTP record(s) for email: $email");

// Insert new OTP using INSERT ... ON DUPLICATE KEY UPDATE
$insertStmt = $conn->prepare("INSERT INTO otp_verification (email, otp, expires_at) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE otp = ?, expires_at = ?");
if (!$insertStmt) {
    // Fallback to simple INSERT if ON DUPLICATE doesn't work
    $insertStmt = $conn->prepare("INSERT INTO otp_verification (email, otp, expires_at) VALUES (?, ?, ?)");
    if (!$insertStmt) {
        $conn->close();
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Database prepare error: ' . $conn->error
        ]);
        exit();
    }
    $insertStmt->bind_param("sss", $email, $otp, $expiresAt);
} else {
    $insertStmt->bind_param("sssss", $email, $otp, $expiresAt, $otp, $expiresAt);
}

if ($insertStmt->execute()) {
    $insertedId = $conn->insert_id;
    error_log("✅ OTP INSERT EXECUTED - Insert ID: $insertedId, Email: $email, OTP: $otp");
    $insertStmt->close();
    
    // IMMEDIATELY verify the insert by checking the database
    $verifyStmt = $conn->prepare("SELECT id, email, otp, expires_at FROM otp_verification WHERE email = ?");
    if (!$verifyStmt) {
        error_log("❌ Failed to prepare verify statement: " . $conn->error);
    } else {
        $verifyStmt->bind_param("s", $email);
        if ($verifyStmt->execute()) {
            $verifyResult = $verifyStmt->get_result();
            
            if ($verifyResult->num_rows > 0) {
                $verifyRow = $verifyResult->fetch_assoc();
                error_log("✅ OTP VERIFIED IN DB - ID: " . $verifyRow['id'] . ", Email: " . $verifyRow['email'] . ", OTP: " . $verifyRow['otp']);
                
                // Double check: Count all records
                $countResult = $conn->query("SELECT COUNT(*) as total FROM otp_verification");
                if ($countResult) {
                    $countRow = $countResult->fetch_assoc();
                    error_log("📊 Total OTP records in table: " . $countRow['total']);
                }
                
                $verifyStmt->close();
                $conn->close();
                
                http_response_code(200);
                echo json_encode([
                    'success' => true,
                    'message' => 'OTP sent successfully',
                    'otp' => $otp, // For testing - remove in production
                    'expires_at' => $expiresAt,
                    'inserted_id' => $verifyRow['id'],
                    'debug' => [
                        'table_count' => $countRow['total'] ?? 0,
                        'email' => $email,
                        'inserted_id' => $insertedId
                    ]
                ]);
                exit();
            } else {
                error_log("❌ CRITICAL: OTP NOT FOUND after insert! Insert ID was: $insertedId");
                
                // Check if any records exist at all
                $allRecords = $conn->query("SELECT * FROM otp_verification");
                if ($allRecords) {
                    error_log("❌ Total records in table: " . $allRecords->num_rows);
                }
                
                $verifyStmt->close();
                $conn->close();
                http_response_code(500);
                echo json_encode([
                    'success' => false,
                    'message' => 'OTP was inserted but could not be verified in database',
                    'debug' => [
                        'inserted_id' => $insertedId,
                        'email' => $email,
                        'error' => 'Insert succeeded but record not found on verification'
                    ]
                ]);
                exit();
            }
        } else {
            error_log("❌ Failed to execute verify statement: " . $verifyStmt->error);
            $verifyStmt->close();
        }
    }
    
    $conn->close();
    http_response_code(200);
    echo json_encode([
        'success' => true,
        'message' => 'OTP sent successfully',
        'otp' => $otp,
        'expires_at' => $expiresAt,
        'inserted_id' => $insertedId
    ]);
} else {
    $error = $conn->error;
    $insertStmt->close();
    $conn->close();
    
    error_log("OTP Insert Failed - Email: $email, Error: $error, Error Code: " . $conn->errno);
    
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to generate OTP: ' . $error,
        'error_code' => $conn->errno
    ]);
}
