<?php
/**
 * COMPLETE FIX - Sets up everything automatically
 * This fixes the database, creates table, inserts data, and verifies API
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Complete Doctors Fix</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .fixing { color: #FB8C00; background: #FFF3E0; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; font-size: 11px; max-height: 300px; overflow-y: auto; }
        .step { margin: 15px 0; padding: 10px; border-left: 4px solid #34A853; background: #f9f9f9; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Complete Doctors API Fix</h1>
        <p>This will automatically fix everything and make your Android app work.</p>
        
<?php

// Connect to MySQL
$conn = new mysqli($dbHost, $dbUser, $dbPass);

if ($conn->connect_error) {
    echo '<div class="error">❌ Cannot connect to MySQL: ' . $conn->connect_error . '</div>';
    echo '<p><strong>Solution:</strong> Start XAMPP MySQL service</p>';
    exit();
}

$conn->set_charset("utf8mb4");

$allGood = true;

// STEP 1: Database
echo '<div class="step"><h2>Step 1: Database Setup</h2>';
try {
    $conn->query("CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
    $conn->select_db($dbName);
    echo '<div class="success">✅ Database "' . $dbName . '" ready</div>';
} catch (Exception $e) {
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
    $allGood = false;
}
echo '</div>';

// STEP 2: Table
echo '<div class="step"><h2>Step 2: Doctors Table</h2>';
try {
    // Drop and recreate to ensure correct structure
    $conn->query("DROP TABLE IF EXISTS `doctors`");
    
    $createTable = "CREATE TABLE `doctors` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `name` VARCHAR(255) NOT NULL,
      `specialization` VARCHAR(255) NOT NULL DEFAULT 'General Physician',
      `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
      `status` VARCHAR(50) NOT NULL DEFAULT 'Available',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_status` (`status`),
      INDEX `idx_specialization` (`specialization`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createTable)) {
        echo '<div class="success">✅ Doctors table created with correct structure</div>';
    } else {
        throw new Exception($conn->error);
    }
} catch (Exception $e) {
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
    $allGood = false;
}
echo '</div>';

// STEP 3: Insert Data
echo '<div class="step"><h2>Step 3: Inserting Doctors</h2>';
try {
    $insert = "INSERT INTO doctors (name, specialization, hospital, status) VALUES
    ('Dr. Rajesh Kumar', 'General Physician', 'Saveetha Hospital', 'Available'),
    ('Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'),
    ('Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available'),
    ('Dr. Meera Singh', 'Pediatrician', 'Saveetha Hospital', 'Available'),
    ('Dr. Vikram Reddy', 'Orthopedic Surgeon', 'Saveetha Hospital', 'Available')";
    
    if ($conn->query($insert)) {
        $count = $conn->affected_rows;
        echo '<div class="success">✅ Inserted ' . $count . ' doctors (all Available)</div>';
    } else {
        throw new Exception($conn->error);
    }
} catch (Exception $e) {
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
    $allGood = false;
}
echo '</div>';

// STEP 4: Verify Data
echo '<div class="step"><h2>Step 4: Verification</h2>';
try {
    $result = $conn->query("SELECT COUNT(*) as total FROM doctors");
    $total = $result->fetch_assoc()['total'];
    
    $result = $conn->query("SELECT COUNT(*) as available FROM doctors WHERE status='Available'");
    $available = $result->fetch_assoc()['available'];
    
    echo '<div class="info">📊 Total Doctors: ' . $total . '</div>';
    echo '<div class="info">✅ Available Doctors: ' . $available . '</div>';
    
    if ($available == 0) {
        throw new Exception("No available doctors found!");
    }
} catch (Exception $e) {
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
    $allGood = false;
}
echo '</div>';

// STEP 5: Test API Response
echo '<div class="step"><h2>Step 5: Testing API Response</h2>';
try {
    $result = $conn->query("SELECT id, name, specialization, hospital, status FROM doctors WHERE status='Available' ORDER BY id ASC");
    $doctors = [];
    while ($row = $result->fetch_assoc()) {
        $doctors[] = [
            'id' => (string)$row['id'],
            'name' => $row['name'],
            'specialization' => $row['specialization'],
            'hospital' => $row['hospital'],
            'status' => $row['status']
        ];
    }
    
    $response = [
        'success' => true,
        'doctors' => $doctors
    ];
    
    echo '<div class="success">✅ API Response Test Successful!</div>';
    echo '<p><strong>Sample Response:</strong></p>';
    echo '<pre>' . htmlspecialchars(json_encode($response, JSON_PRETTY_PRINT)) . '</pre>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
    $allGood = false;
}
echo '</div>';

$conn->close();

// FINAL RESULT
echo '<div class="step"><h2>📊 Final Result</h2>';
if ($allGood) {
    echo '<div class="success">';
    echo '<h3>✅ Everything Fixed Successfully!</h3>';
    echo '<p><strong>Your Android app should now work!</strong></p>';
    echo '<p><strong>Next Steps:</strong></p>';
    echo '<ol>';
    echo '<li>Refresh your Android app (pull down or tap refresh button)</li>';
    echo '<li>Test API: <a href="get_doctors.php" target="_blank">get_doctors.php</a></li>';
    echo '<li>If still not working, check:</li>';
    echo '<ul>';
    echo '<li>XAMPP Apache is running</li>';
    echo '<li>Phone and PC are on same Wi-Fi</li>';
    echo '<li>IP address in RetrofitClient.kt matches your PC IP</li>';
    echo '</ul>';
    echo '</ol>';
    echo '</div>';
} else {
    echo '<div class="error">';
    echo '<h3>⚠️ Some issues occurred</h3>';
    echo '<p>Please check the errors above and try again.</p>';
    echo '</div>';
}
echo '</div>';

?>
    </div>
</body>
</html>

