<?php
/**
 * Test Doctors API - Diagnostic Tool
 * Tests the get_doctors.php endpoint and displays results
 */

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Test Doctors API</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; font-size: 12px; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🧪 Test Doctors API</h1>
        
<?php

// Test 1: Database Connection
echo '<h2>Test 1: Database Connection</h2>';
$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

$conn = new mysqli($dbHost, $dbUser, $dbPass, $dbName);
if ($conn->connect_error) {
    echo '<div class="error">❌ Connection failed: ' . $conn->connect_error . '</div>';
    exit();
}
echo '<div class="success">✅ Database connection successful</div>';

// Test 2: Table Exists
echo '<h2>Test 2: Doctors Table</h2>';
$tableCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    echo '<div class="error">❌ Doctors table does not exist</div>';
} else {
    echo '<div class="success">✅ Doctors table exists</div>';
    
    // Show table structure
    $columns = $conn->query("SHOW COLUMNS FROM doctors");
    echo '<h3>Table Structure:</h3>';
    echo '<table>';
    echo '<tr><th>Column</th><th>Type</th><th>Null</th><th>Key</th><th>Default</th></tr>';
    while ($col = $columns->fetch_assoc()) {
        echo '<tr>';
        echo '<td>' . htmlspecialchars($col['Field']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Type']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Null']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Key']) . '</td>';
        echo '<td>' . htmlspecialchars($col['Default'] ?? 'NULL') . '</td>';
        echo '</tr>';
    }
    echo '</table>';
}

// Test 3: Data Count
echo '<h2>Test 3: Data Count</h2>';
$countResult = $conn->query("SELECT COUNT(*) as total FROM doctors");
$total = $countResult->fetch_assoc()['total'];
echo '<div class="info">Total doctors: ' . $total . '</div>';

$statusCheck = $conn->query("SHOW COLUMNS FROM doctors LIKE 'status'");
if ($statusCheck && $statusCheck->num_rows > 0) {
    $availableResult = $conn->query("SELECT COUNT(*) as available FROM doctors WHERE status='Available'");
    $available = $availableResult->fetch_assoc()['available'];
    echo '<div class="info">Available doctors: ' . $available . '</div>';
    
    // Show all statuses
    $statusResult = $conn->query("SELECT status, COUNT(*) as count FROM doctors GROUP BY status");
    echo '<h3>Status Distribution:</h3>';
    echo '<table>';
    echo '<tr><th>Status</th><th>Count</th></tr>';
    while ($row = $statusResult->fetch_assoc()) {
        echo '<tr><td>' . htmlspecialchars($row['status'] ?? 'NULL') . '</td><td>' . $row['count'] . '</td></tr>';
    }
    echo '</table>';
}

// Test 4: Sample Data
echo '<h2>Test 4: Sample Data (First 5 Records)</h2>';
$sampleResult = $conn->query("SELECT * FROM doctors LIMIT 5");
if ($sampleResult && $sampleResult->num_rows > 0) {
    echo '<table>';
    $firstRow = true;
    while ($row = $sampleResult->fetch_assoc()) {
        if ($firstRow) {
            echo '<tr>';
            foreach (array_keys($row) as $key) {
                echo '<th>' . htmlspecialchars($key) . '</th>';
            }
            echo '</tr>';
            $firstRow = false;
        }
        echo '<tr>';
        foreach ($row as $value) {
            echo '<td>' . htmlspecialchars($value ?? 'NULL') . '</td>';
        }
        echo '</tr>';
    }
    echo '</table>';
} else {
    echo '<div class="error">❌ No data found in doctors table</div>';
}

// Test 5: API Response
echo '<h2>Test 5: API Response</h2>';
$apiUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/api/get_doctors.php';
echo '<p>Testing: <a href="' . $apiUrl . '" target="_blank">' . $apiUrl . '</a></p>';

$ch = curl_init($apiUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 10);
$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if ($httpCode == 200 && $response) {
    $json = json_decode($response, true);
    if ($json) {
        if ($json['success'] && isset($json['doctors'])) {
            echo '<div class="success">✅ API Test PASSED!</div>';
            echo '<p><strong>Doctors Found:</strong> ' . count($json['doctors']) . '</p>';
            echo '<h3>API Response:</h3>';
            echo '<pre>' . htmlspecialchars(json_encode($json, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE)) . '</pre>';
        } else {
            echo '<div class="error">❌ API returned success=false</div>';
            echo '<p><strong>Message:</strong> ' . ($json['message'] ?? 'Unknown error') . '</p>';
            echo '<pre>' . htmlspecialchars($response) . '</pre>';
        }
    } else {
        echo '<div class="error">❌ Invalid JSON response</div>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>';
    }
} else {
    echo '<div class="error">❌ API Test FAILED</div>';
    echo '<p>HTTP Code: ' . $httpCode . '</p>';
    echo '<pre>' . htmlspecialchars($response) . '</pre>';
}

$conn->close();

?>
    </div>
</body>
</html>
