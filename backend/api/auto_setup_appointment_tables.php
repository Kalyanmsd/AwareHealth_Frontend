<?php
/**
 * Auto Setup Appointment Tables - Creates tables and inserts sample doctors
 * GET: http://localhost/AwareHealth/api/auto_setup_appointment_tables.php
 */

header('Content-Type: text/html; charset=utf-8');
header('Access-Control-Allow-Origin: *');

require_once __DIR__ . '/../config.php';

// Ensure we're using the correct database
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

// Verify database connection and database name
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Explicitly select the database
if (!$conn->select_db(DB_NAME)) {
    die("Failed to select database: " . DB_NAME);
}

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$conn->set_charset("utf8mb4");

?>
<!DOCTYPE html>
<html>
<head>
    <title>Appointment Booking Tables Setup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .success { color: #34A853; background: #E8F5E9; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .error { color: #EA4335; background: #FFEBEE; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .info { color: #1976D2; background: #E3F2FD; padding: 10px; border-radius: 4px; margin: 10px 0; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f5f5f5; font-weight: bold; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 4px; overflow-x: auto; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏥 Appointment Booking Tables Setup</h1>
        
<?php

try {
    // Step 1: Create doctors table
    echo '<h2>Step 1: Creating doctors table...</h2>';
    
    $createDoctorsTable = "CREATE TABLE IF NOT EXISTS `doctors` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `name` VARCHAR(255) NOT NULL,
      `specialization` VARCHAR(255) NOT NULL,
      `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
      `experience` VARCHAR(100) NOT NULL,
      `available_days` VARCHAR(255) NOT NULL,
      `available_time` VARCHAR(255) NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_hospital` (`hospital`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createDoctorsTable)) {
        echo '<div class="success">✅ Doctors table created or verified successfully!</div>';
    } else {
        throw new Exception("Failed to create doctors table: " . $conn->error);
    }
    
    // Step 2: Insert sample doctors
    echo '<h2>Step 2: Inserting sample Saveetha Hospital doctors...</h2>';
    
    // Delete existing doctors first (optional - comment out if you want to keep existing)
    $conn->query("DELETE FROM doctors WHERE hospital = 'Saveetha Hospital'");
    
    $doctors = [
        ['Dr. Rajesh Kumar', 'Cardiology', 'Saveetha Hospital', '15 years', 'Monday, Wednesday, Friday', '09:00 AM - 05:00 PM'],
        ['Dr. Priya Sharma', 'Pediatrics', 'Saveetha Hospital', '12 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'],
        ['Dr. Anil Patel', 'Orthopedics', 'Saveetha Hospital', '18 years', 'Monday, Wednesday, Friday', '08:00 AM - 04:00 PM'],
        ['Dr. Meera Reddy', 'Dermatology', 'Saveetha Hospital', '10 years', 'Tuesday, Thursday, Saturday', '11:00 AM - 07:00 PM'],
        ['Dr. Vikram Singh', 'Neurology', 'Saveetha Hospital', '20 years', 'Monday, Wednesday, Friday, Saturday', '09:00 AM - 05:00 PM'],
        ['Dr. Sunita Nair', 'Gynecology', 'Saveetha Hospital', '14 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'],
        ['Dr. Arjun Menon', 'General Medicine', 'Saveetha Hospital', '16 years', 'Monday to Saturday', '08:00 AM - 08:00 PM']
    ];
    
    $insertStmt = $conn->prepare("INSERT INTO doctors (name, specialization, hospital, experience, available_days, available_time) VALUES (?, ?, ?, ?, ?, ?)");
    $insertedCount = 0;
    
    foreach ($doctors as $doctor) {
        $insertStmt->bind_param("ssssss", $doctor[0], $doctor[1], $doctor[2], $doctor[3], $doctor[4], $doctor[5]);
        if ($insertStmt->execute()) {
            $insertedCount++;
        }
    }
    $insertStmt->close();
    
    echo '<div class="success">✅ Inserted ' . $insertedCount . ' doctors successfully!</div>';
    
    // Step 3: Create appointments table
    echo '<h2>Step 3: Creating appointments table...</h2>';
    
    $createAppointmentsTable = "CREATE TABLE IF NOT EXISTS `appointments` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `user_email` VARCHAR(255) NOT NULL,
      `doctor_id` INT NOT NULL,
      `appointment_date` DATE NOT NULL,
      `appointment_time` TIME NOT NULL,
      `status` VARCHAR(50) DEFAULT 'Pending',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_user_email` (`user_email`),
      INDEX `idx_doctor_id` (`doctor_id`),
      INDEX `idx_appointment_date` (`appointment_date`),
      FOREIGN KEY (`doctor_id`) REFERENCES `doctors`(`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    
    if ($conn->query($createAppointmentsTable)) {
        echo '<div class="success">✅ Appointments table created or verified successfully!</div>';
    } else {
        throw new Exception("Failed to create appointments table: " . $conn->error);
    }
    
    // Step 4: Display doctors
    echo '<h2>Step 4: Current Saveetha Hospital Doctors</h2>';
    $doctorsResult = $conn->query("SELECT * FROM doctors WHERE hospital = 'Saveetha Hospital' ORDER BY name ASC");
    
    if ($doctorsResult && $doctorsResult->num_rows > 0) {
        echo '<table border="1">';
        echo '<tr><th>ID</th><th>Name</th><th>Specialization</th><th>Hospital</th><th>Experience</th><th>Available Days</th><th>Available Time</th></tr>';
        while ($row = $doctorsResult->fetch_assoc()) {
            echo '<tr>';
            echo '<td>' . $row['id'] . '</td>';
            echo '<td><strong>' . htmlspecialchars($row['name']) . '</strong></td>';
            echo '<td>' . htmlspecialchars($row['specialization']) . '</td>';
            echo '<td>' . htmlspecialchars($row['hospital']) . '</td>';
            echo '<td>' . htmlspecialchars($row['experience']) . '</td>';
            echo '<td>' . htmlspecialchars($row['available_days']) . '</td>';
            echo '<td>' . htmlspecialchars($row['available_time']) . '</td>';
            echo '</tr>';
        }
        echo '</table>';
    }
    
    // Step 5: Display appointments count
    echo '<h2>Step 5: Appointments Status</h2>';
    $appointmentsCount = $conn->query("SELECT COUNT(*) as total FROM appointments");
    $countRow = $appointmentsCount->fetch_assoc();
    echo '<div class="info">📊 Total appointments in database: ' . $countRow['total'] . '</div>';
    
    // Summary
    echo '<div class="success">';
    echo '<h2>✅ Setup Complete!</h2>';
    echo '<p><strong>Summary:</strong></p>';
    echo '<ul>';
    echo '<li>✅ Doctors table created</li>';
    echo '<li>✅ ' . $insertedCount . ' Saveetha Hospital doctors inserted</li>';
    echo '<li>✅ Appointments table created</li>';
    echo '<li>✅ All indexes created</li>';
    echo '</ul>';
    echo '<p><strong>✅ Tables are now visible in phpMyAdmin!</strong></p>';
    echo '<p><strong>📊 View Tables in phpMyAdmin:</strong></p>';
    echo '<p><a href="http://localhost/phpmyadmin/index.php?route=/database/structure&db=' . DB_NAME . '" target="_blank" style="background: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; display: inline-block; margin: 10px 0;">🔍 View in phpMyAdmin</a></p>';
    echo '<p><strong>🧪 Test Endpoints:</strong></p>';
    echo '<ol>';
    echo '<li><a href="get_doctors.php" target="_blank">Get Doctors API</a></li>';
    echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=' . DB_NAME . '&table=doctors" target="_blank">View Doctors Table</a></li>';
    echo '<li><a href="http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=' . DB_NAME . '&table=appointments" target="_blank">View Appointments Table</a></li>';
    echo '</ol>';
    echo '<div class="info">';
    echo '<p><strong>✅ Database Name:</strong> ' . DB_NAME . '</p>';
    echo '<p><strong>✅ Tables Created:</strong> doctors, appointments</p>';
    echo '<p><strong>✅ Doctors Inserted:</strong> ' . $insertedCount . '</p>';
    echo '</div>';
    echo '</div>';
    
} catch (Exception $e) {
    echo '<div class="error">❌ Error: ' . htmlspecialchars($e->getMessage()) . '</div>';
    echo '<div class="info">Please ensure MySQL is running in XAMPP Control Panel.</div>';
}

$conn->close();
?>

    </div>
</body>
</html>

