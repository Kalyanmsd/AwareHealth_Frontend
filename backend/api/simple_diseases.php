<?php
/**
 * SIMPLE DISEASES ENDPOINT - Direct access without routing
 * Use this to test: http://172.20.10.2/AwareHealth/api/simple_diseases.php
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once __DIR__ . '/../config.php';

// Direct database connection
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed',
        'error' => $conn->connect_error
    ]);
    exit();
}

$conn->set_charset("utf8mb4");

// Helper function to safely parse JSON arrays
$parseJsonArray = function($jsonString) {
    if (empty($jsonString) || $jsonString === null) {
        return [];
    }
    $decoded = json_decode($jsonString, true);
    if (json_last_error() !== JSON_ERROR_NONE) {
        error_log("JSON decode error: " . json_last_error_msg() . " for string: " . substr($jsonString, 0, 100));
        // Try to parse as comma-separated string as fallback
        if (is_string($jsonString)) {
            return array_map('trim', explode(',', $jsonString));
        }
        return [];
    }
    return is_array($decoded) ? $decoded : [];
};

// Get query parameters
$diseaseId = $_GET['id'] ?? null;
$diseaseName = $_GET['name'] ?? null;
$category = $_GET['category'] ?? null;
$search = $_GET['search'] ?? null;

// Log for debugging
error_log("Simple Diseases API - Request: " . $_SERVER['REQUEST_URI']);
error_log("Simple Diseases API - Disease ID: " . ($diseaseId ?? 'null'));
error_log("Simple Diseases API - Disease Name: " . ($diseaseName ?? 'null'));

// If ID or Name is provided, return single disease
if ($diseaseId || $diseaseName) {
    $searchTerm = $diseaseId ?? $diseaseName;
    $searchTerm = trim($searchTerm);
    
    if (empty($searchTerm)) {
        http_response_code(400);
        echo json_encode([
            'success' => false,
            'message' => 'Disease ID or name is required',
            'debug' => 'Received empty or whitespace-only search term'
        ], JSON_UNESCAPED_UNICODE);
        $conn->close();
        exit();
    }
    
    // Build query - try ID first, then name (case-insensitive)
    if ($diseaseId) {
        // Search by ID (exact match)
        error_log("Simple Diseases API - Searching for disease ID: '$searchTerm'");
        $stmt = $conn->prepare("SELECT * FROM diseases WHERE id = ?");
        $stmt->bind_param("s", $searchTerm);
    } else {
        // Search by name (case-insensitive using LOWER or COLLATE)
        error_log("Simple Diseases API - Searching for disease name (case-insensitive): '$searchTerm'");
        $stmt = $conn->prepare("SELECT * FROM diseases WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))");
        $stmt->bind_param("s", $searchTerm);
    }
    
    if (!$stmt) {
        error_log("Simple Diseases API - Query preparation failed: " . $conn->error);
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Query preparation failed',
            'error' => $conn->error
        ], JSON_UNESCAPED_UNICODE);
        $conn->close();
        exit();
    }
    
    if (!$stmt->execute()) {
        error_log("Simple Diseases API - Query execution failed: " . $stmt->error);
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Query execution failed',
            'error' => $stmt->error
        ], JSON_UNESCAPED_UNICODE);
        $stmt->close();
        $conn->close();
        exit();
    }
    
    $result = $stmt->get_result();
    
    if ($result->num_rows === 0) {
        // If searching by ID and not found, try searching by name as fallback
        if ($diseaseId && !$diseaseName) {
            error_log("Simple Diseases API - ID not found, trying name search as fallback: '$searchTerm'");
            $stmt->close();
            $stmt = $conn->prepare("SELECT * FROM diseases WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))");
            $stmt->bind_param("s", $searchTerm);
            
            if ($stmt->execute()) {
                $result = $stmt->get_result();
                if ($result->num_rows > 0) {
                    error_log("Simple Diseases API - Found disease by name (fallback): '$searchTerm'");
                }
            }
        }
        
        if ($result->num_rows === 0) {
            // Debug: Check if any diseases exist
            $countStmt = $conn->query("SELECT COUNT(*) as count FROM diseases");
            $countRow = $countStmt->fetch_assoc();
            $totalDiseases = $countRow['count'] ?? 0;
            
            // Get first few IDs and names for debugging
            $sampleStmt = $conn->query("SELECT id, name FROM diseases LIMIT 5");
            $sampleIds = [];
            while ($row = $sampleStmt->fetch_assoc()) {
                $sampleIds[] = ['id' => $row['id'], 'name' => $row['name']];
            }
            
            error_log("Simple Diseases API - Disease not found. Searched: '$searchTerm'. Total diseases in DB: $totalDiseases");
            
            http_response_code(404);
            echo json_encode([
                'success' => false,
                'message' => 'Disease not found',
                'debug' => [
                    'searched_term' => $searchTerm,
                    'search_type' => $diseaseId ? 'id' : 'name',
                    'total_diseases' => $totalDiseases,
                    'sample_diseases' => $sampleIds
                ]
            ], JSON_UNESCAPED_UNICODE);
            $stmt->close();
            $conn->close();
            exit();
        }
    }
    
    $row = $result->fetch_assoc();
    
    // Ensure all required fields are present
    $disease = [
        'id' => (string)$row['id'], // Ensure ID is string for consistency
        'name' => $row['name'] ?? '',
        'category' => $row['category'] ?? 'General',
        'severity' => $row['severity'] ?? 'Unknown',
        'emoji' => $row['emoji'] ?? '🦠',
        'description' => $row['description'] ?? '', // Required field
        'symptoms' => $parseJsonArray($row['symptoms'] ?? '[]'), // Required field
        'causes' => $parseJsonArray($row['causes'] ?? '[]'),
        'prevention' => $parseJsonArray($row['prevention'] ?? '[]'), // Required field
        'treatment' => $parseJsonArray($row['treatment'] ?? '[]'),
        'affectedPopulation' => $row['affected_population'] ?? 'Unknown',
        'duration' => $row['duration'] ?? 'Unknown'
    ];
    
    error_log("Simple Diseases API - Found disease: " . $disease['name'] . 
              " | Description: " . (empty($disease['description']) ? 'EMPTY' : substr($disease['description'], 0, 50)) .
              " | Symptoms: " . count($disease['symptoms']) . 
              " | Prevention: " . count($disease['prevention']) .
              " | Treatment: " . count($disease['treatment']));
    
    $stmt->close();
    $conn->close();
    
    http_response_code(200);
    echo json_encode([
        'success' => true,
        'disease' => $disease
    ], JSON_UNESCAPED_UNICODE);
    exit();
}

// Otherwise, return list of diseases
$query = "SELECT * FROM diseases WHERE 1=1";
$params = [];
$types = "";

if ($category && $category !== 'All') {
    $query .= " AND category = ?";
    $params[] = $category;
    $types .= "s";
}

if ($search) {
    $query .= " AND (name LIKE ? OR description LIKE ?)";
    $searchTerm = "%$search%";
    $params[] = $searchTerm;
    $params[] = $searchTerm;
    $types .= "ss";
}

$query .= " ORDER BY name ASC";

$stmt = $conn->prepare($query);

if (!$stmt) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Query preparation failed',
        'error' => $conn->error
    ]);
    $conn->close();
    exit();
}

if ($types) {
    $stmt->bind_param($types, ...$params);
}

if (!$stmt->execute()) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Query execution failed',
        'error' => $stmt->error
    ]);
    $stmt->close();
    $conn->close();
    exit();
}

$result = $stmt->get_result();

$diseases = [];
while ($row = $result->fetch_assoc()) {
    $diseases[] = [
        'id' => (string)$row['id'], // Ensure ID is string for consistency
        'name' => $row['name'] ?? '',
        'category' => $row['category'] ?? 'General',
        'severity' => $row['severity'] ?? 'Unknown',
        'emoji' => $row['emoji'] ?? '🦠',
        'description' => $row['description'] ?? '',
        'symptoms' => $parseJsonArray($row['symptoms'] ?? '[]'),
        'causes' => $parseJsonArray($row['causes'] ?? '[]'),
        'prevention' => $parseJsonArray($row['prevention'] ?? '[]'),
        'treatment' => $parseJsonArray($row['treatment'] ?? '[]'),
        'affectedPopulation' => $row['affected_population'] ?? 'Unknown',
        'duration' => $row['duration'] ?? 'Unknown'
    ];
}

$stmt->close();
$conn->close();

http_response_code(200);
echo json_encode([
    'success' => true,
    'diseases' => $diseases,
    'count' => count($diseases)
], JSON_UNESCAPED_UNICODE);

