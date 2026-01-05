<?php
/**
 * Test Disease Details Endpoint
 * Tests the complete disease details API with symptoms and prevention
 * Use: http://localhost/AwareHealth/api/test_disease_details.php?id=1
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

// Connect to database
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    echo json_encode([
        'success' => false,
        'error' => 'Database connection failed: ' . $conn->connect_error,
        'db_host' => DB_HOST,
        'db_name' => DB_NAME,
        'db_user' => DB_USER
    ], JSON_PRETTY_PRINT);
    exit();
}

$conn->set_charset("utf8mb4");

$diseaseId = $_GET['id'] ?? null;

if (!$diseaseId) {
    // Show first 5 diseases for testing
    $result = $conn->query("SELECT id, name FROM diseases LIMIT 5");
    $diseases = [];
    while ($row = $result->fetch_assoc()) {
        $diseases[] = ['id' => $row['id'], 'name' => $row['name']];
    }
    
    echo json_encode([
        'message' => 'No ID provided. Use ?id=DISEASE_ID to test',
        'sample_diseases' => $diseases,
        'example' => 'http://localhost/AwareHealth/api/test_disease_details.php?id=' . ($diseases[0]['id'] ?? '1')
    ], JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
    $conn->close();
    exit();
}

// Fetch disease
$diseaseId = trim($diseaseId);
$stmt = $conn->prepare("SELECT * FROM diseases WHERE id = ?");
$stmt->bind_param("s", $diseaseId);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    echo json_encode([
        'success' => false,
        'message' => 'Disease not found',
        'searched_id' => $diseaseId
    ], JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
    $stmt->close();
    $conn->close();
    exit();
}

$row = $result->fetch_assoc();

// Parse JSON arrays
$parseJson = function($jsonString) {
    if (empty($jsonString)) return [];
    $decoded = json_decode($jsonString, true);
    return is_array($decoded) ? $decoded : [];
};

$disease = [
    'id' => (string)$row['id'],
    'name' => $row['name'] ?? '',
    'category' => $row['category'] ?? '',
    'severity' => $row['severity'] ?? '',
    'emoji' => $row['emoji'] ?? '🦠',
    'description' => $row['description'] ?? '',
    'symptoms' => $parseJson($row['symptoms'] ?? '[]'),
    'causes' => $parseJson($row['causes'] ?? '[]'),
    'prevention' => $parseJson($row['prevention'] ?? '[]'),
    'treatment' => $parseJson($row['treatment'] ?? '[]'),
    'affectedPopulation' => $row['affected_population'] ?? '',
    'duration' => $row['duration'] ?? ''
];

// Show raw data for debugging
echo json_encode([
    'success' => true,
    'disease' => $disease,
    'debug' => [
        'raw_symptoms' => $row['symptoms'],
        'raw_prevention' => $row['prevention'],
        'symptoms_count' => count($disease['symptoms']),
        'prevention_count' => count($disease['prevention']),
        'causes_count' => count($disease['causes']),
        'treatment_count' => count($disease['treatment'])
    ]
], JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);

$stmt->close();
$conn->close();

?>

