-- =====================================================
-- IMPORT THIS FILE INTO phpMyAdmin
-- =====================================================
-- Instructions:
-- 1. Open: http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=awarehealth
-- 2. Copy and paste ALL the SQL below
-- 3. Click "Go"
-- 4. Tables will be created and visible at:
--    http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth
-- =====================================================

USE `awarehealth`;

-- Drop tables if they exist (optional - comment out if you want to keep existing data)
-- DROP TABLE IF EXISTS `appointments`;
-- DROP TABLE IF EXISTS `doctors`;

-- Create doctors table
CREATE TABLE IF NOT EXISTS `doctors` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `specialization` VARCHAR(255) NOT NULL,
  `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
  `experience` VARCHAR(100) NOT NULL,
  `available_days` VARCHAR(255) NOT NULL,
  `available_time` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_hospital` (`hospital`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample Saveetha Hospital doctors
INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `experience`, `available_days`, `available_time`) VALUES
('Dr. Rajesh Kumar', 'Cardiology', 'Saveetha Hospital', '15 years', 'Monday, Wednesday, Friday', '09:00 AM - 05:00 PM'),
('Dr. Priya Sharma', 'Pediatrics', 'Saveetha Hospital', '12 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'),
('Dr. Anil Patel', 'Orthopedics', 'Saveetha Hospital', '18 years', 'Monday, Wednesday, Friday', '08:00 AM - 04:00 PM'),
('Dr. Meera Reddy', 'Dermatology', 'Saveetha Hospital', '10 years', 'Tuesday, Thursday, Saturday', '11:00 AM - 07:00 PM'),
('Dr. Vikram Singh', 'Neurology', 'Saveetha Hospital', '20 years', 'Monday, Wednesday, Friday, Saturday', '09:00 AM - 05:00 PM'),
('Dr. Sunita Nair', 'Gynecology', 'Saveetha Hospital', '14 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'),
('Dr. Arjun Menon', 'General Medicine', 'Saveetha Hospital', '16 years', 'Monday to Saturday', '08:00 AM - 08:00 PM')
ON DUPLICATE KEY UPDATE `name` = `name`;

-- Create appointments table
CREATE TABLE IF NOT EXISTS `appointments` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Verify tables were created
SELECT 'Tables created successfully!' as Status;
SELECT COUNT(*) as 'Total Doctors' FROM `doctors` WHERE `hospital` = 'Saveetha Hospital';
SELECT COUNT(*) as 'Total Appointments' FROM `appointments`;

