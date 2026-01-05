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
-- DISEASE DETAILS TABLE
-- Stores detailed information about diseases including
-- preventions, symptoms, and suggested food
-- =====================================================

CREATE TABLE IF NOT EXISTS `disease_details` (
  `id` VARCHAR(36) PRIMARY KEY,
  `disease_id` VARCHAR(36) NOT NULL,
  `preventions` TEXT DEFAULT NULL COMMENT 'JSON array of prevention tips',
  `symptoms` TEXT DEFAULT NULL COMMENT 'JSON array of symptoms',
  `suggested_food` TEXT DEFAULT NULL COMMENT 'JSON array of suggested foods',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`disease_id`) REFERENCES `diseases`(`id`) ON DELETE CASCADE,
  INDEX `idx_disease_id` (`disease_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- DISEASES_LIST TABLE
-- Same structure as diseases table
-- =====================================================

CREATE TABLE IF NOT EXISTS `diseases_list` (
  `id` VARCHAR(36) PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `category` VARCHAR(100) NOT NULL DEFAULT 'General',
  `severity` VARCHAR(50) NOT NULL DEFAULT 'Mild',
  `emoji` VARCHAR(10) DEFAULT '🏥',
  `description` TEXT NOT NULL,
  `symptoms` TEXT NOT NULL COMMENT 'JSON array of symptoms',
  `causes` TEXT DEFAULT NULL COMMENT 'JSON array of causes',
  `prevention` TEXT DEFAULT NULL COMMENT 'JSON array of prevention tips',
  `treatment` TEXT DEFAULT NULL COMMENT 'JSON array of treatment options',
  `affected_population` VARCHAR(255) DEFAULT NULL,
  `duration` VARCHAR(100) DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_category` (`category`),
  INDEX `idx_severity` (`severity`),
  INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- SUCCESS MESSAGE
-- =====================================================
SELECT 'Disease details and diseases_list tables created successfully!' AS message;

