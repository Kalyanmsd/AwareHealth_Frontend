<?php
/**
 * Get Doctor Appointments - Returns appointments for a specific doctor
 * GET: http://172.20.10.2/AwareHealth/api/get_doctor_appointments.php?doctor_id={doctor_id}
 * 
 * This endpoint returns ONLY appointments for the specified doctor_id
 * Used by doctor dashboard to show their appointments
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Only allow GET
if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use GET.'
    ]);
    exit();
}

require_once __DIR__ . '/../config.php';

// Connect to database
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]);
    exit();
}

$conn->set_charset("utf8mb4");

// Get doctor_id from query parameter
$doctorId = isset($_GET['doctor_id']) ? (int)$_GET['doctor_id'] : null;

if (!$doctorId) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'doctor_id parameter is required'
    ]);
    exit();
}

// Check if appointments table exists
$tableCheck = $conn->query("SHOW TABLES LIKE 'appointments'");
if (!$tableCheck || $tableCheck->num_rows === 0) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Appointments table does not exist'
    ]);
    exit();
}

// Check table structure to determine column names
$columns = [];
$columnCheck = $conn->query("SHOW COLUMNS FROM appointments");
while ($col = $columnCheck->fetch_assoc()) {
    $columns[] = $col['Field'];
}

$hasPatientName = in_array('patient_name', $columns);
$hasPatientEmail = in_array('patient_email', $columns) || in_array('user_email', $columns);
$hasDoctorName = in_array('doctor_name', $columns);
$hasHospital = in_array('hospital', $columns);
$hasAppointmentDate = in_array('appointment_date', $columns);
$hasAppointmentTime = in_array('appointment_time', $columns);

// Build query based on available columns
$patientEmailCol = $hasPatientEmail ? (in_array('patient_email', $columns) ? 'patient_email' : 'user_email') : 'user_email';
$dateCol = $hasAppointmentDate ? 'appointment_date' : 'date';
$timeCol = $hasAppointmentTime ? 'appointment_time' : 'time';

// Get patient name from users table if patient_email/user_email exists
$sql = "
    SELECT 
        a.id,
        a.appointment_id,
        " . ($hasPatientName ? "COALESCE(a.patient_name, u.name, 'Patient')" : "COALESCE(u.name, 'Patient')") . " as patient_name,
        " . ($hasPatientEmail ? "COALESCE(a.$patientEmailCol, u.email)" : "u.email") . " as patient_email,
        a.doctor_id,
        " . ($hasDoctorName ? "COALESCE(a.doctor_name, d.doctor_name, d.name, 'Dr. Doctor')" : "COALESCE(d.doctor_name, d.name, 'Dr. Doctor')") . " as doctor_name,
        COALESCE(d.specialization, d.specialty, 'General Physician') as doctor_specialization,
        a.$dateCol as appointment_date,
        a.$timeCol as appointment_time,
        " . ($hasHospital ? "COALESCE(a.hospital, d.location, d.hospital, 'Saveetha Hospital')" : "COALESCE(d.location, d.hospital, 'Saveetha Hospital')") . " as hospital,
        a.status,
        a.symptoms,
        a.reason,
        a.created_at
    FROM appointments a
    INNER JOIN doctors d ON a.doctor_id = d.id
    LEFT JOIN users u ON " . ($hasPatientEmail ? "a.$patientEmailCol = u.email" : "a.user_email = u.email") . "
    WHERE a.doctor_id = ?
    ORDER BY a.$dateCol ASC, a.$timeCol ASC
";

$stmt = $conn->prepare($sql);

if (!$stmt) {
    $conn->close();
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database query failed: ' . $conn->error
    ]);
    exit();
}

$stmt->bind_param("i", $doctorId);
$stmt->execute();
$result = $stmt->get_result();

$appointments = [];
while ($row = $result->fetch_assoc()) {
    // Format time to 12-hour format
    $appointmentTime = $row['appointment_time'];
    if (strlen($appointmentTime) >= 5) {
        $timeParts = explode(':', $appointmentTime);
        $hour = (int)$timeParts[0];
        $minute = $timeParts[1] ?? '00';
        $ampm = $hour >= 12 ? 'PM' : 'AM';
        $hour12 = $hour > 12 ? $hour - 12 : ($hour == 0 ? 12 : $hour);
        $appointmentTime = sprintf('%d:%s %s', $hour12, $minute, $ampm);
    }
    
    $appointments[] = [
        'id' => (string)$row['id'],
        'appointment_id' => $row['appointment_id'] ?? (string)$row['id'],
        'patientId' => $row['patient_email'] ?? '',
        'patientName' => $row['patient_name'] ?? 'Patient',
        'patientEmail' => $row['patient_email'] ?? '',
        'doctorId' => (string)$row['doctor_id'],
        'doctorName' => $row['doctor_name'] ?? 'Dr. Doctor',
        'doctorSpecialization' => $row['doctor_specialization'] ?? 'General Physician',
        'date' => $row['appointment_date'],
        'time' => $appointmentTime,
        'hospital' => $row['hospital'] ?? 'Saveetha Hospital',
        'status' => strtolower($row['status'] ?? 'pending'),
        'symptoms' => $row['symptoms'] ?? null,
        'reason' => $row['reason'] ?? null,
        'createdAt' => $row['created_at'] ?? null
    ];
}

$stmt->close();
$conn->close();

http_response_code(200);
echo json_encode([
    'success' => true,
    'message' => 'Appointments fetched successfully',
    'appointments' => $appointments,
    'count' => count($appointments),
    'doctor_id' => $doctorId
], JSON_PRETTY_PRINT);

