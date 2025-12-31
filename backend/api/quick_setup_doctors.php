<?php
/**
 * QUICK SETUP - Add doctors immediately
 * Run: http://192.168.1.11/AwareHealth/api/quick_setup_doctors.php
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

ob_end_clean();
ob_start();

header('Content-Type: application/json');

try {
    $conn = getDB();
    
    // Quick insert - ignore duplicates
    $doctors = [
        ['DOC001-USER', 'DOC001', 'SAV001', 'Dr. Rajesh Kumar', 'rajesh.kumar@saveetha.com', 'Cardiology', '15 years', 4.8, 'Mon-Fri: 9 AM - 5 PM'],
        ['DOC002-USER', 'DOC002', 'SAV002', 'Dr. Priya Sharma', 'priya.sharma@saveetha.com', 'Pediatrics', '12 years', 4.7, 'Mon-Sat: 10 AM - 6 PM'],
        ['DOC003-USER', 'DOC003', 'SAV003', 'Dr. Anil Patel', 'anil.patel@saveetha.com', 'Orthopedics', '18 years', 4.9, 'Mon-Fri: 8 AM - 4 PM'],
        ['DOC004-USER', 'DOC004', 'SAV004', 'Dr. Meera Reddy', 'meera.reddy@saveetha.com', 'Gynecology', '14 years', 4.6, 'Mon-Sat: 9 AM - 5 PM'],
        ['DOC005-USER', 'DOC005', 'SAV005', 'Dr. Vikram Singh', 'vikram.singh@saveetha.com', 'Neurology', '16 years', 4.8, 'Mon-Fri: 10 AM - 6 PM']
    ];
    
    $created = 0;
    $updated = 0;
    
    foreach ($doctors as $doc) {
        // Insert/Update user
        $userStmt = $conn->prepare("
            INSERT INTO users (id, name, email, password, phone, user_type) 
            VALUES (?, ?, ?, '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '9876543210', 'doctor')
            ON DUPLICATE KEY UPDATE name = VALUES(name), email = VALUES(email)
        ");
        $userStmt->bind_param("sss", $doc[0], $doc[3], $doc[4]);
        $userStmt->execute();
        if ($userStmt->affected_rows > 0) {
            $created++;
        } else {
            $updated++;
        }
        $userStmt->close();
        
        // Insert/Update doctor
        $doctorStmt = $conn->prepare("
            INSERT INTO doctors (id, user_id, doctor_id, specialty, experience, rating, availability, location) 
            VALUES (?, ?, ?, ?, ?, ?, ?, 'Saveetha Hospital')
            ON DUPLICATE KEY UPDATE 
                specialty = VALUES(specialty),
                experience = VALUES(experience),
                rating = VALUES(rating),
                availability = VALUES(availability),
                location = 'Saveetha Hospital'
        ");
        $doctorStmt->bind_param("sssssds", $doc[1], $doc[0], $doc[2], $doc[5], $doc[6], $doc[7], $doc[8]);
        $doctorStmt->execute();
        $doctorStmt->close();
    }
    
    // Verify
    $check = $conn->query("
        SELECT COUNT(*) as count 
        FROM doctors d 
        JOIN users u ON d.user_id = u.id 
        WHERE d.location = 'Saveetha Hospital'
    ");
    $result = $check->fetch_assoc();
    $count = $result['count'];
    
    ob_end_clean();
    echo json_encode([
        'success' => true,
        'message' => 'Doctors setup complete!',
        'doctors_count' => $count,
        'created' => $created,
        'updated' => $updated
    ], JSON_PRETTY_PRINT);
    
} catch (Exception $e) {
    ob_end_clean();
    echo json_encode([
        'success' => false,
        'message' => 'Error: ' . $e->getMessage()
    ], JSON_PRETTY_PRINT);
}

