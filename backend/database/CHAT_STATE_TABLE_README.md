# chat_state Table Setup

## Overview
The `chat_state` table stores chatbot conversation state to prevent the bot from resetting after each message.

## Table Structure

| Column | Type | Description |
|--------|------|-------------|
| `chat_id` | VARCHAR(255) | Primary key - Unique chat session identifier |
| `step` | VARCHAR(50) | Current step in conversation flow (default: 'ASK_DISEASE') |
| `disease_name` | VARCHAR(255) | Name of the selected disease |
| `prevention` | TEXT | Prevention tips for the disease |
| `food` | TEXT | Food recommendations for the disease |
| `updated_at` | TIMESTAMP | Auto-updates on change |

## Setup Methods

### Method 1: Automated PHP Script (Recommended)
1. Open your browser
2. Navigate to: `http://localhost/AwareHealth/api/create_chat_state_table.php`
3. The script will automatically create the table and show verification

### Method 2: phpMyAdmin SQL
1. Open phpMyAdmin: `http://localhost/phpmyadmin/index.php?route=/sql&db=awarehealth`
2. Copy the SQL from `backend/database/create_chat_state_table.sql`
3. Paste and click "Go"

### Method 3: Direct SQL File Import
1. Open phpMyAdmin
2. Select database: `awarehealth`
3. Go to Import tab
4. Choose file: `backend/database/create_chat_state_table.sql`
5. Click "Go"

## Verification

After setup, verify the table exists:
1. Go to: `http://localhost/phpmyadmin/index.php?route=/database/structure&db=awarehealth`
2. Look for table: **chat_state**
3. Click on the table to view its structure

## Usage

The `chatbot.php` file will automatically use this table to:
- Store conversation state between messages
- Prevent the bot from asking for disease name again after "provide"
- Maintain conversation flow across multiple requests

## Notes

- Table is created only if it doesn't exist (safe to run multiple times)
- Compatible with PHP + MySQL (XAMPP)
- No other tables are modified
- Existing database schema remains unchanged

