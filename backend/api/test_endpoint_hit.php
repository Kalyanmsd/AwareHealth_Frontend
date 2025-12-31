<?php
/**
 * Test Endpoint Hit - Simple endpoint to verify if mobile can reach backend
 * GET or POST: http://172.20.10.2/AwareHealth/api/test_endpoint_hit.php
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

// Log that this endpoint was hit
$logFile = __DIR__ . '/../logs/endpoint_hits.log';
$logDir = dirname($logFile);
if (!is_dir($logDir)) {
    mkdir($logDir, 0777, true);
}

$logMessage = date('Y-m-d H:i:s') . " - test_endpoint_hit.php called\n";
$logMessage .= "  Method: " . $_SERVER['REQUEST_METHOD'] . "\n";
$logMessage .= "  Remote IP: " . ($_SERVER['REMOTE_ADDR'] ?? 'unknown') . "\n";
$logMessage .= "  User Agent: " . ($_SERVER['HTTP_USER_AGENT'] ?? 'unknown') . "\n";
$logMessage .= "  Request URI: " . ($_SERVER['REQUEST_URI'] ?? 'unknown') . "\n";
$logMessage .= "  HTTP Host: " . ($_SERVER['HTTP_HOST'] ?? 'unknown') . "\n";
$logMessage .= "---\n";

file_put_contents($logFile, $logMessage, FILE_APPEND);
error_log("✅ test_endpoint_hit.php called from: " . ($_SERVER['REMOTE_ADDR'] ?? 'unknown'));

echo json_encode([
    'success' => true,
    'message' => 'Endpoint is reachable!',
    'timestamp' => date('Y-m-d H:i:s'),
    'server_info' => [
        'remote_ip' => $_SERVER['REMOTE_ADDR'] ?? 'unknown',
        'method' => $_SERVER['REQUEST_METHOD'] ?? 'unknown',
        'host' => $_SERVER['HTTP_HOST'] ?? 'unknown'
    ],
    'instructions' => 'If you see this, your mobile can reach the backend!'
], JSON_PRETTY_PRINT);

