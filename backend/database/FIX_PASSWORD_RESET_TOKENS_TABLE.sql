-- =====================================================
-- FIX PASSWORD RESET TOKENS TABLE
-- Run this in phpMyAdmin SQL tab
-- =====================================================

-- Drop table if exists (optional - only if you want to recreate)
-- DROP TABLE IF EXISTS `password_reset_tokens`;

-- Create password_reset_tokens table with all required columns
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

-- Check if columns exist, if not add them
SET @dbname = DATABASE();
SET @tablename = 'password_reset_tokens';
SET @columnname = 'otp';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(6) DEFAULT NULL AFTER token, ADD INDEX idx_otp (otp)')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Check if otp_expires_at column exists, if not add it
SET @columnname = 'otp_expires_at';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' DATETIME DEFAULT NULL AFTER expires_at, ADD INDEX idx_otp_expires (otp_expires_at)')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Verify table structure
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'password_reset_tokens'
ORDER BY ORDINAL_POSITION;

