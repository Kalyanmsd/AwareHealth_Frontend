<?php
/**
 * Test endpoint to verify disease lookup by ID
 * Use: http://172.20.10.2/AwareHealth/api/test_disease_by_id.php?id=DISEASE_ID
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

$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    echo json_encode(['error' => 'Database connection failed: ' . $conn->connect_error]);
    exit();
}

$conn->set_charset("utf8mb4");

$diseaseId = $_GET['id'] ?? null;

if (!$diseaseId) {
    // Return all disease IDs for reference
    $result = $conn->query("SELECT id, name, category FROM diseases ORDER BY name LIMIT 20");
    $diseases = [];
    while ($row = $result->fetch_assoc()) {
        $diseases[] = [
            'id' => $row['id'],
            'name' => $row['name'],
            'category' => $row['category']
        ];
    }
    echo json_encode([
        'message' => 'No ID provided. Here are available diseases:',
        'total' => count($diseases),
        'diseases' => $diseases,
        'usage' => 'Add ?id=DISEASE_ID to test a specific disease'
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    $conn->close();
    exit();
}

// Test the lookup
$diseaseId = trim($diseaseId);
$stmt = $conn->prepare("SELECT * FROM diseases WHERE id = ?");
$stmt->bind_param("s", $diseaseId);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    // Show available IDs
    $allResult = $conn->query("SELECT id, name FROM diseases ORDER BY name LIMIT 10");
    $available = [];
    while ($row = $allResult->fetch_assoc()) {
        $available[] = ['id' => $row['id'], 'name' => $row['name']];
    }
    
    echo json_encode([
        'error' => 'Disease not found',
        'searched_id' => $diseaseId,
        'id_length' => strlen($diseaseId),
        'available_ids_sample' => $available,
        'tip' => 'Check if the ID matches exactly (case-sensitive)'
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    $row = $result->fetch_assoc();
    echo json_encode([
        'success' => true,
        'disease' => [
            'id' => $row['id'],
            'name' => $row['name'],
            'category' => $row['category'],
            'has_symptoms' => !empty($row['symptoms']),
            'has_causes' => !empty($row['causes']),
            'has_prevention' => !empty($row['prevention']),
            'has_treatment' => !empty($row['treatment'])
        ]
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}

$stmt->close();
$conn->close();

