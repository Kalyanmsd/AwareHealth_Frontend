<?php
/**
 * Debug endpoint for health API
 * Tests database connection, table existence, and data retrieval
 * URL: http://172.20.10.2/AwareHealth/api/debug_health.php
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle preflight requests
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';

$debug = [
    'timestamp' => date('Y-m-d H:i:s'),
    'config' => [],
    'database' => [],
    'tables' => [],
    'data' => []
];

// Test 1: Check config
$debug['config'] = [
    'db_host' => DB_HOST,
    'db_name' => DB_NAME,
    'db_user' => DB_USER,
    'db_pass_set' => !empty(DB_PASS)
];

// Test 2: Check database connection
try {
    $conn = getDB();
    if ($conn && $conn->connect_error) {
        $debug['database']['connection'] = 'FAILED';
        $debug['database']['error'] = $conn->connect_error;
    } else {
        $debug['database']['connection'] = 'SUCCESS';
        $debug['database']['server_info'] = $conn->server_info;
    }
} catch (Exception $e) {
    $debug['database']['connection'] = 'FAILED';
    $debug['database']['error'] = $e->getMessage();
}

// Test 3: Check if diseases table exists
if (isset($conn) && !$conn->connect_error) {
    $result = $conn->query("SHOW TABLES LIKE 'diseases'");
    if ($result && $result->num_rows > 0) {
        $debug['tables']['diseases'] = 'EXISTS';
        
        // Test 4: Count rows
        $countResult = $conn->query("SELECT COUNT(*) as count FROM diseases");
        if ($countResult) {
            $countRow = $countResult->fetch_assoc();
            $debug['tables']['diseases_count'] = $countRow['count'];
        }
        
        // Test 5: Get sample data
        $sampleResult = $conn->query("SELECT id, name, category, emoji FROM diseases LIMIT 5");
        if ($sampleResult) {
            $samples = [];
            while ($row = $sampleResult->fetch_assoc()) {
                $samples[] = $row;
            }
            $debug['data']['sample_diseases'] = $samples;
        }
        
        // Test 6: Test full query
        $fullResult = $conn->query("SELECT * FROM diseases ORDER BY name ASC LIMIT 3");
        if ($fullResult) {
            $fullDiseases = [];
            while ($row = $fullResult->fetch_assoc()) {
                $fullDiseases[] = [
                    'id' => $row['id'],
                    'name' => $row['name'],
                    'category' => $row['category'],
                    'severity' => $row['severity'],
                    'emoji' => $row['emoji'],
                    'description' => substr($row['description'], 0, 50) . '...',
                    'symptoms' => json_decode($row['symptoms'], true) ?: [],
                    'causes' => json_decode($row['causes'], true) ?: [],
                    'prevention' => json_decode($row['prevention'], true) ?: [],
                    'treatment' => json_decode($row['treatment'], true) ?: [],
                    'affectedPopulation' => $row['affected_population'],
                    'duration' => $row['duration']
                ];
            }
            $debug['data']['full_format'] = $fullDiseases;
        }
    } else {
        $debug['tables']['diseases'] = 'NOT FOUND';
    }
    
    // List all tables
    $tablesResult = $conn->query("SHOW TABLES");
    if ($tablesResult) {
        $allTables = [];
        while ($row = $tablesResult->fetch_array()) {
            $allTables[] = $row[0];
        }
        $debug['tables']['all_tables'] = $allTables;
    }
}

// Test 7: Test API endpoint format
$debug['api_format'] = [
    'expected_response' => [
        'success' => true,
        'diseases' => 'array of disease objects',
        'count' => 'number'
    ],
    'sample_disease_object' => [
        'id' => 'string',
        'name' => 'string',
        'category' => 'string',
        'severity' => 'string',
        'emoji' => 'string',
        'description' => 'string',
        'symptoms' => 'array',
        'causes' => 'array',
        'prevention' => 'array',
        'treatment' => 'array',
        'affectedPopulation' => 'string',
        'duration' => 'string'
    ]
];

http_response_code(200);
echo json_encode($debug, JSON_PRETTY_PRINT);

