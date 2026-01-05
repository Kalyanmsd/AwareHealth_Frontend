# PowerShell script to automatically start Flask API if not running
# This script checks if Flask API is running and starts it if needed

$flaskDir = "C:\xampp\htdocs\AwareHealth\aimodel\aware_health"
$flaskFile = "flask_api.py"
$flaskPath = Join-Path $flaskDir $flaskFile

# Check if Flask API directory exists
if (-not (Test-Path $flaskDir)) {
    Write-Host "[ERROR] Flask API directory not found: $flaskDir" -ForegroundColor Red
    exit 1
}

# Check if Flask API file exists
if (-not (Test-Path $flaskPath)) {
    Write-Host "[ERROR] Flask API file not found: $flaskPath" -ForegroundColor Red
    exit 1
}

# Check if port 5000 is in use (Flask API running)
$portInUse = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue

if ($portInUse) {
    Write-Host "[INFO] Flask API is already running on port 5000" -ForegroundColor Green
    exit 0
}

# Flask API is not running, start it
Write-Host "[INFO] Flask API is not running. Starting now..." -ForegroundColor Yellow

# Change to Flask directory
Set-Location $flaskDir

# Get current IP address dynamically
try {
    $ipAddress = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object { 
        $_.IPAddress -like "192.168.*" -or 
        $_.IPAddress -like "172.*" -or 
        $_.IPAddress -like "10.*" 
    } | Select-Object -First 1).IPAddress
} catch {
    $ipAddress = "YOUR_IP"  # Fallback if IP not found
}

if (-not $ipAddress) {
    $ipAddress = "YOUR_IP"  # Fallback if IP not found
}

# Start Flask API in a new window
$scriptBlock = {
    param($flaskDir, $flaskFile, $ipAddr)
    Set-Location $flaskDir
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   Flask API Server (Auto-Started)" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Server running on: http://localhost:5000" -ForegroundColor Green
    Write-Host "Network access: http://$ipAddr:5000" -ForegroundColor Green
    Write-Host ""
    Write-Host "Keep this window open while developing!" -ForegroundColor Yellow
    Write-Host ""
    python $flaskFile
}

Start-Process powershell -ArgumentList "-NoExit", "-Command", "& {$scriptBlock} -flaskDir '$flaskDir' -flaskFile '$flaskFile' -ipAddr '$ipAddress'" -WindowStyle Normal

# Wait a moment for Flask to start
Start-Sleep -Seconds 3

# Verify Flask started
$portCheck = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue
if ($portCheck) {
    Write-Host "[SUCCESS] Flask API started successfully!" -ForegroundColor Green
} else {
    Write-Host "[WARNING] Flask API might not have started. Please check the Flask API window." -ForegroundColor Yellow
}

