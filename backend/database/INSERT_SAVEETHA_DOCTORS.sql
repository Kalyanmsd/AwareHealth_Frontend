-- =====================================================
-- INSERT SAVEETHA HOSPITAL DOCTORS
-- This script creates doctor accounts for Saveetha Hospital
-- =====================================================

-- Note: Make sure users table and doctors table exist before running this

-- Doctor 1: Dr. Rajesh Kumar - Cardiologist
SET @doctor1_user_id = UUID();
SET @doctor1_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor1_user_id, 'Dr. Rajesh Kumar', 'rajesh.kumar@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543210', 'doctor');
-- Default password: password

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor1_id, @doctor1_user_id, 'Cardiology', '15 years', 4.8, 'Mon-Fri: 9 AM - 5 PM', 'Saveetha Hospital');

-- Doctor 2: Dr. Priya Sharma - Neurologist
SET @doctor2_user_id = UUID();
SET @doctor2_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor2_user_id, 'Dr. Priya Sharma', 'priya.sharma@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543211', 'doctor');

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor2_id, @doctor2_user_id, 'Neurology', '12 years', 4.7, 'Mon-Sat: 10 AM - 6 PM', 'Saveetha Hospital');

-- Doctor 3: Dr. Anil Patel - Orthopedic Surgeon
SET @doctor3_user_id = UUID();
SET @doctor3_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor3_user_id, 'Dr. Anil Patel', 'anil.patel@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543212', 'doctor');

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor3_id, @doctor3_user_id, 'Orthopedics', '18 years', 4.9, 'Mon-Fri: 8 AM - 4 PM', 'Saveetha Hospital');

-- Doctor 4: Dr. Meera Reddy - Pediatrician
SET @doctor4_user_id = UUID();
SET @doctor4_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor4_user_id, 'Dr. Meera Reddy', 'meera.reddy@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543213', 'doctor');

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor4_id, @doctor4_user_id, 'Pediatrics', '10 years', 4.6, 'Mon-Sat: 9 AM - 5 PM', 'Saveetha Hospital');

-- Doctor 5: Dr. Vikram Singh - General Physician
SET @doctor5_user_id = UUID();
SET @doctor5_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor5_user_id, 'Dr. Vikram Singh', 'vikram.singh@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543214', 'doctor');

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor5_id, @doctor5_user_id, 'General Medicine', '14 years', 4.5, 'Mon-Fri: 8 AM - 6 PM', 'Saveetha Hospital');

-- Doctor 6: Dr. Sunita Nair - Gynecologist
SET @doctor6_user_id = UUID();
SET @doctor6_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor6_user_id, 'Dr. Sunita Nair', 'sunita.nair@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543215', 'doctor');

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor6_id, @doctor6_user_id, 'Gynecology', '16 years', 4.8, 'Mon-Sat: 10 AM - 4 PM', 'Saveetha Hospital');

-- Doctor 7: Dr. Ramesh Iyer - Dermatologist
SET @doctor7_user_id = UUID();
SET @doctor7_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor7_user_id, 'Dr. Ramesh Iyer', 'ramesh.iyer@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543216', 'doctor');

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor7_id, @doctor7_user_id, 'Dermatology', '11 years', 4.7, 'Mon-Fri: 9 AM - 5 PM', 'Saveetha Hospital');

-- Doctor 8: Dr. Kavita Desai - Ophthalmologist
SET @doctor8_user_id = UUID();
SET @doctor8_id = UUID();

INSERT INTO users (id, name, email, password, phone, user_type) VALUES
(@doctor8_user_id, 'Dr. Kavita Desai', 'kavita.desai@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+91-9876543217', 'doctor');

INSERT INTO doctors (id, user_id, specialty, experience, rating, availability, location) VALUES
(@doctor8_id, @doctor8_user_id, 'Ophthalmology', '13 years', 4.6, 'Mon-Sat: 9 AM - 5 PM', 'Saveetha Hospital');

-- Show summary
SELECT 
    'Saveetha Hospital Doctors Inserted' AS status,
    COUNT(*) AS total_doctors
FROM doctors 
WHERE location = 'Saveetha Hospital';

-- Show all inserted doctors with their IDs for login
SELECT 
    d.id AS doctor_id,
    u.name AS doctor_name,
    d.specialty,
    d.location,
    'password' AS default_password
FROM doctors d
JOIN users u ON d.user_id = u.id
WHERE d.location = 'Saveetha Hospital'
ORDER BY u.name;

