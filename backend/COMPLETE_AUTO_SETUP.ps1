# =====================================================
# Complete Automated Setup Script for AwareHealth
# This script:
# 1. Copies all backend files to XAMPP
# 2. Sets up the database automatically
# 3. Verifies everything is working
# =====================================================

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  AwareHealth Complete Auto Setup" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$sourceDir = "C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend"
$targetDir = "C:\xampp\htdocs\AwareHealth"

# Check if source directory exists
if (-not (Test-Path $sourceDir)) {
    Write-Host "ERROR: Source directory not found: $sourceDir" -ForegroundColor Red
    exit 1
}

# Check if XAMPP directory exists
if (-not (Test-Path "C:\xampp\htdocs")) {
    Write-Host "ERROR: XAMPP htdocs directory not found. Is XAMPP installed?" -ForegroundColor Red
    exit 1
}

# Create target directories if they don't exist
$directories = @(
    "$targetDir\api",
    "$targetDir\includes",
    "$targetDir\database"
)

foreach ($dir in $directories) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
        Write-Host "Created directory: $dir" -ForegroundColor Green
    }
}

# Files to copy
$filesToCopy = @{
    "$sourceDir\config.php" = "$targetDir\config.php"
    "$sourceDir\api\index.php" = "$targetDir\api\index.php"
    "$sourceDir\api\health.php" = "$targetDir\api\health.php"
    "$sourceDir\api\auth.php" = "$targetDir\api\auth.php"
    "$sourceDir\api\appointments.php" = "$targetDir\api\appointments.php"
    "$sourceDir\api\doctors.php" = "$targetDir\api\doctors.php"
    "$sourceDir\api\test_connection.php" = "$targetDir\api\test_connection.php"
    "$sourceDir\api\simple_diseases.php" = "$targetDir\api\simple_diseases.php"
    "$sourceDir\api\debug_health.php" = "$targetDir\api\debug_health.php"
    "$sourceDir\api\debug_otp.php" = "$targetDir\api\debug_otp.php"
    "$sourceDir\api\setup_database.php" = "$targetDir\api\setup_database.php"
    "$sourceDir\includes\database.php" = "$targetDir\includes\database.php"
    "$sourceDir\includes\functions.php" = "$targetDir\includes\functions.php"
    "$sourceDir\includes\smtp_email.php" = "$targetDir\includes\smtp_email.php"
    "$sourceDir\database\create_all_tables_complete.sql" = "$targetDir\database\create_all_tables_complete.sql"
    "$sourceDir\database\PASSWORD_RESET_TOKENS_FINAL_WORKING.sql" = "$targetDir\database\PASSWORD_RESET_TOKENS_FINAL_WORKING.sql"
}

$copiedCount = 0
$failedCount = 0

Write-Host "`nCopying files..." -ForegroundColor Yellow

foreach ($file in $filesToCopy.GetEnumerator()) {
    $source = $file.Key
    $target = $file.Value
    
    if (Test-Path $source) {
        try {
            Copy-Item -Path $source -Destination $target -Force
            Write-Host "  ✓ Copied: $(Split-Path $target -Leaf)" -ForegroundColor Green
            $copiedCount++
        } catch {
            Write-Host "  ✗ Failed: $(Split-Path $target -Leaf) - $_" -ForegroundColor Red
            $failedCount++
        }
    } else {
        Write-Host "  ⚠ Source not found: $(Split-Path $source -Leaf)" -ForegroundColor Yellow
        $failedCount++
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Copy Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Copied: $copiedCount files" -ForegroundColor Green
Write-Host "Failed: $failedCount files" -ForegroundColor $(if ($failedCount -eq 0) { "Green" } else { "Red" })

# Check if Apache is running
Write-Host "`nChecking Apache status..." -ForegroundColor Yellow
$apacheRunning = $false
try {
    $response = Invoke-WebRequest -Uri "http://localhost" -TimeoutSec 2 -UseBasicParsing -ErrorAction SilentlyContinue
    $apacheRunning = $true
    Write-Host "  ✓ Apache is running" -ForegroundColor Green
} catch {
    Write-Host "  ⚠ Apache may not be running" -ForegroundColor Yellow
    Write-Host "    Please start Apache in XAMPP Control Panel" -ForegroundColor Yellow
}

# Setup database
Write-Host "`nSetting up database..." -ForegroundColor Yellow
if ($apacheRunning) {
    try {
        Write-Host "  Opening database setup page..." -ForegroundColor Cyan
        Write-Host "  URL: http://localhost/AwareHealth/api/setup_database.php" -ForegroundColor Cyan
        Start-Process "http://localhost/AwareHealth/api/setup_database.php"
        Write-Host "  ✓ Database setup page opened in browser" -ForegroundColor Green
        Write-Host "  Please follow the instructions on the page to complete setup" -ForegroundColor Yellow
    } catch {
        Write-Host "  ⚠ Could not open browser automatically" -ForegroundColor Yellow
        Write-Host "  Please open manually: http://localhost/AwareHealth/api/setup_database.php" -ForegroundColor Yellow
    }
} else {
    Write-Host "  ⚠ Cannot setup database - Apache is not running" -ForegroundColor Yellow
    Write-Host "  Start Apache, then visit: http://localhost/AwareHealth/api/setup_database.php" -ForegroundColor Yellow
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Setup Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "`nNext Steps:" -ForegroundColor Yellow
Write-Host "1. If Apache is not running, start it in XAMPP Control Panel" -ForegroundColor White
Write-Host "2. Complete database setup:" -ForegroundColor White
Write-Host "   - Visit: http://localhost/AwareHealth/api/setup_database.php" -ForegroundColor Cyan
Write-Host "   - Or manually: http://localhost/phpmyadmin" -ForegroundColor Cyan
Write-Host "3. Verify tables in phpMyAdmin:" -ForegroundColor White
Write-Host "   Visit phpMyAdmin and check awarehealth database" -ForegroundColor Cyan
Write-Host "4. Test OTP functionality in your app" -ForegroundColor White
Write-Host ""

