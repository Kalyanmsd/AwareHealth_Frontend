<?php
/**
 * VERIFY DISEASE INTEGRATION
 * This endpoint helps verify that the backend-frontend integration is working
 * Access: http://localhost/AwareHealth/api/verify_disease_integration.php
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

$result = [
    'timestamp' => date('Y-m-d H:i:s'),
    'status' => 'checking',
    'checks' => []
];

// Check 1: Database connection
try {
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    if ($conn->connect_error) {
        $result['checks']['database'] = [
            'status' => 'FAILED',
            'error' => $conn->connect_error
        ];
    } else {
        $result['checks']['database'] = ['status' => 'OK'];
        $conn->set_charset("utf8mb4");
        
        // Check 2: Diseases table exists
        $tableCheck = $conn->query("SHOW TABLES LIKE 'diseases'");
        if ($tableCheck && $tableCheck->num_rows > 0) {
            $result['checks']['table_exists'] = ['status' => 'OK'];
            
            // Check 3: Count diseases
            $countResult = $conn->query("SELECT COUNT(*) as count FROM diseases");
            $countRow = $countResult->fetch_assoc();
            $diseaseCount = $countRow['count'];
            $result['checks']['disease_count'] = [
                'status' => 'OK',
                'count' => $diseaseCount
            ];
            
            // Check 4: Get sample disease
            $sampleResult = $conn->query("SELECT id, name, 
                LENGTH(symptoms) as symptoms_length,
                LENGTH(prevention) as prevention_length,
                LENGTH(causes) as causes_length,
                LENGTH(treatment) as treatment_length
                FROM diseases LIMIT 1");
            
            if ($sampleResult && $sampleResult->num_rows > 0) {
                $sample = $sampleResult->fetch_assoc();
                $result['checks']['sample_disease'] = [
                    'status' => 'OK',
                    'id' => $sample['id'],
                    'name' => $sample['name'],
                    'has_symptoms' => $sample['symptoms_length'] > 0,
                    'has_prevention' => $sample['prevention_length'] > 0,
                    'has_causes' => $sample['causes_length'] > 0,
                    'has_treatment' => $sample['treatment_length'] > 0
                ];
                
                // Check 5: Test API endpoint
                $testId = $sample['id'];
                $testStmt = $conn->prepare("SELECT * FROM diseases WHERE id = ?");
                $testStmt->bind_param("s", $testId);
                $testStmt->execute();
                $testResult = $testStmt->get_result();
                
                if ($testResult->num_rows > 0) {
                    $testRow = $testResult->fetch_assoc();
                    $parseJson = function($jsonString) {
                        if (empty($jsonString)) return [];
                        $decoded = json_decode($jsonString, true);
                        return is_array($decoded) ? $decoded : [];
                    };
                    
                    $symptoms = $parseJson($testRow['symptoms'] ?? '[]');
                    $prevention = $parseJson($testRow['prevention'] ?? '[]');
                    
                    $result['checks']['api_test'] = [
                        'status' => 'OK',
                        'test_id' => $testId,
                        'symptoms_count' => count($symptoms),
                        'prevention_count' => count($prevention),
                        'sample_symptoms' => array_slice($symptoms, 0, 3),
                        'sample_prevention' => array_slice($prevention, 0, 3)
                    ];
                    
                    $result['status'] = 'SUCCESS';
                    $result['message'] = 'Integration check passed!';
                    $result['test_url'] = "http://localhost/AwareHealth/api/simple_diseases.php?id=$testId";
                } else {
                    $result['checks']['api_test'] = ['status' => 'FAILED', 'error' => 'Could not fetch test disease'];
                }
                $testStmt->close();
            } else {
                $result['checks']['sample_disease'] = ['status' => 'FAILED', 'error' => 'No diseases found in table'];
            }
        } else {
            $result['checks']['table_exists'] = ['status' => 'FAILED', 'error' => 'Diseases table does not exist'];
        }
        $conn->close();
    }
} catch (Exception $e) {
    $result['checks']['database'] = [
        'status' => 'FAILED',
        'error' => $e->getMessage()
    ];
}

echo json_encode($result, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);

?>

