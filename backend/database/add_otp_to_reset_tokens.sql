-- Add OTP columns to password_reset_tokens table
-- Run this in phpMyAdmin if table already exists

ALTER TABLE `password_reset_tokens` 
ADD COLUMN `otp` VARCHAR(6) DEFAULT NULL AFTER `token`,
ADD COLUMN `otp_expires_at` DATETIME DEFAULT NULL AFTER `expires_at`,
ADD INDEX `idx_otp` (`otp`);

-- OR if creating new table, use this complete version:

CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
  `id` VARCHAR(36) NOT NULL PRIMARY KEY,
  `user_id` VARCHAR(36) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) DEFAULT NULL,
  `otp` VARCHAR(6) DEFAULT NULL,
  `expires_at` DATETIME NOT NULL,
  `otp_expires_at` DATETIME DEFAULT NULL,
  `used` TINYINT(1) DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_token` (`token`),
  INDEX `idx_otp` (`otp`),
  INDEX `idx_email` (`email`),
  INDEX `idx_user_id` (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

