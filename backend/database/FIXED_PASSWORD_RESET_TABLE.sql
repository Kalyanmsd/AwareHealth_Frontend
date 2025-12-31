-- =====================================================
-- FIXED PASSWORD RESET TOKENS TABLE
-- Fix for: Invalid default value for 'otp_expires_at'
-- =====================================================

-- Drop table if exists to recreate it
DROP TABLE IF EXISTS `password_reset_tokens`;

-- Create table with fixed TIMESTAMP field
CREATE TABLE `password_reset_tokens` (
  `id` VARCHAR(36) PRIMARY KEY,
  `user_id` VARCHAR(36) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) DEFAULT NULL,
  `otp` VARCHAR(6) DEFAULT NULL,
  `expires_at` TIMESTAMP NOT NULL,
  `otp_expires_at` TIMESTAMP NULL,
  `used` BOOLEAN DEFAULT FALSE,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_email` (`email`),
  INDEX `idx_token` (`token`),
  INDEX `idx_otp` (`otp`),
  INDEX `idx_expires` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT 'Password reset tokens table created successfully!' AS message;

