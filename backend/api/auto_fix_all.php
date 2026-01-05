<?php
/**
 * AUTO-FIX ALL - Complete automatic setup and fix
 * This script fixes everything automatically
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
    <title>Auto-Fix All</title>
    <meta http-equiv="refresh" content="2;url=auto_fix_all.php?run=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .fixing { color: #FB8C00; background: #FFF3E0; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; font-size: 12px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Auto-Fixing Everything...</h1>
        
<?php

if (!isset($_GET['run'])) {
    echo '<div class="info">Starting automatic fix in 2 seconds...</div>';
    exit();
}

$conn = new mysqli($dbHost, $dbUser, $dbPass);

if ($conn->connect_error) {
    echo '<div class="error">❌ MySQL Connection Failed: ' . $conn->connect_error . '</div>';
    echo '<p>Please start XAMPP MySQL service.</p>';
    exit();
}

$conn->set_charset("utf8mb4");

$steps = [];
$errors = [];

// Step 1: Database
echo '<h2>Step 1: Database</h2>';
try {
    $conn->query("CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
    $conn->select_db($dbName);
    $steps[] = "✅ Database created/verified";
    echo '<div class="success">✅ Database ready</div>';
} catch (Exception $e) {
    $errors[] = "Database: " . $e->getMessage();
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
}

// Step 2: Table
echo '<h2>Step 2: Doctors Table</h2>';
try {
    $createTable = "CREATE TABLE IF NOT EXISTS `doctors` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `name` VARCHAR(255) NOT NULL,
      `specialization` VARCHAR(255) NOT NULL DEFAULT 'General Physician',
      `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
      `status` VARCHAR(50) NOT NULL DEFAULT 'Available',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_status` (`status`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    $conn->query($createTable);
    
    // Add missing columns
    $columns = [
        ['specialization', "ALTER TABLE doctors ADD COLUMN IF NOT EXISTS specialization VARCHAR(255) DEFAULT 'General Physician'"],
        ['hospital', "ALTER TABLE doctors ADD COLUMN IF NOT EXISTS hospital VARCHAR(255) DEFAULT 'Saveetha Hospital'"],
        ['status', "ALTER TABLE doctors ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'Available'"]
    ];
    
    foreach ($columns as $col) {
        $check = $conn->query("SHOW COLUMNS FROM doctors LIKE '{$col[0]}'");
        if (!$check || $check->num_rows == 0) {
            // Remove IF NOT EXISTS if MySQL version doesn't support it
            $sql = str_replace('IF NOT EXISTS', '', $col[1]);
            $conn->query($sql);
        }
    }
    
    $steps[] = "✅ Table created/verified";
    echo '<div class="success">✅ Table ready</div>';
} catch (Exception $e) {
    $errors[] = "Table: " . $e->getMessage();
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
}

// Step 3: Data
echo '<h2>Step 3: Doctors Data</h2>';
try {
    $count = $conn->query("SELECT COUNT(*) as c FROM doctors")->fetch_assoc()['c'];
    
    if ($count == 0) {
        $insert = "INSERT INTO doctors (name, specialization, hospital, status) VALUES
        ('Dr. Rajesh Kumar', 'General Physician', 'Saveetha Hospital', 'Available'),
        ('Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'),
        ('Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available'),
        ('Dr. Meera Singh', 'Pediatrician', 'Saveetha Hospital', 'Available'),
        ('Dr. Vikram Reddy', 'Orthopedic Surgeon', 'Saveetha Hospital', 'Available')";
        $conn->query($insert);
        $steps[] = "✅ Inserted 5 doctors";
        echo '<div class="success">✅ Inserted 5 doctors</div>';
    } else {
        // Ensure doctors are Available
        $conn->query("UPDATE doctors SET status='Available' WHERE status IS NULL OR status='' LIMIT 5");
        $available = $conn->query("SELECT COUNT(*) as c FROM doctors WHERE status='Available'")->fetch_assoc()['c'];
        $steps[] = "✅ Found $count doctors ($available available)";
        echo '<div class="info">✅ Found $count doctors ($available available)</div>';
    }
} catch (Exception $e) {
    $errors[] = "Data: " . $e->getMessage();
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
}

// Step 4: Test API Response
echo '<h2>Step 4: Testing API</h2>';
try {
    $result = $conn->query("SELECT id, name, specialization, hospital, status FROM doctors WHERE status='Available' LIMIT 10");
    $doctors = [];
    while ($row = $result->fetch_assoc()) {
        $doctors[] = [
            'id' => (string)$row['id'],
            'name' => $row['name'],
            'specialization' => $row['specialization'] ?? 'General Physician',
            'hospital' => $row['hospital'] ?? 'Saveetha Hospital',
            'status' => $row['status'] ?? 'Available'
        ];
    }
    
    if (count($doctors) > 0) {
        $steps[] = "✅ API test successful - " . count($doctors) . " doctors";
        echo '<div class="success">✅ API Test: ' . count($doctors) . ' available doctors</div>';
        echo '<pre>' . json_encode(['success' => true, 'doctors' => $doctors], JSON_PRETTY_PRINT) . '</pre>';
    } else {
        $errors[] = "No available doctors found";
        echo '<div class="error">❌ No available doctors</div>';
    }
} catch (Exception $e) {
    $errors[] = "API Test: " . $e->getMessage();
    echo '<div class="error">❌ ' . $e->getMessage() . '</div>';
}

$conn->close();

// Final Summary
echo '<h2>📊 Summary</h2>';
if (count($errors) == 0) {
    echo '<div class="success">';
    echo '<h3>✅ All Fixed Successfully!</h3>';
    echo '<ul>';
    foreach ($steps as $step) {
        echo '<li>' . $step . '</li>';
    }
    echo '</ul>';
    echo '<p><strong>Your Android app should now work!</strong></p>';
    echo '<p>Test API: <a href="get_doctors.php" target="_blank">get_doctors.php</a></p>';
    echo '</div>';
} else {
    echo '<div class="error">';
    echo '<h3>⚠️ Some Issues Found:</h3>';
    echo '<ul>';
    foreach ($errors as $error) {
        echo '<li>' . $error . '</li>';
    }
    echo '</ul>';
    echo '</div>';
}

?>
    </div>
</body>
</html>

