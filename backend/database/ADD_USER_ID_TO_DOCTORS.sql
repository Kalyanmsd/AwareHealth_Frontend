-- Add user_id column to doctors table
-- Run this in phpMyAdmin SQL tab

ALTER TABLE `doctors` 
ADD COLUMN `user_id` VARCHAR(36) DEFAULT NULL AFTER `id`,
ADD INDEX `idx_user_id` (`user_id`);

-- If the above fails because column already exists, that's fine - it means it's already there

