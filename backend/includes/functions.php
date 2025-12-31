<?php
/**
 * Helper Functions
 */

function sendResponse($statusCode, $data) {
    // End and clean any output buffer
    while (ob_get_level() > 0) {
        ob_end_clean();
    }
    
    // Set headers
    http_response_code($statusCode);
    header('Content-Type: application/json; charset=utf-8');
    header('Cache-Control: no-cache, must-revalidate');
    
    // Encode JSON
    $json = json_encode($data, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
    
    if ($json === false) {
        // JSON encoding failed
        error_log("JSON encode error: " . json_last_error_msg());
        http_response_code(500);
        $json = json_encode(['success' => false, 'message' => 'Internal server error: JSON encoding failed'], JSON_UNESCAPED_UNICODE);
    }
    
    // Output JSON
    echo $json;
    
    // Flush and exit
    if (function_exists('fastcgi_finish_request')) {
        fastcgi_finish_request();
    }
    exit();
}

function getJsonInput() {
    $input = file_get_contents('php://input');
    
    // If input is empty, return empty array (for GET requests or empty POST)
    if (empty(trim($input))) {
        return [];
    }
    
    $data = json_decode($input, true);
    
    // Only error if JSON decode fails and input was not empty
    if (json_last_error() !== JSON_ERROR_NONE) {
        sendResponse(400, ['success' => false, 'message' => 'Invalid JSON input']);
    }
    
    return $data ?: [];
}

function generateUUID() {
    return sprintf(
        '%04x%04x-%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
        mt_rand(0, 0xffff),
        mt_rand(0, 0xffff),
        mt_rand(0, 0xffff),
        mt_rand(0, 0xffff),
        mt_rand(0, 0x0fff) | 0x4000,
        mt_rand(0, 0x3fff) | 0x8000,
        mt_rand(0, 0xffff),
        mt_rand(0, 0xffff),
        mt_rand(0, 0xffff),
        mt_rand(0, 0xffff),
        mt_rand(0, 0xffff)
    );
}

function validateRequired($data, $fields) {
    $missing = [];
    foreach ($fields as $field) {
        if (!isset($data[$field]) || empty($data[$field])) {
            $missing[] = $field;
        }
    }
    return $missing;
}

function sanitizeInput($data) {
    if (is_array($data)) {
        return array_map('sanitizeInput', $data);
    }
    return htmlspecialchars(strip_tags(trim($data)), ENT_QUOTES, 'UTF-8');
}

function getPathSegments() {
    $path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
    $basePath = '/AwareHealth/api';
    if (strpos($path, $basePath) === 0) {
        $path = substr($path, strlen($basePath));
    }
    $path = trim($path, '/');
    return $path ? explode('/', $path) : [];
}

