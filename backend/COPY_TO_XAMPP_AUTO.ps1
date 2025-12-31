# =====================================================
# Auto Copy Backend Files to XAMPP
# Run this script to copy all backend files to XAMPP
# =====================================================

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Copying Backend Files to XAMPP" -ForegroundColor Cyan
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
    "$targetDir\includes"
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
}

$copiedCount = 0
$failedCount = 0

foreach ($file in $filesToCopy.GetEnumerator()) {
    $source = $file.Key
    $target = $file.Value
    
    if (Test-Path $source) {
        try {
            Copy-Item -Path $source -Destination $target -Force
            Write-Host "Copied: $(Split-Path $target -Leaf)" -ForegroundColor Green
            $copiedCount++
        } catch {
            Write-Host "Failed: $(Split-Path $target -Leaf) - $_" -ForegroundColor Red
            $failedCount++
        }
    } else {
        Write-Host "Source not found: $(Split-Path $source -Leaf)" -ForegroundColor Yellow
        $failedCount++
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Copy Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Copied: $copiedCount files" -ForegroundColor Green
Write-Host "Failed: $failedCount files" -ForegroundColor $(if ($failedCount -eq 0) { "Green" } else { "Red" })
Write-Host "`nTarget directory: $targetDir" -ForegroundColor White
Write-Host "`nFiles copied! Now restart Apache in XAMPP.`n" -ForegroundColor Green
