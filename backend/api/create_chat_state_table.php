<?php
/**
 * Create chat_state Table - Automated Setup Script
 * 
 * This script creates the chat_state table in the awarehealth database.
 * Access via: http://localhost/AwareHealth/api/create_chat_state_table.php
 * 
 * Purpose:
 * - Persist chatbot conversation state across messages
 * - Store current step and selected disease
 * - Prevent the bot from asking the disease name again after "provide"
 */

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Create chat_state Table</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 20px; 
            background: #f5f5f5; 
        }
        .container { 
            max-width: 800px; 
            margin: 0 auto; 
            background: white; 
            padding: 20px; 
            border-radius: 8px; 
            box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
        }
        h1 { color: #2D3748; }
        .success { 
            color: #34A853; 
            background: #E8F5E9; 
            padding: 15px; 
            border-radius: 4px; 
            margin: 10px 0; 
            border-left: 4px solid #34A853;
        }
        .error { 
            color: #EA4335; 
            background: #FFEBEE; 
            padding: 15px; 
            border-radius: 4px; 
            margin: 10px 0; 
            border-left: 4px solid #EA4335;
        }
        .info { 
            color: #1976D2; 
            background: #E3F2FD; 
            padding: 15px; 
            border-radius: 4px; 
            margin: 10px 0; 
            border-left: 4px solid #1976D2;
        }
        .warning { 
            color: #FB8C00; 
            background: #FFF3E0; 
            padding: 15px; 
            border-radius: 4px; 
            margin: 10px 0; 
            border-left: 4px solid #FB8C00;
        }
        pre { 
            background: #f5f5f5; 
            padding: 15px; 
            border-radius: 4px; 
            overflow-x: auto; 
            border: 1px solid #ddd;
        }
        table { 
            width: 100%; 
            border-collapse: collapse; 
            margin: 20px 0; 
        }
        th, td { 
            padding: 12px; 
            text-align: left; 
            border-bottom: 1px solid #ddd; 
        }
        th { 
            background: #f5f5f5; 
            font-weight: bold;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: #1976D2;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin: 10px 5px 10px 0;
        }
        .btn:hover {
            background: #1565C0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🗨️ Create chat_state Table</h1>
        
<?php

try {
    // Database connection
    $conn = new mysqli("localhost", "root", "", "awarehealth");
    
    if ($conn->connect_error) {
        throw new Exception("Database connection failed: " . $conn->connect_error);
    }
    
    echo '<div class="success">✅ Database connection successful!</div>';
    echo '<div class="info">📋 Database: <strong>awarehealth</strong></div>';
    
    // Check if table already exists
    $checkTable = $conn->query("SHOW TABLES LIKE 'chat_state'");
    $tableExists = $checkTable && $checkTable->num_rows > 0;
    
    if ($tableExists) {
        echo '<div class="warning">⚠️ Table <strong>chat_state</strong> already exists.</div>';
        
        // Show current table structure
        $result = $conn->query("DESCRIBE chat_state");
        if ($result) {
            echo '<h2>Current Table Structure:</h2>';
            echo '<table>';
            echo '<tr><th>Field</th><th>Type</th><th>Null</th><th>Key</th><th>Default</th><th>Extra</th></tr>';
            while ($row = $result->fetch_assoc()) {
                echo '<tr>';
                echo '<td><strong>' . htmlspecialchars($row['Field']) . '</strong></td>';
                echo '<td>' . htmlspecialchars($row['Type']) . '</td>';
                echo '<td>' . htmlspecialchars($row['Null']) . '</td>';
                echo '<td>' . htmlspecialchars($row['Key']) . '</td>';
                echo '<td>' . htmlspecialchars($row['Default'] ?? 'NULL') . '</td>';
                echo '<td>' . htmlspecialchars($row['Extra']) . '</td>';
                echo '</tr>';
            }
            echo '</table>';
        }
        
        // Show row count
        $countResult = $conn->query("SELECT COUNT(*) as count FROM chat_state");
        if ($countResult) {
            $count = $countResult->fetch_assoc()['count'];
            echo '<div class="info">📊 Current records in table: <strong>' . $count . '</strong></div>';
        }
        
        echo '<div class="info">✅ Table is ready to use. No changes needed.</div>';
        
    } else {
        // Create the table
        echo '<h2>Creating chat_state table...</h2>';
        
        $createTableSQL = "
        CREATE TABLE IF NOT EXISTS `chat_state` (
            `chat_id` VARCHAR(255) PRIMARY KEY COMMENT 'Unique chat session identifier',
            `step` VARCHAR(50) NOT NULL DEFAULT 'ASK_DISEASE' COMMENT 'Current step in conversation flow',
            `disease_name` VARCHAR(255) DEFAULT NULL COMMENT 'Name of the selected disease',
            `prevention` TEXT DEFAULT NULL COMMENT 'Prevention tips for the disease',
            `food` TEXT DEFAULT NULL COMMENT 'Food recommendations for the disease',
            `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
            INDEX `idx_step` (`step`),
            INDEX `idx_updated_at` (`updated_at`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores chatbot conversation state to prevent reset'
        ";
        
        if ($conn->query($createTableSQL)) {
            echo '<div class="success">✅ Table <strong>chat_state</strong> created successfully!</div>';
            
            // Show table structure
            $result = $conn->query("DESCRIBE chat_state");
            if ($result) {
                echo '<h2>Table Structure:</h2>';
                echo '<table>';
                echo '<tr><th>Field</th><th>Type</th><th>Null</th><th>Key</th><th>Default</th><th>Extra</th></tr>';
                while ($row = $result->fetch_assoc()) {
                    echo '<tr>';
                    echo '<td><strong>' . htmlspecialchars($row['Field']) . '</strong></td>';
                    echo '<td>' . htmlspecialchars($row['Type']) . '</td>';
                    echo '<td>' . htmlspecialchars($row['Null']) . '</td>';
                    echo '<td>' . htmlspecialchars($row['Key']) . '</td>';
                    echo '<td>' . htmlspecialchars($row['Default'] ?? 'NULL') . '</td>';
                    echo '<td>' . htmlspecialchars($row['Extra']) . '</td>';
                    echo '</tr>';
                }
                echo '</table>';
            }
            
            echo '<div class="success">';
            echo '<h3>✅ Setup Complete!</h3>';
            echo '<p>The <strong>chat_state</strong> table has been created successfully.</p>';
            echo '<p><strong>Purpose:</strong> Persist chatbot conversation state across messages</p>';
            echo '<p><strong>Next Steps:</strong></p>';
            echo '<ul>';
            echo '<li>Verify the table exists in phpMyAdmin: <a href="http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth" target="_blank">View Database</a></li>';
            echo '<li>The chatbot.php will automatically use this table to store conversation state</li>';
            echo '</ul>';
            echo '</div>';
            
        } else {
            throw new Exception("Error creating table: " . $conn->error);
        }
    }
    
    // Verify table exists
    echo '<h2>Verification</h2>';
    $verifyResult = $conn->query("SHOW TABLES LIKE 'chat_state'");
    if ($verifyResult && $verifyResult->num_rows > 0) {
        echo '<div class="success">✅ Verification: Table <strong>chat_state</strong> exists in database <strong>awarehealth</strong></div>';
        
        // Show table info
        $tableInfo = $conn->query("SHOW TABLE STATUS LIKE 'chat_state'");
        if ($tableInfo && $row = $tableInfo->fetch_assoc()) {
            echo '<div class="info">';
            echo '<strong>Table Details:</strong><br>';
            echo 'Engine: ' . htmlspecialchars($row['Engine']) . '<br>';
            echo 'Rows: ' . htmlspecialchars($row['Rows']) . '<br>';
            echo 'Collation: ' . htmlspecialchars($row['Collation']) . '<br>';
            echo '</div>';
        }
    } else {
        echo '<div class="error">❌ Verification failed: Table not found</div>';
    }
    
    $conn->close();
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">';
    echo '<h3>Troubleshooting:</h3>';
    echo '<ul>';
    echo '<li>Make sure XAMPP MySQL is running</li>';
    echo '<li>Verify database "awarehealth" exists</li>';
    echo '<li>Check MySQL credentials (default: root, no password)</li>';
    echo '<li>Try running the SQL directly in phpMyAdmin: <a href="http://localhost/phpmyadmin/index.php?route=/sql&db=awarehealth" target="_blank">Open SQL Tab</a></li>';
    echo '</ul>';
    echo '</div>';
}

?>

        <hr>
        <div class="info">
            <h3>📝 Manual SQL (Alternative Method)</h3>
            <p>If the automated script doesn't work, you can run this SQL directly in phpMyAdmin:</p>
            <pre>
USE `awarehealth`;

CREATE TABLE IF NOT EXISTS `chat_state` (
    `chat_id` VARCHAR(255) PRIMARY KEY,
    `step` VARCHAR(50) NOT NULL DEFAULT 'ASK_DISEASE',
    `disease_name` VARCHAR(255) DEFAULT NULL,
    `prevention` TEXT DEFAULT NULL,
    `food` TEXT DEFAULT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_step` (`step`),
    INDEX `idx_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
            </pre>
            <p><a href="http://localhost/phpmyadmin/index.php?route=/sql&db=awarehealth" target="_blank" class="btn">Open phpMyAdmin SQL Tab</a></p>
        </div>
    </div>
</body>
</html>

