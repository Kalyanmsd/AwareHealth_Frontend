<?php
/**
 * Quick script to create chat_state table
 * Run this once to ensure the table exists in phpMyAdmin
 * Access via: http://localhost/AwareHealth/backend/create_chat_state.php
 */

header('Content-Type: text/html; charset=utf-8');

$conn = new mysqli("localhost", "root", "", "awarehealth");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Create chat_state table
$sql = "
    CREATE TABLE IF NOT EXISTS `chat_state` (
        `chat_id` VARCHAR(255) PRIMARY KEY COMMENT 'Unique chat session identifier',
        `step` VARCHAR(50) NOT NULL DEFAULT 'ASK_DISEASE' COMMENT 'Current step in conversation flow',
        `disease_name` VARCHAR(255) DEFAULT NULL COMMENT 'Name of the selected disease',
        `prevention` TEXT DEFAULT NULL COMMENT 'Prevention tips for the disease',
        `food` TEXT DEFAULT NULL COMMENT 'Food recommendations for the disease',
        `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
        INDEX `idx_step` (`step`),
        INDEX `idx_updated_at` (`updated_at`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores chatbot conversation state to prevent reset'
";

if ($conn->query($sql) === TRUE) {
    echo "<h2>✅ Success!</h2>";
    echo "<p>The <strong>chat_state</strong> table has been created successfully.</p>";
    echo "<p>You can now see it in phpMyAdmin at:</p>";
    echo "<p><a href='http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth' target='_blank'>View Database Structure</a></p>";
    
    // Verify table exists
    $result = $conn->query("SHOW TABLES LIKE 'chat_state'");
    if ($result && $result->num_rows > 0) {
        echo "<p style='color: green;'>✅ Table verified: chat_state exists in database awarehealth</p>";
    }
} else {
    echo "<h2>❌ Error</h2>";
    echo "<p>Error creating table: " . $conn->error . "</p>";
}

$conn->close();
?>

