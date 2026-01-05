<?php
/**
 * Get Doctors API - Using SQL JOIN to fetch names from users table
 * Fetches doctors with status = 'Available' from database
 * Doctor names are fetched from users table via user_id
 * 
 * Endpoint: GET /api/get_doctors.php
 * 
 * Response Format:
 * {
 *   "success": true,
 *   "doctors": [
 *     {
 *       "id": "1",
 *       "name": "Dr. Name",
 *       "specialty": "General Physician",
 *       "experience": "5 years",
 *       "rating": 4.5,
 *       "availability": "Available",
 *       "location": "Saveetha Hospital",
 *       "status": "Available"
 *     }
 *   ]
 * }
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
    
    // Check if users table exists
    $usersTableCheck = $conn->query("SHOW TABLES LIKE 'users'");
    if (!$usersTableCheck || $usersTableCheck->num_rows === 0) {
        throw new Exception('Users table does not exist in database.');
    }
    
    // Check doctors table structure
    $doctorsColumns = $conn->query("SHOW COLUMNS FROM doctors");
    $doctorsCols = [];
    while ($col = $doctorsColumns->fetch_assoc()) {
        $doctorsCols[] = $col['Field'];
    }
    
    // Check what columns exist in doctors table
    $hasUserId = in_array('user_id', $doctorsCols);
    $hasStatus = in_array('status', $doctorsCols);
    $hasSpecialization = in_array('specialization', $doctorsCols);
    $hasSpecialty = in_array('specialty', $doctorsCols);
    $hasHospital = in_array('hospital', $doctorsCols);
    $hasLocation = in_array('location', $doctorsCols);
    $hasExperience = in_array('experience', $doctorsCols);
    $hasRating = in_array('rating', $doctorsCols);
    $hasAvailability = in_array('availability', $doctorsCols);
    
    // Build SQL query with JOIN to users table
    // Determine specialty column name - prioritize 'specialty' if it exists
    $specialtyColumn = $hasSpecialty ? 'd.specialty' : ($hasSpecialization ? 'd.specialization' : "'General Physician'");
    
    // Determine location column name - prioritize 'location' if it exists
    $locationColumn = $hasLocation ? 'd.location' : ($hasHospital ? 'd.hospital' : "'Saveetha Hospital'");
    
    // Determine availability column
    $availabilityColumn = $hasAvailability ? 'd.availability' : ($hasStatus ? 'd.status' : "'Available'");
    
    // Determine status column
    $statusColumn = $hasStatus ? 'd.status' : "'Available'";
    
    if ($hasUserId) {
        // Use JOIN to get name from users table
        $selectFields = [
            'd.id',
            'u.name', // Get name from users table
            "$specialtyColumn as specialty",
            "$locationColumn as location"
        ];
        
        // Add optional columns if they exist
        if ($hasExperience) {
            $selectFields[] = 'd.experience';
        } else {
            $selectFields[] = 'NULL as experience';
        }
        
        if ($hasRating) {
            $selectFields[] = 'd.rating';
        } else {
            $selectFields[] = 'NULL as rating';
        }
        
        $selectFields[] = "$availabilityColumn as availability";
        $selectFields[] = "$statusColumn as status";
        
        $selectClause = implode(', ', $selectFields);
        
        // Build WHERE clause - filter by status='Available' if status column exists
        $whereClause = '';
        if ($hasStatus) {
            $whereClause = "WHERE d.status = 'Available'";
        }
        
        // Build complete SQL query with JOIN
        $sql = "SELECT $selectClause 
                FROM doctors d
                LEFT JOIN users u ON d.user_id = u.id
                $whereClause
                ORDER BY u.name ASC";
        
    } else {
        // Fallback: doctors table doesn't have user_id, try to get name from doctors table directly
        $hasName = in_array('name', $doctorsCols);
        $nameColumn = $hasName ? 'd.name' : "'Dr. Doctor'";
        
        $selectFields = [
            'd.id',
            "$nameColumn as name",
            "$specialtyColumn as specialty",
            "$locationColumn as location"
        ];
        
        // Add optional columns if they exist
        if ($hasExperience) {
            $selectFields[] = 'd.experience';
        } else {
            $selectFields[] = 'NULL as experience';
        }
        
        if ($hasRating) {
            $selectFields[] = 'd.rating';
        } else {
            $selectFields[] = 'NULL as rating';
        }
        
        $selectFields[] = "$availabilityColumn as availability";
        $selectFields[] = "$statusColumn as status";
        
        $selectClause = implode(', ', $selectFields);
        
        $whereClause = '';
        if ($hasStatus) {
            $whereClause = "WHERE d.status = 'Available'";
        }
        
        $sql = "SELECT $selectClause 
                FROM doctors d
                $whereClause
                ORDER BY $nameColumn ASC";
    }
    
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
            'name' => $doctorName, // From users table via JOIN
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
