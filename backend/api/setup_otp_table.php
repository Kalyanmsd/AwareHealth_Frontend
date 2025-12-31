<?php
/**
 * AUTO SETUP OTP VERIFICATION TABLE
 * Creates otp_verification table automatically in phpMyAdmin
 * 
 * Access via: http://localhost/AwareHealth/api/setup_otp_table.php
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

// Database credentials
$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

// Connect to MySQL
$conn = new mysqli($dbHost, $dbUser, $dbPass);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

ob_end_clean();
ob_start();

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Setup OTP Verification Table</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 900px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
        .btn { display: inline-block; padding: 10px 20px; background: #34A853; color: white; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔐 OTP Verification Table Setup</h1>
        
<?php

try {
    // Step 1: Create database if it doesn't exist
    echo '<h2>Step 1: Creating database...</h2>';
    $createDb = "CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
    if ($conn->query($createDb)) {
        echo '<div class="success">✅ Database "' . $dbName . '" created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create database: " . $conn->error);
    }
    
    // Select the database
    $conn->select_db($dbName);
    
    // Step 2: Create otp_verification table
    echo '<h2>Step 2: Creating otp_verification table...</h2>';
    
    $createTable = "CREATE TABLE IF NOT EXISTS `otp_verification` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `email` VARCHAR(255) NOT NULL UNIQUE,
      `otp` VARCHAR(6) NOT NULL,
      `expires_at` DATETIME NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_email` (`email`),
      INDEX `idx_expires_at` (`expires_at`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createTable)) {
        echo '<div class="success">✅ OTP verification table created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create table: " . $conn->error);
    }
    
    // Step 3: Show table structure
    echo '<h2>Step 3: Table structure...</h2>';
    $columns = $conn->query("DESCRIBE otp_verification");
    
    if ($columns) {
        echo '<table border="1">';
        echo '<tr><th>Field</th><th>Type</th><th>Null</th><th>Key</th><th>Default</th><th>Extra</th></tr>';
        while ($col = $columns->fetch_assoc()) {
            echo '<tr>';
            echo '<td>' . htmlspecialchars($col['Field']) . '</td>';
            echo '<td>' . htmlspecialchars($col['Type']) . '</td>';
            echo '<td>' . htmlspecialchars($col['Null']) . '</td>';
            echo '<td>' . htmlspecialchars($col['Key']) . '</td>';
            echo '<td>' . htmlspecialchars($col['Default'] ?? 'NULL') . '</td>';
            echo '<td>' . htmlspecialchars($col['Extra']) . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    }
    
    // Step 4: Test endpoints
    echo '<h2>Step 4: API Endpoints</h2>';
    echo '<div class="info">';
    echo '<p><strong>Available endpoints:</strong></p>';
    echo '<ul>';
    echo '<li><strong>POST /api/send_otp.php</strong> - Send OTP to email</li>';
    echo '<li><strong>POST /api/verify_otp.php</strong> - Verify OTP</li>';
    echo '<li><strong>POST /api/resend_otp.php</strong> - Resend OTP</li>';
    echo '</ul>';
    echo '<p><strong>Example usage:</strong></p>';
    echo '<pre>POST http://localhost/AwareHealth/api/send_otp.php
Content-Type: application/json

{
  "email": "user@example.com"
}</pre>';
    echo '</div>';
    
    // Summary
    echo '<div class="success">';
    echo '<h2>✅ Setup Complete!</h2>';
    echo '<p><strong>Summary:</strong></p>';
    echo '<ul>';
    echo '<li>✅ Database "' . $dbName . '" exists</li>';
    echo '<li>✅ Table "otp_verification" created</li>';
    echo '<li>✅ All indexes created</li>';
    echo '</ul>';
    echo '<p><strong>Next Steps:</strong></p>';
    echo '<ol>';
    echo '<li>✅ Table is now in phpMyAdmin!</li>';
    echo '<li>View in phpMyAdmin: <a href="http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=awarehealth&table=otp_verification" target="_blank">View OTP Table</a></li>';
    echo '<li>Test API endpoints using Postman or your frontend</li>';
    echo '<li>Test send_otp: <a href="http://localhost/AwareHealth/api/send_otp.php" target="_blank">Send OTP</a></li>';
    echo '</ol>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">Make sure MySQL is running in XAMPP.</div>';
}

$conn->close();
ob_end_flush();
?>

    </div>
</body>
</html>

