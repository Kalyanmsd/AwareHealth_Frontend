<?php
/**
 * Get Doctors API - Final Fixed Version
 * Fetches doctors with status = 'Available' from database
 * Ensures doctor names are properly returned
 * 
 * Endpoint: GET /api/get_doctors.php
 */

// Set headers for JSON response and CORS
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle preflight OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Only allow GET requests
if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use GET.'
    ], JSON_UNESCAPED_UNICODE);
    exit();
}

// Database credentials
$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

try {
    // Connect to MySQL database
    $conn = new mysqli($dbHost, $dbUser, $dbPass, $dbName);
    
    // Check connection
    if ($conn->connect_error) {
        throw new Exception('Database connection failed: ' . $conn->connect_error);
    }
    
    // Set charset to UTF-8
    $conn->set_charset("utf8mb4");
    
    // Check if doctors table exists
    $tableCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
    if (!$tableCheck || $tableCheck->num_rows === 0) {
        throw new Exception('Doctors table does not exist in database.');
    }
    
    // Get table structure to determine column names
    $columnsResult = $conn->query("SHOW COLUMNS FROM doctors");
    $columns = [];
    while ($col = $columnsResult->fetch_assoc()) {
        $columns[] = $col['Field'];
    }
    
    // Check what columns exist
    $hasStatus = in_array('status', $columns);
    $hasName = in_array('name', $columns);
    $hasSpecialization = in_array('specialization', $columns);
    $hasSpecialty = in_array('specialty', $columns);
    $hasHospital = in_array('hospital', $columns);
    $hasLocation = in_array('location', $columns);
    $hasExperience = in_array('experience', $columns);
    $hasRating = in_array('rating', $columns);
    $hasAvailability = in_array('availability', $columns);
    
    // Build SELECT clause - ALWAYS include name first
    $selectFields = [];
    
    // ID and NAME - REQUIRED
    $selectFields[] = 'id';
    if ($hasName) {
        $selectFields[] = 'name';
    } else {
        // If name doesn't exist, create a default name from specialty
        $selectFields[] = "COALESCE(specialization, specialty, 'Dr. Name') as name";
    }
    
    // Specialty/Specialization
    if ($hasSpecialization) {
        $selectFields[] = 'specialization as specialty';
    } elseif ($hasSpecialty) {
        $selectFields[] = 'specialty';
    } else {
        $selectFields[] = "'General Physician' as specialty";
    }
    
    // Location/Hospital
    if ($hasLocation) {
        $selectFields[] = 'location';
    } elseif ($hasHospital) {
        $selectFields[] = 'hospital as location';
    } else {
        $selectFields[] = "'Saveetha Hospital' as location";
    }
    
    // Status and Availability
    if ($hasStatus) {
        $selectFields[] = 'status';
        $selectFields[] = 'status as availability';
    } else {
        $selectFields[] = "'Available' as status";
        $selectFields[] = "'Available' as availability";
    }
    
    // Experience
    if ($hasExperience) {
        $selectFields[] = 'experience';
    } else {
        $selectFields[] = 'NULL as experience';
    }
    
    // Rating
    if ($hasRating) {
        $selectFields[] = 'rating';
    } else {
        $selectFields[] = 'NULL as rating';
    }
    
    $selectClause = implode(', ', $selectFields);
    
    // Build WHERE clause - filter by status='Available' if status column exists
    $whereClause = '';
    if ($hasStatus) {
        $whereClause = "WHERE status = 'Available'";
    }
    
    // Build complete SQL query
    $sql = "SELECT $selectClause FROM doctors $whereClause ORDER BY name ASC";
    
    // Execute query
    $result = $conn->query($sql);
    
    if (!$result) {
        throw new Exception('Database query failed: ' . $conn->error);
    }
    
    // Fetch all doctors and build response array
    $doctors = [];
    while ($row = $result->fetch_assoc()) {
        // Ensure name is always present
        $doctorName = $row['name'] ?? 'Dr. ' . ($row['specialty'] ?? 'Doctor');
        
        $doctor = [
            'id' => (string)$row['id'], // Frontend expects String
            'name' => $doctorName, // ALWAYS include name
            'specialty' => $row['specialty'] ?? null,
            'location' => $row['location'] ?? null,
            'status' => $row['status'] ?? null,
            'availability' => $row['availability'] ?? ($row['status'] ?? null),
            'experience' => isset($row['experience']) && $row['experience'] !== null ? (string)$row['experience'] : null,
            'rating' => isset($row['rating']) && $row['rating'] !== null ? (float)$row['rating'] : null
        ];
        
        $doctors[] = $doctor;
    }
    
    // Close database connection
    $conn->close();
    
    // Handle empty results
    if (empty($doctors)) {
        http_response_code(200);
        echo json_encode([
            'success' => false,
            'message' => 'No doctors available',
            'doctors' => null
        ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
        exit();
    }
    
    // Return successful response
    http_response_code(200);
    echo json_encode([
        'success' => true,
        'doctors' => $doctors
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    
} catch (Exception $e) {
    // Close connection if still open
    if (isset($conn) && !$conn->connect_error) {
        $conn->close();
    }
    
    // Return error response
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => $e->getMessage(),
        'doctors' => null
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    exit();
}

?>

