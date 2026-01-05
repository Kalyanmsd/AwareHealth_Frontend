<?php
/**
 * Test Disease API by Name - Verify case-insensitive name search works
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
    <title>Test Disease by Name</title>
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
        .test-section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 4px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🧪 Test Disease API by Name</h1>
        
<?php

// Get sample diseases
$sampleResult = $conn->query("SELECT id, name FROM diseases LIMIT 10");
$sampleDiseases = [];
while ($row = $sampleResult->fetch_assoc()) {
    $sampleDiseases[] = $row;
}

echo '<h2>Sample Diseases in Database</h2>';
if (count($sampleDiseases) > 0) {
    echo '<table>';
    echo '<tr><th>ID</th><th>Name</th></tr>';
    foreach ($sampleDiseases as $disease) {
        echo '<tr>';
        echo '<td>' . htmlspecialchars(substr($disease['id'], 0, 30)) . '...</td>';
        echo '<td><strong>' . htmlspecialchars($disease['name']) . '</strong></td>';
        echo '</tr>';
    }
    echo '</table>';
} else {
    echo '<div class="error">❌ No diseases found in database</div>';
}

if (count($sampleDiseases) > 0) {
    $testDisease = $sampleDiseases[0];
    $testName = $testDisease['name'];
    
    // Test 1: Search by ID
    echo '<div class="test-section">';
    echo '<h2>Test 1: Search by ID</h2>';
    $testId = $testDisease['id'];
    $apiUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/api/simple_diseases.php?id=' . urlencode($testId);
    echo '<div class="info">API URL: <a href="' . htmlspecialchars($apiUrl) . '" target="_blank">' . htmlspecialchars($apiUrl) . '</a></div>';
    
    $ch = curl_init($apiUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($httpCode == 200 && $response) {
        $json = json_decode($response, true);
        if ($json && isset($json['success']) && $json['success'] && isset($json['disease'])) {
            echo '<div class="success">✅ Search by ID PASSED!</div>';
            $disease = $json['disease'];
            echo '<p><strong>Found:</strong> ' . htmlspecialchars($disease['name']) . '</p>';
            echo '<p><strong>Has Description:</strong> ' . (!empty($disease['description']) ? '✅ YES' : '❌ NO') . '</p>';
            echo '<p><strong>Symptoms Count:</strong> ' . count($disease['symptoms'] ?? []) . '</p>';
            echo '<p><strong>Prevention Count:</strong> ' . count($disease['prevention'] ?? []) . '</p>';
        } else {
            echo '<div class="error">❌ API returned error</div>';
            echo '<pre>' . htmlspecialchars($response) . '</pre>';
        }
    } else {
        echo '<div class="error">❌ API Test FAILED (HTTP ' . $httpCode . ')</div>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>';
    }
    echo '</div>';
    
    // Test 2: Search by Name (exact)
    echo '<div class="test-section">';
    echo '<h2>Test 2: Search by Name (Exact - Case Sensitive)</h2>';
    $apiUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/api/simple_diseases.php?name=' . urlencode($testName);
    echo '<div class="info">API URL: <a href="' . htmlspecialchars($apiUrl) . '" target="_blank">' . htmlspecialchars($apiUrl) . '</a></div>';
    
    $ch = curl_init($apiUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($httpCode == 200 && $response) {
        $json = json_decode($response, true);
        if ($json && isset($json['success']) && $json['success'] && isset($json['disease'])) {
            echo '<div class="success">✅ Search by Name (Exact) PASSED!</div>';
            $disease = $json['disease'];
            echo '<p><strong>Found:</strong> ' . htmlspecialchars($disease['name']) . '</p>';
        } else {
            echo '<div class="error">❌ API returned error</div>';
            echo '<pre>' . htmlspecialchars($response) . '</pre>';
        }
    } else {
        echo '<div class="error">❌ API Test FAILED (HTTP ' . $httpCode . ')</div>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>';
    }
    echo '</div>';
    
    // Test 3: Search by Name (Lowercase)
    echo '<div class="test-section">';
    echo '<h2>Test 3: Search by Name (Lowercase - Case Insensitive)</h2>';
    $testNameLower = strtolower($testName);
    $apiUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/api/simple_diseases.php?name=' . urlencode($testNameLower);
    echo '<div class="info">Testing: "' . htmlspecialchars($testNameLower) . '" (lowercase version of "' . htmlspecialchars($testName) . '")</div>';
    echo '<div class="info">API URL: <a href="' . htmlspecialchars($apiUrl) . '" target="_blank">' . htmlspecialchars($apiUrl) . '</a></div>';
    
    $ch = curl_init($apiUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($httpCode == 200 && $response) {
        $json = json_decode($response, true);
        if ($json && isset($json['success']) && $json['success'] && isset($json['disease'])) {
            echo '<div class="success">✅ Search by Name (Lowercase) PASSED! Case-insensitive matching works!</div>';
            $disease = $json['disease'];
            echo '<p><strong>Found:</strong> ' . htmlspecialchars($disease['name']) . '</p>';
            echo '<p><strong>Has Description:</strong> ' . (!empty($disease['description']) ? '✅ YES (' . strlen($disease['description']) . ' chars)' : '❌ NO') . '</p>';
            echo '<p><strong>Symptoms Count:</strong> ' . count($disease['symptoms'] ?? []) . '</p>';
            echo '<p><strong>Prevention Count:</strong> ' . count($disease['prevention'] ?? []) . '</p>';
            if (count($disease['prevention'] ?? []) > 0) {
                echo '<p><strong>First Prevention Tip:</strong> ' . htmlspecialchars($disease['prevention'][0]) . '</p>';
            }
        } else {
            echo '<div class="error">❌ Case-insensitive search FAILED</div>';
            echo '<pre>' . htmlspecialchars($response) . '</pre>';
        }
    } else {
        echo '<div class="error">❌ API Test FAILED (HTTP ' . $httpCode . ')</div>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>';
    }
    echo '</div>';
    
    // Test 4: Search by Name (Uppercase)
    echo '<div class="test-section">';
    echo '<h2>Test 4: Search by Name (Uppercase - Case Insensitive)</h2>';
    $testNameUpper = strtoupper($testName);
    $apiUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/api/simple_diseases.php?name=' . urlencode($testNameUpper);
    echo '<div class="info">Testing: "' . htmlspecialchars($testNameUpper) . '" (uppercase version of "' . htmlspecialchars($testName) . '")</div>';
    echo '<div class="info">API URL: <a href="' . htmlspecialchars($apiUrl) . '" target="_blank">' . htmlspecialchars($apiUrl) . '</a></div>';
    
    $ch = curl_init($apiUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($httpCode == 200 && $response) {
        $json = json_decode($response, true);
        if ($json && isset($json['success']) && $json['success'] && isset($json['disease'])) {
            echo '<div class="success">✅ Search by Name (Uppercase) PASSED! Case-insensitive matching works!</div>';
            $disease = $json['disease'];
            echo '<p><strong>Found:</strong> ' . htmlspecialchars($disease['name']) . '</p>';
        } else {
            echo '<div class="error">❌ Case-insensitive search FAILED</div>';
            echo '<pre>' . htmlspecialchars($response) . '</pre>';
        }
    } else {
        echo '<div class="error">❌ API Test FAILED (HTTP ' . $httpCode . ')</div>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>';
    }
    echo '</div>';
}

$conn->close();

?>
    </div>
</body>
</html>

