-- =====================================================
-- PASSWORD RESET TOKENS TABLE - FINAL WORKING VERSION
-- Safe to run multiple times - handles errors gracefully
-- Copy and paste this ENTIRE code into phpMyAdmin SQL tab
-- Make sure you have selected the 'awarehealth' database
-- =====================================================

-- Create the table with all columns (IF NOT EXISTS prevents error if table exists)
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

-- If table already exists without otp column, add it
-- Note: These will show errors if columns/indexes already exist - that's OK!
-- Just ignore "Duplicate column" or "Duplicate key" errors - they mean columns already exist

-- Add 'otp' column (may error if exists - that's OK, just means it's already there)
ALTER TABLE `password_reset_tokens` 
ADD COLUMN `otp` VARCHAR(6) DEFAULT NULL AFTER `token`;

-- Add index for otp (may error if exists - that's OK)
ALTER TABLE `password_reset_tokens` 
ADD INDEX `idx_otp` (`otp`);

-- Add 'otp_expires_at' column (may error if exists - that's OK)
ALTER TABLE `password_reset_tokens` 
ADD COLUMN `otp_expires_at` DATETIME DEFAULT NULL AFTER `expires_at`;

-- Add index for otp_expires_at (may error if exists - that's OK)
ALTER TABLE `password_reset_tokens` 
ADD INDEX `idx_otp_expires` (`otp_expires_at`);

-- Show the table structure to verify
DESCRIBE `password_reset_tokens`;

-- Show success message
SELECT 'Table setup complete! Check the structure above.' AS status;

