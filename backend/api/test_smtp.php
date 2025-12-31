<?php
/**
 * Test SMTP Email Sending
 * Use this to test if OTP emails are working
 */

require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../includes/smtp_email.php';

header('Content-Type: application/json');

// Only allow POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendResponse(405, ['success' => false, 'message' => 'Method not allowed']);
}

$input = getJsonInput();

if (!isset($input['email'])) {
    sendResponse(400, ['success' => false, 'message' => 'Email is required']);
}

$email = sanitizeInput($input['email']);
$testOTP = generateOTP();

error_log("Testing SMTP email to: $email with OTP: $testOTP");

$result = sendOTPEmail($email, $testOTP, 'Test User');

if ($result) {
    sendResponse(200, [
        'success' => true,
        'message' => 'Test email sent successfully! Check your inbox.',
        'otp' => $testOTP // Only for testing - remove in production
    ]);
} else {
    sendResponse(500, [
        'success' => false,
        'message' => 'Failed to send test email. Check error logs.',
        'hint' => 'Check C:\\xampp\\apache\\logs\\error.log for details'
    ]);
}

