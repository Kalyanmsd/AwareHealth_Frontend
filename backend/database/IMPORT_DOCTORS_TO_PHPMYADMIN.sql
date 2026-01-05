-- =====================================================
-- IMPORT THIS SQL FILE TO PHPMYADMIN
-- Database: awarehealth
-- Path: http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth
-- =====================================================
-- 
-- INSTRUCTIONS:
-- 1. Open phpMyAdmin: http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth
-- 2. Click on "SQL" tab
-- 3. Copy and paste the SQL below
-- 4. Click "Go" to execute
-- =====================================================

USE `awarehealth`;

-- =====================================================
-- DOCTORS TABLE CREATION
-- Creates doctors table with required columns:
-- id, name, specialization, hospital, status
-- =====================================================

-- Drop existing table if you want to recreate it (BACKUP FIRST!)
-- DROP TABLE IF EXISTS `doctors`;

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

-- Insert sample doctors (only if table is empty)
INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `status`) 
SELECT * FROM (
  SELECT 'Dr. Rajesh Kumar' as `name`, 'General Physician' as `specialization`, 'Saveetha Hospital' as `hospital`, 'Available' as `status`
  UNION ALL
  SELECT 'Dr. Priya Sharma', 'Cardiologist', 'Saveetha Hospital', 'Available'
  UNION ALL
  SELECT 'Dr. Anil Patel', 'Dermatologist', 'Saveetha Hospital', 'Available'
  UNION ALL
  SELECT 'Dr. Meera Singh', 'Pediatrician', 'Saveetha Hospital', 'Busy'
  UNION ALL
  SELECT 'Dr. Vikram Reddy', 'Orthopedic Surgeon', 'Saveetha Hospital', 'Available'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `doctors`);

-- =====================================================
-- SUCCESS MESSAGE
-- =====================================================
SELECT 'Doctors table created and sample data inserted successfully!' AS message;

