# PowerShell Script to Copy Backend Files to XAMPP
# Run this script from the project root directory

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Copying AwareHealth Backend to XAMPP" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# XAMPP htdocs path
$xamppPath = "C:\xampp\htdocs\AwareHealth"

# Check if XAMPP path exists
if (-not (Test-Path "C:\xampp\htdocs")) {
    Write-Host "ERROR: XAMPP htdocs folder not found!" -ForegroundColor Red
    Write-Host "Expected path: C:\xampp\htdocs" -ForegroundColor Yellow
    Write-Host "Please install XAMPP or update the path in this script." -ForegroundColor Yellow
    exit 1
}

# Create AwareHealth directory if it doesn't exist
if (-not (Test-Path $xamppPath)) {
    Write-Host "Creating directory: $xamppPath" -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $xamppPath -Force | Out-Null
}

# Create subdirectories
$directories = @(
    "$xamppPath\api",
    "$xamppPath\includes",
    "$xamppPath\database"
)

foreach ($dir in $directories) {
    if (-not (Test-Path $dir)) {
        Write-Host "Creating directory: $dir" -ForegroundColor Yellow
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }
}

Write-Host ""
Write-Host "Copying files..." -ForegroundColor Green
Write-Host ""

# Files to copy (source -> destination relative to xamppPath)
$filesToCopy = @(
    @{Source = "backend\config.php"; Dest = "config.php"},
    @{Source = "backend\api\index.php"; Dest = "api\index.php"},
    @{Source = "backend\api\health.php"; Dest = "api\health.php"},
    @{Source = "backend\api\health_simple.php"; Dest = "api\health_simple.php"},
    @{Source = "backend\api\auth.php"; Dest = "api\auth.php"},
    @{Source = "backend\api\test_health.php"; Dest = "api\test_health.php"},
    @{Source = "backend\includes\database.php"; Dest = "includes\database.php"},
    @{Source = "backend\includes\functions.php"; Dest = "includes\functions.php"},
    @{Source = "backend\includes\smtp_email.php"; Dest = "includes\smtp_email.php"}
)

$copiedCount = 0
$failedCount = 0

foreach ($file in $filesToCopy) {
    $sourcePath = Join-Path $PSScriptRoot $file.Source
    $destPath = Join-Path $xamppPath $file.Dest
    
    if (Test-Path $sourcePath) {
        try {
            Copy-Item -Path $sourcePath -Destination $destPath -Force
            Write-Host "  ✓ Copied: $($file.Source) -> $($file.Dest)" -ForegroundColor Green
            $copiedCount++
        } catch {
            Write-Host "  ✗ Failed: $($file.Source)" -ForegroundColor Red
            Write-Host "    Error: $_" -ForegroundColor Red
            $failedCount++
        }
    } else {
        Write-Host "  ⚠ Not found: $($file.Source)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Copy Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Files copied: $copiedCount" -ForegroundColor Green
if ($failedCount -gt 0) {
    Write-Host "Files failed: $failedCount" -ForegroundColor Red
}
Write-Host ""
Write-Host "Files are now in: $xamppPath" -ForegroundColor Yellow
Write-Host ""
Write-Host "NEXT STEPS:" -ForegroundColor Cyan
Write-Host "1. Open XAMPP Control Panel" -ForegroundColor White
Write-Host "2. Stop Apache (if running)" -ForegroundColor White
Write-Host "3. Start Apache" -ForegroundColor White
Write-Host "4. Test in browser: http://localhost/AwareHealth/api/health_simple" -ForegroundColor White
Write-Host ""


