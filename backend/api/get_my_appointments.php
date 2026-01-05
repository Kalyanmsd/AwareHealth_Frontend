<?php
/**
 * Get My Appointments - Fetch appointments for a user
 * GET: http://172.20.10.2/AwareHealth/api/get_my_appointments.php?email=patient@example.com
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

// Get email from query parameter
$userEmail = isset($_GET['email']) ? strtolower(trim($_GET['email'])) : null;

if (empty($userEmail)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Email parameter is required. Use: ?email=your-email@example.com'
    ]);
    exit();
}

// Validate email format
if (!filter_var($userEmail, FILTER_VALIDATE_EMAIL)) {
    $conn->close();
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid email format'
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
        'message' => 'Appointments table does not exist. Please run the SQL script to create it.'
    ]);
    exit();
}

// Fetch appointments for user
// Handle different column names (appointment_date/date, appointment_time/time)
$columnsResult = $conn->query("SHOW COLUMNS FROM appointments");
$hasAppointmentDate = false;
$hasDate = false;
$hasAppointmentTime = false;
$hasTime = false;

while ($col = $columnsResult->fetch_assoc()) {
    if ($col['Field'] === 'appointment_date') $hasAppointmentDate = true;
    if ($col['Field'] === 'date') $hasDate = true;
    if ($col['Field'] === 'appointment_time') $hasAppointmentTime = true;
    if ($col['Field'] === 'time') $hasTime = true;
}

// Check if doctors table has user_id
$doctorsColumns = $conn->query("SHOW COLUMNS FROM doctors");
$doctorsHasUserId = false;
while ($col = $doctorsColumns->fetch_assoc()) {
    if ($col['Field'] === 'user_id') {
        $doctorsHasUserId = true;
        break;
    }
}

// Build column names based on what exists
$dateColumn = $hasAppointmentDate ? 'a.appointment_date' : ($hasDate ? 'a.date' : 'a.appointment_date');
$timeColumn = $hasAppointmentTime ? 'a.appointment_time' : ($hasTime ? 'a.time' : 'a.appointment_time');

// Check doctors table columns for specialty/specialization
$doctorsColumnsCheck = $conn->query("SHOW COLUMNS FROM doctors");
$doctorsHasSpecialty = false;
$doctorsHasSpecialization = false;
$doctorsHasLocation = false;
$doctorsHasHospital = false;

while ($col = $doctorsColumnsCheck->fetch_assoc()) {
    if ($col['Field'] === 'specialty') $doctorsHasSpecialty = true;
    if ($col['Field'] === 'specialization') $doctorsHasSpecialization = true;
    if ($col['Field'] === 'location') $doctorsHasLocation = true;
    if ($col['Field'] === 'hospital') $doctorsHasHospital = true;
}

// Determine specialty column - prioritize 'specialty' if it exists
$specialtyCol = $doctorsHasSpecialty ? 'd.specialty' : ($doctorsHasSpecialization ? 'd.specialization' : "'General'");
$locationCol = $doctorsHasLocation ? 'd.location' : ($doctorsHasHospital ? 'd.hospital' : "'Saveetha Hospital'");

// Build JOIN query - get doctor name from users table if user_id exists
if ($doctorsHasUserId) {
    // Use JOIN to get name from users table
    $sql = "
        SELECT 
            a.id,
            a.user_email,
            a.doctor_id,
            $dateColumn as appointment_date,
            $timeColumn as appointment_time,
            a.status,
            a.created_at,
            COALESCE(u.name, d.name, CONCAT('Dr. ', $specialtyCol)) as doctor_name,
            $specialtyCol as doctor_specialization,
            $locationCol as hospital
        FROM appointments a
        LEFT JOIN doctors d ON a.doctor_id = d.id
        LEFT JOIN users u ON d.user_id = u.id
        WHERE a.user_email = ?
        ORDER BY $dateColumn DESC, $timeColumn DESC
    ";
} else {
    // Fallback: doctors table doesn't have user_id
    $sql = "
        SELECT 
            a.id,
            a.user_email,
            a.doctor_id,
            $dateColumn as appointment_date,
            $timeColumn as appointment_time,
            a.status,
            a.created_at,
            COALESCE(d.name, CONCAT('Dr. ', $specialtyCol)) as doctor_name,
            $specialtyCol as doctor_specialization,
            $locationCol as hospital
        FROM appointments a
        LEFT JOIN doctors d ON a.doctor_id = d.id
        WHERE a.user_email = ?
        ORDER BY $dateColumn DESC, $timeColumn DESC
    ";
}

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

$stmt->bind_param("s", $userEmail);
$stmt->execute();
$result = $stmt->get_result();

$appointments = [];
while ($row = $result->fetch_assoc()) {
    // Format date properly (YYYY-MM-DD)
    $appointmentDate = $row['appointment_date'];
    // Format time properly (HH:MM or HH:MM:SS -> HH:MM AM/PM)
    $appointmentTime = $row['appointment_time'];
    
    // Convert 24-hour time to 12-hour format if needed
    if (strlen($appointmentTime) >= 5) {
        $timeParts = explode(':', $appointmentTime);
        $hour = (int)$timeParts[0];
        $minute = $timeParts[1] ?? '00';
        $ampm = $hour >= 12 ? 'PM' : 'AM';
        $hour12 = $hour > 12 ? $hour - 12 : ($hour == 0 ? 12 : $hour);
        $appointmentTime = sprintf('%d:%s %s', $hour12, $minute, $ampm);
    }
    
    $appointments[] = [
        'id' => (string)$row['id'], // Frontend expects String
        'patientEmail' => $row['user_email'],
        'doctorId' => (string)$row['doctor_id'], // Frontend expects String
        'doctorName' => $row['doctor_name'] ?? 'Dr. ' . ($row['doctor_specialization'] ?? 'Doctor'),
        'date' => $appointmentDate, // Frontend expects 'date' not 'appointment_date'
        'time' => $appointmentTime, // Frontend expects 'time' not 'appointment_time'
        'status' => strtolower($row['status'] ?? 'pending'),
        'reason' => null
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
    'user_email' => $userEmail
], JSON_PRETTY_PRINT);

