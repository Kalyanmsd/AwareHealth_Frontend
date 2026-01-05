<?php
/**
 * AwareHealth Chatbot - Using chat_state Table
 * 
 * This implementation uses the chat_state table for robust state management
 * to ensure conversation state is preserved across all requests.
 */

session_start();
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

// Handle preflight requests
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Helper function to send response
function sendResponse($reply, $sessionId = null) {
    $response = [
        "success" => true,
        "response" => $reply,
        "conversationId" => $sessionId ?? ""
    ];
    echo json_encode($response);
    exit;
}

function sendError($message) {
    echo json_encode([
        "success" => false,
        "response" => $message,
        "conversationId" => ""
    ]);
    exit;
}

// DB connection
$conn = new mysqli("localhost", "root", "", "awarehealth");
if ($conn->connect_error) {
    sendError("Database connection error");
}

// Create chat_state table if it doesn't exist (matches the requested structure)
$conn->query("
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
");

// Clean up old sessions (older than 1 hour)
$conn->query("DELETE FROM chat_state WHERE updated_at < DATE_SUB(NOW(), INTERVAL 1 HOUR)");

// Read JSON input
$input = file_get_contents('php://input');
$data = json_decode($input, true);
$message = strtolower(trim($data['message'] ?? ""));

// Define yes words - these should NEVER be treated as disease names
$yesWords = ["yes", "provide", "ok", "okay", "sure", "fine"];

// Generate stable session ID
// Priority: conversationId > PHP session ID > IP-based ID
$conversationId = $data['conversationId'] ?? null;
$phpSessionId = session_id();

if ($conversationId) {
    $chatId = $conversationId;
} elseif ($phpSessionId && $phpSessionId !== '') {
    $chatId = $phpSessionId;
} else {
    // Generate stable ID from IP + User-Agent
    $ip = $_SERVER['REMOTE_ADDR'] ?? 'unknown';
    $userAgent = $_SERVER['HTTP_USER_AGENT'] ?? 'unknown';
    $chatId = md5($ip . $userAgent . 'awarehealth_chatbot');
}

// CRITICAL: Get current state from chat_state table FIRST
$stateQuery = $conn->prepare("SELECT step, disease_name, prevention, food FROM chat_state WHERE chat_id = ?");
$stateQuery->bind_param("s", $chatId);
$stateQuery->execute();
$stateResult = $stateQuery->get_result();
$currentState = $stateResult->fetch_assoc();

$currentStep = $currentState['step'] ?? "ASK_DISEASE";
$diseaseName = $currentState['disease_name'] ?? null;
$prevention = $currentState['prevention'] ?? null;
$food = $currentState['food'] ?? null;

// CRITICAL: If message is empty, handle based on current step
if ($message == "") {
    switch ($currentStep) {
        case "ASK_DISEASE":
            sendResponse("Please enter the disease name.", $chatId);
        case "ASK_PREVENTION":
            sendResponse("Please reply yes, provide, okay, sure, or fine to continue.", $chatId);
        case "ASK_DAYS":
            sendResponse("Please enter number of days.", $chatId);
        default:
            sendResponse("Please enter the disease name.", $chatId);
    }
}

/* ---------- CHAT FLOW ---------- */
switch ($currentStep) {

    /* 1️⃣ ASK DISEASE */
    case "ASK_DISEASE":

        // CRITICAL: Check if message is a yes word FIRST
        // If it is, it means state was lost - try to recover
        if (in_array($message, $yesWords)) {
            // Try to recover state from database
            if ($diseaseName && $prevention && $food) {
                // We have disease data - show prevention + food together
                $nextStep = "ASK_DAYS";
                $updateStmt = $conn->prepare("UPDATE chat_state SET step = ? WHERE chat_id = ?");
                $updateStmt->bind_param("ss", $nextStep, $chatId);
                $updateStmt->execute();
                
                sendResponse(
                    "Here are the prevention tips:\n" .
                    $prevention .
                    "\n\nFood Recommendations:\n" .
                    $food .
                    "\n\nFrom how many days are you suffering from " .
                    $diseaseName . "?",
                    $chatId
                );
            } else {
                // No disease data found - ask for disease name
                sendResponse("Please enter the disease name.", $chatId);
            }
        }

        // Search for disease in database
        $stmt = $conn->prepare(
            "SELECT name, symptoms, prevention, food 
             FROM diseases 
             WHERE LOWER(name) LIKE CONCAT('%', ?, '%')
             LIMIT 1"
        );
        $stmt->bind_param("s", $message);
        $stmt->execute();
        $res = $stmt->get_result();

        if ($res->num_rows == 0) {
            sendResponse("Disease not found. Please try another disease name.", $chatId);
        }

        $disease = $res->fetch_assoc();
        
        // Save to chat_state table with separate columns for prevention and food
        $nextStep = "ASK_PREVENTION";
        $saveStmt = $conn->prepare("
            INSERT INTO chat_state (chat_id, step, disease_name, prevention, food) 
            VALUES (?, ?, ?, ?, ?) 
            ON DUPLICATE KEY UPDATE 
                step = VALUES(step), 
                disease_name = VALUES(disease_name), 
                prevention = VALUES(prevention), 
                food = VALUES(food)
        ");
        $saveStmt->bind_param("sssss", $chatId, $nextStep, $disease['name'], $disease['prevention'], $disease['food']);
        $saveStmt->execute();
        
        // Also save to PHP session as backup
        $_SESSION['step'] = $nextStep;
        $_SESSION['disease'] = $disease;
        $_SESSION['chatbot_chat_id'] = $chatId;

        sendResponse(
            "Symptoms of " . $disease['name'] . ":\n" .
            $disease['symptoms'] .
            "\n\nCan I provide prevention tips?",
            $chatId
        );

    /* 2️⃣ ASK PREVENTION */
    case "ASK_PREVENTION":

        // CRITICAL: Check if message is a yes word
        if (!in_array($message, $yesWords)) {
            sendResponse("Please reply yes, provide, okay, sure, or fine to continue.", $chatId);
        }

        // Get disease data from chat_state table
        if (!$diseaseName || !$prevention || !$food) {
            // Try to get from current state query result
            if ($currentState) {
                $diseaseName = $currentState['disease_name'] ?? $diseaseName;
                $prevention = $currentState['prevention'] ?? $prevention;
                $food = $currentState['food'] ?? $food;
            }
            
            // If still missing, try PHP session
            if ((!$diseaseName || !$prevention || !$food) && isset($_SESSION['disease'])) {
                $diseaseFromSession = $_SESSION['disease'];
                $diseaseName = $diseaseName ?? $diseaseFromSession['name'] ?? null;
                $prevention = $prevention ?? $diseaseFromSession['prevention'] ?? null;
                $food = $food ?? $diseaseFromSession['food'] ?? null;
                
                // Sync to database
                if ($diseaseName && $prevention && $food) {
                    $syncStmt = $conn->prepare("UPDATE chat_state SET disease_name = ?, prevention = ?, food = ? WHERE chat_id = ?");
                    $syncStmt->bind_param("ssss", $diseaseName, $prevention, $food, $chatId);
                    $syncStmt->execute();
                }
            }
        }

        // If still no disease data, reset
        if (!$diseaseName || !$prevention || !$food) {
            // Reset state
            $resetStmt = $conn->prepare("DELETE FROM chat_state WHERE chat_id = ?");
            $resetStmt->bind_param("s", $chatId);
            $resetStmt->execute();
            unset($_SESSION['step']);
            unset($_SESSION['disease']);
            
            sendResponse("Session expired. Please enter the disease name again.", $chatId);
        }

        // Move to ASK_DAYS step and show prevention + food together in ONE message
        $nextStep = "ASK_DAYS";
        $updateStmt = $conn->prepare("UPDATE chat_state SET step = ? WHERE chat_id = ?");
        $updateStmt->bind_param("ss", $nextStep, $chatId);
        $updateStmt->execute();
        
        $_SESSION['step'] = $nextStep;

        sendResponse(
            "Here are the prevention tips:\n" .
            $prevention .
            "\n\nFood Recommendations:\n" .
            $food .
            "\n\nFrom how many days are you suffering from " .
            $diseaseName . "?",
            $chatId
        );

    /* 3️⃣ ASK DAYS */
    case "ASK_DAYS":

        if (!is_numeric($message)) {
            sendResponse("Please enter number of days.", $chatId);
        }

        $days = intval($message);
        
        // Get disease name for response
        $responseDiseaseName = $diseaseName ?? "this disease";
        if (!$responseDiseaseName && $currentState && $currentState['disease_name']) {
            $responseDiseaseName = $currentState['disease_name'];
        }

        // Clean up session
        $deleteStmt = $conn->prepare("DELETE FROM chat_state WHERE chat_id = ?");
        $deleteStmt->bind_param("s", $chatId);
        $deleteStmt->execute();
        
        // Clear PHP session
        unset($_SESSION['step']);
        unset($_SESSION['disease']);
        unset($_SESSION['chatbot_chat_id']);

        if ($days > 3) {
            sendResponse(
                "You have been suffering for more than 3 days.\n" .
                "Please consult Saveetha Hospital.\n" .
                "Appointment Booking Number:\n" .
                "+91 44 2681 0000",
                "" // No conversationId after completion
            );
        } else {
            sendResponse(
                "Avoid being alone and take care of your health.",
                "" // No conversationId after completion
            );
        }
}

// Fallback - should never reach here
sendError("An error occurred. Please try again.");
