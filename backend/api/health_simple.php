<?php
/**
 * Simplified Health API - Direct diseases endpoint
 * Use this if routing is still not working
 */

header('Content-Type: application/json');

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

$method = $_SERVER['REQUEST_METHOD'];
$conn = getDB();

if ($method !== 'GET') {
    sendResponse(405, ['success' => false, 'message' => 'Method not allowed']);
}

// Get all diseases or filter by category
$category = $_GET['category'] ?? null;
$search = $_GET['search'] ?? null;

error_log("Health Simple API - Category: " . ($category ?? 'null') . ", Search: " . ($search ?? 'null'));

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

error_log("Health Simple API - Query: " . $query);

$stmt = $conn->prepare($query);
if (!$stmt) {
    error_log("Health Simple API - Prepare failed: " . $conn->error);
    sendResponse(500, [
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
}

if ($types) {
    $stmt->bind_param($types, ...$params);
}

if (!$stmt->execute()) {
    error_log("Health Simple API - Execute failed: " . $stmt->error);
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

error_log("Health Simple API - Found " . count($diseases) . " diseases");

sendResponse(200, [
    'success' => true,
    'diseases' => $diseases,
    'count' => count($diseases)
]);
$stmt->close();
?>

