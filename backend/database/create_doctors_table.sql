-- =====================================================
-- DOCTORS TABLE CREATION
-- Creates doctors table with required columns:
-- id, name, specialization, hospital, status
-- =====================================================

USE `awarehealth`;

-- Create doctors table if it doesn't exist
CREATE TABLE IF NOT EXISTS `doctors` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `specialization` VARCHAR(255) NOT NULL DEFAULT 'General Physician',
  `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
  `status` VARCHAR(50) NOT NULL DEFAULT 'Available',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_status` (`status`),
  INDEX `idx_specialization` (`specialization`),
  INDEX `idx_hospital` (`hospital`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- INSERT SAMPLE DOCTORS DATA
-- At least 3 doctors, with at least one status = 'Available'
-- =====================================================

-- Insert sample doctors
INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `status`) VALUES
('Dr. Rajesh Kumar', 'General Physician', 'Saveetha Hospital', 'Available'),
('Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'),
('Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available'),
('Dr. Meera Singh', 'Pediatrician', 'Saveetha Hospital', 'Busy'),
('Dr. Vikram Reddy', 'Orthopedic Surgeon', 'Saveetha Hospital', 'Available');

-- =====================================================
-- VERIFY DATA
-- =====================================================

-- Check all doctors
SELECT * FROM `doctors`;

-- Check only available doctors
SELECT * FROM `doctors` WHERE `status` = 'Available';

-- Count available doctors
SELECT COUNT(*) as available_count FROM `doctors` WHERE `status` = 'Available';

