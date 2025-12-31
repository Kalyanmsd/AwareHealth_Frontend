# =====================================================
# Complete Doctors Integration Script
# This script:
# 1. Copies all backend files to XAMPP
# 2. Opens setup page to create doctors
# 3. Tests the API
# =====================================================

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Complete Doctors Integration" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$sourceDir = "C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend"
$targetDir = "C:\xampp\htdocs\AwareHealth"

# Check if source directory exists
if (-not (Test-Path $sourceDir)) {
    Write-Host "ERROR: Source directory not found: $sourceDir" -ForegroundColor Red
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
    "$sourceDir\api\doctors.php" = "$targetDir\api\doctors.php"
    "$sourceDir\api\auth.php" = "$targetDir\api\auth.php"
    "$sourceDir\api\appointments.php" = "$targetDir\api\appointments.php"
    "$sourceDir\api\auto_setup_doctors.php" = "$targetDir\api\auto_setup_doctors.php"
    "$sourceDir\api\test_doctors_api.php" = "$targetDir\api\test_doctors_api.php"
    "$sourceDir\includes\database.php" = "$targetDir\includes\database.php"
    "$sourceDir\includes\functions.php" = "$targetDir\includes\functions.php"
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
            Write-Host "  ✗ Failed: $(Split-Path $target -Leaf)" -ForegroundColor Red
            $failedCount++
        }
    } else {
        Write-Host "  ⚠ Source not found: $(Split-Path $source -Leaf)" -ForegroundColor Yellow
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

# Open setup page
Write-Host "`nOpening setup page..." -ForegroundColor Yellow
if ($apacheRunning) {
    try {
        Start-Process "http://localhost/AwareHealth/api/auto_setup_doctors.php"
        Write-Host "  ✓ Setup page opened in browser" -ForegroundColor Green
        Write-Host "  Please follow the instructions on the page" -ForegroundColor Yellow
    } catch {
        Write-Host "  ⚠ Could not open browser automatically" -ForegroundColor Yellow
        Write-Host "  Please open manually: http://localhost/AwareHealth/api/auto_setup_doctors.php" -ForegroundColor Yellow
    }
} else {
    Write-Host "  ⚠ Cannot open setup page - Apache is not running" -ForegroundColor Yellow
    Write-Host "  Start Apache, then visit: http://localhost/AwareHealth/api/auto_setup_doctors.php" -ForegroundColor Yellow
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Integration Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "`nNext Steps:" -ForegroundColor Yellow
Write-Host "1. Complete setup in browser (create doctors)" -ForegroundColor White
Write-Host "2. Test API: http://localhost/AwareHealth/api/test_doctors_api.php" -ForegroundColor Cyan
Write-Host "3. Restart Apache in XAMPP" -ForegroundColor White
Write-Host "4. Rebuild app in Android Studio" -ForegroundColor White
Write-Host "5. Test in app - doctors should appear!`n" -ForegroundColor White

