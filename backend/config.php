<?php
/**
 * Database Configuration for AwareHealth
 * Database Name: awarehealth
 */

// Database credentials
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', ''); // Empty password for XAMPP default
define('DB_NAME', 'awarehealth'); // Database name is lowercase 'awarehealth'

// Error reporting (disable in production)
error_reporting(E_ALL);
ini_set('display_errors', '0'); // Set to '1' for debugging
ini_set('log_errors', '1');
ini_set('error_log', __DIR__ . '/../logs/php_errors.log');

// Timezone
date_default_timezone_set('Asia/Kolkata');

?>

