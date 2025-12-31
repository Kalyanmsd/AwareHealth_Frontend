<?php
/**
 * Automatic Password Reset Tokens Table Setup
 * Visit: http://localhost/AwareHealth/api/setup_password_reset_table.php
 * This will automatically create/update the password_reset_tokens table
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Password Reset Tokens Table Setup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #2d3748; }
        .success { background: #d4edda; color: #155724; padding: 15px; border-radius: 4px; margin: 10px 0; }
        .error { background: #f8d7da; color: #721c24; padding: 15px; border-radius: 4px; margin: 10px 0; }
        .info { background: #d1ecf1; color: #0c5460; padding: 15px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; font-weight: bold; }
        code { background: #f4f4f4; padding: 2px 6px; border-radius: 3px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Password Reset Tokens Table Setup</h1>
        
<?php

try {
    $conn = getDB();
    echo '<div class="info">✅ Connected to database: <code>' . DB_NAME . '</code></div>';
    
    // Check if table exists
    $tableExists = $conn->query("SHOW TABLES LIKE 'password_reset_tokens'");
    
    if ($tableExists && $tableExists->num_rows > 0) {
        echo '<div class="info">ℹ️ Table <code>password_reset_tokens</code> already exists. Checking structure...</div>';
        
        // Check current columns
        $columns = $conn->query("SHOW COLUMNS FROM password_reset_tokens");
        $existingColumns = [];
        while ($row = $columns->fetch_assoc()) {
            $existingColumns[] = $row['Field'];
        }
        
        echo '<h3>Current Table Structure:</h3>';
        echo '<table><tr><th>Column</th><th>Type</th><th>Null</th><th>Default</th></tr>';
        $columns = $conn->query("SHOW COLUMNS FROM password_reset_tokens");
        while ($row = $columns->fetch_assoc()) {
            echo '<tr><td><code>' . htmlspecialchars($row['Field']) . '</code></td>';
            echo '<td>' . htmlspecialchars($row['Type']) . '</td>';
            echo '<td>' . htmlspecialchars($row['Null']) . '</td>';
            echo '<td>' . htmlspecialchars($row['Default'] ?? 'NULL') . '</td></tr>';
        }
        echo '</table>';
        
        // Add missing columns
        $alterations = [];
        
        if (!in_array('otp', $existingColumns)) {
            $conn->query("ALTER TABLE password_reset_tokens ADD COLUMN otp VARCHAR(6) DEFAULT NULL AFTER token");
            $conn->query("ALTER TABLE password_reset_tokens ADD INDEX idx_otp (otp)");
            $alterations[] = "Added 'otp' column";
        }
        
        if (!in_array('otp_expires_at', $existingColumns)) {
            $conn->query("ALTER TABLE password_reset_tokens ADD COLUMN otp_expires_at DATETIME DEFAULT NULL AFTER expires_at");
            $conn->query("ALTER TABLE password_reset_tokens ADD INDEX idx_otp_expires (otp_expires_at)");
            $alterations[] = "Added 'otp_expires_at' column";
        }
        
        if (!in_array('used', $existingColumns)) {
            $conn->query("ALTER TABLE password_reset_tokens ADD COLUMN used TINYINT(1) DEFAULT 0");
            $conn->query("ALTER TABLE password_reset_tokens ADD INDEX idx_used (used)");
            $alterations[] = "Added 'used' column";
        }
        
        if (empty($alterations)) {
            echo '<div class="success">✅ Table structure is correct. No changes needed!</div>';
        } else {
            echo '<div class="success">✅ Table updated successfully!<br>';
            foreach ($alterations as $alt) {
                echo '&nbsp;&nbsp;• ' . $alt . '<br>';
            }
            echo '</div>';
        }
        
    } else {
        echo '<div class="info">ℹ️ Table does not exist. Creating it now...</div>';
        
        // Create table
        $createTable = "CREATE TABLE `password_reset_tokens` (
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
        
        if ($conn->query($createTable)) {
            echo '<div class="success">✅ Table <code>password_reset_tokens</code> created successfully!</div>';
        } else {
            throw new Exception("Failed to create table: " . $conn->error);
        }
    }
    
    // Show final structure
    echo '<h3>Final Table Structure:</h3>';
    echo '<table><tr><th>Column</th><th>Type</th><th>Null</th><th>Default</th><th>Key</th></tr>';
    $columns = $conn->query("SHOW COLUMNS FROM password_reset_tokens");
    while ($row = $columns->fetch_assoc()) {
        echo '<tr>';
        echo '<td><code>' . htmlspecialchars($row['Field']) . '</code></td>';
        echo '<td>' . htmlspecialchars($row['Type']) . '</td>';
        echo '<td>' . htmlspecialchars($row['Null']) . '</td>';
        echo '<td>' . htmlspecialchars($row['Default'] ?? 'NULL') . '</td>';
        echo '<td>' . htmlspecialchars($row['Key'] ?? '') . '</td>';
        echo '</tr>';
    }
    echo '</table>';
    
    // Check row count
    $count = $conn->query("SELECT COUNT(*) as cnt FROM password_reset_tokens");
    $row = $count->fetch_assoc();
    echo '<div class="info">📊 Current rows in table: <code>' . $row['cnt'] . '</code></div>';
    
    echo '<div class="success"><strong>✅ Setup Complete!</strong><br>';
    echo 'The password_reset_tokens table is now ready to use.</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
}

?>
        <hr>
        <p><strong>Next Steps:</strong></p>
        <ol>
            <li>Test OTP generation by requesting a password reset</li>
            <li>Check the table in phpMyAdmin to see OTP entries</li>
            <li>Verify OTP verification works in your app</li>
        </ol>
    </div>
</body>
</html>

