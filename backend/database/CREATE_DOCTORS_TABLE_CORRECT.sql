-- Correct doctors table structure that works with user_id linking
-- This is the recommended structure

-- Drop existing doctors table if you want to recreate it (BACKUP FIRST!)
-- DROP TABLE IF EXISTS `doctors`;

CREATE TABLE IF NOT EXISTS `doctors` (
  `id` VARCHAR(36) PRIMARY KEY,
  `user_id` VARCHAR(36) NOT NULL,
  `specialty` VARCHAR(255) NOT NULL DEFAULT 'General',
  `experience` VARCHAR(100) DEFAULT NULL,
  `rating` DECIMAL(3,2) DEFAULT 0.00,
  `availability` VARCHAR(255) DEFAULT NULL,
  `location` VARCHAR(255) DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_specialty` (`specialty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Note: doctor name is stored in users table, linked via user_id
-- To get doctor name: JOIN doctors d ON d.user_id = users.id

