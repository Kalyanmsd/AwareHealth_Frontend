<?php
/**
 * Simple Email Test - Direct SMTP Test
 * This bypasses all the complex routing and tests SMTP directly
 */

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);
ini_set('log_errors', 1);

header('Content-Type: application/json');

// SMTP Configuration (directly here for testing)
$smtpHost = 'smtp.gmail.com';
$smtpPort = 587;
$smtpUser = 'chsaikalyan124@gmail.com';
$smtpPass = 'tgupnonavcasfxib'; // App password (no spaces)
$fromEmail = 'chsaikalyan124@gmail.com';
$fromName = 'AwareHealth';

// Get email from request
$toEmail = $_GET['email'] ?? $_POST['email'] ?? 'chsaikalyan124@gmail.com';
$testOTP = str_pad(rand(0, 999999), 6, '0', STR_PAD_LEFT);

$result = [
    'success' => false,
    'message' => '',
    'otp' => $testOTP,
    'to' => $toEmail,
    'steps' => []
];

try {
    // Step 1: Connect
    $result['steps']['1_connect'] = 'Attempting connection...';
    $socket = @fsockopen($smtpHost, $smtpPort, $errno, $errstr, 30);
    
    if (!$socket) {
        $result['message'] = "Connection failed: $errstr ($errno)";
        $result['steps']['1_connect'] = "FAILED: $errstr ($errno)";
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['1_connect'] = 'SUCCESS';
    
    // Step 2: Read greeting
    $result['steps']['2_greeting'] = 'Reading server greeting...';
    $response = fgets($socket, 515);
    if (strpos($response, '220') === false) {
        $result['message'] = "Greeting failed: $response";
        $result['steps']['2_greeting'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['2_greeting'] = 'SUCCESS';
    
    // Step 3: EHLO
    $result['steps']['3_ehlo'] = 'Sending EHLO...';
    fwrite($socket, "EHLO localhost\r\n");
    $response = '';
    while ($line = fgets($socket, 515)) {
        $response .= $line;
        if (preg_match('/^\d{3} /', $line)) break;
    }
    if (strpos($response, '250') === false) {
        $result['message'] = "EHLO failed: $response";
        $result['steps']['3_ehlo'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['3_ehlo'] = 'SUCCESS';
    
    // Step 4: STARTTLS
    $result['steps']['4_starttls'] = 'Starting TLS...';
    fwrite($socket, "STARTTLS\r\n");
    $response = fgets($socket, 515);
    if (strpos($response, '220') === false) {
        $result['message'] = "STARTTLS failed: $response";
        $result['steps']['4_starttls'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['4_starttls'] = 'SUCCESS';
    
    // Step 5: Enable TLS
    $result['steps']['5_enable_tls'] = 'Enabling TLS encryption...';
    if (!stream_socket_enable_crypto($socket, true, STREAM_CRYPTO_METHOD_TLS_CLIENT)) {
        $result['message'] = "TLS encryption failed";
        $result['steps']['5_enable_tls'] = 'FAILED';
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['5_enable_tls'] = 'SUCCESS';
    
    // Step 6: EHLO again
    $result['steps']['6_ehlo_again'] = 'Sending EHLO after TLS...';
    fwrite($socket, "EHLO localhost\r\n");
    $response = '';
    while ($line = fgets($socket, 515)) {
        $response .= $line;
        if (preg_match('/^\d{3} /', $line)) break;
    }
    if (strpos($response, '250') === false) {
        $result['message'] = "EHLO after TLS failed: $response";
        $result['steps']['6_ehlo_again'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['6_ehlo_again'] = 'SUCCESS';
    
    // Step 7: AUTH LOGIN
    $result['steps']['7_auth'] = 'Starting authentication...';
    fwrite($socket, "AUTH LOGIN\r\n");
    $response = fgets($socket, 515);
    if (strpos($response, '334') === false) {
        $result['message'] = "AUTH LOGIN failed: $response";
        $result['steps']['7_auth'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['7_auth'] = 'SUCCESS';
    
    // Step 8: Send username
    $result['steps']['8_username'] = 'Sending username...';
    fwrite($socket, base64_encode($smtpUser) . "\r\n");
    $response = fgets($socket, 515);
    if (strpos($response, '334') === false) {
        $result['message'] = "Username failed: $response";
        $result['steps']['8_username'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['8_username'] = 'SUCCESS';
    
    // Step 9: Send password
    $result['steps']['9_password'] = 'Sending password...';
    fwrite($socket, base64_encode($smtpPass) . "\r\n");
    $response = fgets($socket, 515);
    if (strpos($response, '235') === false) {
        $result['message'] = "Authentication failed: $response";
        $result['steps']['9_password'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['9_password'] = 'SUCCESS';
    
    // Step 10: MAIL FROM
    $result['steps']['10_mail_from'] = 'Setting sender...';
    fwrite($socket, "MAIL FROM: <$fromEmail>\r\n");
    $response = fgets($socket, 515);
    if (strpos($response, '250') === false) {
        $result['message'] = "MAIL FROM failed: $response";
        $result['steps']['10_mail_from'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['10_mail_from'] = 'SUCCESS';
    
    // Step 11: RCPT TO
    $result['steps']['11_rcpt_to'] = 'Setting recipient...';
    fwrite($socket, "RCPT TO: <$toEmail>\r\n");
    $response = fgets($socket, 515);
    if (strpos($response, '250') === false && strpos($response, '251') === false) {
        $result['message'] = "RCPT TO failed: $response";
        $result['steps']['11_rcpt_to'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['11_rcpt_to'] = 'SUCCESS';
    
    // Step 12: DATA
    $result['steps']['12_data'] = 'Starting email data...';
    fwrite($socket, "DATA\r\n");
    $response = fgets($socket, 515);
    if (strpos($response, '354') === false) {
        $result['message'] = "DATA failed: $response";
        $result['steps']['12_data'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['12_data'] = 'SUCCESS';
    
    // Step 13: Send email
    $result['steps']['13_send_email'] = 'Sending email content...';
    $subject = "AwareHealth - Test OTP";
    $body = "Your OTP code is: $testOTP\n\nThis is a test email from AwareHealth.";
    
    $emailData = "From: $fromName <$fromEmail>\r\n";
    $emailData .= "To: $toEmail\r\n";
    $emailData .= "Subject: $subject\r\n";
    $emailData .= "MIME-Version: 1.0\r\n";
    $emailData .= "Content-Type: text/plain; charset=UTF-8\r\n";
    $emailData .= "\r\n";
    $emailData .= $body . "\r\n";
    $emailData .= ".\r\n";
    
    fwrite($socket, $emailData);
    $response = fgets($socket, 515);
    if (strpos($response, '250') === false) {
        $result['message'] = "Email send failed: $response";
        $result['steps']['13_send_email'] = "FAILED: $response";
        fclose($socket);
        echo json_encode($result, JSON_PRETTY_PRINT);
        exit;
    }
    $result['steps']['13_send_email'] = 'SUCCESS';
    
    // Step 14: QUIT
    fwrite($socket, "QUIT\r\n");
    fgets($socket, 515);
    fclose($socket);
    
    $result['success'] = true;
    $result['message'] = "Email sent successfully! Check your inbox (and spam folder). OTP: $testOTP";
    $result['steps']['14_complete'] = 'SUCCESS';
    
} catch (Exception $e) {
    $result['message'] = "Exception: " . $e->getMessage();
    $result['steps']['exception'] = $e->getMessage();
    if (isset($socket)) {
        @fclose($socket);
    }
}

echo json_encode($result, JSON_PRETTY_PRINT);

