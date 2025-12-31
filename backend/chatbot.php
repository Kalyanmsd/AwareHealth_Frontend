<?php
/**
 * AwareHealth Chatbot API Endpoint
 * 
 * This endpoint handles chatbot requests using OpenAI Chat Completions API.
 * Endpoint: POST /chatbot.php
 * 
 * Required JSON input:
 * {
 *   "message": "user message here",
 *   "days": 3
 * }
 */

// Include configuration file
require_once 'config.php';

// Set response headers
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

// Only allow POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405); // Method Not Allowed
    echo json_encode([
        'success' => false,
        'error' => 'Only POST method is allowed'
    ]);
    exit;
}

// Get JSON input from request body
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Validate input
if (empty($data)) {
    http_response_code(400); // Bad Request
    echo json_encode([
        'success' => false,
        'error' => 'Invalid JSON input'
    ]);
    exit;
}

// Validate required fields
if (!isset($data['message']) || empty(trim($data['message']))) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'Message field is required and cannot be empty'
    ]);
    exit;
}

if (!isset($data['days']) || !is_numeric($data['days'])) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'Days field is required and must be a number'
    ]);
    exit;
}

// Sanitize input
$userMessage = trim($data['message']);
$days = (int)$data['days'];

// Validate message length
if (strlen($userMessage) > MAX_MESSAGE_LENGTH) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'Message is too long. Maximum length is ' . MAX_MESSAGE_LENGTH . ' characters'
    ]);
    exit;
}

// Validate API key
if (OPENAI_API_KEY === 'your-openai-api-key-here') {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'OpenAI API key not configured. Please set your API key in config.php'
    ]);
    exit;
}

// Build the system prompt with healthcare safety rules
$systemPrompt = "You are a healthcare assistant chatbot for AwareHealth. Your role is to provide general health guidance, NOT medical diagnosis.

IMPORTANT RULES:
1. This is NOT a medical diagnosis service
2. Do NOT provide prescriptions or specific medicine dosages
3. Do NOT claim to diagnose medical conditions
4. Provide general health guidance and advice only
5. Always recommend consulting with healthcare professionals for serious concerns

If the user has been experiencing symptoms for 3 or more days, you MUST:
- Strongly advise visiting a nearby/local hospital or clinic
- Suggest booking an appointment with a doctor
- Provide a clear warning that professional medical evaluation is needed
- Emphasize the importance of seeking timely medical care

Keep responses concise, helpful, and empathetic. Remember: you are providing guidance, not medical advice.";

// Build user message with context
if ($days >= 3) {
    $userMessageWithContext = $userMessage . "\n\n[User has been experiencing symptoms for " . $days . " days]";
} else {
    $userMessageWithContext = $userMessage . "\n\n[User has been experiencing symptoms for " . $days . " day(s)]";
}

// Prepare OpenAI API request
$openaiData = [
    'model' => OPENAI_MODEL,
    'messages' => [
        [
            'role' => 'system',
            'content' => $systemPrompt
        ],
        [
            'role' => 'user',
            'content' => $userMessageWithContext
        ]
    ],
    'max_tokens' => 300, // Limit response length
    'temperature' => 0.7
];

// Make request to OpenAI API using cURL
$ch = curl_init(OPENAI_API_URL);

curl_setopt_array($ch, [
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_POST => true,
    CURLOPT_POSTFIELDS => json_encode($openaiData),
    CURLOPT_HTTPHEADER => [
        'Content-Type: application/json',
        'Authorization: Bearer ' . OPENAI_API_KEY
    ],
    CURLOPT_TIMEOUT => 30 // 30 second timeout
]);

$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
$curlError = curl_error($ch);
curl_close($ch);

// Handle cURL errors
if ($response === false || !empty($curlError)) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'Failed to connect to OpenAI API: ' . $curlError
    ]);
    exit;
}

// Parse OpenAI response
$openaiResponse = json_decode($response, true);

// Handle API errors
if ($httpCode !== 200) {
    $errorMessage = isset($openaiResponse['error']['message']) 
        ? $openaiResponse['error']['message'] 
        : 'Unknown error from OpenAI API';
    
    http_response_code($httpCode >= 400 && $httpCode < 500 ? $httpCode : 500);
    echo json_encode([
        'success' => false,
        'error' => 'OpenAI API error: ' . $errorMessage
    ]);
    exit;
}

// Extract chatbot response
if (!isset($openaiResponse['choices'][0]['message']['content'])) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'Invalid response format from OpenAI API'
    ]);
    exit;
}

$chatbotResponse = trim($openaiResponse['choices'][0]['message']['content']);

// Enforce hospital visit recommendation if days >= 3
if ($days >= 3) {
    $hospitalWarning = "\n\n⚠️ IMPORTANT: Since your symptoms have persisted for " . $days . " days, we strongly recommend:\n" .
                       "1. Visiting a nearby hospital or clinic for professional medical evaluation\n" .
                       "2. Booking an appointment with a healthcare provider\n" .
                       "3. Seeking timely medical attention for proper diagnosis and treatment\n\n" .
                       "This chatbot provides general guidance only and cannot replace professional medical care.";
    
    $chatbotResponse = $chatbotResponse . $hospitalWarning;
}

// Limit response length
if (strlen($chatbotResponse) > MAX_RESPONSE_LENGTH) {
    $chatbotResponse = substr($chatbotResponse, 0, MAX_RESPONSE_LENGTH) . '...';
}

// Return successful response
http_response_code(200);
echo json_encode([
    'success' => true,
    'response' => $chatbotResponse,
    'days' => $days,
    'recommends_hospital' => $days >= 3
], JSON_PRETTY_PRINT);

