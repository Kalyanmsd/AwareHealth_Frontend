<?php
/**
 * AUTO CREATE OTP VERIFICATION TABLE
 * Automatically creates otp_verification table in awarehealth database
 * 
 * Access via: http://localhost/AwareHealth/api/auto_create_otp_table.php
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
    <title>Auto Create OTP Verification Table</title>
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
    </style>
</head>
<body>
    <div class="container">
        <h1>🔐 Auto Create OTP Verification Table</h1>
        
<?php

try {
    // Step 1: Select database
    echo '<h2>Step 1: Selecting database...</h2>';
    if (!$conn->select_db($dbName)) {
        // Create database if it doesn't exist
        $createDb = "CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        if ($conn->query($createDb)) {
            echo '<div class="success">✅ Database "' . $dbName . '" created!</div>';
            $conn->select_db($dbName);
        } else {
            throw new Exception("Failed to create/select database: " . $conn->error);
        }
    } else {
        echo '<div class="success">✅ Database "' . $dbName . '" selected!</div>';
    }
    
    // Step 2: Check if table exists
    echo '<h2>Step 2: Checking if table exists...</h2>';
    $tableCheck = $conn->query("SHOW TABLES LIKE 'otp_verification'");
    
    if ($tableCheck && $tableCheck->num_rows > 0) {
        echo '<div class="info">ℹ️ Table "otp_verification" already exists. Dropping and recreating...</div>';
        $dropTable = $conn->query("DROP TABLE IF EXISTS `otp_verification`");
        if ($dropTable) {
            echo '<div class="success">✅ Old table dropped</div>';
        }
    }
    
    // Step 3: Create table
    echo '<h2>Step 3: Creating otp_verification table...</h2>';
    
    $createTable = "CREATE TABLE `otp_verification` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `email` VARCHAR(255) NOT NULL UNIQUE,
      `otp` VARCHAR(6) NOT NULL,
      `expires_at` DATETIME NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_email` (`email`),
      INDEX `idx_expires_at` (`expires_at`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createTable)) {
        echo '<div class="success">✅ Table "otp_verification" created successfully!</div>';
    } else {
        throw new Exception("Failed to create table: " . $conn->error);
    }
    
    // Step 4: Verify table structure
    echo '<h2>Step 4: Verifying table structure...</h2>';
    $columns = $conn->query("DESCRIBE otp_verification");
    
    if ($columns) {
        echo '<table border="1">';
        echo '<tr><th>Field</th><th>Type</th><th>Null</th><th>Key</th><th>Default</th><th>Extra</th></tr>';
        while ($col = $columns->fetch_assoc()) {
            echo '<tr>';
            echo '<td><strong>' . htmlspecialchars($col['Field']) . '</strong></td>';
            echo '<td>' . htmlspecialchars($col['Type']) . '</td>';
            echo '<td>' . htmlspecialchars($col['Null']) . '</td>';
            echo '<td>' . htmlspecialchars($col['Key']) . '</td>';
            echo '<td>' . htmlspecialchars($col['Default'] ?? 'NULL') . '</td>';
            echo '<td>' . htmlspecialchars($col['Extra']) . '</td>';
            echo '</tr>';
        }
        echo '</table>';
        echo '<div class="success">✅ Table structure verified!</div>';
    }
    
    // Step 5: Show indexes
    echo '<h2>Step 5: Verifying indexes...</h2>';
    $indexes = $conn->query("SHOW INDEXES FROM otp_verification");
    
    if ($indexes) {
        echo '<table border="1">';
        echo '<tr><th>Key Name</th><th>Column</th><th>Non Unique</th></tr>';
        while ($idx = $indexes->fetch_assoc()) {
            echo '<tr>';
            echo '<td>' . htmlspecialchars($idx['Key_name']) . '</td>';
            echo '<td>' . htmlspecialchars($idx['Column_name']) . '</td>';
            echo '<td>' . htmlspecialchars($idx['Non_unique']) . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    }
    
    // Step 6: Test insert (optional)
    echo '<h2>Step 6: Testing table (insert test record)...</h2>';
    $testEmail = 'test@example.com';
    $testOTP = '123456';
    $testExpires = date('Y-m-d H:i:s', strtotime('+5 minutes'));
    
    $testStmt = $conn->prepare("INSERT INTO otp_verification (email, otp, expires_at) VALUES (?, ?, ?)");
    $testStmt->bind_param("sss", $testEmail, $testOTP, $testExpires);
    
    if ($testStmt->execute()) {
        echo '<div class="success">✅ Test record inserted successfully!</div>';
        
        // Delete test record
        $conn->query("DELETE FROM otp_verification WHERE email = 'test@example.com'");
        echo '<div class="info">ℹ️ Test record deleted</div>';
    } else {
        echo '<div class="error">❌ Test insert failed: ' . $conn->error . '</div>';
    }
    $testStmt->close();
    
    // Summary
    echo '<div class="success">';
    echo '<h2>✅ Setup Complete!</h2>';
    echo '<p><strong>Summary:</strong></p>';
    echo '<ul>';
    echo '<li>✅ Database "' . $dbName . '" selected</li>';
    echo '<li>✅ Table "otp_verification" created</li>';
    echo '<li>✅ All columns created</li>';
    echo '<li>✅ All indexes created</li>';
    echo '<li>✅ Table tested and working</li>';
    echo '</ul>';
    echo '<p><strong>✅ The table is now visible in phpMyAdmin!</strong></p>';
    echo '<p><strong>Next Steps:</strong></p>';
    echo '<ol>';
    echo '<li>✅ Refresh phpMyAdmin: <a href="http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth" target="_blank">View Database Structure</a></li>';
    echo '<li>✅ You should see "otp_verification" table in the list</li>';
    echo '<li>✅ Test API: <a href="http://localhost/AwareHealth/api/send_otp.php" target="_blank">Send OTP</a></li>';
    echo '</ol>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">Make sure MySQL is running in XAMPP Control Panel.</div>';
}

$conn->close();
ob_end_flush();
?>

    </div>
</body>
</html>

