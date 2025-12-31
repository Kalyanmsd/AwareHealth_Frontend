<?php
/**
 * SMTP Email Helper Functions
 * Sends OTP emails via SMTP using improved Gmail-compatible implementation
 */

require_once __DIR__ . '/../config.php';

/**
 * Read SMTP response (handles multi-line responses)
 */
function readSMTPResponse($socket) {
    $response = '';
    while ($line = fgets($socket, 515)) {
        $response .= $line;
        // Check if this is the last line (starts with 3 digits followed by space)
        if (preg_match('/^\d{3} /', $line)) {
            break;
        }
    }
    return $response;
}

/**
 * Send OTP email via SMTP
 */
function sendOTPEmail($to, $otp, $userName = 'User') {
    $smtpHost = SMTP_HOST;
    $smtpPort = SMTP_PORT;
    $smtpUser = SMTP_USERNAME;
    $smtpPass = SMTP_PASSWORD;
    $fromEmail = SMTP_FROM_EMAIL;
    $fromName = SMTP_FROM_NAME;
    
    $socket = null;
    
    try {
        // Create socket connection with TLS
        $context = stream_context_create([
            'ssl' => [
                'verify_peer' => false,
                'verify_peer_name' => false,
                'allow_self_signed' => true
            ]
        ]);
        
        $socket = @stream_socket_client(
            "tcp://$smtpHost:$smtpPort",
            $errno,
            $errstr,
            30,
            STREAM_CLIENT_CONNECT,
            $context
        );
        
        if (!$socket) {
            error_log("SMTP Connection failed: $errstr ($errno)");
            return false;
        }
        
        // Set timeout
        stream_set_timeout($socket, 30);
        
        // Read initial greeting
        $response = readSMTPResponse($socket);
        if (strpos($response, '220') === false) {
            error_log("SMTP Initial greeting failed: $response");
            fclose($socket);
            return false;
        }
        
        // Send EHLO
        fwrite($socket, "EHLO " . gethostname() . "\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '250') === false) {
            error_log("SMTP EHLO failed: $response");
            fclose($socket);
            return false;
        }
        
        // Start TLS
        fwrite($socket, "STARTTLS\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '220') === false) {
            error_log("SMTP STARTTLS failed: $response");
            fclose($socket);
            return false;
        }
        
        // Enable TLS encryption
        if (!stream_socket_enable_crypto($socket, true, STREAM_CRYPTO_METHOD_TLS_CLIENT)) {
            error_log("SMTP TLS encryption failed");
            fclose($socket);
            return false;
        }
        
        // EHLO again after TLS
        fwrite($socket, "EHLO " . gethostname() . "\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '250') === false) {
            error_log("SMTP EHLO after TLS failed: $response");
            fclose($socket);
            return false;
        }
        
        // Authenticate
        fwrite($socket, "AUTH LOGIN\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '334') === false) {
            error_log("SMTP AUTH LOGIN failed: $response");
            fclose($socket);
            return false;
        }
        
        // Send username
        fwrite($socket, base64_encode($smtpUser) . "\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '334') === false) {
            error_log("SMTP Username failed: $response");
            fclose($socket);
            return false;
        }
        
        // Send password (remove spaces from app password)
        $smtpPassClean = str_replace(' ', '', $smtpPass);
        fwrite($socket, base64_encode($smtpPassClean) . "\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '235') === false) {
            error_log("SMTP Authentication failed: $response");
            fclose($socket);
            return false;
        }
        
        // Set sender
        fwrite($socket, "MAIL FROM: <$fromEmail>\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '250') === false) {
            error_log("SMTP MAIL FROM failed: $response");
            fclose($socket);
            return false;
        }
        
        // Set recipient
        fwrite($socket, "RCPT TO: <$to>\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '250') === false && strpos($response, '251') === false) {
            error_log("SMTP RCPT TO failed: $response");
            fclose($socket);
            return false;
        }
        
        // Send email data
        fwrite($socket, "DATA\r\n");
        $response = readSMTPResponse($socket);
        if (strpos($response, '354') === false) {
            error_log("SMTP DATA failed: $response");
            fclose($socket);
            return false;
        }
        
        $subject = "AwareHealth - Password Reset OTP";
        $body = getOTPEmailTemplate($otp, $userName);
        
        // Build email headers and body
        $emailData = "From: $fromName <$fromEmail>\r\n";
        $emailData .= "To: $to\r\n";
        $emailData .= "Subject: $subject\r\n";
        $emailData .= "MIME-Version: 1.0\r\n";
        $emailData .= "Content-Type: text/html; charset=UTF-8\r\n";
        $emailData .= "Content-Transfer-Encoding: 8bit\r\n";
        $emailData .= "\r\n";
        $emailData .= $body . "\r\n";
        $emailData .= ".\r\n";
        
        fwrite($socket, $emailData);
        $response = readSMTPResponse($socket);
        if (strpos($response, '250') === false) {
            error_log("SMTP Email send failed: $response");
            fclose($socket);
            return false;
        }
        
        // Quit
        fwrite($socket, "QUIT\r\n");
        readSMTPResponse($socket);
        fclose($socket);
        
        error_log("OTP email sent successfully to: $to - OTP: $otp");
        return true;
        
    } catch (Exception $e) {
        error_log("Failed to send OTP email: " . $e->getMessage());
        if ($socket) {
            @fclose($socket);
        }
        return false;
    }
}

