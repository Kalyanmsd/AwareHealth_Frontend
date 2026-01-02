# PowerShell script to check and start Flask API before Gradle build
# This can be called from Gradle build scripts

$flaskDir = "C:\xampp\htdocs\AwareHealth\aimodel\aware_health"
$flaskFile = "flask_api.py"

# Check if Flask API is running
$portInUse = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue

if (-not $portInUse) {
    Write-Host "[Gradle] Flask API not running, starting..." -ForegroundColor Yellow
    
    if (Test-Path (Join-Path $flaskDir $flaskFile)) {
        Set-Location $flaskDir
        $scriptBlock = {
            Set-Location $using:flaskDir
            python $using:flaskFile
        }
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "& {$scriptBlock}" -WindowStyle Minimized
        Start-Sleep -Seconds 2
        Write-Host "[Gradle] Flask API started" -ForegroundColor Green
    } else {
        Write-Host "[Gradle] Warning: Flask API file not found" -ForegroundColor Yellow
    }
} else {
    Write-Host "[Gradle] Flask API is already running" -ForegroundColor Green
}

