<?php
/**
 * Automated Database Setup Script
 * This script creates all necessary tables for AwareHealth
 * Access via: http://localhost/AwareHealth/api/setup_database.php
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
    <title>AwareHealth Database Setup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #2D3748; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .warning { color: #FB8C00; background: #FFF3E0; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 AwareHealth Database Setup</h1>
        
<?php

try {
    $conn = getDB();
    echo '<div class="success">✅ Database connection successful!</div>';
    
    // Read SQL file
    $sqlFile = __DIR__ . '/../database/create_all_tables_complete.sql';
    if (!file_exists($sqlFile)) {
        throw new Exception("SQL file not found: $sqlFile");
    }
    
    $sql = file_get_contents($sqlFile);
    
    // Split SQL into individual statements
    $statements = array_filter(
        array_map('trim', explode(';', $sql)),
        function($stmt) {
            return !empty($stmt) && 
                   !preg_match('/^--/', $stmt) && 
                   !preg_match('/^SELECT.*AS message/', $stmt);
        }
    );
    
    echo '<h2>Creating Tables...</h2>';
    echo '<table>';
    echo '<tr><th>Table</th><th>Status</th></tr>';
    
    $successCount = 0;
    $errorCount = 0;
    $errors = [];
    
    foreach ($statements as $statement) {
        if (empty(trim($statement))) continue;
        
        // Extract table name from CREATE TABLE statement
        if (preg_match('/CREATE TABLE.*?`?(\w+)`?/i', $statement, $matches)) {
            $tableName = $matches[1];
        } elseif (preg_match('/ALTER TABLE.*?`?(\w+)`?/i', $statement, $matches)) {
            $tableName = $matches[1] . ' (ALTER)';
        } else {
            $tableName = 'Unknown';
        }
        
        // Execute statement
        if ($conn->multi_query($statement)) {
            do {
                if ($result = $conn->store_result()) {
                    $result->free();
                }
            } while ($conn->next_result());
            
            echo "<tr><td>$tableName</td><td class='success'>✅ Created/Updated</td></tr>";
            $successCount++;
        } else {
            $error = $conn->error;
            // Ignore "Duplicate column" and "Duplicate key" errors (they're OK)
            if (strpos($error, 'Duplicate column') === false && 
                strpos($error, 'Duplicate key') === false &&
                strpos($error, 'already exists') === false) {
                echo "<tr><td>$tableName</td><td class='error'>❌ Error: $error</td></tr>";
                $errors[] = "$tableName: $error";
                $errorCount++;
            } else {
                echo "<tr><td>$tableName</td><td class='info'>ℹ️ Already exists (OK)</td></tr>";
                $successCount++;
            }
        }
    }
    
    echo '</table>';
    
    // Verify tables exist
    echo '<h2>Verifying Tables...</h2>';
    $expectedTables = [
        'users',
        'doctors',
        'appointments',
        'password_reset_tokens',
        'diseases',
        'symptoms',
        'prevention_tips',
        'health_articles',
        'vaccination_reminders'
    ];
    
    $result = $conn->query("SHOW TABLES");
    $existingTables = [];
    while ($row = $result->fetch_array()) {
        $existingTables[] = $row[0];
    }
    
    echo '<table>';
    echo '<tr><th>Table</th><th>Status</th></tr>';
    
    foreach ($expectedTables as $table) {
        if (in_array($table, $existingTables)) {
            // Check if password_reset_tokens has OTP columns
            if ($table === 'password_reset_tokens') {
                $columns = $conn->query("SHOW COLUMNS FROM `$table`");
                $hasOtp = false;
                $hasOtpExpires = false;
                while ($col = $columns->fetch_assoc()) {
                    if ($col['Field'] === 'otp') $hasOtp = true;
                    if ($col['Field'] === 'otp_expires_at') $hasOtpExpires = true;
                }
                if ($hasOtp && $hasOtpExpires) {
                    echo "<tr><td>$table</td><td class='success'>✅ Exists (with OTP support)</td></tr>";
                } else {
                    echo "<tr><td>$table</td><td class='warning'>⚠️ Exists (missing OTP columns)</td></tr>";
                }
            } else {
                echo "<tr><td>$table</td><td class='success'>✅ Exists</td></tr>";
            }
        } else {
            echo "<tr><td>$table</td><td class='error'>❌ Missing</td></tr>";
        }
    }
    
    echo '</table>';
    
    // Summary
    echo '<h2>Summary</h2>';
    echo "<div class='success'>✅ Successfully processed: $successCount statements</div>";
    if ($errorCount > 0) {
        echo "<div class='error'>❌ Errors: $errorCount</div>";
        echo '<pre>' . implode("\n", $errors) . '</pre>';
    } else {
        echo "<div class='success'>✅ No errors!</div>";
    }
    
    echo '<div class="info">';
    echo '<h3>Next Steps:</h3>';
    echo '<ol>';
    echo '<li>Verify tables in phpMyAdmin: <a href="http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth" target="_blank">View Database</a></li>';
    echo '<li>Restart Apache in XAMPP Control Panel</li>';
    echo '<li>Test OTP functionality in your app</li>';
    echo '</ol>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">Make sure MySQL is running in XAMPP and the database "awarehealth" exists.</div>';
}

ob_end_flush();
?>
    </div>
</body>
</html>

