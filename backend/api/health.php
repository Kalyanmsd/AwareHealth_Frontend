<?php
/**
 * Health Information API Endpoints
 * Handles diseases, symptoms, prevention tips, health articles
 */

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

// Set CORS headers
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle preflight requests
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

$segments = getPathSegments();
$method = $_SERVER['REQUEST_METHOD'];

// Get database connection with error handling
try {
    $conn = getDB();
    if ($conn->connect_error) {
        error_log("Health API - Database connection error: " . $conn->connect_error);
        sendResponse(500, [
            'success' => false,
            'message' => 'Database connection failed',
            'error' => $conn->connect_error
        ]);
    }
} catch (Exception $e) {
    error_log("Health API - Database exception: " . $e->getMessage());
    sendResponse(500, [
        'success' => false,
        'message' => 'Database error: ' . $e->getMessage()
    ]);
}

// Only get JSON input for POST/PUT requests
$input = ($method === 'POST' || $method === 'PUT') ? getJsonInput() : [];

// Debug logging
error_log("Health API - Path segments: " . json_encode($segments));
error_log("Health API - Method: " . $method);
error_log("Health API - Request URI: " . $_SERVER['REQUEST_URI']);
error_log("Health API - Query string: " . ($_SERVER['QUERY_STRING'] ?? 'none'));

// Parse endpoint from segments
// URL: /AwareHealth/api/health/diseases -> segments: ['health', 'diseases']
// URL: /AwareHealth/api/diseases -> segments: ['diseases']
$endpoint = 'diseases'; // Default to diseases

if (!empty($segments)) {
    // If first segment is 'health', use the second segment as endpoint
    if ($segments[0] === 'health' && isset($segments[1])) {
        $endpoint = $segments[1];
    } 
    // If first segment is 'diseases', use it
    elseif ($segments[0] === 'diseases') {
        $endpoint = 'diseases';
    }
    // If we have multiple segments and first is not 'health', try second
    elseif (count($segments) > 1 && isset($segments[1])) {
        $endpoint = $segments[1];
    }
    // Otherwise use first segment if it's not 'health'
    elseif ($segments[0] !== 'health' && !empty($segments[0])) {
        $endpoint = $segments[0];
    }
}

// If endpoint is still empty or unknown, default to diseases
if (empty($endpoint) || ($endpoint !== 'diseases' && $endpoint !== 'symptoms' && $endpoint !== 'prevention-tips' && $endpoint !== 'health-articles')) {
    error_log("Health API - Endpoint empty or unknown, defaulting to diseases. Original: '" . $endpoint . "'");
    $endpoint = 'diseases';
}

error_log("Health API - Segments: " . json_encode($segments));
error_log("Health API - Resolved endpoint: '" . $endpoint . "'");
error_log("Health API - Full request: " . $_SERVER['REQUEST_URI']);

