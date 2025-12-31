<?php
/**
 * Simple Health API Test Endpoint
 * Test if health API is accessible
 */

header('Content-Type: application/json');

// Test database connection
require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';

try {
    $conn = getDB();
    
    // Test query
    $result = $conn->query("SELECT COUNT(*) as count FROM diseases");
    
    if ($result) {
        $row = $result->fetch_assoc();
        $count = $row['count'] ?? 0;
        
        http_response_code(200);
        echo json_encode([
            'success' => true,
            'message' => 'Health API is working',
            'database_connected' => true,
            'diseases_count' => (int)$count,
            'test' => 'This is a test endpoint'
        ]);
    } else {
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Database query failed',
            'error' => $conn->error ?? 'Unknown error'
        ]);
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Server error',
        'error' => $e->getMessage()
    ]);
}
?>

