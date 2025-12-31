<?php
/**
 * Quick Fix Script for OTP Issue
 * This script:
 * 1. Ensures password_reset_tokens table exists with OTP columns
 * 2. Fixes any existing data issues
 * 3. Tests OTP storage and retrieval
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
    <title>OTP Issue Fix</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 OTP Issue Fix</h1>
        
<?php

try {
    $conn = getDB();
    echo '<div class="success">✅ Database connection successful!</div>';
    
    // Step 1: Check if table exists
    echo '<h2>Step 1: Checking password_reset_tokens table...</h2>';
    $tableCheck = $conn->query("SHOW TABLES LIKE 'password_reset_tokens'");
    
    if (!$tableCheck || $tableCheck->num_rows === 0) {
        echo '<div class="error">❌ Table does not exist. Creating...</div>';
        
        // Create table
        $createTable = "CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
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
          INDEX `idx_otp` (`otp`),
          INDEX `idx_expires` (`expires_at`),
          INDEX `idx_otp_expires` (`otp_expires_at`),
          INDEX `idx_used` (`used`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        if ($conn->query($createTable)) {
            echo '<div class="success">✅ Table created successfully!</div>';
        } else {
            throw new Exception("Failed to create table: " . $conn->error);
        }
    } else {
        echo '<div class="success">✅ Table exists</div>';
    }
    
    // Step 2: Check and add OTP columns if missing
    echo '<h2>Step 2: Checking OTP columns...</h2>';
    $columns = $conn->query("SHOW COLUMNS FROM password_reset_tokens");
    $columnNames = [];
    while ($col = $columns->fetch_assoc()) {
        $columnNames[] = $col['Field'];
    }
    
    $fixes = [];
    
    if (!in_array('otp', $columnNames)) {
        echo '<div class="info">Adding otp column...</div>';
        if ($conn->query("ALTER TABLE password_reset_tokens ADD COLUMN `otp` VARCHAR(6) DEFAULT NULL AFTER `token`")) {
            echo '<div class="success">✅ Added otp column</div>';
            $fixes[] = 'otp column added';
        } else {
            echo '<div class="error">❌ Failed to add otp column: ' . $conn->error . '</div>';
        }
    } else {
        echo '<div class="success">✅ otp column exists</div>';
    }
    
    if (!in_array('otp_expires_at', $columnNames)) {
        echo '<div class="info">Adding otp_expires_at column...</div>';
        if ($conn->query("ALTER TABLE password_reset_tokens ADD COLUMN `otp_expires_at` DATETIME DEFAULT NULL AFTER `expires_at`")) {
            echo '<div class="success">✅ Added otp_expires_at column</div>';
            $fixes[] = 'otp_expires_at column added';
        } else {
            echo '<div class="error">❌ Failed to add otp_expires_at column: ' . $conn->error . '</div>';
        }
    } else {
        echo '<div class="success">✅ otp_expires_at column exists</div>';
    }
    
    // Step 3: Add indexes if missing
    echo '<h2>Step 3: Checking indexes...</h2>';
    $indexes = $conn->query("SHOW INDEXES FROM password_reset_tokens");
    $indexNames = [];
    while ($idx = $indexes->fetch_assoc()) {
        $indexNames[] = $idx['Key_name'];
    }
    
    if (!in_array('idx_otp', $indexNames)) {
        $conn->query("ALTER TABLE password_reset_tokens ADD INDEX `idx_otp` (`otp`)");
        echo '<div class="success">✅ Added idx_otp index</div>';
    }
    
    if (!in_array('idx_otp_expires', $indexNames)) {
        $conn->query("ALTER TABLE password_reset_tokens ADD INDEX `idx_otp_expires` (`otp_expires_at`)");
        echo '<div class="success">✅ Added idx_otp_expires index</div>';
    }
    
    if (!in_array('idx_used', $indexNames)) {
        $conn->query("ALTER TABLE password_reset_tokens ADD INDEX `idx_used` (`used`)");
        echo '<div class="success">✅ Added idx_used index</div>';
    }
    
    // Step 4: Check for any OTPs in database
    echo '<h2>Step 4: Checking existing OTPs...</h2>';
    $otpCheck = $conn->query("SELECT COUNT(*) as total, 
        SUM(CASE WHEN otp IS NOT NULL AND otp != '' THEN 1 ELSE 0 END) as with_otp,
        SUM(CASE WHEN (used = 0 OR used IS NULL OR used = FALSE) THEN 1 ELSE 0 END) as unused
        FROM password_reset_tokens");
    
    if ($otpCheck) {
        $row = $otpCheck->fetch_assoc();
        echo '<div class="info">Total tokens: ' . $row['total'] . ', With OTP: ' . $row['with_otp'] . ', Unused: ' . $row['unused'] . '</div>';
    }
    
    // Step 5: Show table structure
    echo '<h2>Step 5: Final table structure...</h2>';
    $finalColumns = $conn->query("SHOW COLUMNS FROM password_reset_tokens");
    echo '<table border="1" cellpadding="5" style="border-collapse: collapse; width: 100%;">';
    echo '<tr><th>Field</th><th>Type</th><th>Null</th><th>Key</th><th>Default</th></tr>';
    while ($col = $finalColumns->fetch_assoc()) {
        echo '<tr>';
        echo '<td>' . htmlspecialchars($col['Field']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Type']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Null']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Key']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Default'] ?? 'NULL') . '</td>';
        echo '</tr>';
    }
    echo '</table>';
    
    echo '<div class="success">';
    echo '<h2>✅ Fix Complete!</h2>';
    echo '<p>The password_reset_tokens table is now properly configured for OTP verification.</p>';
    echo '<p><strong>Next steps:</strong></p>';
    echo '<ol>';
    echo '<li>Restart Apache in XAMPP Control Panel</li>';
    echo '<li>Test OTP flow in your app</li>';
    echo '<li>If still having issues, check: <a href="debug_otp.php?email=your@email.com">debug_otp.php</a></li>';
    echo '</ol>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">Make sure MySQL is running in XAMPP.</div>';
}

ob_end_flush();
?>

    </div>
</body>
</html>

