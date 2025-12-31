<?php
/**
 * Direct test endpoint for diseases
 * Use this to test: http://172.20.10.2/AwareHealth/api/test_diseases.php
 */

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

$conn = getDB();

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
    sendResponse(500, [
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
}

if ($types) {
    $stmt->bind_param($types, ...$params);
}

if (!$stmt->execute()) {
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

sendResponse(200, [
    'success' => true,
    'diseases' => $diseases,
    'count' => count($diseases),
    'message' => 'Direct test endpoint - ' . count($diseases) . ' diseases found'
]);

