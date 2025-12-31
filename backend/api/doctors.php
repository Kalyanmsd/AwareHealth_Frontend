<?php
/**
 * Doctors API Endpoints
 * 
 * Endpoints:
 * GET /doctors          - Get all doctors
 * GET /doctors/{id}     - Get single doctor
 */

// Start output buffering IMMEDIATELY
ob_start();

// Turn off ALL error display
ini_set('display_errors', '0');
ini_set('display_startup_errors', '0');
error_reporting(E_ALL & ~E_NOTICE & ~E_WARNING & ~E_DEPRECATED);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

// Discard ALL output from require statements
ob_end_clean();
ob_start();

// CORS headers
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    ob_end_clean();
    exit();
}

// Get database connection
try {
    $conn = getDB();
} catch (Exception $e) {
    error_log("Doctors API - Database connection error: " . $e->getMessage());
    ob_end_clean();
    sendResponse(500, [
        'success' => false,
        'message' => 'Database connection failed'
    ]);
}
$method = $_SERVER['REQUEST_METHOD'];
$segments = getPathSegments();

// Get doctor ID from URL if present
$doctorId = $segments[1] ?? null;

try {
    switch ($method) {
        case 'GET':
            if ($doctorId) {
                // Get single doctor
                // GET /doctors/{id}
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
                    WHERE d.id = ?
                ");
                $stmt->bind_param("s", $doctorId);
                $stmt->execute();
                $result = $stmt->get_result();
                
                if ($result->num_rows === 0) {
                    sendResponse(404, [
                        'success' => false,
                        'message' => 'Doctor not found'
                    ]);
                }
                
                $doctor = $result->fetch_assoc();
                sendResponse(200, [
                    'success' => true,
                    'doctor' => [
                        'id' => $doctor['id'],
                        'name' => $doctor['name'],
                        'specialty' => $doctor['specialty'] ?: 'General',
                        'experience' => $doctor['experience'] ?: '0 years',
                        'rating' => floatval($doctor['rating'] ?: 0),
                        'availability' => $doctor['availability'] ?: 'Available',
                        'location' => $doctor['location'] ?: 'Not specified'
                    ]
                ]);
            } else {
                // Get all doctors from Saveetha Hospital
                // GET /doctors
                // Filter by location = 'Saveetha Hospital' or show all if location is not set
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
                
                sendResponse(200, [
                    'success' => true,
                    'doctors' => $doctors,
                    'count' => count($doctors),
                    'hospital' => 'Saveetha Hospital'
                ]);
            }
            break;
            
        default:
            sendResponse(405, [
                'success' => false,
                'message' => 'Method not allowed'
            ]);
            break;
    }
} catch (Exception $e) {
    error_log("Doctors API Error: " . $e->getMessage());
    ob_end_clean();
    sendResponse(500, [
        'success' => false,
        'message' => 'Internal server error: ' . $e->getMessage()
    ]);
}

