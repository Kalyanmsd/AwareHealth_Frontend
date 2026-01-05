<?php
/**
 * AUTOMATIC DISEASE DETAILS TABLES SETUP
 * This script automatically:
 * 1. Creates 'disease_details' table with preventions, symptoms, and suggested_food
 * 2. Creates 'diseases_list' table with same structure as 'diseases' table
 * 
 * Access via: http://localhost/AwareHealth/api/auto_create_disease_details_tables.php
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
$conn = new mysqli($dbHost, $dbUser, $dbPass, $dbName);

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
    <title>Automatic Disease Details Tables Setup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .warning { color: #FB8C00; background: #FFF3E0; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
        .btn { display: inline-block; padding: 10px 20px; background: #34A853; color: white; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 Automatic Disease Details Tables Setup</h1>
        
<?php

$conn->set_charset("utf8mb4");

try {
    // Check if diseases table exists first
    echo '<h2>Step 1: Checking diseases table...</h2>';
    $checkDiseases = $conn->query("SHOW TABLES LIKE 'diseases'");
    if ($checkDiseases && $checkDiseases->num_rows > 0) {
        echo '<div class="success">✅ Diseases table exists!</div>';
    } else {
        echo '<div class="warning">⚠️ Diseases table does not exist. Creating it first...</div>';
        
        // Create diseases table first
        $createDiseases = "CREATE TABLE IF NOT EXISTS `diseases` (
          `id` VARCHAR(36) PRIMARY KEY,
          `name` VARCHAR(255) NOT NULL,
          `category` VARCHAR(100) NOT NULL DEFAULT 'General',
          `severity` VARCHAR(50) NOT NULL DEFAULT 'Mild',
          `emoji` VARCHAR(10) DEFAULT '🏥',
          `description` TEXT NOT NULL,
          `symptoms` TEXT NOT NULL COMMENT 'JSON array of symptoms',
          `causes` TEXT DEFAULT NULL COMMENT 'JSON array of causes',
          `prevention` TEXT DEFAULT NULL COMMENT 'JSON array of prevention tips',
          `treatment` TEXT DEFAULT NULL COMMENT 'JSON array of treatment options',
          `affected_population` VARCHAR(255) DEFAULT NULL,
          `duration` VARCHAR(100) DEFAULT NULL,
          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
          INDEX `idx_category` (`category`),
          INDEX `idx_severity` (`severity`),
          INDEX `idx_name` (`name`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        if ($conn->query($createDiseases)) {
            echo '<div class="success">✅ Diseases table created successfully!</div>';
        } else {
            throw new Exception("Failed to create diseases table: " . $conn->error);
        }
    }
    
    // Step 2: Create disease_details table
    echo '<h2>Step 2: Creating disease_details table...</h2>';
    $createDiseaseDetails = "CREATE TABLE IF NOT EXISTS `disease_details` (
      `id` VARCHAR(36) PRIMARY KEY,
      `disease_id` VARCHAR(36) NOT NULL,
      `preventions` TEXT DEFAULT NULL COMMENT 'JSON array of prevention tips',
      `symptoms` TEXT DEFAULT NULL COMMENT 'JSON array of symptoms',
      `suggested_food` TEXT DEFAULT NULL COMMENT 'JSON array of suggested foods',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      FOREIGN KEY (`disease_id`) REFERENCES `diseases`(`id`) ON DELETE CASCADE,
      INDEX `idx_disease_id` (`disease_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createDiseaseDetails)) {
        echo '<div class="success">✅ Disease details table created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create disease_details table: " . $conn->error);
    }
    
    // Step 3: Create diseases_list table (same structure as diseases)
    echo '<h2>Step 3: Creating diseases_list table...</h2>';
    $createDiseasesList = "CREATE TABLE IF NOT EXISTS `diseases_list` (
      `id` VARCHAR(36) PRIMARY KEY,
      `name` VARCHAR(255) NOT NULL,
      `category` VARCHAR(100) NOT NULL DEFAULT 'General',
      `severity` VARCHAR(50) NOT NULL DEFAULT 'Mild',
      `emoji` VARCHAR(10) DEFAULT '🏥',
      `description` TEXT NOT NULL,
      `symptoms` TEXT NOT NULL COMMENT 'JSON array of symptoms',
      `causes` TEXT DEFAULT NULL COMMENT 'JSON array of causes',
      `prevention` TEXT DEFAULT NULL COMMENT 'JSON array of prevention tips',
      `treatment` TEXT DEFAULT NULL COMMENT 'JSON array of treatment options',
      `affected_population` VARCHAR(255) DEFAULT NULL,
      `duration` VARCHAR(100) DEFAULT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_category` (`category`),
      INDEX `idx_severity` (`severity`),
      INDEX `idx_name` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createDiseasesList)) {
        echo '<div class="success">✅ Diseases_list table created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create diseases_list table: " . $conn->error);
    }
    
    // Step 4: Verify tables
    echo '<h2>Step 4: Verifying tables...</h2>';
    $tables = ['disease_details', 'diseases_list'];
    $allSuccess = true;
    
    foreach ($tables as $table) {
        $check = $conn->query("SHOW TABLES LIKE '$table'");
        if ($check && $check->num_rows > 0) {
            // Get table structure
            $structure = $conn->query("DESCRIBE `$table`");
            $columns = [];
            while ($row = $structure->fetch_assoc()) {
                $columns[] = $row['Field'];
            }
            
            echo '<div class="info">';
            echo "✅ Table <strong>$table</strong> exists with " . count($columns) . " columns:<br>";
            echo "<small>" . implode(', ', $columns) . "</small>";
            echo '</div>';
        } else {
            echo '<div class="error">❌ Table <strong>' . $table . '</strong> not found!</div>';
            $allSuccess = false;
        }
    }
    
    if ($allSuccess) {
        echo '<div class="success">';
        echo '<h2>✅ All tables created successfully!</h2>';
        echo '<p><strong>Tables created:</strong></p>';
        echo '<ul>';
        echo '<li><strong>disease_details</strong> - Stores preventions, symptoms, and suggested_food for each disease</li>';
        echo '<li><strong>diseases_list</strong> - Same structure as diseases table</li>';
        echo '</ul>';
        echo '<p><strong>phpMyAdmin Links:</strong></p>';
        echo '<ul>';
        echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth" target="_blank">View Database Structure</a></li>';
        echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/sql&db=awarehealth&table=disease_details&pos=0" target="_blank">View disease_details Table</a></li>';
        echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/sql&db=awarehealth&table=diseases_list&pos=0" target="_blank">View diseases_list Table</a></li>';
        echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/sql&db=awarehealth&table=diseases&pos=0" target="_blank">View diseases Table</a></li>';
        echo '</ul>';
        echo '</div>';
    }
    
} catch (Exception $e) {
    echo '<div class="error">';
    echo '<h2>❌ Error occurred:</h2>';
    echo '<p>' . htmlspecialchars($e->getMessage()) . '</p>';
    echo '</div>';
}

$conn->close();

?>
    </div>
</body>
</html>

