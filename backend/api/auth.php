<?php
// Authentication endpoints

// Start output buffering IMMEDIATELY
ob_start();

// Turn off ALL error display
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
error_reporting(E_ALL & ~E_NOTICE & ~E_WARNING & ~E_DEPRECATED);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

// Discard ALL output from require statements
ob_end_clean();
ob_start();

// CORS headers
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    ob_end_clean();
    exit();
}

$segments = getPathSegments();
$method = $_SERVER['REQUEST_METHOD'];

// Get database connection
try {
    $conn = getDB();
} catch (Exception $e) {
    error_log("Auth API - Database connection error: " . $e->getMessage());
    ob_end_clean();
    sendResponse(500, [
        'success' => false,
        'message' => 'Database connection failed'
    ]);
}

$input = getJsonInput();

switch ($method) {
    case 'POST':
        $endpoint = $segments[1] ?? '';
        
        switch ($endpoint) {
            case 'register':
                $required = ['name', 'email', 'password', 'userType'];
                $missing = validateRequired($input, $required);
                
                if (!empty($missing)) {
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Missing required fields: ' . implode(', ', $missing)]);
                }
                
                $name = sanitizeInput($input['name']);
                $email = sanitizeInput($input['email']);
                $password = $input['password'];
                $userType = sanitizeInput($input['userType']);
                $phone = sanitizeInput($input['phone'] ?? '');
                
                // Check if email already exists
                $check = $conn->prepare("SELECT id FROM users WHERE email = ?");
                if (!$check) {
                    $error = $conn->error;
                    error_log("Prepare failed for email check: " . $error);
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database error: ' . $error]);
                }
                
                $check->bind_param("s", $email);
                if (!$check->execute()) {
                    $error = $check->error;
                    $check->close();
                    error_log("Email check execute failed: " . $error);
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database error: ' . $error]);
                }
                
                $result = $check->get_result();
                
                if ($result->num_rows > 0) {
                    $check->close();
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'User already exists']);
                }
                $check->close();
                
                $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
                $id = generateUUID();
                
                // Verify table structure first
                $tableCheck = $conn->query("SHOW COLUMNS FROM users");
                $columns = [];
                while ($row = $tableCheck->fetch_assoc()) {
                    $columns[] = $row['Field'];
                }
                
                // Check if required columns exist
                $requiredColumns = ['id', 'user_type', 'name', 'email', 'phone', 'password'];
                $missingColumns = array_diff($requiredColumns, $columns);
                
                if (!empty($missingColumns)) {
                    error_log("Missing columns in users table: " . implode(', ', $missingColumns));
                    ob_end_clean();
                    sendResponse(500, [
                        'success' => false,
                        'message' => 'Database table structure error. Missing columns: ' . implode(', ', $missingColumns)
                    ]);
                }
                
                $stmt = $conn->prepare("INSERT INTO users (id, user_type, name, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)");
                if (!$stmt) {
                    $error = $conn->error;
                    error_log("Prepare failed for user insert: " . $error);
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database error: ' . $error]);
                }
                
                $stmt->bind_param("ssssss", $id, $userType, $name, $email, $phone, $hashedPassword);
                
                if ($stmt->execute()) {
                    $doctorCreated = true;
                    
                    if ($userType === 'doctor') {
                        $doctorId = generateUUID();
                        $specialty = sanitizeInput($input['specialty'] ?? 'General');
                        $location = 'Saveetha Hospital'; // Set default location
                        
                        // Try to insert with user_id and location (new structure)
                        $stmt2 = $conn->prepare("INSERT INTO doctors (id, user_id, specialty, location) VALUES (?, ?, ?, ?)");
                        if ($stmt2) {
                            $stmt2->bind_param("ssss", $doctorId, $id, $specialty, $location);
                            if (!$stmt2->execute()) {
                                // If failed, try without location
                                $stmt2->close();
                                $stmt2 = $conn->prepare("INSERT INTO doctors (id, user_id, specialty) VALUES (?, ?, ?)");
                                if ($stmt2) {
                                    $stmt2->bind_param("sss", $doctorId, $id, $specialty);
                                    if (!$stmt2->execute()) {
                                        // If failed, try old structure without user_id
                                        $stmt2->close();
                                        $stmt2 = $conn->prepare("INSERT INTO doctors (id, name, specialty, location) VALUES (?, ?, ?, ?)");
                                        if ($stmt2) {
                                            $stmt2->bind_param("ssss", $doctorId, $name, $specialty, $location);
                                            if (!$stmt2->execute()) {
                                                error_log("Failed to create doctor record: " . $conn->error);
                                                $doctorCreated = false;
                                            }
                                        }
                                    }
                                }
                            }
                            if ($stmt2) {
                                $stmt2->close();
                            }
                        } else {
                            // Prepare failed, try old structure
                            error_log("Prepare failed for doctors table with user_id. Trying old structure.");
                            $stmt2 = $conn->prepare("INSERT INTO doctors (id, name, specialty, location) VALUES (?, ?, ?, ?)");
                            if ($stmt2) {
                                $stmt2->bind_param("ssss", $doctorId, $name, $specialty, $location);
                                if (!$stmt2->execute()) {
                                    error_log("Failed to create doctor record: " . $conn->error);
                                    $doctorCreated = false;
                                }
                                $stmt2->close();
                            }
                        }
                    }
                    
                    $stmt->close();
                    ob_end_clean();
                    sendResponse(201, [
                        'success' => true,
                        'message' => 'Registration successful',
                        'token' => base64_encode($id),
                        'user' => [
                            'id' => $id,
                            'name' => $name,
                            'email' => $email,
                            'userType' => $userType
                        ]
                    ]);
                } else {
                    $error = $stmt->error ?: $conn->error;
                    $stmt->close();
                    error_log("User registration failed: " . $error);
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Registration failed: ' . $error]);
                }
                break;
                
            case 'doctor-login':
                // Doctor login with ID
                $required = ['doctorId', 'password'];
                $missing = validateRequired($input, $required);
                
                if (!empty($missing)) {
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Doctor ID and password are required']);
                }
                
                $doctorId = sanitizeInput($input['doctorId']);
                $password = $input['password'];
                
                // Find doctor by doctor_id field first, then fallback to id
                // Support both doctor_id (like SAV001) and UUID id
                $stmt = $conn->prepare("
                    SELECT 
                        d.id as doctor_table_id,
                        d.doctor_id,
                        d.user_id,
                        u.id,
                        u.name,
                        u.email,
                        u.password,
                        u.user_type,
                        d.specialty,
                        d.location
                    FROM doctors d
                    JOIN users u ON d.user_id = u.id
                    WHERE (d.doctor_id = ? OR d.id = ?) AND u.user_type = 'doctor'
                ");
                
                if (!$stmt) {
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database query failed']);
                }
                
                $stmt->bind_param("ss", $doctorId, $doctorId);
                $stmt->execute();
                $result = $stmt->get_result();
                
                if ($result->num_rows === 0) {
                    $stmt->close();
                    ob_end_clean();
                    sendResponse(401, ['success' => false, 'message' => 'Invalid doctor ID. Please check your doctor ID and try again.']);
                }
                
                $doctor = $result->fetch_assoc();
                $stmt->close();
                
                // Verify password
                if (!password_verify($password, $doctor['password'])) {
                    ob_end_clean();
                    sendResponse(401, ['success' => false, 'message' => 'Invalid password']);
                }
                
                // Check if doctor is from Saveetha Hospital
                $location = $doctor['location'] ?? '';
                if ($location !== 'Saveetha Hospital' && !empty($location)) {
                    ob_end_clean();
                    sendResponse(403, ['success' => false, 'message' => 'Access denied. Only Saveetha Hospital doctors can login']);
                }
                
                ob_end_clean();
                sendResponse(200, [
                    'success' => true,
                    'message' => 'Login successful',
                    'token' => base64_encode($doctor['user_id']),
                    'user' => [
                        'id' => $doctor['user_id'],
                        'doctorId' => $doctor['doctor_table_id'], // Use doctor table id
                        'doctorIdShort' => $doctor['doctor_id'] ?? $doctor['doctor_table_id'], // Short ID if available
                        'name' => $doctor['name'],
                        'email' => $doctor['email'],
                        'userType' => 'doctor',
                        'specialty' => $doctor['specialty'] ?: 'General',
                        'location' => $location ?: 'Saveetha Hospital'
                    ]
                ]);
                break;
                
            case 'login':
                $required = ['email', 'password', 'userType'];
                $missing = validateRequired($input, $required);
                
                if (!empty($missing)) {
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Missing required fields: ' . implode(', ', $missing)]);
                }
                
                $email = sanitizeInput($input['email']);
                $password = $input['password'];
                $userType = sanitizeInput($input['userType']);
                
                $stmt = $conn->prepare("SELECT id, name, password FROM users WHERE email = ? AND user_type = ?");
                if (!$stmt) {
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database query failed']);
                }
                
                $stmt->bind_param("ss", $email, $userType);
                $stmt->execute();
                $result = $stmt->get_result();
                
                if ($result->num_rows == 0) {
                    $stmt->close();
                    ob_end_clean();
                    sendResponse(401, ['success' => false, 'message' => 'Invalid username and password']);
                }
                
                $user = $result->fetch_assoc();
                $stmt->close();
                
                if (!password_verify($password, $user['password'])) {
                    ob_end_clean();
                    sendResponse(401, ['success' => false, 'message' => 'Invalid username and password']);
                }
                
                ob_end_clean();
                sendResponse(200, [
                    'success' => true,
                    'message' => 'Login successful',
                    'token' => base64_encode($user['id']),
                    'user' => [
                        'id' => $user['id'],
                        'name' => $user['name'],
                        'email' => $email,
                        'userType' => $userType
                    ]
                ]);
                break;
                
            case 'forgot-password':
                $required = ['email'];
                $missing = validateRequired($input, $required);
                
                if (!empty($missing)) {
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Email is required']);
                }
                
                $email = strtolower(trim(sanitizeInput($input['email'])));
                
                $stmt = $conn->prepare("SELECT id, name FROM users WHERE LOWER(email) = ?");
                if (!$stmt) {
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database query failed']);
                }
                
                $stmt->bind_param("s", $email);
                $stmt->execute();
                $result = $stmt->get_result();
                
                if ($result->num_rows === 0) {
                    $stmt->close();
                    ob_end_clean();
                    sendResponse(200, [
                        'success' => true,
                        'message' => 'If the email exists, an OTP has been sent to your email'
                    ]);
                }
                
                $user = $result->fetch_assoc();
                $userId = $user['id'];
                $userName = $user['name'];
                $stmt->close();
                
                require_once __DIR__ . '/../includes/smtp_email.php';
                
                $otp = generateOTP();
                $tokenId = generateUUID();
                $expiresAt = date('Y-m-d H:i:s', strtotime('+1 hour'));
                $otpExpiresAt = date('Y-m-d H:i:s', strtotime('+10 minutes'));
                
                // Log OTP for debugging
                error_log("OTP Generated for email: $email, OTP: $otp, Token ID: $tokenId");
                
                $checkColumn = $conn->query("SHOW COLUMNS FROM password_reset_tokens LIKE 'otp'");
                if ($checkColumn && $checkColumn->num_rows > 0) {
                    $insertStmt = $conn->prepare("INSERT INTO password_reset_tokens (id, user_id, email, otp, expires_at, otp_expires_at) VALUES (?, ?, ?, ?, ?, ?)");
                    if ($insertStmt) {
                        $insertStmt->bind_param("ssssss", $tokenId, $userId, $email, $otp, $expiresAt, $otpExpiresAt);
                        
                        if ($insertStmt->execute()) {
                            error_log("OTP stored in database successfully for email: $email, OTP: $otp");
                            $emailSent = sendOTPEmail($email, $otp, $userName);
                            if (!$emailSent) {
                                $errorLogFile = ini_get('error_log') ?: 'C:\\xampp\\apache\\logs\\error.log';
                                error_log("Failed to send OTP email to: $email. OTP was: $otp. Check SMTP configuration.");
                                $insertStmt->close();
                                ob_end_clean();
                                sendResponse(500, [
                                    'success' => false,
                                    'message' => 'OTP generated but email failed to send. Check SMTP settings.',
                                    'error' => 'SMTP send failed. Check error logs for details.',
                                    'hint' => 'Verify Gmail app password and SMTP configuration in config.php'
                                ]);
                            }
                            cleanupExpiredTokens($conn);
                            $insertStmt->close();
                            ob_end_clean();
                            sendResponse(200, [
                                'success' => true,
                                'message' => 'If the email exists, an OTP has been sent to your email'
                            ]);
                        } else {
                            $insertStmt->close();
                            error_log("Failed to insert OTP into database: " . $conn->error);
                            ob_end_clean();
                            sendResponse(500, ['success' => false, 'message' => 'Failed to generate OTP']);
                        }
                    } else {
                        error_log("Failed to prepare OTP insert statement: " . $conn->error);
                        ob_end_clean();
                        sendResponse(500, ['success' => false, 'message' => 'Failed to generate OTP']);
                    }
                } else {
                    $alterStmt = $conn->query("ALTER TABLE password_reset_tokens ADD COLUMN otp VARCHAR(6) DEFAULT NULL AFTER token, ADD COLUMN otp_expires_at DATETIME DEFAULT NULL AFTER expires_at, ADD INDEX idx_otp (otp)");
                    if ($alterStmt) {
                        $insertStmt = $conn->prepare("INSERT INTO password_reset_tokens (id, user_id, email, otp, expires_at, otp_expires_at) VALUES (?, ?, ?, ?, ?, ?)");
                        if ($insertStmt) {
                            $insertStmt->bind_param("ssssss", $tokenId, $userId, $email, $otp, $expiresAt, $otpExpiresAt);
                            if ($insertStmt->execute()) {
                                error_log("OTP stored in database successfully for email: $email, OTP: $otp");
                                $emailSent = sendOTPEmail($email, $otp, $userName);
                                cleanupExpiredTokens($conn);
                                
                                if ($emailSent) {
                                    error_log("OTP email sent successfully to: $email - OTP: $otp");
                                    $insertStmt->close();
                                    ob_end_clean();
                                    sendResponse(200, [
                                        'success' => true,
                                        'message' => 'If the email exists, an OTP has been sent to your email'
                                    ]);
                                } else {
                                    $insertStmt->close();
                                    error_log("Failed to send OTP email to: $email. OTP was: $otp. Check SMTP configuration.");
                                    ob_end_clean();
                                    sendResponse(500, [
                                        'success' => false,
                                        'message' => 'OTP generated but email failed to send. Check SMTP settings.',
                                        'error' => 'SMTP send failed. Check error logs for details.'
                                    ]);
                                }
                            } else {
                                $insertStmt->close();
                                error_log("Failed to insert OTP into database: " . $conn->error);
                                ob_end_clean();
                                sendResponse(500, ['success' => false, 'message' => 'Failed to generate OTP']);
                            }
                        } else {
                            error_log("Failed to prepare OTP insert statement: " . $conn->error);
                            ob_end_clean();
                            sendResponse(500, ['success' => false, 'message' => 'Failed to generate OTP']);
                        }
                    } else {
                        error_log("Failed to alter password_reset_tokens table: " . $conn->error);
                        ob_end_clean();
                        sendResponse(500, ['success' => false, 'message' => 'Failed to generate OTP']);
                    }
                }
                break;
                
            case 'verify-otp':
                $required = ['email', 'otp'];
                $missing = validateRequired($input, $required);
                
                if (!empty($missing)) {
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Email and OTP are required']);
                }
                
                $email = strtolower(trim(sanitizeInput($input['email'])));
                $otp = trim($input['otp']); // Don't sanitize OTP, just trim
                
                // Remove any non-numeric characters from OTP (in case user entered spaces or dashes)
                $otp = preg_replace('/[^0-9]/', '', $otp);
                
                error_log("OTP Verification attempt - Email: $email, OTP: $otp");
                
                // First, check if table exists
                $tableCheck = $conn->query("SHOW TABLES LIKE 'password_reset_tokens'");
                if (!$tableCheck || $tableCheck->num_rows === 0) {
                    error_log("password_reset_tokens table does not exist");
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database table not found. Please contact administrator.']);
                }
                
                // Get all tokens for this email (not used) to check OTP - use case-insensitive email comparison
                // Handle both boolean and integer 'used' column values (FALSE, 0, NULL, '0', 'false')
                // Also check if otp_expires_at is not null and not expired
                // IMPORTANT: Check for used column in multiple ways to handle different MySQL versions
                $query = "SELECT id, user_id, email, otp, otp_expires_at, used, created_at 
                         FROM password_reset_tokens 
                         WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))
                         AND otp IS NOT NULL 
                         AND otp != '' 
                         AND TRIM(otp) != ''
                         AND (used = 0 OR used IS NULL OR used = FALSE OR CAST(used AS CHAR) = '0' OR CAST(used AS CHAR) = 'false')
                         ORDER BY created_at DESC LIMIT 10";
                
                $stmt = $conn->prepare($query);
                if (!$stmt) {
                    error_log("Prepare failed for OTP verification: " . $conn->error);
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database query failed: ' . $conn->error]);
                }
                
                $stmt->bind_param("s", $email);
                if (!$stmt->execute()) {
                    $error = $stmt->error;
                    $stmt->close();
                    error_log("Execute failed for OTP verification: " . $error);
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database query failed: ' . $error]);
                }
                
                $result = $stmt->get_result();
                
                if ($result->num_rows === 0) {
                    $stmt->close();
                    error_log("No OTP tokens found for email: $email (checked for unused, non-empty OTPs)");
                    
                    // Double check: maybe OTP exists but is marked as used or expired
                    $checkAll = $conn->prepare("SELECT COUNT(*) as total, 
                        SUM(CASE WHEN otp IS NOT NULL AND otp != '' THEN 1 ELSE 0 END) as with_otp,
                        SUM(CASE WHEN (used = TRUE OR used = 1 OR used = '1' OR used = 'true') THEN 1 ELSE 0 END) as used_count
                        FROM password_reset_tokens WHERE LOWER(email) = ?");
                    if ($checkAll) {
                        $checkAll->bind_param("s", $email);
                        $checkAll->execute();
                        $checkResult = $checkAll->get_result();
                        $checkRow = $checkResult->fetch_assoc();
                        error_log("Debug info for email $email - Total tokens: " . $checkRow['total'] . ", With OTP: " . $checkRow['with_otp'] . ", Used: " . $checkRow['used_count']);
                        $checkAll->close();
                    }
                    
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'No OTP found for this email. Please request a new OTP.']);
                }
                
                error_log("Found " . $result->num_rows . " OTP token(s) for email: $email");
                
                // Check each token to find matching OTP
                $tokenFound = false;
                $validToken = null;
                $checkedOTPs = [];
                $expiredCount = 0;
                $mismatchCount = 0;
                
                while ($row = $result->fetch_assoc()) {
                    $storedOTP = trim($row['otp']);
                    
                    // Check if OTP is NULL or empty
                    if (empty($storedOTP)) {
                        error_log("Skipping token with empty OTP - Token ID: " . $row['id']);
                        continue;
                    }
                    
                    // Remove non-numeric characters from stored OTP for comparison
                    $storedOTP = preg_replace('/[^0-9]/', '', $storedOTP);
                    
                    $checkedOTPs[] = $storedOTP;
                    
                    error_log("Checking OTP - Stored: '$storedOTP' (length: " . strlen($storedOTP) . "), Entered: '$otp' (length: " . strlen($otp) . "), Email: " . $row['email'] . ", Expires: " . $row['otp_expires_at']);
                    
                    // Compare OTPs (exact match first, then case-insensitive)
                    if ($storedOTP === $otp || strcasecmp($storedOTP, $otp) === 0) {
                        // Check expiration
                        $otpExpiresAt = null;
                        if (!empty($row['otp_expires_at'])) {
                            $otpExpiresAt = strtotime($row['otp_expires_at']);
                        }
                        $currentTime = time();
                        
                        if ($otpExpiresAt === null || $otpExpiresAt === false) {
                            // No expiration set, consider it valid
                            $tokenFound = true;
                            $validToken = $row;
                            error_log("OTP matched (no expiration set)! Token ID: " . $row['id']);
                            break;
                        } elseif ($currentTime <= $otpExpiresAt) {
                            $tokenFound = true;
                            $validToken = $row;
                            error_log("OTP matched and valid! Token ID: " . $row['id']);
                            break;
                        } else {
                            $expiredCount++;
                            error_log("OTP expired for email: $email. Expires at: " . $row['otp_expires_at'] . ", Current time: " . date('Y-m-d H:i:s', $currentTime));
                        }
                    } else {
                        $mismatchCount++;
                        error_log("OTP mismatch - Stored: '$storedOTP' (length: " . strlen($storedOTP) . "), Entered: '$otp' (length: " . strlen($otp) . ")");
                    }
                }
                
                $stmt->close();
                
                if (!$tokenFound || !$validToken) {
                    $errorDetails = "Email: $email, Entered OTP: $otp, Checked OTPs: " . implode(', ', $checkedOTPs);
                    if ($expiredCount > 0) {
                        $errorDetails .= ", Expired: $expiredCount";
                    }
                    if ($mismatchCount > 0) {
                        $errorDetails .= ", Mismatched: $mismatchCount";
                    }
                    error_log("OTP verification failed - $errorDetails");
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Invalid or expired OTP. Please request a new OTP.']);
                }
                
                error_log("OTP verified successfully for email: $email");
                
                // OTP is valid - DON'T mark as used yet (only mark during password reset)
                // This allows navigation to reset password screen
                ob_end_clean();
                sendResponse(200, [
                    'success' => true,
                    'message' => 'OTP verified successfully'
                ]);
                break;
                
            case 'reset-password':
                $required = ['email', 'otp', 'newPassword'];
                $missing = validateRequired($input, $required);
                
                if (!empty($missing)) {
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Email, OTP, and new password are required']);
                }
                
                $email = strtolower(trim(sanitizeInput($input['email'])));
                $otp = trim($input['otp']); // Don't sanitize OTP, just trim
                $newPassword = $input['newPassword'];
                
                error_log("Password reset attempt - Email: $email, OTP: $otp");
                
                // Find token with matching OTP (not used yet) - use case-insensitive email comparison
                // Handle both boolean and integer 'used' column values
                $stmt = $conn->prepare("SELECT id, user_id, otp, otp_expires_at FROM password_reset_tokens WHERE LOWER(email) = ? AND (used = FALSE OR used = 0 OR used IS NULL) ORDER BY created_at DESC LIMIT 5");
                if (!$stmt) {
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database query failed']);
                }
                
                $stmt->bind_param("s", $email);
                $stmt->execute();
                $result = $stmt->get_result();
                
                $tokenFound = false;
                $validToken = null;
                
                while ($row = $result->fetch_assoc()) {
                    $storedOTP = trim($row['otp']);
                    if (strcasecmp($storedOTP, $otp) === 0) {
                        $otpExpiresAt = strtotime($row['otp_expires_at']);
                        if ($otpExpiresAt && time() <= $otpExpiresAt) {
                            $tokenFound = true;
                            $validToken = $row;
                            break;
                        }
                    }
                }
                
                if (!$tokenFound || !$validToken) {
                    $stmt->close();
                    ob_end_clean();
                    sendResponse(400, ['success' => false, 'message' => 'Invalid or expired OTP. Please verify OTP again.']);
                }
                
                $userId = $validToken['user_id'];
                $tokenId = $validToken['id'];
                $stmt->close();
                
                $hashedPassword = password_hash($newPassword, PASSWORD_DEFAULT);
                
                $updateStmt = $conn->prepare("UPDATE users SET password = ? WHERE id = ?");
                if (!$updateStmt) {
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Database query failed']);
                }
                
                $updateStmt->bind_param("ss", $hashedPassword, $userId);
                
                if ($updateStmt->execute()) {
                    // Mark OTP as used after successful password reset
                    $markUsedStmt = $conn->prepare("UPDATE password_reset_tokens SET used = TRUE WHERE id = ?");
                    if ($markUsedStmt) {
                        $markUsedStmt->bind_param("s", $tokenId);
                        $markUsedStmt->execute();
                        $markUsedStmt->close();
                    }
                    
                    $updateStmt->close();
                    ob_end_clean();
                    sendResponse(200, [
                        'success' => true,
                        'message' => 'Password reset successfully'
                    ]);
                } else {
                    $error = $updateStmt->error;
                    $updateStmt->close();
                    error_log("Password reset failed: " . $error);
                    ob_end_clean();
                    sendResponse(500, ['success' => false, 'message' => 'Password reset failed']);
                }
                break;
                
            default:
                ob_end_clean();
                sendResponse(404, ['success' => false, 'message' => 'Endpoint not found']);
                break;
        }
        break;
        
    default:
        ob_end_clean();
        sendResponse(405, ['success' => false, 'message' => 'Method not allowed']);
        break;
}
