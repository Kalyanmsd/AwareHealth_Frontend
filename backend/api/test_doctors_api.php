<?php
/**
 * Test Doctors API Endpoint
 * This tests if the doctors API is working correctly
 */

ob_start();
ini_set('display_errors', '0');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

ob_end_clean();
ob_start();

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');

try {
    $conn = getDB();
    
    // Test query - get all Saveetha Hospital doctors
    $stmt = $conn->prepare("
        SELECT 
            d.id,
            d.user_id,
            u.name,
            d.specialty,
            d.experience,
            d.rating,
            d.availability,
            d.location
        FROM doctors d
        JOIN users u ON d.user_id = u.id
        WHERE d.location = 'Saveetha Hospital'
        ORDER BY u.name ASC
    ");
    
    $stmt->execute();
    $result = $stmt->get_result();
    
    $doctors = [];
    while ($row = $result->fetch_assoc()) {
        $doctors[] = [
            'id' => $row['id'],
            'name' => $row['name'],
            'specialty' => $row['specialty'] ?: 'General',
            'experience' => $row['experience'] ?: '0 years',
            'rating' => floatval($row['rating'] ?: 0),
            'availability' => $row['availability'] ?: 'Available',
            'location' => $row['location'] ?: 'Saveetha Hospital'
        ];
    }
    
    ob_end_clean();
    echo json_encode([
        'success' => true,
        'doctors' => $doctors,
        'count' => count($doctors),
        'hospital' => 'Saveetha Hospital',
        'message' => count($doctors) > 0 ? 'Doctors found' : 'No doctors found. Run auto_setup_doctors.php first.'
    ], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    
} catch (Exception $e) {
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => $e->getMessage(),
        'doctors' => [],
        'count' => 0
    ], JSON_UNESCAPED_UNICODE);
}

