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

// Get query parameters
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

$stmt->close();
$conn->close();

http_response_code(200);
echo json_encode([
    'success' => true,
    'diseases' => $diseases,
    'count' => count($diseases)
], JSON_UNESCAPED_UNICODE);

