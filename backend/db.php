<?php
/**
 * Database Connection File
 * Connects to MySQL database named 'awarehealth'
 * 
 * Configuration:
 * - Host: localhost
 * - User: root
 * - Password: (empty - XAMPP default)
 * - Database: awarehealth
 */

// Database configuration
$db_host = 'localhost';
$db_user = 'root';
$db_pass = ''; // Empty password for XAMPP default
$db_name = 'awarehealth';

// Create connection
$conn = new mysqli($db_host, $db_user, $db_pass, $db_name);

// Check connection
if ($conn->connect_error) {
    // Return JSON error response
    header('Content-Type: application/json');
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit();
}

// Set charset to UTF-8 for proper character encoding
$conn->set_charset("utf8mb4");

// Connection successful - $conn is now available for use
// Note: This file should be included/required in other PHP files
// Do not close connection here - let the calling file manage it

?>

