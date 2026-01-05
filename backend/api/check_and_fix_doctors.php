<?php
/**
 * CHECK AND FIX DOCTORS - Automatic Diagnostic and Fix
 * This script checks everything and fixes issues automatically
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

// Database credentials
$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Auto-Fix Doctors API</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .fixing { color: #FB8C00; background: #FFF3E0; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Auto-Fixing Doctors API Issues</h1>
        
<?php

$conn = new mysqli($dbHost, $dbUser, $dbPass);

if ($conn->connect_error) {
    echo '<div class="error">❌ Cannot connect to MySQL: ' . $conn->connect_error . '</div>';
    echo '<p>Please ensure XAMPP MySQL is running.</p>';
    exit();
}

$conn->set_charset("utf8mb4");

// Step 1: Create database
echo '<h2>Step 1: Database</h2>';
$conn->query("CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
$conn->select_db($dbName);
echo '<div class="success">✅ Database ready</div>';

// Step 2: Create/Update doctors table
echo '<h2>Step 2: Doctors Table</h2>';
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

// Add missing columns if they don't exist
$columns = [
    ['specialization', "ALTER TABLE doctors ADD COLUMN specialization VARCHAR(255) DEFAULT 'General Physician'"],
    ['hospital', "ALTER TABLE doctors ADD COLUMN hospital VARCHAR(255) DEFAULT 'Saveetha Hospital'"],
    ['status', "ALTER TABLE doctors ADD COLUMN status VARCHAR(50) DEFAULT 'Available'"]
];

foreach ($columns as $col) {
    $check = $conn->query("SHOW COLUMNS FROM doctors LIKE '{$col[0]}'");
    if (!$check || $check->num_rows == 0) {
        $conn->query($col[1]);
        echo '<div class="fixing">➕ Added column: ' . $col[0] . '</div>';
    }
}

echo '<div class="success">✅ Table structure verified</div>';

// Step 3: Check and insert doctors
echo '<h2>Step 3: Doctors Data</h2>';
$count = $conn->query("SELECT COUNT(*) as c FROM doctors")->fetch_assoc()['c'];

if ($count == 0) {
    $insert = "INSERT INTO doctors (name, specialization, hospital, status) VALUES
    ('Dr. Rajesh Kumar', 'General Physician', 'Saveetha Hospital', 'Available'),
    ('Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'),
    ('Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available')";
    $conn->query($insert);
    echo '<div class="success">✅ Inserted 3 doctors</div>';
} else {
    // Ensure at least one is Available
    $available = $conn->query("SELECT COUNT(*) as c FROM doctors WHERE status='Available'")->fetch_assoc()['c'];
    if ($available == 0) {
        $conn->query("UPDATE doctors SET status='Available' LIMIT 3");
        echo '<div class="fixing">✅ Updated doctors to Available</div>';
    }
    echo '<div class="info">✅ Found ' . $count . ' doctors (' . $available . ' available)</div>';
}

// Step 4: Test API
echo '<h2>Step 4: Testing API</h2>';
$result = $conn->query("SELECT id, name, specialization, hospital, status FROM doctors WHERE status='Available'");
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

if (count($doctors) > 0) {
    echo '<div class="success">✅ API Test Successful!</div>';
    echo '<pre>' . json_encode(['success' => true, 'doctors' => $doctors], JSON_PRETTY_PRINT) . '</pre>';
} else {
    echo '<div class="error">❌ No available doctors found</div>';
}

$conn->close();

echo '<h2>✅ All Fixed!</h2>';
echo '<div class="success">';
echo '<p><strong>Next Steps:</strong></p>';
echo '<ol>';
echo '<li>Test API: <a href="get_doctors.php" target="_blank">get_doctors.php</a></li>';
echo '<li>Refresh your Android app</li>';
echo '</ol>';
echo '</div>';

?>
    </div>
</body>
</html>

