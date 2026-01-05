-- =====================================================
-- Create chat_state table for AwareHealth Chatbot
-- =====================================================
-- Purpose: Persist chatbot conversation state across messages
-- Database: awarehealth
-- =====================================================

USE `awarehealth`;

-- Create chat_state table
CREATE TABLE IF NOT EXISTS `chat_state` (
    `chat_id` VARCHAR(255) PRIMARY KEY COMMENT 'Unique chat session identifier',
    `step` VARCHAR(50) NOT NULL DEFAULT 'ASK_DISEASE' COMMENT 'Current step in conversation flow',
    `disease_name` VARCHAR(255) DEFAULT NULL COMMENT 'Name of the selected disease',
    `prevention` TEXT DEFAULT NULL COMMENT 'Prevention tips for the disease',
    `food` TEXT DEFAULT NULL COMMENT 'Food recommendations for the disease',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    INDEX `idx_step` (`step`),
    INDEX `idx_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores chatbot conversation state to prevent reset';

-- =====================================================
-- Table created successfully!
-- =====================================================
-- To verify in phpMyAdmin:
-- 1. Go to: http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth
-- 2. Look for table: chat_state
-- =====================================================

