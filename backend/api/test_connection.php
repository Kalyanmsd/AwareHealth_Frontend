<?php
/**
 * Simple connection test endpoint
 * Test URL: http://192.168.1.10/AwareHealth/api/test_connection.php
 */

header('Content-Type: application/json');

$response = [
    'success' => true,
    'message' => 'Backend is reachable!',
    'timestamp' => date('Y-m-d H:i:s'),
    'server' => $_SERVER['SERVER_NAME'] ?? 'unknown',
    'method' => $_SERVER['REQUEST_METHOD'] ?? 'unknown'
];

echo json_encode($response, JSON_PRETTY_PRINT);

