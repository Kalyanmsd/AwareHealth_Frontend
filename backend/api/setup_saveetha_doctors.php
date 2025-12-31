<?php
/**
 * Automatic Saveetha Hospital Doctors Setup
 * This script automatically creates Saveetha Hospital doctors in the database
 * Access via: http://localhost/AwareHealth/api/setup_saveetha_doctors.php
 */

ob_start();
ini_set('display_errors', '0');
error_reporting(E_ALL);

require_once __DIR__ . '/../config.php';
require_once __DIR__ . '/../includes/database.php';
require_once __DIR__ . '/../includes/functions.php';

ob_end_clean();
ob_start();

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Saveetha Hospital Doctors Setup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 Saveetha Hospital Doctors - Automatic Setup</h1>
        
<?php

try {
    $conn = getDB();
    echo '<div class="success">✅ Database connection successful!</div>';
    
    // Ensure doctors table has required columns
    echo '<h2>Step 1: Checking doctors table structure...</h2>';
    
    $columns = $conn->query("SHOW COLUMNS FROM doctors");
    $columnNames = [];
    while ($col = $columns->fetch_assoc()) {
        $columnNames[] = $col['Field'];
    }
    
    if (!in_array('location', $columnNames)) {
        $conn->query("ALTER TABLE doctors ADD COLUMN `location` VARCHAR(255) DEFAULT 'Saveetha Hospital' AFTER `availability`");
        echo '<div class="success">✅ Added location column</div>';
    }
    
    if (!in_array('doctor_id', $columnNames)) {
        $conn->query("ALTER TABLE doctors ADD COLUMN `doctor_id` VARCHAR(50) UNIQUE DEFAULT NULL COMMENT 'Unique doctor ID for login' AFTER `id`");
        $conn->query("ALTER TABLE doctors ADD INDEX `idx_doctor_id` (`doctor_id`)");
        echo '<div class="success">✅ Added doctor_id column</div>';
    }
    
    // Sample Saveetha Hospital Doctors
    $doctors = [
        [
            'user_id' => 'DOC001-USER',
            'doctor_id' => 'SAV001',
            'name' => 'Dr. Rajesh Kumar',
            'email' => 'rajesh.kumar@saveetha.com',
            'password' => password_hash('password', PASSWORD_DEFAULT),
            'phone' => '9876543210',
            'specialty' => 'Cardiology',
            'experience' => '15 years',
            'rating' => 4.8,
            'availability' => 'Mon-Fri: 9 AM - 5 PM',
            'location' => 'Saveetha Hospital'
        ],
        [
            'user_id' => 'DOC002-USER',
            'doctor_id' => 'SAV002',
            'name' => 'Dr. Priya Sharma',
            'email' => 'priya.sharma@saveetha.com',
            'password' => password_hash('password', PASSWORD_DEFAULT),
            'phone' => '9876543211',
            'specialty' => 'Pediatrics',
            'experience' => '12 years',
            'rating' => 4.7,
            'availability' => 'Mon-Sat: 10 AM - 6 PM',
            'location' => 'Saveetha Hospital'
        ],
        [
            'user_id' => 'DOC003-USER',
            'doctor_id' => 'SAV003',
            'name' => 'Dr. Anil Patel',
            'email' => 'anil.patel@saveetha.com',
            'password' => password_hash('password', PASSWORD_DEFAULT),
            'phone' => '9876543212',
            'specialty' => 'Orthopedics',
            'experience' => '18 years',
            'rating' => 4.9,
            'availability' => 'Mon-Fri: 8 AM - 4 PM',
            'location' => 'Saveetha Hospital'
        ],
        [
            'user_id' => 'DOC004-USER',
            'doctor_id' => 'SAV004',
            'name' => 'Dr. Meera Reddy',
            'email' => 'meera.reddy@saveetha.com',
            'password' => password_hash('password', PASSWORD_DEFAULT),
            'phone' => '9876543213',
            'specialty' => 'Gynecology',
            'experience' => '14 years',
            'rating' => 4.6,
            'availability' => 'Mon-Sat: 9 AM - 5 PM',
            'location' => 'Saveetha Hospital'
        ],
        [
            'user_id' => 'DOC005-USER',
            'doctor_id' => 'SAV005',
            'name' => 'Dr. Vikram Singh',
            'email' => 'vikram.singh@saveetha.com',
            'password' => password_hash('password', PASSWORD_DEFAULT),
            'phone' => '9876543214',
            'specialty' => 'Neurology',
            'experience' => '16 years',
            'rating' => 4.8,
            'availability' => 'Mon-Fri: 10 AM - 6 PM',
            'location' => 'Saveetha Hospital'
        ]
    ];
    
    echo '<h2>Step 2: Creating Saveetha Hospital doctors...</h2>';
    echo '<table border="1">';
    echo '<tr><th>Doctor ID</th><th>Name</th><th>Specialty</th><th>Status</th></tr>';
    
    $created = 0;
    $updated = 0;
    
    foreach ($doctors as $doc) {
        // Check if user exists
        $checkUser = $conn->prepare("SELECT id FROM users WHERE id = ? OR email = ?");
        $checkUser->bind_param("ss", $doc['user_id'], $doc['email']);
        $checkUser->execute();
        $userExists = $checkUser->get_result()->num_rows > 0;
        $checkUser->close();
        
        if (!$userExists) {
            // Create user
            $userStmt = $conn->prepare("INSERT INTO users (id, name, email, password, phone, user_type) VALUES (?, ?, ?, ?, ?, 'doctor')");
            $userStmt->bind_param("sssss", $doc['user_id'], $doc['name'], $doc['email'], $doc['password'], $doc['phone']);
            $userStmt->execute();
            $userStmt->close();
        } else {
            // Update user
            $userStmt = $conn->prepare("UPDATE users SET name = ?, email = ?, password = ?, phone = ? WHERE id = ?");
            $userStmt->bind_param("sssss", $doc['name'], $doc['email'], $doc['password'], $doc['phone'], $doc['user_id']);
            $userStmt->execute();
            $userStmt->close();
        }
        
        // Check if doctor exists
        $checkDoctor = $conn->prepare("SELECT id FROM doctors WHERE doctor_id = ? OR user_id = ?");
        $checkDoctor->bind_param("ss", $doc['doctor_id'], $doc['user_id']);
        $checkDoctor->execute();
        $doctorExists = $checkDoctor->get_result()->num_rows > 0;
        $checkDoctor->close();
        
        $doctorId = $doc['user_id'] . '-DOC';
        
        if (!$doctorExists) {
            // Create doctor
            $doctorStmt = $conn->prepare("INSERT INTO doctors (id, user_id, doctor_id, specialty, experience, rating, availability, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            $doctorStmt->bind_param("sssssdss", $doctorId, $doc['user_id'], $doc['doctor_id'], $doc['specialty'], $doc['experience'], $doc['rating'], $doc['availability'], $doc['location']);
            if ($doctorStmt->execute()) {
                echo "<tr><td>{$doc['doctor_id']}</td><td>{$doc['name']}</td><td>{$doc['specialty']}</td><td class='success'>✅ Created</td></tr>";
                $created++;
            } else {
                echo "<tr><td>{$doc['doctor_id']}</td><td>{$doc['name']}</td><td>{$doc['specialty']}</td><td class='error'>❌ Error: " . $conn->error . "</td></tr>";
            }
            $doctorStmt->close();
        } else {
            // Update doctor
            $doctorStmt = $conn->prepare("UPDATE doctors SET doctor_id = ?, specialty = ?, experience = ?, rating = ?, availability = ?, location = ? WHERE user_id = ?");
            $doctorStmt->bind_param("sssdsss", $doc['doctor_id'], $doc['specialty'], $doc['experience'], $doc['rating'], $doc['availability'], $doc['location'], $doc['user_id']);
            if ($doctorStmt->execute()) {
                echo "<tr><td>{$doc['doctor_id']}</td><td>{$doc['name']}</td><td>{$doc['specialty']}</td><td class='info'>ℹ️ Updated</td></tr>";
                $updated++;
            } else {
                echo "<tr><td>{$doc['doctor_id']}</td><td>{$doc['name']}</td><td>{$doc['specialty']}</td><td class='error'>❌ Error: " . $conn->error . "</td></tr>";
            }
            $doctorStmt->close();
        }
    }
    
    echo '</table>';
    
    echo '<div class="success">';
    echo '<h2>✅ Setup Complete!</h2>';
    echo "<p>Created: $created doctors</p>";
    echo "<p>Updated: $updated doctors</p>";
    echo '<p><strong>Doctor Login Credentials:</strong></p>';
    echo '<ul>';
    echo '<li>SAV001 - Dr. Rajesh Kumar (Cardiologist) - Password: password</li>';
    echo '<li>SAV002 - Dr. Priya Sharma (Pediatrician) - Password: password</li>';
    echo '<li>SAV003 - Dr. Anil Patel (Orthopedic Surgeon) - Password: password</li>';
    echo '<li>SAV004 - Dr. Meera Reddy (Gynecologist) - Password: password</li>';
    echo '<li>SAV005 - Dr. Vikram Singh (Neurologist) - Password: password</li>';
    echo '</ul>';
    echo '<p><strong>Test API:</strong> <a href="doctors" target="_blank">http://localhost/AwareHealth/api/doctors</a></p>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
}

ob_end_flush();
?>

    </div>
</body>
</html>

