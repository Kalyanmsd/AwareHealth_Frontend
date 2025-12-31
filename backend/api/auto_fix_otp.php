<?php
/**
 * AUTOMATIC OTP FIX AND DATABASE SETUP
 * This script automatically fixes the OTP issue and sets up the database
 * Access via: http://localhost/AwareHealth/api/auto_fix_otp.php
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';

ob_end_clean();
ob_start();

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Auto Fix OTP - AwareHealth</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 900px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #2D3748; }
        .success { color: #34A853; background: #E8F5E9; padding: 15px; border-radius: 4px; margin: 10px 0; border-left: 4px solid #34A853; }
        .error { color: #EA4335; background: #FFEBEE; padding: 15px; border-radius: 4px; margin: 10px 0; border-left: 4px solid #EA4335; }
        .info { color: #1976D2; background: #E3F2FD; padding: 15px; border-radius: 4px; margin: 10px 0; border-left: 4px solid #1976D2; }
        .warning { color: #FB8C00; background: #FFF3E0; padding: 15px; border-radius: 4px; margin: 10px 0; border-left: 4px solid #FB8C00; }
        pre { background: #f5f5f5; padding: 15px; border-radius: 4px; overflow-x: auto; font-size: 12px; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        .btn { display: inline-block; padding: 10px 20px; background: #34A853; color: white; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
        .btn:hover { background: #2E7D32; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Automatic OTP Fix & Database Setup</h1>
        
<?php

try {
    $conn = getDB();
    echo '<div class="success">✅ Database connection successful!</div>';
    
    $steps = [];
    $errors = [];
    
    // Step 1: Create database if not exists
    echo '<h2>Step 1: Checking Database...</h2>';
    $dbCheck = $conn->query("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'awarehealth'");
    if ($dbCheck->num_rows === 0) {
        $conn->query("CREATE DATABASE IF NOT EXISTS awarehealth");
        $conn->select_db('awarehealth');
        $steps[] = "Created database 'awarehealth'";
        echo '<div class="success">✅ Database created</div>';
    } else {
        $conn->select_db('awarehealth');
        $steps[] = "Database 'awarehealth' exists";
        echo '<div class="success">✅ Database exists</div>';
    }
    
    // Step 2: Create password_reset_tokens table with proper structure
    echo '<h2>Step 2: Creating/Fixing password_reset_tokens Table...</h2>';
    
    $createTableSQL = "CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
      `id` VARCHAR(36) PRIMARY KEY,
      `user_id` VARCHAR(36) NOT NULL,
      `email` VARCHAR(255) NOT NULL,
      `token` VARCHAR(255) DEFAULT NULL,
      `otp` VARCHAR(6) DEFAULT NULL,
      `expires_at` TIMESTAMP NOT NULL,
      `otp_expires_at` DATETIME DEFAULT NULL,
      `used` TINYINT(1) DEFAULT 0,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_user_id` (`user_id`),
      INDEX `idx_email` (`email`),
      INDEX `idx_token` (`token`),
      INDEX `idx_otp` (`otp`),
      INDEX `idx_expires` (`expires_at`),
      INDEX `idx_otp_expires` (`otp_expires_at`),
      INDEX `idx_used` (`used`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createTableSQL)) {
        $steps[] = "Table created/verified";
        echo '<div class="success">✅ Table structure verified</div>';
    } else {
        $errors[] = "Failed to create table: " . $conn->error;
        echo '<div class="error">❌ Failed to create table: ' . htmlspecialchars($conn->error) . '</div>';
    }
    
    // Step 3: Add OTP columns if they don't exist
    echo '<h2>Step 3: Adding OTP Columns...</h2>';
    
    $columns = $conn->query("SHOW COLUMNS FROM password_reset_tokens");
    $columnNames = [];
    while ($col = $columns->fetch_assoc()) {
        $columnNames[] = $col['Field'];
    }
    
    if (!in_array('otp', $columnNames)) {
        $conn->query("ALTER TABLE password_reset_tokens ADD COLUMN otp VARCHAR(6) DEFAULT NULL AFTER token");
        $steps[] = "Added 'otp' column";
        echo '<div class="success">✅ Added OTP column</div>';
    } else {
        $steps[] = "OTP column exists";
        echo '<div class="info">ℹ️ OTP column already exists</div>';
    }
    
    if (!in_array('otp_expires_at', $columnNames)) {
        $conn->query("ALTER TABLE password_reset_tokens ADD COLUMN otp_expires_at DATETIME DEFAULT NULL AFTER expires_at");
        $steps[] = "Added 'otp_expires_at' column";
        echo '<div class="success">✅ Added OTP expiration column</div>';
    } else {
        $steps[] = "OTP expiration column exists";
        echo '<div class="info">ℹ️ OTP expiration column already exists</div>';
    }
    
    // Step 4: Add indexes
    echo '<h2>Step 4: Adding Indexes...</h2>';
    
    $indexes = $conn->query("SHOW INDEXES FROM password_reset_tokens");
    $indexNames = [];
    while ($idx = $indexes->fetch_assoc()) {
        $indexNames[] = $idx['Key_name'];
    }
    
    $indexesToAdd = [
        'idx_otp' => 'otp',
        'idx_otp_expires' => 'otp_expires_at',
        'idx_used' => 'used'
    ];
    
    foreach ($indexesToAdd as $idxName => $column) {
        if (!in_array($idxName, $indexNames)) {
            $conn->query("ALTER TABLE password_reset_tokens ADD INDEX $idxName ($column)");
            $steps[] = "Added index $idxName";
            echo '<div class="success">✅ Added index: ' . $idxName . '</div>';
        }
    }
    
    // Step 5: Verify table structure
    echo '<h2>Step 5: Verifying Table Structure...</h2>';
    
    $columns = $conn->query("SHOW COLUMNS FROM password_reset_tokens");
    echo '<table>';
    echo '<tr><th>Column</th><th>Type</th><th>Null</th><th>Default</th></tr>';
    $hasOtp = false;
    $hasOtpExpires = false;
    while ($col = $columns->fetch_assoc()) {
        echo '<tr>';
        echo '<td>' . htmlspecialchars($col['Field']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Type']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Null']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Default'] ?? 'NULL') . '</td>';
        echo '</tr>';
        if ($col['Field'] === 'otp') $hasOtp = true;
        if ($col['Field'] === 'otp_expires_at') $hasOtpExpires = true;
    }
    echo '</table>';
    
    if ($hasOtp && $hasOtpExpires) {
        echo '<div class="success">✅ Table structure is correct! OTP support is enabled.</div>';
        $steps[] = "Table structure verified - OTP support enabled";
    } else {
        echo '<div class="error">❌ Table structure is incomplete. Missing OTP columns.</div>';
        $errors[] = "Table structure incomplete";
    }
    
    // Step 6: Test OTP storage and retrieval
    echo '<h2>Step 6: Testing OTP Functionality...</h2>';
    
    $testEmail = 'test@example.com';
    $testOtp = '123456';
    $testUserId = 'test-user-id-' . time();
    $testTokenId = 'test-token-' . time();
    $expiresAt = date('Y-m-d H:i:s', strtotime('+1 hour'));
    $otpExpiresAt = date('Y-m-d H:i:s', strtotime('+10 minutes'));
    
    // Insert test OTP
    $testInsert = $conn->prepare("INSERT INTO password_reset_tokens (id, user_id, email, otp, expires_at, otp_expires_at, used) VALUES (?, ?, ?, ?, ?, ?, 0)");
    $testInsert->bind_param("ssssss", $testTokenId, $testUserId, $testEmail, $testOtp, $expiresAt, $otpExpiresAt);
    
    if ($testInsert->execute()) {
        echo '<div class="success">✅ Test OTP inserted successfully</div>';
        $steps[] = "Test OTP inserted";
        
        // Try to retrieve it
        $testSelect = $conn->prepare("SELECT id, email, otp, otp_expires_at, used FROM password_reset_tokens WHERE LOWER(email) = ? AND otp IS NOT NULL AND otp != '' AND (used = FALSE OR used = 0 OR used IS NULL) ORDER BY created_at DESC LIMIT 1");
        $testSelect->bind_param("s", $testEmail);
        $testSelect->execute();
        $testResult = $testSelect->get_result();
        
        if ($testResult->num_rows > 0) {
            $testRow = $testResult->fetch_assoc();
            if ($testRow['otp'] === $testOtp) {
                echo '<div class="success">✅ Test OTP retrieved successfully! OTP verification query works.</div>';
                $steps[] = "OTP retrieval test passed";
            } else {
                echo '<div class="error">❌ OTP mismatch. Stored: ' . htmlspecialchars($testRow['otp']) . ', Expected: ' . htmlspecialchars($testOtp) . '</div>';
                $errors[] = "OTP retrieval mismatch";
            }
        } else {
            echo '<div class="error">❌ Could not retrieve test OTP. Query returned no results.</div>';
            $errors[] = "OTP retrieval failed";
        }
        
        // Clean up test data
        $conn->query("DELETE FROM password_reset_tokens WHERE id = '$testTokenId'");
        echo '<div class="info">ℹ️ Test data cleaned up</div>';
    } else {
        echo '<div class="error">❌ Failed to insert test OTP: ' . htmlspecialchars($conn->error) . '</div>';
        $errors[] = "Test OTP insertion failed";
    }
    
    // Summary
    echo '<h2>Summary</h2>';
    echo '<div class="info">';
    echo '<h3>Steps Completed:</h3>';
    echo '<ul>';
    foreach ($steps as $step) {
        echo '<li>✅ ' . htmlspecialchars($step) . '</li>';
    }
    echo '</ul>';
    echo '</div>';
    
    if (count($errors) > 0) {
        echo '<div class="error">';
        echo '<h3>Errors:</h3>';
        echo '<ul>';
        foreach ($errors as $error) {
            echo '<li>❌ ' . htmlspecialchars($error) . '</li>';
        }
        echo '</ul>';
        echo '</div>';
    } else {
        echo '<div class="success">';
        echo '<h3>✅ All Steps Completed Successfully!</h3>';
        echo '<p><strong>OTP functionality should now work correctly.</strong></p>';
        echo '<p>Next steps:</p>';
        echo '<ol>';
        echo '<li>Restart Apache in XAMPP Control Panel</li>';
        echo '<li>Test OTP in your app</li>';
        echo '<li>If issues persist, check: <a href="http://localhost/AwareHealth/api/debug_otp?email=your@email.com" target="_blank">Debug OTP Endpoint</a></li>';
        echo '</ol>';
        echo '</div>';
    }
    
    // Show current OTP tokens (if any)
    echo '<h2>Current OTP Tokens in Database</h2>';
    $allTokens = $conn->query("SELECT id, email, otp, otp_expires_at, used, created_at FROM password_reset_tokens WHERE otp IS NOT NULL AND otp != '' ORDER BY created_at DESC LIMIT 10");
    
    if ($allTokens->num_rows > 0) {
        echo '<table>';
        echo '<tr><th>Email</th><th>OTP</th><th>Expires At</th><th>Used</th><th>Created</th></tr>';
        while ($token = $allTokens->fetch_assoc()) {
            $isExpired = strtotime($token['otp_expires_at']) < time();
            $rowClass = $isExpired ? 'style="background: #ffebee;"' : ($token['used'] ? 'style="background: #fff3e0;"' : '');
            echo '<tr ' . $rowClass . '>';
            echo '<td>' . htmlspecialchars($token['email']) . '</td>';
            echo '<td>' . htmlspecialchars($token['otp']) . '</td>';
            echo '<td>' . htmlspecialchars($token['otp_expires_at']) . ($isExpired ? ' (EXPIRED)' : '') . '</td>';
            echo '<td>' . ($token['used'] ? 'Yes' : 'No') . '</td>';
            echo '<td>' . htmlspecialchars($token['created_at']) . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    } else {
        echo '<div class="info">ℹ️ No OTP tokens found in database. Request an OTP from your app to test.</div>';
    }
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">Make sure MySQL is running in XAMPP and the database "awarehealth" exists.</div>';
}

ob_end_flush();
?>

    </div>
</body>
</html>

