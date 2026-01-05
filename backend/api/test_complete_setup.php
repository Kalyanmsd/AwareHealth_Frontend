<?php
/**
 * COMPLETE SETUP TEST - Tests everything end-to-end
 */

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Complete Setup Test</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; font-size: 11px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🧪 Complete Setup Test</h1>
        
<?php

// First, run the fix
echo '<div class="info">Running automatic fix first...</div>';
include 'fix_doctors_complete.php';

echo '<hr><h2>Testing API Endpoint</h2>';

// Test the actual API endpoint
$testUrl = 'http://' . $_SERVER['HTTP_HOST'] . '/AwareHealth/backend/api/get_doctors.php';
echo '<p>Testing: <a href="' . $testUrl . '" target="_blank">' . $testUrl . '</a></p>';

$ch = curl_init($testUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 10);
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
$error = curl_error($ch);
curl_close($ch);

if ($httpCode == 200 && $response) {
    $json = json_decode($response, true);
    if ($json && isset($json['success']) && $json['success'] && isset($json['doctors'])) {
        echo '<div class="success">✅ API Test PASSED!</div>';
        echo '<p><strong>Response:</strong></p>';
        echo '<pre>' . htmlspecialchars(json_encode($json, JSON_PRETTY_PRINT)) . '</pre>';
        echo '<p><strong>Doctors Found:</strong> ' . count($json['doctors']) . '</p>';
    } else {
        echo '<div class="error">❌ API returned invalid response</div>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>';
    }
} else {
    echo '<div class="error">❌ API Test FAILED</div>';
    echo '<p>HTTP Code: ' . $httpCode . '</p>';
    if ($error) {
        echo '<p>Error: ' . htmlspecialchars($error) . '</p>';
    }
    echo '<pre>' . htmlspecialchars($response) . '</pre>';
}

// Test from mobile perspective
echo '<hr><h2>Mobile Device Test URLs</h2>';
echo '<div class="info">';
echo '<p>For your Android app, use these URLs (replace 172.20.10.2 with your PC IP):</p>';
echo '<ul>';
echo '<li><strong>API Endpoint:</strong> <code>http://172.20.10.2/AwareHealth/backend/api/get_doctors.php</code></li>';
echo '<li><strong>Alternative:</strong> <code>http://172.20.10.2/AwareHealth/api/get_doctors.php</code></li>';
echo '</ul>';
echo '<p><strong>Note:</strong> Make sure your RetrofitClient.kt BASE_URL matches!</p>';
echo '</div>';

?>
    </div>
</body>
</html>

