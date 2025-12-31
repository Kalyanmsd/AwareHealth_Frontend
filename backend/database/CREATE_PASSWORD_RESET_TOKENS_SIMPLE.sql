-- =====================================================
-- CREATE/FIX PASSWORD RESET TOKENS TABLE (SIMPLE VERSION)
-- Copy and paste this ENTIRE code into phpMyAdmin SQL tab
-- Select database: awarehealth
-- =====================================================

-- Create table if not exists
CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
  `id` VARCHAR(36) PRIMARY KEY,
  `user_id` VARCHAR(36) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) DEFAULT NULL,
  `otp` VARCHAR(6) DEFAULT NULL,
  `expires_at` TIMESTAMP NOT NULL,
  `otp_expires_at` DATETIME DEFAULT NULL,
  `used` TINYINT(1) DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_email` (`email`),
  INDEX `idx_token` (`token`),
  INDEX `idx_otp` (`otp`),
  INDEX `idx_expires` (`expires_at`),
  INDEX `idx_otp_expires` (`otp_expires_at`),
  INDEX `idx_used` (`used`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add otp column if it doesn't exist
ALTER TABLE `password_reset_tokens` 
ADD COLUMN IF NOT EXISTS `otp` VARCHAR(6) DEFAULT NULL AFTER `token`,
ADD INDEX IF NOT EXISTS `idx_otp` (`otp`);

-- Add otp_expires_at column if it doesn't exist
ALTER TABLE `password_reset_tokens` 
ADD COLUMN IF NOT EXISTS `otp_expires_at` DATETIME DEFAULT NULL AFTER `expires_at`,
ADD INDEX IF NOT EXISTS `idx_otp_expires` (`otp_expires_at`);

-- Show table structure
DESCRIBE `password_reset_tokens`;

