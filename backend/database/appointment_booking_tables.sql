-- =====================================================
-- Appointment Booking System - Complete SQL
-- Copy and paste this into phpMyAdmin SQL tab
-- =====================================================

-- Step 1: Create doctors table (if not exists)
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

-- Step 2: Insert sample Saveetha Hospital doctors
INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `experience`, `available_days`, `available_time`) VALUES
('Dr. Rajesh Kumar', 'Cardiology', 'Saveetha Hospital', '15 years', 'Monday, Wednesday, Friday', '09:00 AM - 05:00 PM'),
('Dr. Priya Sharma', 'Pediatrics', 'Saveetha Hospital', '12 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'),
('Dr. Anil Patel', 'Orthopedics', 'Saveetha Hospital', '18 years', 'Monday, Wednesday, Friday', '08:00 AM - 04:00 PM'),
('Dr. Meera Reddy', 'Dermatology', 'Saveetha Hospital', '10 years', 'Tuesday, Thursday, Saturday', '11:00 AM - 07:00 PM'),
('Dr. Vikram Singh', 'Neurology', 'Saveetha Hospital', '20 years', 'Monday, Wednesday, Friday, Saturday', '09:00 AM - 05:00 PM'),
('Dr. Sunita Nair', 'Gynecology', 'Saveetha Hospital', '14 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'),
('Dr. Arjun Menon', 'General Medicine', 'Saveetha Hospital', '16 years', 'Monday to Saturday', '08:00 AM - 08:00 PM');

-- Step 3: Create appointments table (if not exists)
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

-- Step 4: Verify tables were created
SHOW TABLES LIKE 'doctors';
SHOW TABLES LIKE 'appointments';

-- Step 5: View inserted doctors
SELECT * FROM `doctors` WHERE `hospital` = 'Saveetha Hospital';

-- Step 6: View appointments (should be empty initially)
SELECT * FROM `appointments`;

