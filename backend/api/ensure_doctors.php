<?php
/**
 * Ensure Doctors Exist - Auto-insert sample doctors if table is empty
 * GET: http://172.20.10.2/AwareHealth/api/ensure_doctors.php
 */

header('Content-Type: text/html; charset=utf-8');
header('Access-Control-Allow-Origin: *');

require_once __DIR__ . '/../config.php';

$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    die("❌ Connection failed: " . $conn->connect_error);
}

$conn->set_charset("utf8mb4");

?>
<!DOCTYPE html>
<html>
<head>
    <title>Ensure Doctors Exist</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 15px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 15px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 15px; border-radius: 4px; margin: 10px 0; }
        .link { display: inline-block; background: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 Ensure Doctors Exist</h1>
        
<?php

try {
    // Check if doctors table exists
    $tableCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
    if (!$tableCheck || $tableCheck->num_rows === 0) {
        echo '<div class="error">❌ Doctors table does not exist. Please create it first.</div>';
        echo '<div class="info">Run: CREATE TABLE doctors (...)</div>';
        $conn->close();
        exit();
    }
    
    // Check if status column exists
    $checkStatusColumn = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
    $hasStatusColumn = $checkStatusColumn && $checkStatusColumn->num_rows > 0;
    
    // Count existing doctors
    $countResult = $conn->query("SELECT COUNT(*) as total FROM doctors");
    $countRow = $countResult->fetch_assoc();
    $totalDoctors = (int)$countRow['total'];
    
    echo '<div class="info">';
    echo '<strong>Current Status:</strong><br>';
    echo "✅ Doctors table exists<br>";
    echo "📊 Total doctors: $totalDoctors<br>";
    echo "🔍 Status column: " . ($hasStatusColumn ? "Exists" : "Does not exist") . "<br>";
    echo '</div>';
    
    if ($totalDoctors === 0) {
        echo '<div class="info">⚠️ Doctors table is empty. Inserting sample doctors...</div>';
        
        // Insert sample doctors
        $doctors = [
            ['Dr. Rajesh Kumar', 'Cardiology', 'Saveetha Hospital', '15 years', 'Monday, Wednesday, Friday', '09:00 AM - 05:00 PM'],
            ['Dr. Priya Sharma', 'Pediatrics', 'Saveetha Hospital', '12 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'],
            ['Dr. Anil Patel', 'Orthopedics', 'Saveetha Hospital', '18 years', 'Monday, Wednesday, Friday', '08:00 AM - 04:00 PM'],
            ['Dr. Meera Reddy', 'Dermatology', 'Saveetha Hospital', '10 years', 'Tuesday, Thursday, Saturday', '11:00 AM - 07:00 PM'],
            ['Dr. Vikram Singh', 'Neurology', 'Saveetha Hospital', '20 years', 'Monday, Wednesday, Friday, Saturday', '09:00 AM - 05:00 PM']
        ];
        
        if ($hasStatusColumn) {
            $insertStmt = $conn->prepare("INSERT INTO doctors (name, specialization, hospital, experience, available_days, available_time, status) VALUES (?, ?, ?, ?, ?, ?, 'Available')");
        } else {
            $insertStmt = $conn->prepare("INSERT INTO doctors (name, specialization, hospital, experience, available_days, available_time) VALUES (?, ?, ?, ?, ?, ?)");
        }
        
        $insertedCount = 0;
        foreach ($doctors as $doctor) {
            if ($hasStatusColumn) {
                $insertStmt->bind_param("ssssss", $doctor[0], $doctor[1], $doctor[2], $doctor[3], $doctor[4], $doctor[5]);
            } else {
                $insertStmt->bind_param("ssssss", $doctor[0], $doctor[1], $doctor[2], $doctor[3], $doctor[4], $doctor[5]);
            }
            if ($insertStmt->execute()) {
                $insertedCount++;
            }
        }
        $insertStmt->close();
        
        echo '<div class="success">✅ Inserted $insertedCount sample doctors!</div>';
    } else {
        // Check if any doctors have status = 'Available'
        if ($hasStatusColumn) {
            $availableCount = $conn->query("SELECT COUNT(*) as total FROM doctors WHERE status = 'Available'");
            $availableRow = $availableCount->fetch_assoc();
            $availableDoctors = (int)$availableRow['total'];
            
            if ($availableDoctors === 0) {
                echo '<div class="info">⚠️ No doctors with status="Available". Updating all doctors to Available...</div>';
                $conn->query("UPDATE doctors SET status = 'Available'");
                echo '<div class="success">✅ Updated all doctors to status="Available"</div>';
            } else {
                echo '<div class="success">✅ $availableDoctors doctors are available!</div>';
            }
        } else {
            echo '<div class="success">✅ $totalDoctors doctors found (no status filter applied)</div>';
        }
    }
    
    // Final count
    if ($hasStatusColumn) {
        $finalCount = $conn->query("SELECT COUNT(*) as total FROM doctors WHERE status = 'Available'");
        $finalRow = $finalCount->fetch_assoc();
        $finalAvailable = (int)$finalRow['total'];
    } else {
        $finalCount = $conn->query("SELECT COUNT(*) as total FROM doctors");
        $finalRow = $finalCount->fetch_assoc();
        $finalAvailable = (int)$finalRow['total'];
    }
    
    echo '<div class="success">';
    echo '<h2>✅ Setup Complete!</h2>';
    echo '<p><strong>Available doctors:</strong> $finalAvailable</p>';
    echo '<p><a href="get_doctors.php" target="_blank" class="link">🧪 Test API</a></p>';
    echo '<p><a href="http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=' . DB_NAME . '&table=doctors" target="_blank" class="link">📊 View in phpMyAdmin</a></p>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
}

$conn->close();
?>

    </div>
</body>
</html>

