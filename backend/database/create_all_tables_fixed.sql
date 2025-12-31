-- =====================================================
-- COMPLETE DATABASE SETUP FOR AWAREHEALTH (FIXED VERSION)
-- Copy and paste this entire code into phpMyAdmin SQL tab
-- Tables are created in correct order to avoid foreign key errors
-- =====================================================

-- =====================================================
-- 1. USERS TABLE (no dependencies)
-- =====================================================
CREATE TABLE IF NOT EXISTS `users` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. DOCTORS TABLE (no dependencies)
-- =====================================================
CREATE TABLE IF NOT EXISTS `doctors` (
  `id` VARCHAR(36) PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `specialty` VARCHAR(255) NOT NULL,
  `experience` VARCHAR(100) DEFAULT NULL,
  `rating` DECIMAL(3,2) DEFAULT 0.00,
  `availability` VARCHAR(255) DEFAULT NULL,
  `location` VARCHAR(255) DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_specialty` (`specialty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. DISEASES TABLE (no dependencies - create before prevention_tips)
-- =====================================================
CREATE TABLE IF NOT EXISTS `diseases` (
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
-- 4. SYMPTOMS TABLE (no dependencies)
-- =====================================================
CREATE TABLE IF NOT EXISTS `symptoms` (
  `id` VARCHAR(36) PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `emoji` VARCHAR(10) DEFAULT '🤒',
  `definition` TEXT NOT NULL,
  `normal_range` VARCHAR(100) DEFAULT NULL,
  `fever_range` VARCHAR(100) DEFAULT NULL,
  `severity` TEXT DEFAULT NULL COMMENT 'JSON object with severity levels',
  `possible_causes` TEXT DEFAULT NULL COMMENT 'JSON array of possible causes',
  `what_to_do` TEXT DEFAULT NULL COMMENT 'JSON array of actions',
  `when_to_seek_help` TEXT DEFAULT NULL COMMENT 'JSON array of warning signs',
  `associated_symptoms` TEXT DEFAULT NULL COMMENT 'JSON array of related symptoms',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 5. HEALTH ARTICLES TABLE (no dependencies)
-- =====================================================
CREATE TABLE IF NOT EXISTS `health_articles` (
  `id` VARCHAR(36) PRIMARY KEY,
  `title` VARCHAR(255) NOT NULL,
  `category` VARCHAR(100) DEFAULT 'General',
  `summary` TEXT NOT NULL,
  `content` TEXT NOT NULL,
  `image_url` VARCHAR(500) DEFAULT NULL,
  `author` VARCHAR(255) DEFAULT 'AwareHealth Team',
  `published_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_featured` BOOLEAN DEFAULT FALSE,
  INDEX `idx_category` (`category`),
  INDEX `idx_published` (`published_at`),
  INDEX `idx_featured` (`is_featured`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 6. APPOINTMENTS TABLE (depends on users and doctors)
-- =====================================================
CREATE TABLE IF NOT EXISTS `appointments` (
  `id` VARCHAR(36) PRIMARY KEY,
  `patient_id` VARCHAR(36) NOT NULL,
  `doctor_id` VARCHAR(36) NOT NULL,
  `date` DATE NOT NULL,
  `time` TIME NOT NULL,
  `symptoms` TEXT DEFAULT NULL,
  `status` VARCHAR(50) DEFAULT 'pending',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_patient_id` (`patient_id`),
  INDEX `idx_doctor_id` (`doctor_id`),
  INDEX `idx_date` (`date`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add foreign keys after table creation (safer approach)
ALTER TABLE `appointments` 
  ADD CONSTRAINT `fk_appointments_patient` 
  FOREIGN KEY (`patient_id`) REFERENCES `users`(`id`) ON DELETE CASCADE;

ALTER TABLE `appointments` 
  ADD CONSTRAINT `fk_appointments_doctor` 
  FOREIGN KEY (`doctor_id`) REFERENCES `doctors`(`id`) ON DELETE CASCADE;

-- =====================================================
-- 7. PASSWORD RESET TOKENS TABLE (depends on users)
-- =====================================================
CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
  `id` VARCHAR(36) PRIMARY KEY,
  `user_id` VARCHAR(36) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) DEFAULT NULL,
  `otp` VARCHAR(6) DEFAULT NULL,
  `expires_at` TIMESTAMP NOT NULL,
  `otp_expires_at` TIMESTAMP DEFAULT NULL,
  `used` BOOLEAN DEFAULT FALSE,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_email` (`email`),
  INDEX `idx_token` (`token`),
  INDEX `idx_otp` (`otp`),
  INDEX `idx_expires` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add foreign key after table creation
ALTER TABLE `password_reset_tokens` 
  ADD CONSTRAINT `fk_password_reset_user` 
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE;

-- =====================================================
-- 8. PREVENTION TIPS TABLE (depends on diseases)
-- =====================================================
CREATE TABLE IF NOT EXISTS `prevention_tips` (
  `id` VARCHAR(36) PRIMARY KEY,
  `disease_id` VARCHAR(36) DEFAULT NULL,
  `category` VARCHAR(100) DEFAULT 'General' COMMENT 'Hygiene, Nutrition, Exercise, Mental Health, etc.',
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `emoji` VARCHAR(10) DEFAULT NULL,
  `priority` INT DEFAULT 0 COMMENT 'Higher number = higher priority',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_disease_id` (`disease_id`),
  INDEX `idx_category` (`category`),
  INDEX `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add foreign key after table creation
ALTER TABLE `prevention_tips` 
  ADD CONSTRAINT `fk_prevention_disease` 
  FOREIGN KEY (`disease_id`) REFERENCES `diseases`(`id`) ON DELETE CASCADE;

-- =====================================================
-- 9. VACCINATION REMINDERS TABLE (depends on users)
-- =====================================================
CREATE TABLE IF NOT EXISTS `vaccination_reminders` (
  `id` VARCHAR(36) PRIMARY KEY,
  `user_id` VARCHAR(36) DEFAULT NULL,
  `vaccine_name` VARCHAR(255) NOT NULL,
  `dose_number` INT DEFAULT 1,
  `scheduled_date` DATE NOT NULL,
  `completed_date` DATE DEFAULT NULL,
  `is_completed` BOOLEAN DEFAULT FALSE,
  `reminder_sent` BOOLEAN DEFAULT FALSE,
  `notes` TEXT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_scheduled_date` (`scheduled_date`),
  INDEX `idx_completed` (`is_completed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add foreign key after table creation
ALTER TABLE `vaccination_reminders` 
  ADD CONSTRAINT `fk_vaccination_user` 
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE;

-- =====================================================
-- SUCCESS MESSAGE
-- =====================================================
SELECT 'All tables created successfully!' AS message;

