<?php
/**
 * Debug SMTP Connection
 * This will show detailed SMTP connection information
 */

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/smtp_email.php';

header('Content-Type: application/json');

$debug = [
    'smtp_config' => [
        'host' => SMTP_HOST,
        'port' => SMTP_PORT,
        'username' => SMTP_USERNAME,
        'password_length' => strlen(SMTP_PASSWORD),
        'from_email' => SMTP_FROM_EMAIL,
        'secure' => SMTP_SECURE
    ],
    'test_results' => []
];

// Test 1: Check if functions exist
$debug['test_results']['functions_exist'] = [
    'sendOTPEmail' => function_exists('sendOTPEmail'),
    'generateOTP' => function_exists('generateOTP'),
    'readSMTPResponse' => function_exists('readSMTPResponse')
];

// Test 2: Test OTP generation
$testOTP = generateOTP();
$debug['test_results']['otp_generation'] = [
    'success' => !empty($testOTP),
    'otp' => $testOTP,
    'length' => strlen($testOTP)
];

// Test 3: Test SMTP connection (without sending email)
$smtpHost = SMTP_HOST;
$smtpPort = SMTP_PORT;

$debug['test_results']['smtp_connection'] = [
    'host' => $smtpHost,
    'port' => $smtpPort,
    'can_connect' => false,
    'error' => null
];

// Try to connect
$socket = @fsockopen($smtpHost, $smtpPort, $errno, $errstr, 10);
if ($socket) {
    $debug['test_results']['smtp_connection']['can_connect'] = true;
    fclose($socket);
} else {
    $debug['test_results']['smtp_connection']['error'] = "$errstr ($errno)";
}

// Test 4: Try sending test email (if email provided)
if (isset($_GET['email']) || isset($_POST['email'])) {
    $testEmail = $_GET['email'] ?? $_POST['email'];
    $debug['test_results']['email_send'] = [
        'email' => $testEmail,
        'attempted' => true,
        'success' => false,
        'error' => null
    ];
    
    $result = sendOTPEmail($testEmail, $testOTP, 'Debug Test');
    $debug['test_results']['email_send']['success'] = $result;
    
    if (!$result) {
        $debug['test_results']['email_send']['error'] = 'Check error logs for details';
    }
}

// Test 5: Check PHP error reporting
$debug['test_results']['php_config'] = [
    'error_reporting' => error_reporting(),
    'display_errors' => ini_get('display_errors'),
    'log_errors' => ini_get('log_errors'),
    'error_log' => ini_get('error_log')
];

echo json_encode($debug, JSON_PRETTY_PRINT);

