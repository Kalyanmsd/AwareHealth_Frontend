<?php
/**
 * Database Connection for OTP Verification
 * Simple database connection helper
 */

// Database configuration
require_once __DIR__ . '/../config.php';

// Database connection function
function getOTPDB() {
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    
    if ($conn->connect_error) {
        // Log error
        error_log("OTP DB Connection Error: " . $conn->connect_error);
        
        // Return JSON error response
        header('Content-Type: application/json');
        header('Access-Control-Allow-Origin: *');
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Database connection failed: ' . $conn->connect_error
        ]);
        exit();
    }
    
    $conn->set_charset("utf8mb4");
    return $conn;
}
