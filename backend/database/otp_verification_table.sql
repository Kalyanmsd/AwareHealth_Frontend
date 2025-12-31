-- =====================================================
-- OTP Verification Table - Complete SQL
-- Copy and paste this into phpMyAdmin SQL tab
-- =====================================================

-- Step 1: Drop table if exists (optional - remove if you want to keep existing data)
DROP TABLE IF EXISTS `otp_verification`;

-- Step 2: Create otp_verification table
CREATE TABLE `otp_verification` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `otp` VARCHAR(6) NOT NULL,
  `expires_at` DATETIME NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_email` (`email`),
  INDEX `idx_expires_at` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 3: Verify table was created
SHOW TABLES LIKE 'otp_verification';

-- Step 4: Show table structure
DESCRIBE `otp_verification`;

-- =====================================================
-- TEST DATA (Optional - Remove after testing)
-- =====================================================

-- Insert test OTP
INSERT INTO `otp_verification` (`email`, `otp`, `expires_at`) 
VALUES ('test@example.com', '123456', DATE_ADD(NOW(), INTERVAL 5 MINUTE));

-- View test data
SELECT * FROM `otp_verification`;

-- Delete test data (run this after testing)
-- DELETE FROM `otp_verification` WHERE email = 'test@example.com';

