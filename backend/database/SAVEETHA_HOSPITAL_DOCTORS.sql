-- =====================================================
-- SAVEETHA HOSPITAL DOCTORS SETUP
-- This script creates sample doctors for Saveetha Hospital
-- Run this in phpMyAdmin after creating users and doctors tables
-- =====================================================

-- First, ensure doctors table has location column
ALTER TABLE `doctors` 
ADD COLUMN IF NOT EXISTS `location` VARCHAR(255) DEFAULT 'Saveetha Hospital' AFTER `availability`,
ADD COLUMN IF NOT EXISTS `doctor_id` VARCHAR(50) UNIQUE DEFAULT NULL COMMENT 'Unique doctor ID for login' AFTER `id`;

-- Add index for doctor_id
ALTER TABLE `doctors` 
ADD INDEX IF NOT EXISTS `idx_doctor_id` (`doctor_id`);

-- Sample Saveetha Hospital Doctors
-- Note: You need to create users first, then link them to doctors

-- Example: Create a user for Dr. Rajesh Kumar (Cardiologist)
-- Replace 'USER_ID_1' with actual user ID from users table
-- Or create users first, then run this script

-- Sample SQL to create complete doctor records:
-- This assumes you have user accounts created

-- Doctor 1: Dr. Rajesh Kumar - Cardiologist
-- First create user (if not exists):
INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `user_type`, `created_at`) 
VALUES 
('DOC001-USER', 'Dr. Rajesh Kumar', 'rajesh.kumar@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '9876543210', 'doctor', NOW())
ON DUPLICATE KEY UPDATE `name` = 'Dr. Rajesh Kumar';

-- Then create doctor record:
INSERT INTO `doctors` (`id`, `user_id`, `doctor_id`, `specialty`, `experience`, `rating`, `availability`, `location`, `created_at`) 
VALUES 
('DOC001', 'DOC001-USER', 'SAV001', 'Cardiology', '15 years', 4.8, 'Mon-Fri: 9 AM - 5 PM', 'Saveetha Hospital', NOW())
ON DUPLICATE KEY UPDATE `location` = 'Saveetha Hospital';

-- Doctor 2: Dr. Priya Sharma - Pediatrician
INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `user_type`, `created_at`) 
VALUES 
('DOC002-USER', 'Dr. Priya Sharma', 'priya.sharma@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '9876543211', 'doctor', NOW())
ON DUPLICATE KEY UPDATE `name` = 'Dr. Priya Sharma';

INSERT INTO `doctors` (`id`, `user_id`, `doctor_id`, `specialty`, `experience`, `rating`, `availability`, `location`, `created_at`) 
VALUES 
('DOC002', 'DOC002-USER', 'SAV002', 'Pediatrics', '12 years', 4.7, 'Mon-Sat: 10 AM - 6 PM', 'Saveetha Hospital', NOW())
ON DUPLICATE KEY UPDATE `location` = 'Saveetha Hospital';

-- Doctor 3: Dr. Anil Patel - Orthopedic Surgeon
INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `user_type`, `created_at`) 
VALUES 
('DOC003-USER', 'Dr. Anil Patel', 'anil.patel@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '9876543212', 'doctor', NOW())
ON DUPLICATE KEY UPDATE `name` = 'Dr. Anil Patel';

INSERT INTO `doctors` (`id`, `user_id`, `doctor_id`, `specialty`, `experience`, `rating`, `availability`, `location`, `created_at`) 
VALUES 
('DOC003', 'DOC003-USER', 'SAV003', 'Orthopedics', '18 years', 4.9, 'Mon-Fri: 8 AM - 4 PM', 'Saveetha Hospital', NOW())
ON DUPLICATE KEY UPDATE `location` = 'Saveetha Hospital';

-- Doctor 4: Dr. Meera Reddy - Gynecologist
INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `user_type`, `created_at`) 
VALUES 
('DOC004-USER', 'Dr. Meera Reddy', 'meera.reddy@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '9876543213', 'doctor', NOW())
ON DUPLICATE KEY UPDATE `name` = 'Dr. Meera Reddy';

INSERT INTO `doctors` (`id`, `user_id`, `doctor_id`, `specialty`, `experience`, `rating`, `availability`, `location`, `created_at`) 
VALUES 
('DOC004', 'DOC004-USER', 'SAV004', 'Gynecology', '14 years', 4.6, 'Mon-Sat: 9 AM - 5 PM', 'Saveetha Hospital', NOW())
ON DUPLICATE KEY UPDATE `location` = 'Saveetha Hospital';

-- Doctor 5: Dr. Vikram Singh - Neurologist
INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `user_type`, `created_at`) 
VALUES 
('DOC005-USER', 'Dr. Vikram Singh', 'vikram.singh@saveetha.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '9876543214', 'doctor', NOW())
ON DUPLICATE KEY UPDATE `name` = 'Dr. Vikram Singh';

INSERT INTO `doctors` (`id`, `user_id`, `doctor_id`, `specialty`, `experience`, `rating`, `availability`, `location`, `created_at`) 
VALUES 
('DOC005', 'DOC005-USER', 'SAV005', 'Neurology', '16 years', 4.8, 'Mon-Fri: 10 AM - 6 PM', 'Saveetha Hospital', NOW())
ON DUPLICATE KEY UPDATE `location` = 'Saveetha Hospital';

-- Show success message
SELECT 'Saveetha Hospital doctors created successfully!' AS message;

-- Show all Saveetha Hospital doctors
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
ORDER BY u.name;

-- Note: Default password for all doctors is 'password' (hashed)
-- Doctor IDs for login: SAV001, SAV002, SAV003, SAV004, SAV005

