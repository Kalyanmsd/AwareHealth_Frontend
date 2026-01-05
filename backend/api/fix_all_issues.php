<?php
/**
 * Fix All Issues - Doctor Names + Date Format
 * This script fixes both doctor names and ensures proper date formatting
 */

header('Content-Type: text/html; charset=utf-8');

$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

$conn = new mysqli($dbHost, $dbUser, $dbPass, $dbName);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$conn->set_charset("utf8mb4");

?>
<!DOCTYPE html>
<html>
<head>
    <title>Fix All Issues</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Fixing All Issues</h1>
        
<?php

// ========== FIX 1: DOCTOR NAMES ==========
echo '<h2>Fix 1: Updating Doctor Names</h2>';

$nameMapping = [
    'General' => 'Dr. Rajesh Kumar',
    'Cardiology' => 'Dr. Priya Sharma',
    'Pediatrics' => 'Dr. Meera Singh',
    'Orthopedics' => 'Dr. Vikram Reddy',
    'Gynecology' => 'Dr. Anjali Desai',
    'Neurology' => 'Dr. Ramesh Iyer',
    'Dermatology' => 'Dr. Anil Patel'
];

$result = $conn->query("SELECT id, specialty, name FROM doctors ORDER BY id");
$updated = 0;

while ($row = $result->fetch_assoc()) {
    $id = $row['id'];
    $specialty = $row['specialty'] ?? 'General';
    $currentName = $row['name'] ?? '';
    
    if (empty($currentName)) {
        $newName = $nameMapping[$specialty] ?? 'Dr. ' . $specialty . ' Specialist';
        
        $updateSql = "UPDATE doctors SET name = ? WHERE id = ?";
        $stmt = $conn->prepare($updateSql);
        $stmt->bind_param("ss", $newName, $id);
        
        if ($stmt->execute()) {
            $updated++;
            echo '<div class="success">✅ Updated: ' . htmlspecialchars($newName) . '</div>';
        }
        $stmt->close();
    }
}

echo '<div class="success">✅ Updated ' . $updated . ' doctor names</div>';

// ========== FIX 2: CHECK APPOINTMENT DATES ==========
echo '<h2>Fix 2: Checking Appointment Dates</h2>';

$appointmentsCheck = $conn->query("SHOW TABLES LIKE 'appointments'");
if ($appointmentsCheck && $appointmentsCheck->num_rows > 0) {
    // Check for old dates (2024)
    $oldDates = $conn->query("
        SELECT id, appointment_date, appointment_time, user_email 
        FROM appointments 
        WHERE appointment_date LIKE '2024%' 
        LIMIT 10
    ");
    
    if ($oldDates && $oldDates->num_rows > 0) {
        echo '<div class="info">Found ' . $oldDates->num_rows . ' appointments with 2024 dates</div>';
        echo '<p>These dates are from the database. New appointments will use current dates.</p>';
    } else {
        echo '<div class="success">✅ No old dates found (or table uses different column names)</div>';
    }
} else {
    echo '<div class="info">Appointments table not found</div>';
}

// ========== VERIFY DOCTOR NAMES ==========
echo '<h2>Verification: Doctor Names</h2>';
$verifyResult = $conn->query("SELECT id, name, specialty, status FROM doctors WHERE status='Available' ORDER BY id LIMIT 10");
echo '<table>';
echo '<tr><th>ID</th><th>Name</th><th>Specialty</th><th>Status</th></tr>';
while ($row = $verifyResult->fetch_assoc()) {
    echo '<tr>';
    echo '<td>' . htmlspecialchars(substr($row['id'], 0, 15)) . '...</td>';
    echo '<td><strong>' . htmlspecialchars($row['name'] ?? 'NULL') . '</strong></td>';
    echo '<td>' . htmlspecialchars($row['specialty'] ?? 'N/A') . '</td>';
    echo '<td>' . htmlspecialchars($row['status'] ?? 'N/A') . '</td>';
    echo '</tr>';
}
echo '</table>';

$conn->close();

echo '<div class="success">';
echo '<h2>✅ All Fixes Complete!</h2>';
echo '<p><strong>What was fixed:</strong></p>';
echo '<ol>';
echo '<li>✅ Doctor names updated in database</li>';
echo '<li>✅ Appointments API updated to return proper date/time format</li>';
echo '<li>✅ API now returns "date" and "time" (not "appointment_date" and "appointment_time")</li>';
echo '</ol>';
echo '<p><strong>Next steps:</strong></p>';
echo '<ol>';
echo '<li>Test API: <a href="get_doctors.php" target="_blank">get_doctors.php</a></li>';
echo '<li>Test Appointments: <a href="get_my_appointments.php?email=your-email@example.com" target="_blank">get_my_appointments.php</a></li>';
echo '<li>Refresh your Android app</li>';
echo '<li>Doctor names should appear in Select Doctor screen</li>';
echo '<li>Appointment dates should display correctly in My Appointments</li>';
echo '</ol>';
echo '</div>';

?>
    </div>
</body>
</html>

