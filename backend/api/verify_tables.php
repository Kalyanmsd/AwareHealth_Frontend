<?php
/**
 * Verify Tables - Check if appointment booking tables exist
 * GET: http://localhost/AwareHealth/api/verify_tables.php
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

require_once __DIR__ . '/../config.php';

$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error,
        'database' => DB_NAME
    ]);
    exit();
}

$conn->set_charset("utf8mb4");

$result = [
    'success' => true,
    'database' => DB_NAME,
    'tables' => [],
    'doctors_count' => 0,
    'appointments_count' => 0,
    'phpmyadmin_url' => 'http://localhost/phpmyadmin/index.php?route=/database/structure&db=' . DB_NAME
];

// Check if doctors table exists
$doctorsCheck = $conn->query("SHOW TABLES LIKE 'doctors'");
if ($doctorsCheck && $doctorsCheck->num_rows > 0) {
    $result['tables']['doctors'] = 'exists';
    
    // Count doctors
    $doctorsCount = $conn->query("SELECT COUNT(*) as count FROM doctors WHERE hospital = 'Saveetha Hospital'");
    if ($doctorsCount) {
        $row = $doctorsCount->fetch_assoc();
        $result['doctors_count'] = (int)$row['count'];
    }
} else {
    $result['tables']['doctors'] = 'not_found';
}

// Check if appointments table exists
$appointmentsCheck = $conn->query("SHOW TABLES LIKE 'appointments'");
if ($appointmentsCheck && $appointmentsCheck->num_rows > 0) {
    $result['tables']['appointments'] = 'exists';
    
    // Count appointments
    $appointmentsCount = $conn->query("SELECT COUNT(*) as count FROM appointments");
    if ($appointmentsCount) {
        $row = $appointmentsCount->fetch_assoc();
        $result['appointments_count'] = (int)$row['count'];
    }
} else {
    $result['tables']['appointments'] = 'not_found';
}

$conn->close();

// Determine overall status
$allTablesExist = isset($result['tables']['doctors']) && $result['tables']['doctors'] === 'exists' &&
                  isset($result['tables']['appointments']) && $result['tables']['appointments'] === 'exists';

if (!$allTablesExist) {
    $result['message'] = 'Some tables are missing. Run auto_setup_appointment_tables.php to create them.';
    $result['setup_url'] = 'http://localhost/AwareHealth/api/auto_setup_appointment_tables.php';
} else {
    $result['message'] = 'All tables exist and are ready!';
}

http_response_code(200);
echo json_encode($result, JSON_PRETTY_PRINT);

