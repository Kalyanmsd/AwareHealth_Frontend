<?php
/**
 * AUTOMATIC DOCTORS TABLE IMPORT
 * This script automatically imports the doctors table SQL directly to MySQL
 * No need to manually import via phpMyAdmin
 * 
 * Access via: http://localhost/AwareHealth/backend/api/auto_import_doctors.php
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
    <title>Automatic Doctors Table Import</title>
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
        .btn { display: inline-block; padding: 10px 20px; background: #34A853; color: white; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🚀 Automatic Doctors Table Import</h1>
        <p>This script will automatically create the doctors table and insert sample data directly into MySQL.</p>
        
<?php

$conn->set_charset("utf8mb4");

try {
    // Step 1: Create database if it doesn't exist
    echo '<h2>Step 1: Creating/Verifying Database...</h2>';
    $createDb = "CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
    if ($conn->query($createDb)) {
        echo '<div class="success">✅ Database "' . $dbName . '" created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create database: " . $conn->error);
    }
    
    // Select the database
    $conn->select_db($dbName);
    
    // Step 2: Create doctors table
    echo '<h2>Step 2: Creating Doctors Table...</h2>';
    
    $createTable = "CREATE TABLE IF NOT EXISTS `doctors` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `name` VARCHAR(255) NOT NULL,
      `specialization` VARCHAR(255) NOT NULL DEFAULT 'General Physician',
      `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
      `status` VARCHAR(50) NOT NULL DEFAULT 'Available',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_status` (`status`),
      INDEX `idx_specialization` (`specialization`),
      INDEX `idx_hospital` (`hospital`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createTable)) {
        echo '<div class="success">✅ Doctors table created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create doctors table: " . $conn->error);
    }
    
    // Step 3: Check if doctors already exist
    echo '<h2>Step 3: Checking Existing Data...</h2>';
    $countResult = $conn->query("SELECT COUNT(*) as count FROM doctors");
    $count = $countResult->fetch_assoc()['count'];
    
    if ($count > 0) {
        echo '<div class="warning">⚠️ Doctors already exist in table (' . $count . ' records).</div>';
        echo '<div class="info">Options:</div>';
        echo '<ul>';
        echo '<li>Keep existing data (recommended)</li>';
        echo '<li><a href="?action=clear" style="color: #EA4335;">Clear and re-import</a></li>';
        echo '</ul>';
        
        // Check if user wants to clear
        if (isset($_GET['action']) && $_GET['action'] === 'clear') {
            echo '<div class="info">Clearing existing data...</div>';
            $conn->query("TRUNCATE TABLE doctors");
            echo '<div class="success">✅ Existing data cleared!</div>';
            $count = 0; // Reset count to allow insertion
        }
    }
    
    // Step 4: Insert sample doctors (only if table is empty)
    if ($count == 0) {
        echo '<h2>Step 4: Inserting Sample Doctors...</h2>';
        
        $insertDoctors = "INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `status`) VALUES
        ('Dr. Rajesh Kumar', 'General Physician', 'Saveetha Hospital', 'Available'),
        ('Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'),
        ('Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available'),
        ('Dr. Meera Singh', 'Pediatrician', 'Saveetha Hospital', 'Busy'),
        ('Dr. Vikram Reddy', 'Orthopedic Surgeon', 'Saveetha Hospital', 'Available')";
        
        if ($conn->query($insertDoctors)) {
            $insertedCount = $conn->affected_rows;
            echo '<div class="success">✅ Successfully inserted ' . $insertedCount . ' doctors!</div>';
        } else {
            throw new Exception("Failed to insert doctors: " . $conn->error);
        }
    } else {
        echo '<h2>Step 4: Skipping Data Insert (Data Already Exists)</h2>';
        echo '<div class="info">To re-import, <a href="?action=clear">click here to clear and re-import</a></div>';
    }
    
    // Step 5: Verify and display results
    echo '<h2>Step 5: Verification Results...</h2>';
    
    // Get all doctors
    $allDoctors = $conn->query("SELECT * FROM doctors ORDER BY id ASC");
    $allCount = $allDoctors->num_rows;
    
    // Get available doctors
    $availableDoctors = $conn->query("SELECT * FROM doctors WHERE status = 'Available' ORDER BY id ASC");
    $availableCount = $availableDoctors->num_rows;
    
    echo '<div class="success">';
    echo '<h3>✅ Import Complete!</h3>';
    echo '<p><strong>Total Doctors:</strong> ' . $allCount . '</p>';
    echo '<p><strong>Available Doctors:</strong> ' . $availableCount . '</p>';
    
    if ($availableCount > 0) {
        echo '<h4>Available Doctors List:</h4>';
        echo '<table>';
        echo '<tr><th>ID</th><th>Name</th><th>Specialization</th><th>Hospital</th><th>Status</th></tr>';
        while ($row = $availableDoctors->fetch_assoc()) {
            echo '<tr>';
            echo '<td>' . htmlspecialchars($row['id']) . '</td>';
            echo '<td>' . htmlspecialchars($row['name']) . '</td>';
            echo '<td>' . htmlspecialchars($row['specialization']) . '</td>';
            echo '<td>' . htmlspecialchars($row['hospital']) . '</td>';
            echo '<td><strong style="color: #34A853;">' . htmlspecialchars($row['status']) . '</strong></td>';
            echo '</tr>';
        }
        echo '</table>';
    }
    
    // Test API endpoint
    echo '<h4>🧪 Test API Endpoints:</h4>';
    echo '<ul>';
    echo '<li><a href="get_doctors.php" target="_blank">http://localhost/AwareHealth/backend/api/get_doctors.php</a></li>';
    echo '<li><a href="../get_doctors.php" target="_blank">http://localhost/AwareHealth/backend/get_doctors.php</a></li>';
    echo '</ul>';
    
    // phpMyAdmin link
    echo '<h4>📊 View in phpMyAdmin:</h4>';
    echo '<ul>';
    echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/database/structure&db=' . $dbName . '" target="_blank">Database Structure</a></li>';
    echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/sql&db=' . $dbName . '&table=doctors&pos=0" target="_blank">Doctors Table</a></li>';
    echo '</ul>';
    
    echo '</div>';
    
    // Step 6: Test API response
    echo '<h2>Step 6: Testing API Response...</h2>';
    echo '<div class="info">';
    echo '<p>Testing get_doctors.php endpoint...</p>';
    
    // Make a test request to the API
    $testUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/backend/api/get_doctors.php';
    $ch = curl_init($testUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 5);
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($httpCode == 200 && $response) {
        $jsonData = json_decode($response, true);
        if ($jsonData && isset($jsonData['success']) && $jsonData['success']) {
            echo '<div class="success">✅ API Test Successful!</div>';
            echo '<p><strong>Response:</strong></p>';
            echo '<pre>' . htmlspecialchars(json_encode($jsonData, JSON_PRETTY_PRINT)) . '</pre>';
        } else {
            echo '<div class="warning">⚠️ API returned success=false</div>';
            echo '<pre>' . htmlspecialchars($response) . '</pre>';
        }
    } else {
        echo '<div class="warning">⚠️ Could not test API (HTTP Code: ' . $httpCode . ')</div>';
        echo '<p>You can test manually: <a href="get_doctors.php" target="_blank">get_doctors.php</a></p>';
    }
    
    echo '</div>';
    
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

