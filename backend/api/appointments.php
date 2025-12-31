<?php
/**
 * Appointments API Endpoints
 * 
 * Endpoints:
 * POST   /appointments          - Create new appointment
 * GET    /appointments?userId=  - Get appointments for a user
 * GET    /appointments/{id}     - Get single appointment
 * PUT    /appointments/{id}     - Update appointment (status)
 * DELETE /appointments/{id}     - Delete appointment
 */

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

// CORS headers
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

$conn = getDB();
$method = $_SERVER['REQUEST_METHOD'];
$input = getJsonInput();
$segments = getPathSegments();

// Get appointment ID from URL if present (e.g., /appointments/{id})
$appointmentId = $segments[1] ?? null;

try {
    switch ($method) {
        case 'POST':
            // Create new appointment
            // POST /appointments
            $required = ['patientId', 'doctorId', 'date', 'time'];
            $missing = validateRequired($input, $required);
            
            if (!empty($missing)) {
                sendResponse(400, [
                    'success' => false,
                    'message' => 'Missing required fields: ' . implode(', ', $missing)
                ]);
            }
            
            $patientId = sanitizeInput($input['patientId']);
            $doctorId = sanitizeInput($input['doctorId']);
            $date = sanitizeInput($input['date']);
            $time = sanitizeInput($input['time']);
            $symptoms = sanitizeInput($input['symptoms'] ?? '');
            $status = sanitizeInput($input['status'] ?? 'pending');
            
            // Validate date format (YYYY-MM-DD)
            if (!preg_match('/^\d{4}-\d{2}-\d{2}$/', $date)) {
                sendResponse(400, [
                    'success' => false,
                    'message' => 'Invalid date format. Use YYYY-MM-DD'
                ]);
            }
            
            // Validate time format (HH:MM:SS or HH:MM)
            if (!preg_match('/^\d{2}:\d{2}(:\d{2})?$/', $time)) {
                sendResponse(400, [
                    'success' => false,
                    'message' => 'Invalid time format. Use HH:MM or HH:MM:SS'
                ]);
            }
            
            // Ensure time has seconds
            if (strlen($time) === 5) {
                $time .= ':00';
            }
            
            // Verify patient exists
            $checkPatient = $conn->prepare("SELECT id, name FROM users WHERE id = ? AND user_type = 'patient'");
            $checkPatient->bind_param("s", $patientId);
            $checkPatient->execute();
            $patientResult = $checkPatient->get_result();
            
            if ($patientResult->num_rows === 0) {
                sendResponse(404, [
                    'success' => false,
                    'message' => 'Patient not found'
                ]);
            }
            
            // Verify doctor exists
            $checkDoctor = $conn->prepare("SELECT d.id, u.name FROM doctors d JOIN users u ON d.user_id = u.id WHERE d.id = ?");
            $checkDoctor->bind_param("s", $doctorId);
            $checkDoctor->execute();
            $doctorResult = $checkDoctor->get_result();
            
            if ($doctorResult->num_rows === 0) {
                sendResponse(404, [
                    'success' => false,
                    'message' => 'Doctor not found'
                ]);
            }
            
            // Generate appointment ID
            $id = generateUUID();
            
            // Insert appointment
            $stmt = $conn->prepare("INSERT INTO appointments (id, patient_id, doctor_id, date, time, symptoms, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("sssssss", $id, $patientId, $doctorId, $date, $time, $symptoms, $status);
            
            if ($stmt->execute()) {
                // Fetch the created appointment with doctor name
                $fetchStmt = $conn->prepare("
                    SELECT 
                        a.id,
                        a.patient_id,
                        a.doctor_id,
                        a.date,
                        a.time,
                        a.symptoms,
                        a.status,
                        a.created_at,
                        a.updated_at,
                        u.name as doctor_name,
                        p.name as patient_name
                    FROM appointments a
                    JOIN doctors d ON a.doctor_id = d.id
                    JOIN users u ON d.user_id = u.id
                    JOIN users p ON a.patient_id = p.id
                    WHERE a.id = ?
                ");
                $fetchStmt->bind_param("s", $id);
                $fetchStmt->execute();
                $result = $fetchStmt->get_result();
                $appointment = $result->fetch_assoc();
                
                sendResponse(201, [
                    'success' => true,
                    'message' => 'Appointment created successfully',
                    'appointment' => [
                        'id' => $appointment['id'],
                        'patientId' => $appointment['patient_id'],
                        'doctorId' => $appointment['doctor_id'],
                        'date' => $appointment['date'],
                        'time' => $appointment['time'],
                        'symptoms' => $appointment['symptoms'],
                        'status' => $appointment['status']
                    ]
                ]);
            } else {
                error_log("Failed to create appointment: " . $conn->error);
                sendResponse(500, [
                    'success' => false,
                    'message' => 'Failed to create appointment: ' . $conn->error
                ]);
            }
            break;
            
        case 'GET':
            if ($appointmentId) {
                // Get single appointment
                // GET /appointments/{id}
                $stmt = $conn->prepare("
                    SELECT 
                        a.id,
                        a.patient_id,
                        a.doctor_id,
                        a.date,
                        a.time,
                        a.symptoms,
                        a.status,
                        a.created_at,
                        a.updated_at,
                        u.name as doctor_name,
                        p.name as patient_name
                    FROM appointments a
                    JOIN doctors d ON a.doctor_id = d.id
                    JOIN users u ON d.user_id = u.id
                    JOIN users p ON a.patient_id = p.id
                    WHERE a.id = ?
                ");
                $stmt->bind_param("s", $appointmentId);
                $stmt->execute();
                $result = $stmt->get_result();
                
                if ($result->num_rows === 0) {
                    sendResponse(404, [
                        'success' => false,
                        'message' => 'Appointment not found'
                    ]);
                }
                
                $appointment = $result->fetch_assoc();
                sendResponse(200, [
                    'success' => true,
                    'appointment' => [
                        'id' => $appointment['id'],
                        'patientId' => $appointment['patient_id'],
                        'doctorId' => $appointment['doctor_id'],
                        'date' => $appointment['date'],
                        'time' => $appointment['time'],
                        'symptoms' => $appointment['symptoms'],
                        'status' => $appointment['status']
                    ]
                ]);
            } else {
                // Get appointments for a user
                // GET /appointments?userId=xxx
                $userId = $_GET['userId'] ?? null;
                
                if (!$userId) {
                    sendResponse(400, [
                        'success' => false,
                        'message' => 'userId parameter is required'
                    ]);
                }
                
                // Determine if user is patient or doctor
                $userCheck = $conn->prepare("SELECT user_type FROM users WHERE id = ?");
                $userCheck->bind_param("s", $userId);
                $userCheck->execute();
                $userResult = $userCheck->get_result();
                
                if ($userResult->num_rows === 0) {
                    sendResponse(404, [
                        'success' => false,
                        'message' => 'User not found'
                    ]);
                }
                
                $user = $userResult->fetch_assoc();
                $userType = $user['user_type'];
                
                if ($userType === 'patient') {
                    // Get appointments for patient
                    $stmt = $conn->prepare("
                        SELECT 
                            a.id,
                            a.patient_id,
                            a.doctor_id,
                            a.date,
                            a.time,
                            a.symptoms,
                            a.status,
                            a.created_at,
                            a.updated_at,
                            u.name as doctor_name
                        FROM appointments a
                        JOIN doctors d ON a.doctor_id = d.id
                        JOIN users u ON d.user_id = u.id
                        WHERE a.patient_id = ?
                        ORDER BY a.date DESC, a.time DESC
                    ");
                } else if ($userType === 'doctor') {
                    // Get appointments for doctor - need to get doctor_id from user_id
                    $getDoctorId = $conn->prepare("SELECT id FROM doctors WHERE user_id = ?");
                    $getDoctorId->bind_param("s", $userId);
                    $getDoctorId->execute();
                    $doctorIdResult = $getDoctorId->get_result();
                    
                    if ($doctorIdResult->num_rows === 0) {
                        sendResponse(404, [
                            'success' => false,
                            'message' => 'Doctor profile not found'
                        ]);
                    }
                    
                    $doctorRow = $doctorIdResult->fetch_assoc();
                    $actualDoctorId = $doctorRow['id'];
                    $getDoctorId->close();
                    
                    // Get appointments for doctor
                    $stmt = $conn->prepare("
                        SELECT 
                            a.id,
                            a.patient_id,
                            a.doctor_id,
                            a.date,
                            a.time,
                            a.symptoms,
                            a.status,
                            a.created_at,
                            a.updated_at,
                            p.name as patient_name,
                            p.email as patient_email,
                            p.phone as patient_phone
                        FROM appointments a
                        JOIN doctors d ON a.doctor_id = d.id
                        JOIN users p ON a.patient_id = p.id
                        WHERE a.doctor_id = ?
                        ORDER BY a.date DESC, a.time DESC
                    ");
                    $stmt->bind_param("s", $actualDoctorId);
                } else {
                    sendResponse(400, [
                        'success' => false,
                        'message' => 'Invalid user type'
                    ]);
                }
                
                $stmt->bind_param("s", $userId);
                $stmt->execute();
                $result = $stmt->get_result();
                
                $appointments = [];
                while ($row = $result->fetch_assoc()) {
                    $appointments[] = [
                        'id' => $row['id'],
                        'patientId' => $row['patient_id'],
                        'doctorId' => $row['doctor_id'],
                        'date' => $row['date'],
                        'time' => $row['time'],
                        'symptoms' => $row['symptoms'],
                        'status' => $row['status']
                    ];
                }
                
                sendResponse(200, [
                    'success' => true,
                    'appointments' => $appointments,
                    'count' => count($appointments)
                ]);
            }
            break;
            
        case 'PUT':
            // Update appointment (mainly status)
            // PUT /appointments/{id}
            if (!$appointmentId) {
                sendResponse(400, [
                    'success' => false,
                    'message' => 'Appointment ID is required'
                ]);
            }
            
            $status = sanitizeInput($input['status'] ?? '');
            
            if (empty($status)) {
                sendResponse(400, [
                    'success' => false,
                    'message' => 'status field is required'
                ]);
            }
            
            // Validate status values
            $validStatuses = ['pending', 'accepted', 'rejected', 'cancelled', 'completed'];
            if (!in_array($status, $validStatuses)) {
                sendResponse(400, [
                    'success' => false,
                    'message' => 'Invalid status. Must be one of: ' . implode(', ', $validStatuses)
                ]);
            }
            
            // Update appointment
            $stmt = $conn->prepare("UPDATE appointments SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?");
            $stmt->bind_param("ss", $status, $appointmentId);
            
            if ($stmt->execute()) {
                if ($stmt->affected_rows === 0) {
                    sendResponse(404, [
                        'success' => false,
                        'message' => 'Appointment not found'
                    ]);
                }
                
                // Fetch updated appointment
                $fetchStmt = $conn->prepare("
                    SELECT 
                        a.id,
                        a.patient_id,
                        a.doctor_id,
                        a.date,
                        a.time,
                        a.symptoms,
                        a.status,
                        a.created_at,
                        a.updated_at
                    FROM appointments a
                    WHERE a.id = ?
                ");
                $fetchStmt->bind_param("s", $appointmentId);
                $fetchStmt->execute();
                $result = $fetchStmt->get_result();
                $appointment = $result->fetch_assoc();
                
                sendResponse(200, [
                    'success' => true,
                    'message' => 'Appointment updated successfully',
                    'appointment' => [
                        'id' => $appointment['id'],
                        'patientId' => $appointment['patient_id'],
                        'doctorId' => $appointment['doctor_id'],
                        'date' => $appointment['date'],
                        'time' => $appointment['time'],
                        'symptoms' => $appointment['symptoms'],
                        'status' => $appointment['status']
                    ]
                ]);
            } else {
                error_log("Failed to update appointment: " . $conn->error);
                sendResponse(500, [
                    'success' => false,
                    'message' => 'Failed to update appointment: ' . $conn->error
                ]);
            }
            break;
            
        case 'DELETE':
            // Delete appointment
            // DELETE /appointments/{id}
            if (!$appointmentId) {
                sendResponse(400, [
                    'success' => false,
                    'message' => 'Appointment ID is required'
                ]);
            }
            
            $stmt = $conn->prepare("DELETE FROM appointments WHERE id = ?");
            $stmt->bind_param("s", $appointmentId);
            
            if ($stmt->execute()) {
                if ($stmt->affected_rows === 0) {
                    sendResponse(404, [
                        'success' => false,
                        'message' => 'Appointment not found'
                    ]);
                }
                
                sendResponse(200, [
                    'success' => true,
                    'message' => 'Appointment deleted successfully'
                ]);
            } else {
                error_log("Failed to delete appointment: " . $conn->error);
                sendResponse(500, [
                    'success' => false,
                    'message' => 'Failed to delete appointment: ' . $conn->error
                ]);
            }
            break;
            
        default:
            sendResponse(405, [
                'success' => false,
                'message' => 'Method not allowed'
            ]);
            break;
    }
} catch (Exception $e) {
    error_log("Appointments API Error: " . $e->getMessage());
    sendResponse(500, [
        'success' => false,
        'message' => 'Internal server error: ' . $e->getMessage()
    ]);
}