/**
 * Get OTP email HTML template
 */
function getOTPEmailTemplate($otp, $userName) {
    return "
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset='UTF-8'>
        <style>
            body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }
            .container { max-width: 600px; margin: 0 auto; padding: 20px; }
            .header { background-color: #AEE4C1; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
            .header h1 { margin: 0; color: #2D3748; font-size: 28px; }
            .content { padding: 40px 30px; background-color: #ffffff; border: 1px solid #E2E8F0; }
            .otp-box { background-color: #F7FAFC; border: 2px dashed #AEE4C1; padding: 20px; text-align: center; margin: 30px 0; border-radius: 10px; }
            .otp-code { font-size: 36px; font-weight: bold; color: #2D3748; letter-spacing: 8px; font-family: 'Courier New', monospace; }
            .footer { padding: 20px; text-align: center; font-size: 12px; color: #718096; background-color: #F7FAFC; border-radius: 0 0 10px 10px; }
            .warning { color: #E53E3E; font-weight: bold; margin-top: 20px; }
        </style>
    </head>
    <body>
        <div class='container'>
            <div class='header'>
                <h1>AwareHealth</h1>
            </div>
            <div class='content'>
                <h2 style='color: #2D3748; margin-top: 0;'>Password Reset OTP</h2>
                <p>Hello " . htmlspecialchars($userName) . ",</p>
                <p>We received a request to reset your password for your AwareHealth account.</p>
                <p>Use the following OTP code to reset your password:</p>
                
                <div class='otp-box'>
                    <div style='color: #718096; font-size: 14px; margin-bottom: 10px;'>Your OTP Code</div>
                    <div class='otp-code'>" . htmlspecialchars($otp) . "</div>
                </div>
                
                <p class='warning'>⚠️ This OTP will expire in 10 minutes.</p>
                
                <p>If you didn't request a password reset, please ignore this email. Your password will remain unchanged.</p>
                
                <p style='margin-top: 30px;'>Best regards,<br>The AwareHealth Team</p>
            </div>
            <div class='footer'>
                <p>© " . date('Y') . " AwareHealth. All rights reserved.</p>
                <p>This is an automated message, please do not reply.</p>
            </div>
        </div>
    </body>
    </html>
    ";
}

/**
 * Generate a 6-digit OTP
 */
function generateOTP() {
    return str_pad(mt_rand(0, 999999), 6, '0', STR_PAD_LEFT);
}

/**
 * Clean up expired tokens
 */
function cleanupExpiredTokens($conn) {
    $stmt = $conn->prepare("DELETE FROM password_reset_tokens WHERE expires_at < NOW() OR (otp_expires_at IS NOT NULL AND otp_expires_at < NOW()) OR used = 1");
    if ($stmt) {
        $stmt->execute();
        $stmt->close();
    }
}
?>

