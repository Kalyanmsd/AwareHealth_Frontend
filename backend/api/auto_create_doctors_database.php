<?php
/**
 * AUTOMATIC DATABASE & DOCTORS SETUP
 * This script automatically:
 * 1. Creates 'awarehealth' database if it doesn't exist
 * 2. Creates users and doctors tables
 * 3. Adds 5 Saveetha Hospital doctors
 * 
 * Access via: http://localhost/AwareHealth/api/auto_create_doctors_database.php
 */

ob_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);

// Database credentials
$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'awarehealth';

// Connect to MySQL (without selecting database first)
$conn = new mysqli($dbHost, $dbUser, $dbPass);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

ob_end_clean();
ob_start();

header('Content-Type: text/html; charset=utf-8');

?>
<!DOCTYPE html>
<html>
<head>
    <title>Automatic Database & Doctors Setup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .warning { color: #FB8C00; background: #FFF3E0; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
        .btn { display: inline-block; padding: 10px 20px; background: #34A853; color: white; text-decoration: none; border-radius: 4px; margin: 10px 5px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 Automatic Database & Saveetha Doctors Setup</h1>
        
<?php

try {
    // Step 1: Create database if it doesn't exist
    echo '<h2>Step 1: Creating database...</h2>';
    $createDb = "CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
    if ($conn->query($createDb)) {
        echo '<div class="success">✅ Database "' . $dbName . '" created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create database: " . $conn->error);
    }
    
    // Select the database
    $conn->select_db($dbName);
    
    // Step 2: Create users table
    echo '<h2>Step 2: Creating users table...</h2>';
    $createUsers = "CREATE TABLE IF NOT EXISTS `users` (
      `id` VARCHAR(36) PRIMARY KEY,
      `name` VARCHAR(255) NOT NULL,
      `email` VARCHAR(255) NOT NULL UNIQUE,
      `password` VARCHAR(255) NOT NULL,
      `phone` VARCHAR(20) DEFAULT NULL,
      `user_type` VARCHAR(50) NOT NULL DEFAULT 'patient',
      `google_id` VARCHAR(255) DEFAULT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_email` (`email`),
      INDEX `idx_user_type` (`user_type`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createUsers)) {
        echo '<div class="success">✅ Users table created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create users table: " . $conn->error);
    }
    
    // Step 3: Create doctors table
    echo '<h2>Step 3: Creating doctors table...</h2>';
    $createDoctors = "CREATE TABLE IF NOT EXISTS `doctors` (
      `id` VARCHAR(36) PRIMARY KEY,
      `user_id` VARCHAR(36) NOT NULL,
      `doctor_id` VARCHAR(50) UNIQUE DEFAULT NULL COMMENT 'Unique doctor ID for login (e.g., SAV001)',
      `specialty` VARCHAR(255) NOT NULL DEFAULT 'General',
      `experience` VARCHAR(100) DEFAULT NULL,
      `rating` DECIMAL(3,2) DEFAULT 0.00,
      `availability` VARCHAR(255) DEFAULT NULL,
      `location` VARCHAR(255) DEFAULT 'Saveetha Hospital',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_user_id` (`user_id`),
      INDEX `idx_doctor_id` (`doctor_id`),
      INDEX `idx_specialty` (`specialty`),
      INDEX `idx_location` (`location`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createDoctors)) {
        echo '<div class="success">✅ Doctors table created/verified successfully!</div>';
    } else {
        throw new Exception("Failed to create doctors table: " . $conn->error);
    }
    
    // Step 4: Add missing columns if table exists
    echo '<h2>Step 4: Ensuring all columns exist...</h2>';
    $columns = $conn->query("SHOW COLUMNS FROM doctors");
    $columnNames = [];
    while ($col = $columns->fetch_assoc()) {
        $columnNames[] = $col['Field'];
    }
    
    if (!in_array('user_id', $columnNames)) {
        $conn->query("ALTER TABLE doctors ADD COLUMN `user_id` VARCHAR(36) NOT NULL AFTER `id`");
        $conn->query("ALTER TABLE doctors ADD INDEX `idx_user_id` (`user_id`)");
        echo '<div class="success">✅ Added user_id column</div>';
    }
    
    if (!in_array('doctor_id', $columnNames)) {
        $conn->query("ALTER TABLE doctors ADD COLUMN `doctor_id` VARCHAR(50) UNIQUE DEFAULT NULL COMMENT 'Unique doctor ID for login' AFTER `id`");
        $conn->query("ALTER TABLE doctors ADD INDEX `idx_doctor_id` (`doctor_id`)");
        echo '<div class="success">✅ Added doctor_id column</div>';
    }
    
    if (!in_array('location', $columnNames)) {
        $conn->query("ALTER TABLE doctors ADD COLUMN `location` VARCHAR(255) DEFAULT 'Saveetha Hospital' AFTER `availability`");
        $conn->query("ALTER TABLE doctors ADD INDEX `idx_location` (`location`)");
        echo '<div class="success">✅ Added location column</div>';
    }
    
    // Step 5: Create Saveetha Hospital Doctors
    echo '<h2>Step 5: Creating Saveetha Hospital doctors...</h2>';
    
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
    
    echo '<table border="1">';
    echo '<tr><th>Doctor ID</th><th>Name</th><th>Specialty</th><th>User Status</th><th>Doctor Status</th></tr>';
    
    $createdUsers = 0;
    $updatedUsers = 0;
    $createdDoctors = 0;
    $updatedDoctors = 0;
    
    foreach ($doctors as $doc) {
        $doctorId = $doc['user_id'] . '-DOC';
        
        // Create or update user
        $checkUser = $conn->prepare("SELECT id FROM users WHERE id = ? OR email = ?");
        $checkUser->bind_param("ss", $doc['user_id'], $doc['email']);
        $checkUser->execute();
        $userResult = $checkUser->get_result();
        $userExists = $userResult->num_rows > 0;
        $checkUser->close();
        
        $userStatus = '';
        if (!$userExists) {
            $userStmt = $conn->prepare("INSERT INTO users (id, name, email, password, phone, user_type) VALUES (?, ?, ?, ?, ?, 'doctor')");
            $userStmt->bind_param("sssss", $doc['user_id'], $doc['name'], $doc['email'], $doc['password'], $doc['phone']);
            if ($userStmt->execute()) {
                $userStatus = '✅ Created';
                $createdUsers++;
            } else {
                $userStatus = '❌ Error: ' . $conn->error;
            }
            $userStmt->close();
        } else {
            $userStmt = $conn->prepare("UPDATE users SET name = ?, email = ?, password = ?, phone = ? WHERE id = ?");
            $userStmt->bind_param("sssss", $doc['name'], $doc['email'], $doc['password'], $doc['phone'], $doc['user_id']);
            if ($userStmt->execute()) {
                $userStatus = 'ℹ️ Updated';
                $updatedUsers++;
            } else {
                $userStatus = '❌ Error: ' . $conn->error;
            }
            $userStmt->close();
        }
        
        // Create or update doctor
        $checkDoctor = $conn->prepare("SELECT id FROM doctors WHERE doctor_id = ? OR user_id = ?");
        $checkDoctor->bind_param("ss", $doc['doctor_id'], $doc['user_id']);
        $checkDoctor->execute();
        $doctorResult = $checkDoctor->get_result();
        $doctorExists = $doctorResult->num_rows > 0;
        $checkDoctor->close();
        
        $doctorStatus = '';
        if (!$doctorExists) {
            $doctorStmt = $conn->prepare("INSERT INTO doctors (id, user_id, doctor_id, specialty, experience, rating, availability, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            $doctorStmt->bind_param("sssssdss", $doctorId, $doc['user_id'], $doc['doctor_id'], $doc['specialty'], $doc['experience'], $doc['rating'], $doc['availability'], $doc['location']);
            if ($doctorStmt->execute()) {
                $doctorStatus = '✅ Created';
                $createdDoctors++;
            } else {
                $doctorStatus = '❌ Error: ' . $conn->error;
            }
            $doctorStmt->close();
        } else {
            $doctorStmt = $conn->prepare("UPDATE doctors SET doctor_id = ?, specialty = ?, experience = ?, rating = ?, availability = ?, location = ? WHERE user_id = ?");
            $doctorStmt->bind_param("sssdsss", $doc['doctor_id'], $doc['specialty'], $doc['experience'], $doc['rating'], $doc['availability'], $doc['location'], $doc['user_id']);
            if ($doctorStmt->execute()) {
                $doctorStatus = 'ℹ️ Updated';
                $updatedDoctors++;
            } else {
                $doctorStatus = '❌ Error: ' . $conn->error;
            }
            $doctorStmt->close();
        }
        
        echo "<tr>";
        echo "<td><strong>{$doc['doctor_id']}</strong></td>";
        echo "<td>{$doc['name']}</td>";
        echo "<td>{$doc['specialty']}</td>";
        echo "<td>$userStatus</td>";
        echo "<td>$doctorStatus</td>";
        echo "</tr>";
    }
    
    echo '</table>';
    
    // Step 6: Show all doctors in database
    echo '<h2>Step 6: All Saveetha Hospital doctors in database...</h2>';
    $allDoctors = $conn->query("
        SELECT 
            d.doctor_id AS 'Doctor ID',
            u.name AS 'Doctor Name',
            d.specialty AS 'Specialty',
            d.experience AS 'Experience',
            d.rating AS 'Rating',
            d.location AS 'Location'
        FROM doctors d
        JOIN users u ON d.user_id = u.id
        WHERE d.location = 'Saveetha Hospital'
        ORDER BY d.doctor_id
    ");
    
    if ($allDoctors && $allDoctors->num_rows > 0) {
        echo '<table border="1">';
        echo '<tr><th>Doctor ID</th><th>Name</th><th>Specialty</th><th>Experience</th><th>Rating</th><th>Location</th></tr>';
        while ($row = $allDoctors->fetch_assoc()) {
            echo '<tr>';
            echo '<td><strong>' . htmlspecialchars($row['Doctor ID']) . '</strong></td>';
            echo '<td>' . htmlspecialchars($row['Doctor Name']) . '</td>';
            echo '<td>' . htmlspecialchars($row['Specialty']) . '</td>';
            echo '<td>' . htmlspecialchars($row['Experience']) . '</td>';
            echo '<td>' . htmlspecialchars($row['Rating']) . '</td>';
            echo '<td>' . htmlspecialchars($row['Location']) . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    } else {
        echo '<div class="warning">⚠️ No doctors found in database</div>';
    }
    
    // Summary
    echo '<div class="success">';
    echo '<h2>✅ Setup Complete!</h2>';
    echo '<p><strong>Summary:</strong></p>';
    echo '<ul>';
    echo "<li>Database: <strong>$dbName</strong> - ✅ Created/Verified</li>";
    echo "<li>Users table - ✅ Created/Verified</li>";
    echo "<li>Doctors table - ✅ Created/Verified</li>";
    echo "<li>Users created: $createdUsers</li>";
    echo "<li>Users updated: $updatedUsers</li>";
    echo "<li>Doctors created: $createdDoctors</li>";
    echo "<li>Doctors updated: $updatedDoctors</li>";
    echo '</ul>';
    echo '<p><strong>Doctor Login Credentials (all use password: <code>password</code>):</strong></p>';
    echo '<ul>';
    echo '<li><strong>SAV001</strong> - Dr. Rajesh Kumar (Cardiologist)</li>';
    echo '<li><strong>SAV002</strong> - Dr. Priya Sharma (Pediatrician)</li>';
    echo '<li><strong>SAV003</strong> - Dr. Anil Patel (Orthopedic Surgeon)</li>';
    echo '<li><strong>SAV004</strong> - Dr. Meera Reddy (Gynecologist)</li>';
    echo '<li><strong>SAV005</strong> - Dr. Vikram Singh (Neurologist)</li>';
    echo '</ul>';
    echo '<p><strong>Next Steps:</strong></p>';
    echo '<ol>';
    echo '<li>✅ Database and doctors are now in phpMyAdmin!</li>';
    echo '<li>Restart Apache in XAMPP Control Panel</li>';
    echo '<li>Test API: <a href="test_doctors_api.php" target="_blank">http://localhost/AwareHealth/api/test_doctors_api.php</a></li>';
    echo '<li>View in phpMyAdmin: <a href="http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=awarehealth&table=doctors" target="_blank">View Doctors Table</a></li>';
    echo '<li>Rebuild app in Android Studio</li>';
    echo '<li>Test in app - Select Doctor screen should now show Saveetha Hospital doctors!</li>';
    echo '</ol>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">Make sure MySQL is running in XAMPP.</div>';
}

$conn->close();
ob_end_flush();
?>

    </div>
</body>
</html>

