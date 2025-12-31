# =====================================================
# Saveetha Hospital Backend Setup Script
# This script copies all backend files to XAMPP
# =====================================================

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Saveetha Hospital Backend Setup" -ForegroundColor Cyan
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
    "$sourceDir\api\auth.php" = "$targetDir\api\auth.php"
    "$sourceDir\api\doctors.php" = "$targetDir\api\doctors.php"
    "$sourceDir\api\appointments.php" = "$targetDir\api\appointments.php"
    "$sourceDir\api\health.php" = "$targetDir\api\health.php"
    "$sourceDir\api\debug_otp.php" = "$targetDir\api\debug_otp.php"
    "$sourceDir\api\fix_otp_issue.php" = "$targetDir\api\fix_otp_issue.php"
    "$sourceDir\api\setup_database.php" = "$targetDir\api\setup_database.php"
    "$sourceDir\includes\database.php" = "$targetDir\includes\database.php"
    "$sourceDir\includes\functions.php" = "$targetDir\includes\functions.php"
    "$sourceDir\includes\smtp_email.php" = "$targetDir\includes\smtp_email.php"
    "$sourceDir\database\SAVEETHA_HOSPITAL_DOCTORS.sql" = "$targetDir\database\SAVEETHA_HOSPITAL_DOCTORS.sql"
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

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Setup Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "`nNext Steps:" -ForegroundColor Yellow
Write-Host "1. Restart Apache in XAMPP Control Panel" -ForegroundColor White
Write-Host "2. Run SQL script in phpMyAdmin:" -ForegroundColor White
Write-Host "   - Open: http://localhost/phpmyadmin" -ForegroundColor Cyan
Write-Host "   - Select 'awarehealth' database" -ForegroundColor Cyan
Write-Host "   - Go to SQL tab" -ForegroundColor Cyan
Write-Host "   - Run: backend/database/SAVEETHA_HOSPITAL_DOCTORS.sql" -ForegroundColor Cyan
Write-Host "3. Test doctor login with:" -ForegroundColor White
Write-Host "   - Doctor ID: SAV001, SAV002, SAV003, SAV004, or SAV005" -ForegroundColor Cyan
Write-Host "   - Password: password" -ForegroundColor Cyan
Write-Host ""

