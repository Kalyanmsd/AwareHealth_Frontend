# PowerShell script to start Flask API Server
# Usage: .\start_flask_api.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Starting Flask API Server" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Get current IP address
$ipAddress = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object {$_.InterfaceAlias -notlike "*Loopback*" -and $_.IPAddress -notlike "169.254.*"} | Select-Object -First 1).IPAddress

# Flask API directory path
$flaskDir = "C:\xampp\htdocs\AwareHealth\aimodel\aware_health"
$flaskFile = "flask_api.py"

Write-Host "Checking if Flask API is already running..." -ForegroundColor Yellow

# Check if port 5000 is in use
$portInUse = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host ""
    Write-Host "[WARNING] Port 5000 is already in use!" -ForegroundColor Yellow
    Write-Host "Flask API might already be running." -ForegroundColor Yellow
    Write-Host ""
    $continue = Read-Host "Do you want to continue anyway? (Y/N)"
    if ($continue -ne "Y" -and $continue -ne "y") {
        exit
    }
}

Write-Host ""
Write-Host "Flask API Directory: $flaskDir" -ForegroundColor Green
Write-Host "Flask API File: $flaskFile" -ForegroundColor Green
Write-Host "Server will run on: http://localhost:5000" -ForegroundColor Green
Write-Host "Network access: http://$ipAddress:5000" -ForegroundColor Green
Write-Host ""

# Check if directory exists
if (-not (Test-Path $flaskDir)) {
    Write-Host "[ERROR] Flask API directory not found!" -ForegroundColor Red
    Write-Host "Expected location: $flaskDir" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please update the `$flaskDir variable in this script." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Flask file exists
$flaskPath = Join-Path $flaskDir $flaskFile
if (-not (Test-Path $flaskPath)) {
    Write-Host "[ERROR] Flask API file not found!" -ForegroundColor Red
    Write-Host "Expected file: $flaskPath" -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Change to Flask directory
Set-Location $flaskDir

Write-Host "Starting Flask API server..." -ForegroundColor Green
Write-Host ""
Write-Host "[IMPORTANT] Keep this window open while the server is running!" -ForegroundColor Yellow
Write-Host "Press Ctrl+C to stop the server." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Python is available
try {
    $pythonVersion = python --version 2>&1
    Write-Host "Python version: $pythonVersion" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Python is not installed or not in PATH!" -ForegroundColor Red
    Write-Host "Please install Python and add it to your PATH." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Start Flask server
Write-Host "Running: python $flaskFile" -ForegroundColor Cyan
Write-Host ""
try {
    python $flaskFile
} catch {
    Write-Host ""
    Write-Host "[ERROR] Failed to start Flask API server!" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Flask API server stopped." -ForegroundColor Yellow
Read-Host "Press Enter to exit"

