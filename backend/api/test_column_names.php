<?php
/**
 * Test Column Names - Verify correct column usage
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
    <title>Test Column Names</title>
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
        <h1>🔍 Test Column Names</h1>
        
<?php

// Check doctors table structure
echo '<h2>Doctors Table Columns</h2>';
$columnsResult = $conn->query("SHOW COLUMNS FROM doctors");
$columns = [];
while ($col = $columnsResult->fetch_assoc()) {
    $columns[] = $col['Field'];
}

echo '<div class="info">Columns found: ' . implode(', ', $columns) . '</div>';

$hasSpecialty = in_array('specialty', $columns);
$hasSpecialization = in_array('specialization', $columns);
$hasUserId = in_array('user_id', $columns);
$hasStatus = in_array('status', $columns);
$hasLocation = in_array('location', $columns);
$hasHospital = in_array('hospital', $columns);

echo '<table>';
echo '<tr><th>Column</th><th>Exists</th></tr>';
echo '<tr><td>specialty</td><td>' . ($hasSpecialty ? '✅ YES' : '❌ NO') . '</td></tr>';
echo '<tr><td>specialization</td><td>' . ($hasSpecialization ? '✅ YES' : '❌ NO') . '</td></tr>';
echo '<tr><td>user_id</td><td>' . ($hasUserId ? '✅ YES' : '❌ NO') . '</td></tr>';
echo '<tr><td>status</td><td>' . ($hasStatus ? '✅ YES' : '❌ NO') . '</td></tr>';
echo '<tr><td>location</td><td>' . ($hasLocation ? '✅ YES' : '❌ NO') . '</td></tr>';
echo '<tr><td>hospital</td><td>' . ($hasHospital ? '✅ YES' : '❌ NO') . '</td></tr>';
echo '</table>';

// Determine which column to use
$specialtyColumn = $hasSpecialty ? 'd.specialty' : ($hasSpecialization ? 'd.specialization' : "'General Physician'");
$locationColumn = $hasLocation ? 'd.location' : ($hasHospital ? 'd.hospital' : "'Saveetha Hospital'");

echo '<h2>Column Selection Logic</h2>';
echo '<div class="info">Specialty column: <strong>' . $specialtyColumn . '</strong></div>';
echo '<div class="info">Location column: <strong>' . $locationColumn . '</strong></div>';

// Test SQL query
echo '<h2>Test SQL Query</h2>';

if ($hasUserId) {
    $sql = "SELECT 
                d.id,
                u.name,
                $specialtyColumn as specialty,
                $locationColumn as location,
                d.experience,
                d.rating,
                d.status
            FROM doctors d
            LEFT JOIN users u ON d.user_id = u.id
            " . ($hasStatus ? "WHERE d.status = 'Available'" : "") . "
            ORDER BY u.name ASC
            LIMIT 5";
} else {
    $sql = "SELECT 
                d.id,
                d.name,
                $specialtyColumn as specialty,
                $locationColumn as location,
                d.experience,
                d.rating,
                d.status
            FROM doctors d
            " . ($hasStatus ? "WHERE d.status = 'Available'" : "") . "
            ORDER BY d.name ASC
            LIMIT 5";
}

echo '<pre>' . htmlspecialchars($sql) . '</pre>';

$result = $conn->query($sql);

if ($result) {
    if ($result->num_rows > 0) {
        echo '<div class="success">✅ Query executed successfully! Found ' . $result->num_rows . ' doctors</div>';
        echo '<table>';
        echo '<tr><th>ID</th><th>Name</th><th>Specialty</th><th>Location</th><th>Experience</th><th>Rating</th><th>Status</th></tr>';
        while ($row = $result->fetch_assoc()) {
            echo '<tr>';
            echo '<td>' . htmlspecialchars(substr($row['id'], 0, 20)) . '...</td>';
            echo '<td>' . htmlspecialchars($row['name'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['specialty'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['location'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['experience'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['rating'] ?? 'N/A') . '</td>';
            echo '<td>' . htmlspecialchars($row['status'] ?? 'N/A') . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    } else {
        echo '<div class="error">❌ Query executed but returned no results</div>';
    }
} else {
    echo '<div class="error">❌ Query failed: ' . $conn->error . '</div>';
}

// Test API
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
    if ($json) {
        if (isset($json['success']) && $json['success'] && isset($json['doctors'])) {
            echo '<div class="success">✅ API Test PASSED!</div>';
            echo '<p><strong>Doctors Found:</strong> ' . count($json['doctors']) . '</p>';
            echo '<pre>' . htmlspecialchars(json_encode(array_slice($json['doctors'], 0, 2), JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE)) . '</pre>';
        } else {
            echo '<div class="error">❌ API returned error: ' . ($json['message'] ?? 'Unknown error') . '</div>';
            echo '<pre>' . htmlspecialchars($response) . '</pre>';
        }
    } else {
        echo '<div class="error">❌ API returned invalid JSON</div>';
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

