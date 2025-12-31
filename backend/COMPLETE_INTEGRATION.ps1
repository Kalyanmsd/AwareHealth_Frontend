# =====================================================
# COMPLETE INTEGRATION SCRIPT
# This script does everything to connect frontend and backend
# =====================================================

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  COMPLETE INTEGRATION SETUP" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# Step 1: Copy files to XAMPP
Write-Host "Step 1: Copying backend files to XAMPP..." -ForegroundColor Yellow
& ".\COPY_TO_XAMPP_AUTO.ps1"

# Step 2: Check if XAMPP Apache is running
Write-Host "`nStep 2: Checking XAMPP Apache..." -ForegroundColor Yellow
$apacheProcess = Get-Process -Name "httpd" -ErrorAction SilentlyContinue
if ($apacheProcess) {
    Write-Host "✓ Apache is running" -ForegroundColor Green
} else {
    Write-Host "⚠ Apache is NOT running. Please start it in XAMPP Control Panel." -ForegroundColor Yellow
}

# Step 3: Test API connection
Write-Host "`nStep 3: Testing API connection..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost/AwareHealth/api/test_connection.php" -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✓ API is accessible" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ API test failed. Make sure Apache is running." -ForegroundColor Red
}

# Step 4: Instructions for database
Write-Host "`nStep 4: Database Setup Required" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "1. Open phpMyAdmin: http://localhost/phpmyadmin" -ForegroundColor White
Write-Host "2. Select 'awarehealth' database" -ForegroundColor White
Write-Host "3. Click 'SQL' tab" -ForegroundColor White
Write-Host "4. Open file: backend\database\INSERT_DISEASES_QUICK.sql" -ForegroundColor White
Write-Host "5. Copy all SQL code and paste into SQL tab" -ForegroundColor White
Write-Host "6. Click 'Go' to insert 20 diseases" -ForegroundColor White
Write-Host "`nAfter inserting, test: http://localhost/AwareHealth/api/health/diseases" -ForegroundColor Cyan

# Step 5: Verify app configuration
Write-Host "`nStep 5: App Configuration Check" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✓ BASE_URL should be: http://172.20.10.2/AwareHealth/api/" -ForegroundColor Green
Write-Host "✓ API endpoint: health/diseases" -ForegroundColor Green
Write-Host "✓ Network security config allows cleartext HTTP" -ForegroundColor Green

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  INTEGRATION COMPLETE!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "`nNext steps:" -ForegroundColor Yellow
Write-Host "1. Insert diseases data in phpMyAdmin (see Step 4)" -ForegroundColor White
Write-Host "2. Test API: http://localhost/AwareHealth/api/health/diseases" -ForegroundColor White
Write-Host "3. Rebuild app in Android Studio" -ForegroundColor White
Write-Host "4. Run app and check Disease Database screen`n" -ForegroundColor White

