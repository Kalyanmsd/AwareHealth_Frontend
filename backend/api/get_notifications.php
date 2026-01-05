<?php
/**
 * Get Notifications API
 * Fetches appointment notifications with JOIN to get doctor names from users table
 * 
 * GET /api/get_notifications.php?email=user@example.com
 */

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use GET.'
    ], JSON_UNESCAPED_UNICODE);
    exit();
}

$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

try {
    $conn = new mysqli($dbHost, $dbUser, $dbPass, $dbName);
    
    if ($conn->connect_error) {
        throw new Exception('Database connection failed: ' . $conn->connect_error);
    }
    
    $conn->set_charset("utf8mb4");
    
    // Get user email from query parameter
    $userEmail = $_GET['email'] ?? null;
    
    if (empty($userEmail)) {
        $conn->close();
        http_response_code(400);
        echo json_encode([
            'success' => false,
            'message' => 'Email parameter is required'
        ], JSON_UNESCAPED_UNICODE);
        exit();
    }
    
    // Check if appointments table exists
    $tableCheck = $conn->query("SHOW TABLES LIKE 'appointments'");
    if (!$tableCheck || $tableCheck->num_rows === 0) {
        throw new Exception('Appointments table does not exist in database.');
    }
    
    // Check appointments table columns
    $appointmentsColumns = $conn->query("SHOW COLUMNS FROM appointments");
    $hasAppointmentDate = false;
    $hasDate = false;
    $hasAppointmentTime = false;
    $hasTime = false;
    
    while ($col = $appointmentsColumns->fetch_assoc()) {
        if ($col['Field'] === 'appointment_date') $hasAppointmentDate = true;
        if ($col['Field'] === 'date') $hasDate = true;
        if ($col['Field'] === 'appointment_time') $hasAppointmentTime = true;
        if ($col['Field'] === 'time') $hasTime = true;
    }
    
    $dateColumn = $hasAppointmentDate ? 'a.appointment_date' : ($hasDate ? 'a.date' : 'a.appointment_date');
    $timeColumn = $hasAppointmentTime ? 'a.appointment_time' : ($hasTime ? 'a.time' : 'a.appointment_time');
    
    // Check if doctors table has user_id
    $doctorsColumns = $conn->query("SHOW COLUMNS FROM doctors");
    $doctorsHasUserId = false;
    $doctorsHasSpecialty = false;
    $doctorsHasSpecialization = false;
    $doctorsHasLocation = false;
    $doctorsHasHospital = false;
    
    while ($col = $doctorsColumns->fetch_assoc()) {
        if ($col['Field'] === 'user_id') $doctorsHasUserId = true;
        if ($col['Field'] === 'specialty') $doctorsHasSpecialty = true;
        if ($col['Field'] === 'specialization') $doctorsHasSpecialization = true;
        if ($col['Field'] === 'location') $doctorsHasLocation = true;
        if ($col['Field'] === 'hospital') $doctorsHasHospital = true;
    }
    
    $specialtyCol = $doctorsHasSpecialty ? 'd.specialty' : ($doctorsHasSpecialization ? 'd.specialization' : "'General Physician'");
    $locationCol = $doctorsHasLocation ? 'd.location' : ($doctorsHasHospital ? 'd.hospital' : "'Saveetha Hospital'");
    
    // Build query with JOIN to get doctor name from users table
    if ($doctorsHasUserId) {
        $sql = "
            SELECT 
                a.id,
                a.user_email,
                a.doctor_id,
                $dateColumn as appointment_date,
                $timeColumn as appointment_time,
                a.status,
                a.created_at,
                COALESCE(u.name, d.name, 'Dr. Doctor') as doctor_name,
                $specialtyCol as doctor_specialization,
                $locationCol as location
            FROM appointments a
            LEFT JOIN doctors d ON a.doctor_id = d.id
            LEFT JOIN users u ON d.user_id = u.id
            WHERE a.user_email = ?
            ORDER BY a.created_at DESC
            LIMIT 20
        ";
    } else {
        $sql = "
            SELECT 
                a.id,
                a.user_email,
                a.doctor_id,
                $dateColumn as appointment_date,
                $timeColumn as appointment_time,
                a.status,
                a.created_at,
                COALESCE(d.name, 'Dr. Doctor') as doctor_name,
                $specialtyCol as doctor_specialization,
                $locationCol as location
            FROM appointments a
            LEFT JOIN doctors d ON a.doctor_id = d.id
            WHERE a.user_email = ?
            ORDER BY a.created_at DESC
            LIMIT 20
        ";
    }
    
    $stmt = $conn->prepare($sql);
    
    if (!$stmt) {
        throw new Exception('Database query failed: ' . $conn->error);
    }
    
    $stmt->bind_param("s", $userEmail);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $notifications = [];
    while ($row = $result->fetch_assoc()) {
        // Format time to 12-hour format
        $time12Hour = date("g:i A", strtotime($row['appointment_time']));
        
        // Create notification message based on status
        $status = $row['status'] ?? 'Pending';
        $title = "Appointment " . ucfirst(strtolower($status));
        $message = "Your appointment with " . $row['doctor_name'] . " (" . $row['doctor_specialization'] . ") on " . 
                   date("M d, Y", strtotime($row['appointment_date'])) . " at " . $time12Hour . " has been " . strtolower($status) . ".";
        
        // Calculate time ago
        $createdAt = strtotime($row['created_at']);
        $now = time();
        $diff = $now - $createdAt;
        
        if ($diff < 60) {
            $timeAgo = "Just now";
        } elseif ($diff < 3600) {
            $timeAgo = floor($diff / 60) . " mins ago";
        } elseif ($diff < 86400) {
            $timeAgo = floor($diff / 3600) . " hours ago";
        } else {
            $timeAgo = floor($diff / 86400) . " days ago";
        }
        
        $notifications[] = [
            'id' => (string)$row['id'],
            'title' => $title,
            'message' => $message,
            'time' => $timeAgo,
            'type' => 'appointment',
            'read' => false,
            'appointment_id' => (string)$row['id'],
            'doctor_name' => $row['doctor_name'],
            'doctor_specialization' => $row['doctor_specialization'],
            'appointment_date' => $row['appointment_date'],
            'appointment_time' => $time12Hour,
            'location' => $row['location'],
            'status' => $status
        ];
    }
    
    $stmt->close();
    $conn->close();
    
    http_response_code(200);
    echo json_encode([
        'success' => true,
        'notifications' => $notifications,
        'count' => count($notifications)
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    
} catch (Exception $e) {
    if (isset($conn) && !$conn->connect_error) {
        $conn->close();
    }
    
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => $e->getMessage(),
        'notifications' => null
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    exit();
}

?>

