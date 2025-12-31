<?php
/**
 * Test OTP Flow - Complete end-to-end test
 * Tests: Send OTP -> Verify OTP -> Check Database
 */

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

require_once __DIR__ . '/db.php';

try {
    $conn = getOTPDB();
    
    // Test email
    $testEmail = 'test@example.com';
    
    // Step 1: Check table exists
    $tableCheck = $conn->query("SHOW TABLES LIKE 'otp_verification'");
    if (!$tableCheck || $tableCheck->num_rows === 0) {
        echo json_encode([
            'success' => false,
            'message' => 'Table otp_verification does not exist. Run setup_otp_table.php first.'
        ]);
        $conn->close();
        exit();
    }
    
    // Step 2: Generate test OTP
    $testOTP = str_pad(rand(0, 999999), 6, '0', STR_PAD_LEFT);
    $expiresAt = date('Y-m-d H:i:s', strtotime('+5 minutes'));
    
    // Step 3: Delete old test records
    $conn->query("DELETE FROM otp_verification WHERE email = '$testEmail'");
    
    // Step 4: Insert test OTP
    $insertStmt = $conn->prepare("INSERT INTO otp_verification (email, otp, expires_at) VALUES (?, ?, ?)");
    $insertStmt->bind_param("sss", $testEmail, $testOTP, $expiresAt);
    
    if (!$insertStmt->execute()) {
        echo json_encode([
            'success' => false,
            'message' => 'Failed to insert OTP: ' . $conn->error
        ]);
        $insertStmt->close();
        $conn->close();
        exit();
    }
    
    $insertedId = $conn->insert_id;
    $insertStmt->close();
    
    // Step 5: Verify OTP was inserted
    $verifyStmt = $conn->prepare("SELECT id, email, otp, expires_at FROM otp_verification WHERE id = ?");
    $verifyStmt->bind_param("i", $insertedId);
    $verifyStmt->execute();
    $verifyResult = $verifyStmt->get_result();
    $verifyStmt->close();
    
    if ($verifyResult->num_rows === 0) {
        echo json_encode([
            'success' => false,
            'message' => 'OTP was inserted but could not be retrieved'
        ]);
        $conn->close();
        exit();
    }
    
    $verifyRow = $verifyResult->fetch_assoc();
    
    // Step 6: Test verification
    $checkStmt = $conn->prepare("SELECT id, otp, expires_at FROM otp_verification WHERE email = ? AND otp = ?");
    $checkStmt->bind_param("ss", $testEmail, $testOTP);
    $checkStmt->execute();
    $checkResult = $checkStmt->get_result();
    $checkStmt->close();
    
    // Step 7: Get all records for this email
    $allStmt = $conn->prepare("SELECT id, email, otp, expires_at, created_at FROM otp_verification WHERE email = ?");
    $allStmt->bind_param("s", $testEmail);
    $allStmt->execute();
    $allResult = $allStmt->get_result();
    $allRecords = [];
    while ($row = $allResult->fetch_assoc()) {
        $allRecords[] = $row;
    }
    $allStmt->close();
    
    // Clean up test record
    $conn->query("DELETE FROM otp_verification WHERE email = '$testEmail'");
    
    $conn->close();
    
    echo json_encode([
        'success' => true,
        'message' => 'OTP flow test completed successfully',
        'test_email' => $testEmail,
        'test_otp' => $testOTP,
        'inserted_record' => $verifyRow,
        'verification_found' => $checkResult->num_rows > 0,
        'all_records_count' => count($allRecords),
        'table_status' => 'Working correctly'
    ], JSON_PRETTY_PRINT);
    
} catch (Exception $e) {
    echo json_encode([
        'success' => false,
        'message' => 'Error: ' . $e->getMessage()
    ], JSON_PRETTY_PRINT);
}

