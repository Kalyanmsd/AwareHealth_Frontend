<?php
/**
 * Test Doctors JOIN - Verify the JOIN query works correctly
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
    <title>Test Doctors JOIN</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; font-size: 11px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🧪 Test Doctors JOIN Query</h1>
        
<?php

// Check if user_id column exists
$columnsResult = $conn->query("SHOW COLUMNS FROM doctors");
$hasUserId = false;
$hasStatus = false;

while ($col = $columnsResult->fetch_assoc()) {
    if ($col['Field'] === 'user_id') $hasUserId = true;
    if ($col['Field'] === 'status') $hasStatus = true;
}

echo '<h2>Table Structure Check</h2>';
echo '<div class="info">';
echo 'Doctors table has user_id: ' . ($hasUserId ? '✅ YES' : '❌ NO') . '<br>';
echo 'Doctors table has status: ' . ($hasStatus ? '✅ YES' : '❌ NO') . '<br>';
echo '</div>';

// Test JOIN query
echo '<h2>JOIN Query Test</h2>';

if ($hasUserId) {
    $sql = "SELECT 
                d.id,
                d.user_id,
                u.name as doctor_name,
                COALESCE(d.specialization, d.specialty, 'General Physician') as specialty,
                COALESCE(d.location, d.hospital, 'Saveetha Hospital') as location,
                d.experience,
                d.rating,
                COALESCE(d.availability, d.status, 'Available') as availability,
                COALESCE(d.status, 'Available') as status
            FROM doctors d
            LEFT JOIN users u ON d.user_id = u.id";
    
    if ($hasStatus) {
        $sql .= " WHERE d.status = 'Available'";
    }
    $sql .= " ORDER BY u.name ASC LIMIT 10";
    
    echo '<div class="info">SQL Query:</div>';
    echo '<pre>' . htmlspecialchars($sql) . '</pre>';
    
    $result = $conn->query($sql);
    
    if ($result && $result->num_rows > 0) {
        echo '<div class="success">✅ JOIN Query Successful! Found ' . $result->num_rows . ' doctors</div>';
        echo '<table>';
        echo '<tr><th>ID</th><th>User ID</th><th>Doctor Name (from users)</th><th>Specialty</th><th>Location</th><th>Experience</th><th>Rating</th><th>Status</th></tr>';
        while ($row = $result->fetch_assoc()) {
            echo '<tr>';
            echo '<td>' . htmlspecialchars(substr($row['id'], 0, 20)) . '...</td>';
            echo '<td>' . htmlspecialchars(substr($row['user_id'] ?? 'NULL', 0, 20)) . '...</td>';
            echo '<td><strong>' . htmlspecialchars($row['doctor_name'] ?? 'NULL') . '</strong></td>';
            echo '<td>' . htmlspecialchars($row['specialty'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['location'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['experience'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['rating'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['status'] ?? 'N/A') . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    } else {
        echo '<div class="error">❌ JOIN Query returned no results</div>';
        echo '<p>Possible issues:</p>';
        echo '<ul>';
        echo '<li>No doctors in database</li>';
        echo '<li>No matching users for doctors</li>';
        echo '<li>All doctors have status != "Available"</li>';
        echo '</ul>';
    }
} else {
    echo '<div class="error">❌ Doctors table does not have user_id column</div>';
    echo '<p>Cannot use JOIN. Please add user_id column to doctors table.</p>';
}

// Test API response
echo '<h2>API Response Test</h2>';
$apiUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/api/get_doctors.php';
$ch = curl_init($apiUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 10);
$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if ($httpCode == 200 && $response) {
    $json = json_decode($response, true);
    if ($json && isset($json['doctors'])) {
        echo '<div class="success">✅ API Test PASSED!</div>';
        echo '<p><strong>Doctors Found:</strong> ' . count($json['doctors']) . '</p>';
        
        // Check if names are present
        $allHaveNames = true;
        foreach ($json['doctors'] as $doctor) {
            if (empty($doctor['name'])) {
                $allHaveNames = false;
                break;
            }
        }
        
        if ($allHaveNames) {
            echo '<div class="success">✅ All doctors have names!</div>';
        } else {
            echo '<div class="error">❌ Some doctors are missing names</div>';
        }
        
        echo '<h3>Sample Response:</h3>';
        echo '<pre>' . htmlspecialchars(json_encode(array_slice($json['doctors'], 0, 3), JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE)) . '</pre>';
    } else {
        echo '<div class="error">❌ API returned invalid response</div>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>';
    }
} else {
    echo '<div class="error">❌ API Test FAILED (HTTP ' . $httpCode . ')</div>';
    echo '<pre>' . htmlspecialchars($response) . '</pre>';
}

$conn->close();

?>
    </div>
</body>
</html>

