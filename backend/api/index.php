<?php
/**
 * API Router - Routes requests to appropriate endpoints
 * 
 * This file handles routing for all API endpoints
 * URL: /AwareHealth/api/auth/forgot-password
 */

// Get the request path
$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$basePath = '/AwareHealth/api';

// Remove base path
if (strpos($path, $basePath) === 0) {
    $path = substr($path, strlen($basePath));
}

$path = trim($path, '/');
$segments = $path ? explode('/', $path) : [];

// Route to appropriate endpoint file
if (count($segments) > 0) {
    $endpoint = $segments[0]; // 'auth', 'chatbot', etc.
    
        switch ($endpoint) {
        case 'auth':
            require_once __DIR__ . '/auth.php';
            break;
            
        case 'health':
            require_once __DIR__ . '/health.php';
            break;
            
        case 'diseases':
            require_once __DIR__ . '/health.php';
            break;
            
        case 'health_simple':
            require_once __DIR__ . '/health_simple.php';
            break;
            
        case 'test_health':
            require_once __DIR__ . '/test_health.php';
            break;
            
        case 'test_connection':
            require_once __DIR__ . '/test_connection.php';
            break;
            
        case 'test_diseases':
            require_once __DIR__ . '/test_diseases.php';
            break;
            
        case 'debug_health':
            require_once __DIR__ . '/debug_health.php';
            break;
            
        case 'simple_diseases':
            require_once __DIR__ . '/simple_diseases.php';
            break;
            
        case 'test_register':
            require_once __DIR__ . '/test_register.php';
            break;
            
        case 'debug_otp':
            require_once __DIR__ . '/debug_otp.php';
            break;
            
        case 'debug_otp_status':
            require_once __DIR__ . '/debug_otp_status.php';
            break;
            
        case 'setup_password_reset_table':
            require_once __DIR__ . '/setup_password_reset_table.php';
            break;
            
        case 'auto_fix_otp':
            require_once __DIR__ . '/auto_fix_otp.php';
            break;
            
        case 'setup_database':
            require_once __DIR__ . '/setup_database.php';
            break;
            
        case 'appointments':
            require_once __DIR__ . '/appointments.php';
            break;
            
        case 'doctors':
            require_once __DIR__ . '/doctors.php';
            break;
            
        case 'setup_saveetha_doctors':
            require_once __DIR__ . '/setup_saveetha_doctors.php';
            break;
            
        case 'auto_setup_doctors':
            require_once __DIR__ . '/auto_setup_doctors.php';
            break;
            
        case 'auto_create_doctors_database':
            require_once __DIR__ . '/auto_create_doctors_database.php';
            break;
            
        case 'create_select_doctors_table':
            require_once __DIR__ . '/create_select_doctors_table.php';
            break;
            
        case 'setup_select_doctor_database':
            require_once __DIR__ . '/setup_select_doctor_database.php';
            break;
            
        case 'setup_otp_table':
            require_once __DIR__ . '/setup_otp_table.php';
            break;
            
        case 'auto_create_otp_table':
            require_once __DIR__ . '/auto_create_otp_table.php';
            break;
            
        case 'create_otp_table_direct':
            require_once __DIR__ . '/create_otp_table_direct.php';
            break;
            
        case 'verify_otp_table':
            require_once __DIR__ . '/verify_otp_table.php';
            break;
            
        case 'test_otp_flow':
            require_once __DIR__ . '/test_otp_flow.php';
            break;
            
        case 'debug_otp_insert':
            require_once __DIR__ . '/debug_otp_insert.php';
            break;
            
        case 'force_create_otp_table':
            require_once __DIR__ . '/force_create_otp_table.php';
            break;
            
        case 'send_otp':
            require_once __DIR__ . '/send_otp.php';
            break;
            
        case 'verify_otp':
            require_once __DIR__ . '/verify_otp.php';
            break;
            
        case 'resend_otp':
            require_once __DIR__ . '/resend_otp.php';
            break;
            
        case 'test_otp_simple':
            require_once __DIR__ . '/test_otp_simple.php';
            break;
            
        case 'check_otp_status':
            require_once __DIR__ . '/check_otp_status.php';
            break;
            
        case 'verify_table_exists':
            require_once __DIR__ . '/verify_table_exists.php';
            break;
            
        case 'test_endpoint_hit':
            require_once __DIR__ . '/test_endpoint_hit.php';
            break;
            
        case 'get_doctors':
            require_once __DIR__ . '/get_doctors.php';
            break;
            
        case 'book_appointment':
            require_once __DIR__ . '/book_appointment.php';
            break;
            
        case 'get_my_appointments':
            require_once __DIR__ . '/get_my_appointments.php';
            break;
            
        case 'auto_setup_appointment_tables':
            require_once __DIR__ . '/auto_setup_appointment_tables.php';
            break;
            
        case 'verify_tables':
            require_once __DIR__ . '/verify_tables.php';
            break;
            
        case 'create_doctors_table_direct':
            require_once __DIR__ . '/create_doctors_table_direct.php';
            break;
            
        case 'get_doctor_details':
            require_once __DIR__ . '/get_doctor_details.php';
            break;
            
        case 'test_get_doctors':
            require_once __DIR__ . '/test_get_doctors.php';
            break;
            
        case 'ensure_doctors':
            require_once __DIR__ . '/ensure_doctors.php';
            break;
            
        default:
            http_response_code(404);
            header('Content-Type: application/json');
            echo json_encode([
                'success' => false,
                'message' => 'Endpoint not found: ' . $endpoint
            ]);
            break;
    }
} else {
    // No endpoint specified
    http_response_code(400);
    header('Content-Type: application/json');
    echo json_encode([
        'success' => false,
        'message' => 'No endpoint specified'
    ]);
}

