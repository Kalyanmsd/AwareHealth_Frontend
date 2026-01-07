-- =====================================================
-- Fix Appointment Booking System - Complete SQL
-- Ensures correct table structure for dynamic doctor routing
-- =====================================================

-- Step 1: Ensure doctors table has correct structure
CREATE TABLE IF NOT EXISTS `doctors` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `doctor_id` VARCHAR(50) UNIQUE,
  `doctor_name` VARCHAR(255) NOT NULL,
  `specialization` VARCHAR(255) NOT NULL,
  `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
  `user_id` VARCHAR(255),
  `specialty` VARCHAR(255),
  `location` VARCHAR(255),
  `experience` VARCHAR(100),
  `available_days` VARCHAR(255),
  `available_time` VARCHAR(255),
  `status` VARCHAR(50) DEFAULT 'Available',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_doctor_id` (`doctor_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_hospital` (`hospital`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 2: Ensure appointments table has correct structure
CREATE TABLE IF NOT EXISTS `appointments` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `appointment_id` VARCHAR(255) UNIQUE,
  `patient_id` VARCHAR(255),
  `patient_name` VARCHAR(255),
  `patient_email` VARCHAR(255),
  `user_email` VARCHAR(255),
  `doctor_id` INT NOT NULL,
  `doctor_name` VARCHAR(255) NOT NULL,
  `appointment_date` DATE NOT NULL,
  `appointment_time` TIME NOT NULL,
  `hospital` VARCHAR(255) DEFAULT 'Saveetha Hospital',
  `status` VARCHAR(50) DEFAULT 'pending',
  `symptoms` TEXT,
  `reason` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_patient_email` (`user_email`, `patient_email`),
  INDEX `idx_doctor_id` (`doctor_id`),
  INDEX `idx_appointment_date` (`appointment_date`),
  INDEX `idx_status` (`status`),
  FOREIGN KEY (`doctor_id`) REFERENCES `doctors`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 3: Add missing columns if they don't exist
ALTER TABLE `appointments` 
  ADD COLUMN IF NOT EXISTS `patient_name` VARCHAR(255) AFTER `patient_id`,
  ADD COLUMN IF NOT EXISTS `doctor_name` VARCHAR(255) AFTER `doctor_id`,
  ADD COLUMN IF NOT EXISTS `hospital` VARCHAR(255) DEFAULT 'Saveetha Hospital' AFTER `appointment_time`,
  ADD COLUMN IF NOT EXISTS `appointment_id` VARCHAR(255) UNIQUE AFTER `id`;

ALTER TABLE `doctors`
  ADD COLUMN IF NOT EXISTS `doctor_id` VARCHAR(50) UNIQUE AFTER `id`,
  ADD COLUMN IF NOT EXISTS `doctor_name` VARCHAR(255) AFTER `doctor_id`,
  ADD COLUMN IF NOT EXISTS `user_id` VARCHAR(255) AFTER `doctor_name`;

-- Step 4: Update existing appointments to have doctor_name if missing
UPDATE `appointments` a
INNER JOIN `doctors` d ON a.doctor_id = d.id
SET a.doctor_name = COALESCE(d.doctor_name, d.name, 'Dr. Doctor')
WHERE a.doctor_name IS NULL OR a.doctor_name = '';

-- Step 5: Verify structure
SELECT 'Doctors table structure:' as info;
DESCRIBE `doctors`;

SELECT 'Appointments table structure:' as info;
DESCRIBE `appointments`;

