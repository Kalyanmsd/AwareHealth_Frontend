-- Fix doctors table structure to support user_id linking
-- This script checks if user_id column exists and adds it if needed

-- Check if user_id column exists
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'doctors' 
    AND COLUMN_NAME = 'user_id'
);

-- If column doesn't exist, add it
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `doctors` ADD COLUMN `user_id` VARCHAR(36) DEFAULT NULL AFTER `id`, ADD INDEX `idx_user_id` (`user_id`)',
    'SELECT "user_id column already exists" AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- If name column doesn't exist in doctors table (if using user_id linking), we can keep it for backward compatibility
-- The table structure should be:
-- id, user_id, name (optional - can be retrieved from users table), specialty, experience, rating, availability, location

