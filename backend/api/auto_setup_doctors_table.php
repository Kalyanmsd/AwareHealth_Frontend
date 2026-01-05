<?php
/**
 * AUTOMATIC DOCTORS TABLE SETUP
 * This script automatically:
 * 1. Creates doctors table with required columns
 * 2. Inserts sample doctors data
 * 
 * Access via: http://localhost/AwareHealth/backend/api/auto_setup_doctors_table.php
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
    <title>Automatic Doctors Table Setup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        .btn { display: inline-block; padding: 10px 20px; background: #34A853; color: white; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 Automatic Doctors Table Setup</h1>
        
<?php

$conn->set_charset("utf8mb4");

try {
    // Step 1: Create doctors table
    echo '<h2>Step 1: Creating doctors table...</h2>';
    
    // Check if table exists
    $tableCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
    if ($tableCheck && $tableCheck->num_rows > 0) {
        echo '<div class="info">⚠️ Doctors table already exists. Checking structure...</div>';
        
        // Check if required columns exist
        $statusCheck = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
        $specializationCheck = $conn->query("SHOW COLUMNS FROM doctors LIKE 'specialization'");
        $hospitalCheck = $conn->query("SHOW COLUMNS FROM doctors LIKE 'hospital'");
        
        $needsUpdate = false;
        if (!$statusCheck || $statusCheck->num_rows === 0) {
            echo '<div class="info">Adding status column...</div>';
            $conn->query("ALTER TABLE doctors ADD COLUMN status VARCHAR(50) DEFAULT 'Available'");
            $needsUpdate = true;
        }
        if (!$specializationCheck || $specializationCheck->num_rows === 0) {
            echo '<div class="info">Adding specialization column...</div>';
            $conn->query("ALTER TABLE doctors ADD COLUMN specialization VARCHAR(255) DEFAULT 'General Physician'");
            $needsUpdate = true;
        }
        if (!$hospitalCheck || $hospitalCheck->num_rows === 0) {
            echo '<div class="info">Adding hospital column...</div>';
            $conn->query("ALTER TABLE doctors ADD COLUMN hospital VARCHAR(255) DEFAULT 'Saveetha Hospital'");
            $needsUpdate = true;
        }
        
        if ($needsUpdate) {
            echo '<div class="success">✅ Table structure updated!</div>';
        } else {
            echo '<div class="success">✅ Table structure is correct!</div>';
        }
    } else {
        // Create table
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
            echo '<div class="success">✅ Doctors table created successfully!</div>';
        } else {
            throw new Exception("Failed to create doctors table: " . $conn->error);
        }
    }
    
    // Step 2: Insert sample doctors
    echo '<h2>Step 2: Inserting sample doctors...</h2>';
    
    // Check if doctors already exist
    $countResult = $conn->query("SELECT COUNT(*) as count FROM doctors");
    $count = $countResult->fetch_assoc()['count'];
    
    if ($count > 0) {
        echo '<div class="info">⚠️ Doctors already exist in table. Skipping insert.</div>';
    } else {
        $insertDoctors = "INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `status`) VALUES
        ('Dr. Rajesh Kumar', 'General Physician', 'Saveetha Hospital', 'Available'),
        ('Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'),
        ('Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available'),
        ('Dr. Meera Singh', 'Pediatrician', 'Saveetha Hospital', 'Busy'),
        ('Dr. Vikram Reddy', 'Orthopedic Surgeon', 'Saveetha Hospital', 'Available')";
        
        if ($conn->query($insertDoctors)) {
            echo '<div class="success">✅ Sample doctors inserted successfully!</div>';
        } else {
            throw new Exception("Failed to insert doctors: " . $conn->error);
        }
    }
    
    // Step 3: Verify data
    echo '<h2>Step 3: Verifying data...</h2>';
    
    $result = $conn->query("SELECT * FROM doctors WHERE status = 'Available'");
    $availableDoctors = [];
    while ($row = $result->fetch_assoc()) {
        $availableDoctors[] = $row;
    }
    
    echo '<div class="success">';
    echo '<h3>✅ Setup Complete!</h3>';
    echo '<p><strong>Available Doctors:</strong> ' . count($availableDoctors) . '</p>';
    
    if (count($availableDoctors) > 0) {
        echo '<table>';
        echo '<tr><th>ID</th><th>Name</th><th>Specialization</th><th>Hospital</th><th>Status</th></tr>';
        foreach ($availableDoctors as $doctor) {
            echo '<tr>';
            echo '<td>' . htmlspecialchars($doctor['id']) . '</td>';
            echo '<td>' . htmlspecialchars($doctor['name']) . '</td>';
            echo '<td>' . htmlspecialchars($doctor['specialization'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($doctor['hospital'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($doctor['status'] ?? 'N/A') . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    }
    
    echo '<p><strong>Test API:</strong></p>';
    echo '<ul>';
    echo '<li><a href="get_doctors.php" target="_blank">http://localhost/AwareHealth/backend/api/get_doctors.php</a></li>';
    echo '<li><a href="../get_doctors.php" target="_blank">http://localhost/AwareHealth/backend/get_doctors.php</a></li>';
    echo '</ul>';
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

