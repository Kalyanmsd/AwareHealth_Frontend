<?php
/**
 * Create Doctors Table - Direct creation for phpMyAdmin visibility
 * GET: http://localhost/AwareHealth/api/create_doctors_table_direct.php
 */

header('Content-Type: text/html; charset=utf-8');
header('Access-Control-Allow-Origin: *');

require_once __DIR__ . '/../config.php';

$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    die("❌ Connection failed: " . $conn->connect_error . "<br>Database: " . DB_NAME);
}

// Explicitly select database
if (!$conn->select_db(DB_NAME)) {
    die("❌ Cannot select database: " . DB_NAME);
}

$conn->set_charset("utf8mb4");

?>
<!DOCTYPE html>
<html>
<head>
    <title>Create Doctors Table</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 15px; border-radius: 4px; margin: 10px 0; border-left: 4px solid #34A853; }
        .error { color: #EA4335; background: #FFEBEE; padding: 15px; border-radius: 4px; margin: 10px 0; border-left: 4px solid #EA4335; }
        .info { color: #1976D2; background: #E3F2FD; padding: 15px; border-radius: 4px; margin: 10px 0; border-left: 4px solid #1976D2; }
        .link { display: inline-block; background: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 Create Doctors Table</h1>
        <div class="info">
            <strong>Database:</strong> <?php echo DB_NAME; ?><br>
            <strong>Host:</strong> <?php echo DB_HOST; ?>
        </div>
        
<?php

try {
    // Step 1: Drop if exists and create fresh
    echo '<h2>Step 1: Creating doctors table...</h2>';
    
    $dropSQL = "DROP TABLE IF EXISTS `doctors`";
    $conn->query($dropSQL);
    echo '<div class="info">ℹ️ Dropped existing table (if any)</div>';
    
    // Step 2: Create table
    $createSQL = "CREATE TABLE `doctors` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `name` VARCHAR(255) NOT NULL,
      `specialization` VARCHAR(255) NOT NULL,
      `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
      `experience` VARCHAR(100) NOT NULL,
      `available_days` VARCHAR(255) NOT NULL,
      `available_time` VARCHAR(255) NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_hospital` (`hospital`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createSQL)) {
        echo '<div class="success">✅ Doctors table created successfully!</div>';
    } else {
        throw new Exception("Failed to create table: " . $conn->error);
    }
    
    // Step 3: Insert doctors
    echo '<h2>Step 2: Inserting doctors...</h2>';
    
    $doctors = [
        ['Dr. Rajesh Kumar', 'Cardiology', 'Saveetha Hospital', '15 years', 'Monday, Wednesday, Friday', '09:00 AM - 05:00 PM'],
        ['Dr. Priya Sharma', 'Pediatrics', 'Saveetha Hospital', '12 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'],
        ['Dr. Anil Patel', 'Orthopedics', 'Saveetha Hospital', '18 years', 'Monday, Wednesday, Friday', '08:00 AM - 04:00 PM'],
        ['Dr. Meera Reddy', 'Dermatology', 'Saveetha Hospital', '10 years', 'Tuesday, Thursday, Saturday', '11:00 AM - 07:00 PM'],
        ['Dr. Vikram Singh', 'Neurology', 'Saveetha Hospital', '20 years', 'Monday, Wednesday, Friday, Saturday', '09:00 AM - 05:00 PM'],
        ['Dr. Sunita Nair', 'Gynecology', 'Saveetha Hospital', '14 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'],
        ['Dr. Arjun Menon', 'General Medicine', 'Saveetha Hospital', '16 years', 'Monday to Saturday', '08:00 AM - 08:00 PM']
    ];
    
    $insertStmt = $conn->prepare("INSERT INTO doctors (name, specialization, hospital, experience, available_days, available_time) VALUES (?, ?, ?, ?, ?, ?)");
    $insertedCount = 0;
    
    foreach ($doctors as $doctor) {
        $insertStmt->bind_param("ssssss", $doctor[0], $doctor[1], $doctor[2], $doctor[3], $doctor[4], $doctor[5]);
        if ($insertStmt->execute()) {
            $insertedCount++;
        }
    }
    $insertStmt->close();
    
    echo '<div class="success">✅ Inserted ' . $insertedCount . ' doctors!</div>';
    
    // Step 4: Verify
    echo '<h2>Step 3: Verifying...</h2>';
    $verify = $conn->query("SHOW TABLES LIKE 'doctors'");
    if ($verify && $verify->num_rows > 0) {
        echo '<div class="success">✅ Table EXISTS in database "' . DB_NAME . '"</div>';
        
        $count = $conn->query("SELECT COUNT(*) as total FROM doctors");
        $row = $count->fetch_assoc();
        echo '<div class="success">✅ Total doctors in table: ' . $row['total'] . '</div>';
    } else {
        throw new Exception("Verification failed");
    }
    
    // Display doctors
    echo '<h2>Doctors in Database:</h2>';
    $result = $conn->query("SELECT * FROM doctors ORDER BY id");
    if ($result && $result->num_rows > 0) {
        echo '<table border="1">';
        echo '<tr><th>ID</th><th>Name</th><th>Specialization</th><th>Hospital</th></tr>';
        while ($row = $result->fetch_assoc()) {
            echo '<tr>';
            echo '<td>' . $row['id'] . '</td>';
            echo '<td><strong>' . htmlspecialchars($row['name']) . '</strong></td>';
            echo '<td>' . htmlspecialchars($row['specialization']) . '</td>';
            echo '<td>' . htmlspecialchars($row['hospital']) . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    }
    
    // Success
    echo '<div class="success">';
    echo '<h2>✅ SUCCESS!</h2>';
    echo '<p><strong>Doctors table is now visible in phpMyAdmin!</strong></p>';
    echo '<p><a href="http://localhost/phpmyadmin/index.php?route=/database/structure&db=' . DB_NAME . '" target="_blank" class="link">🔍 View in phpMyAdmin</a></p>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">';
    echo '<h2>❌ Error: ' . htmlspecialchars($e->getMessage()) . '</h2>';
    echo '<p>Database: ' . DB_NAME . '</p>';
    echo '</div>';
}

$conn->close();
?>

    </div>
</body>
</html>