switch ($method) {
    case 'GET':
        
        switch ($endpoint) {
            case 'diseases':
            diseases_case:
                // Check if requesting single disease or list
                $diseaseId = $segments[2] ?? null;
                
                if ($diseaseId) {
                    // Get single disease by ID
                    $stmt = $conn->prepare("SELECT * FROM diseases WHERE id = ?");
                    $stmt->bind_param("s", $diseaseId);
                    $stmt->execute();
                    $result = $stmt->get_result();
                    
                    if ($result->num_rows === 0) {
                        sendResponse(404, ['success' => false, 'message' => 'Disease not found']);
                    }
                    
                    $row = $result->fetch_assoc();
                    $disease = [
                        'id' => $row['id'],
                        'name' => $row['name'],
                        'category' => $row['category'],
                        'severity' => $row['severity'],
                        'emoji' => $row['emoji'],
                        'description' => $row['description'],
                        'symptoms' => json_decode($row['symptoms'], true) ?: [],
                        'causes' => json_decode($row['causes'], true) ?: [],
                        'prevention' => json_decode($row['prevention'], true) ?: [],
                        'treatment' => json_decode($row['treatment'], true) ?: [],
                        'affectedPopulation' => $row['affected_population'],
                        'duration' => $row['duration']
                    ];
                    
                    sendResponse(200, [
                        'success' => true,
                        'disease' => $disease
                    ]);
                } else {
                    // Get all diseases or filter by category
                    $category = $_GET['category'] ?? null;
                    $search = $_GET['search'] ?? null;
                    
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
                    
                    error_log("Health API - Query: " . $query);
                    
                    $stmt = $conn->prepare($query);
                    if (!$stmt) {
                        error_log("Health API - Prepare failed: " . $conn->error);
                        sendResponse(500, [
                            'success' => false,
                            'message' => 'Database query failed: ' . $conn->error
                        ]);
                    }
                    
                    if ($types) {
                        $stmt->bind_param($types, ...$params);
                    }
                    
                    if (!$stmt->execute()) {
                        error_log("Health API - Execute failed: " . $stmt->error);
                        sendResponse(500, [
                            'success' => false,
                            'message' => 'Database query execution failed: ' . $stmt->error
                        ]);
                    }
                    
                    $result = $stmt->get_result();
                    
                    $diseases = [];
                    while ($row = $result->fetch_assoc()) {
                        $diseases[] = [
                            'id' => $row['id'],
                            'name' => $row['name'],
                            'category' => $row['category'],
                            'severity' => $row['severity'],
                            'emoji' => $row['emoji'],
                            'description' => $row['description'],
                            'symptoms' => json_decode($row['symptoms'], true) ?: [],
                            'causes' => json_decode($row['causes'], true) ?: [],
                            'prevention' => json_decode($row['prevention'], true) ?: [],
                            'treatment' => json_decode($row['treatment'], true) ?: [],
                            'affectedPopulation' => $row['affected_population'],
                            'duration' => $row['duration']
                        ];
                    }
                    
                    error_log("Health API - Found " . count($diseases) . " diseases");
                    
                    // Check if database connection is still valid
                    if ($conn->connect_error) {
                        error_log("Health API - Database connection error: " . $conn->connect_error);
                        sendResponse(500, [
                            'success' => false,
                            'message' => 'Database connection error',
                            'error' => $conn->connect_error
                        ]);
                    }
                    
                    sendResponse(200, [
                        'success' => true,
                        'diseases' => $diseases,
                        'count' => count($diseases)
                    ]);
                    $stmt->close();
                }
                break;
                
            case 'symptoms':
                // Get all symptoms or by ID
                $symptomId = $segments[2] ?? null;
                
                if ($symptomId) {
                    $stmt = $conn->prepare("SELECT * FROM symptoms WHERE id = ?");
                    $stmt->bind_param("s", $symptomId);
                    $stmt->execute();
                    $result = $stmt->get_result();
                    
                    if ($result->num_rows === 0) {
                        sendResponse(404, ['success' => false, 'message' => 'Symptom not found']);
                    }
                    
                    $row = $result->fetch_assoc();
                    $symptom = [
                        'id' => $row['id'],
                        'name' => $row['name'],
                        'emoji' => $row['emoji'],
                        'definition' => $row['definition'],
                        'normalRange' => $row['normal_range'],
                        'feverRange' => $row['fever_range'],
                        'severity' => json_decode($row['severity_info'], true) ?: [],
                        'possibleCauses' => json_decode($row['possible_causes'], true) ?: [],
                        'whatToDo' => json_decode($row['what_to_do'], true) ?: [],
                        'whenToSeekHelp' => json_decode($row['when_to_seek_help'], true) ?: [],
                        'associatedSymptoms' => json_decode($row['associated_symptoms'], true) ?: []
                    ];
                    
                    sendResponse(200, [
                        'success' => true,
                        'symptom' => $symptom
                    ]);
                } else {
                    $stmt = $conn->prepare("SELECT * FROM symptoms ORDER BY name ASC");
                    $stmt->execute();
                    $result = $stmt->get_result();
                    
                    $symptoms = [];
                    while ($row = $result->fetch_assoc()) {
                        $symptoms[] = [
                            'id' => $row['id'],
                            'name' => $row['name'],
                            'emoji' => $row['emoji'],
                            'definition' => $row['definition']
                        ];
                    }
                    
                    sendResponse(200, [
                        'success' => true,
                        'symptoms' => $symptoms,
                        'count' => count($symptoms)
                    ]);
                }
                break;
                
            case 'prevention-tips':
                // Get prevention tips for a disease
                $diseaseId = $_GET['disease_id'] ?? null;
                $category = $_GET['category'] ?? null;
                
                $query = "SELECT * FROM prevention_tips WHERE 1=1";
                $params = [];
                $types = "";
                
                if ($diseaseId) {
                    $query .= " AND disease_id = ?";
                    $params[] = $diseaseId;
                    $types .= "s";
                }
                
                if ($category && $category !== 'All') {
                    $query .= " AND category = ?";
                    $params[] = $category;
                    $types .= "s";
                }
                
                $query .= " ORDER BY priority DESC, title ASC";
                
                $stmt = $conn->prepare($query);
                if ($types) {
                    $stmt->bind_param($types, ...$params);
                }
                $stmt->execute();
                $result = $stmt->get_result();
                
                $tips = [];
                while ($row = $result->fetch_assoc()) {
                    $tips[] = [
                        'id' => $row['id'],
                        'diseaseId' => $row['disease_id'],
                        'category' => $row['category'],
                        'title' => $row['title'],
                        'description' => $row['description'],
                        'priority' => $row['priority']
                    ];
                }
                
                sendResponse(200, [
                    'success' => true,
                    'tips' => $tips,
                    'count' => count($tips)
                ]);
                break;
                
            case 'health-articles':
                // Get health articles
                $isFeatured = $_GET['featured'] ?? null;
                
                $query = "SELECT * FROM health_articles WHERE 1=1";
                $params = [];
                $types = "";
                
                if ($isFeatured === 'true') {
                    $query .= " AND is_featured = 1";
                }
                
                $query .= " ORDER BY published_at DESC";
                
                $stmt = $conn->prepare($query);
                if ($types) {
                    $stmt->bind_param($types, ...$params);
                }
                $stmt->execute();
                $result = $stmt->get_result();
                
                $articles = [];
                while ($row = $result->fetch_assoc()) {
                    $articles[] = [
                        'id' => $row['id'],
                        'title' => $row['title'],
                        'category' => $row['category'],
                        'summary' => $row['summary'],
                        'content' => $row['content'],
                        'imageUrl' => $row['image_url'],
                        'author' => $row['author'],
                        'publishedAt' => $row['published_at'],
                        'isFeatured' => (bool)$row['is_featured']
                    ];
                }
                
                sendResponse(200, [
                    'success' => true,
                    'articles' => $articles,
                    'count' => count($articles)
                ]);
                break;
                
            default:
                // Always default to diseases list if endpoint is unknown
                error_log("Health API - Unknown endpoint: '" . $endpoint . "', defaulting to diseases. Segments: " . json_encode($segments));
                // Fall through to diseases case
                goto diseases_case;
        }
        break;
        
    default:
        sendResponse(405, ['success' => false, 'message' => 'Method not allowed']);
        break;
}

