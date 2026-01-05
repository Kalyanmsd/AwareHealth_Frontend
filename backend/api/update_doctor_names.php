<?php
/**
 * Update Doctor Names - Add names to all doctors in database
 * This will update the name column for all doctors based on their specialty
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
    <title>Update Doctor Names</title>
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
        <h1>🔧 Updating Doctor Names</h1>
        
<?php

// Name mapping based on specialty
$nameMapping = [
    'General' => 'Dr. Rajesh Kumar',
    'Cardiology' => 'Dr. Priya Sharma',
    'Pediatrics' => 'Dr. Meera Singh',
    'Orthopedics' => 'Dr. Vikram Reddy',
    'Gynecology' => 'Dr. Anjali Desai',
    'Neurology' => 'Dr. Ramesh Iyer',
    'Dermatology' => 'Dr. Anil Patel'
];

// Get all doctors
$result = $conn->query("SELECT id, specialty, name FROM doctors ORDER BY id");

$updated = 0;
$doctors = [];

while ($row = $result->fetch_assoc()) {
    $id = $row['id'];
    $specialty = $row['specialty'] ?? 'General';
    $currentName = $row['name'] ?? '';
    
    // Determine new name
    $newName = $nameMapping[$specialty] ?? 'Dr. ' . $specialty . ' Specialist';
    
    // Update if name is empty or NULL
    if (empty($currentName)) {
        $updateSql = "UPDATE doctors SET name = ? WHERE id = ?";
        $stmt = $conn->prepare($updateSql);
        $stmt->bind_param("ss", $newName, $id);
        
        if ($stmt->execute()) {
            $updated++;
            echo '<div class="success">✅ Updated ID ' . htmlspecialchars($id) . ': "' . htmlspecialchars($newName) . '"</div>';
        }
        $stmt->close();
    }
    
    $doctors[] = ['id' => $id, 'specialty' => $specialty, 'name' => $newName];
}

echo '<div class="success">✅ Updated ' . $updated . ' doctor names</div>';

// Show final result
echo '<h2>Final Result</h2>';
$finalResult = $conn->query("SELECT id, name, specialty, status FROM doctors ORDER BY id");
echo '<table>';
echo '<tr><th>ID</th><th>Name</th><th>Specialty</th><th>Status</th></tr>';
while ($row = $finalResult->fetch_assoc()) {
    echo '<tr>';
    echo '<td>' . htmlspecialchars(substr($row['id'], 0, 20)) . '...</td>';
    echo '<td><strong>' . htmlspecialchars($row['name'] ?? 'NULL') . '</strong></td>';
    echo '<td>' . htmlspecialchars($row['specialty'] ?? 'N/A') . '</td>';
    echo '<td>' . htmlspecialchars($row['status'] ?? 'N/A') . '</td>';
    echo '</tr>';
}
echo '</table>';

$conn->close();

echo '<div class="success">';
echo '<h2>✅ Update Complete!</h2>';
echo '<p>All doctors now have names. Refresh your app to see them!</p>';
echo '</div>';

?>
    </div>
</body>
</html>

