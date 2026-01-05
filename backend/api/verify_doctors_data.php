<?php
/**
 * Verify Doctors Data - Check what's actually in the database
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
    <title>Verify Doctors Data</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        .warning { color: #FB8C00; background: #FFF3E0; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Verify Doctors Data</h1>
        
        <h2>All Doctors in Database</h2>
        <?php
        $result = $conn->query("SELECT * FROM doctors ORDER BY id");
        if ($result && $result->num_rows > 0) {
            echo '<table>';
            $firstRow = true;
            while ($row = $result->fetch_assoc()) {
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
                    $displayValue = $value ?? '<em style="color: #999;">NULL</em>';
                    if ($displayValue === '') {
                        $displayValue = '<em style="color: #999;">EMPTY</em>';
                    }
                    echo '<td>' . htmlspecialchars($displayValue) . '</td>';
                }
                echo '</tr>';
            }
            echo '</table>';
        } else {
            echo '<p>No doctors found in database.</p>';
        }
        ?>
        
        <h2>Available Doctors (status='Available')</h2>
        <?php
        $availableResult = $conn->query("SELECT * FROM doctors WHERE status='Available' ORDER BY id");
        if ($availableResult && $availableResult->num_rows > 0) {
            echo '<table>';
            $firstRow = true;
            while ($row = $availableResult->fetch_assoc()) {
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
                    $displayValue = $value ?? '<em style="color: #999;">NULL</em>';
                    if ($displayValue === '') {
                        $displayValue = '<em style="color: #999;">EMPTY</em>';
                    }
                    echo '<td>' . htmlspecialchars($displayValue) . '</td>';
                }
                echo '</tr>';
            }
            echo '</table>';
        } else {
            echo '<p>No available doctors found.</p>';
        }
        ?>
        
        <h2>Check for Empty Names</h2>
        <?php
        $emptyNames = $conn->query("SELECT id, name, specialization, status FROM doctors WHERE name IS NULL OR name = ''");
        if ($emptyNames && $emptyNames->num_rows > 0) {
            echo '<div class="warning">⚠️ Found ' . $emptyNames->num_rows . ' doctors with empty names:</div>';
            echo '<table>';
            echo '<tr><th>ID</th><th>Name</th><th>Specialization</th><th>Status</th></tr>';
            while ($row = $emptyNames->fetch_assoc()) {
                echo '<tr>';
                echo '<td>' . htmlspecialchars($row['id']) . '</td>';
                echo '<td><strong style="color: red;">EMPTY</strong></td>';
                echo '<td>' . htmlspecialchars($row['specialization'] ?? 'N/A') . '</td>';
                echo '<td>' . htmlspecialchars($row['status'] ?? 'N/A') . '</td>';
                echo '</tr>';
            }
            echo '</table>';
        } else {
            echo '<p>✅ All doctors have names.</p>';
        }
        ?>
        
        <h2>API Response Test</h2>
        <?php
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
                echo '<p>✅ API Response:</p>';
                echo '<pre>' . htmlspecialchars(json_encode($json, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE)) . '</pre>';
                
                // Check if names are present
                $namesMissing = false;
                foreach ($json['doctors'] as $doctor) {
                    if (empty($doctor['name'])) {
                        $namesMissing = true;
                        break;
                    }
                }
                
                if ($namesMissing) {
                    echo '<div class="warning">⚠️ WARNING: Some doctors are missing names in API response!</div>';
                } else {
                    echo '<p>✅ All doctors have names in API response.</p>';
                }
            }
        }
        ?>
        
    </div>
</body>
</html>

<?php
$conn->close();
?>

