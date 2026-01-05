<?php
/**
 * Fix Doctor Names - Update database with proper doctor names
 * This script updates doctors table to ensure all doctors have names
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
    <title>Fix Doctor Names</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Fix Doctor Names</h1>
        
<?php

// Check if name column exists
$columnsResult = $conn->query("SHOW COLUMNS FROM doctors");
$hasNameColumn = false;
$hasSpecialization = false;
$hasSpecialty = false;

while ($col = $columnsResult->fetch_assoc()) {
    if ($col['Field'] === 'name') {
        $hasNameColumn = true;
    }
    if ($col['Field'] === 'specialization') {
        $hasSpecialization = true;
    }
    if ($col['Field'] === 'specialty') {
        $hasSpecialty = true;
    }
}

// Step 1: Add name column if it doesn't exist
if (!$hasNameColumn) {
    echo '<h2>Step 1: Adding name column</h2>';
    $alterSql = "ALTER TABLE doctors ADD COLUMN name VARCHAR(255) DEFAULT NULL AFTER id";
    if ($conn->query($alterSql)) {
        echo '<div class="success">✅ Name column added successfully</div>';
        $hasNameColumn = true;
    } else {
        echo '<div class="error">❌ Error adding name column: ' . $conn->error . '</div>';
    }
} else {
    echo '<h2>Step 1: Name column exists</h2>';
    echo '<div class="info">✅ Name column already exists</div>';
}

// Step 2: Check current data
echo '<h2>Step 2: Checking current data</h2>';
$result = $conn->query("SELECT id, name, specialization, specialty, status FROM doctors ORDER BY id");
$doctors = [];
while ($row = $result->fetch_assoc()) {
    $doctors[] = $row;
}

echo '<p>Found ' . count($doctors) . ' doctors in database</p>';

// Step 3: Update names based on specialization/specialty
echo '<h2>Step 3: Updating doctor names</h2>';

$nameMapping = [
    'General Physician' => 'Dr. Rajesh Kumar',
    'Cardiologist' => 'Dr. Priya Sharma',
    'Cardiology' => 'Dr. Priya Sharma',
    'Dermatologist' => 'Dr. Anil Patel',
    'Dermatology' => 'Dr. Anil Patel',
    'Pediatrician' => 'Dr. Meera Singh',
    'Pediatrics' => 'Dr. Meera Singh',
    'Orthopedic Surgeon' => 'Dr. Vikram Reddy',
    'Orthopedics' => 'Dr. Vikram Reddy'
];

$updatedCount = 0;
foreach ($doctors as $doctor) {
    $id = $doctor['id'];
    $currentName = $doctor['name'] ?? '';
    $specialization = $doctor['specialization'] ?? $doctor['specialty'] ?? '';
    
    // Skip if name already exists and is not empty
    if (!empty($currentName)) {
        continue;
    }
    
    // Determine new name based on specialization
    $newName = null;
    if (!empty($specialization)) {
        $newName = $nameMapping[$specialization] ?? 'Dr. ' . $specialization;
    } else {
        $newName = 'Dr. Doctor ' . $id;
    }
    
    // Update name
    $updateSql = "UPDATE doctors SET name = ? WHERE id = ?";
    $stmt = $conn->prepare($updateSql);
    $stmt->bind_param("si", $newName, $id);
    
    if ($stmt->execute()) {
        $updatedCount++;
        echo '<div class="info">✅ Updated doctor ID ' . $id . ': "' . $newName . '"</div>';
    } else {
        echo '<div class="error">❌ Error updating doctor ID ' . $id . ': ' . $conn->error . '</div>';
    }
    $stmt->close();
}

if ($updatedCount > 0) {
    echo '<div class="success">✅ Updated ' . $updatedCount . ' doctor names</div>';
} else {
    echo '<div class="info">✅ All doctors already have names</div>';
}

// Step 4: Verify all doctors have names
echo '<h2>Step 4: Verification</h2>';
$verifyResult = $conn->query("SELECT id, name, specialization, specialty FROM doctors WHERE name IS NULL OR name = ''");
if ($verifyResult && $verifyResult->num_rows > 0) {
    echo '<div class="error">⚠️ Still found ' . $verifyResult->num_rows . ' doctors without names</div>';
} else {
    echo '<div class="success">✅ All doctors now have names!</div>';
}

// Step 5: Show final data
echo '<h2>Step 5: Final Data</h2>';
$finalResult = $conn->query("SELECT id, name, specialization, specialty, status FROM doctors ORDER BY id");
echo '<table>';
echo '<tr><th>ID</th><th>Name</th><th>Specialization</th><th>Status</th></tr>';
while ($row = $finalResult->fetch_assoc()) {
    echo '<tr>';
    echo '<td>' . htmlspecialchars($row['id']) . '</td>';
    echo '<td><strong>' . htmlspecialchars($row['name'] ?? 'NULL') . '</strong></td>';
    echo '<td>' . htmlspecialchars($row['specialization'] ?? $row['specialty'] ?? 'N/A') . '</td>';
    echo '<td>' . htmlspecialchars($row['status'] ?? 'N/A') . '</td>';
    echo '</tr>';
}
echo '</table>';

$conn->close();

echo '<div class="success">';
echo '<h2>✅ Fix Complete!</h2>';
echo '<p><strong>Next steps:</strong></p>';
echo '<ol>';
echo '<li>Test API: <a href="get_doctors.php" target="_blank">get_doctors.php</a></li>';
echo '<li>Refresh your Android app</li>';
echo '<li>Doctor names should now appear!</li>';
echo '</ol>';
echo '</div>';

?>
    </div>
</body>
</html>

