# PowerShell script to start Flask API Server in background
# Usage: .\start_flask_api_background.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Starting Flask API Server (Background)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Get current IP address
$ipAddress = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object {$_.InterfaceAlias -notlike "*Loopback*" -and $_.IPAddress -notlike "169.254.*"} | Select-Object -First 1).IPAddress

# Flask API directory path
$flaskDir = "C:\xampp\htdocs\AwareHealth\aimodel\aware_health"
$flaskFile = "flask_api.py"

# Check if port 5000 is in use
$portInUse = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host "[INFO] Port 5000 is already in use." -ForegroundColor Yellow
    Write-Host "Flask API might already be running." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To stop the existing server, run:" -ForegroundColor Cyan
    Write-Host "  Get-Process python | Where-Object {`$_.CommandLine -like '*flask_api*'} | Stop-Process" -ForegroundColor Gray
    Write-Host ""
    $continue = Read-Host "Do you want to kill existing process and start new one? (Y/N)"
    if ($continue -eq "Y" -or $continue -eq "y") {
        Get-Process python -ErrorAction SilentlyContinue | Where-Object {
            $_.CommandLine -like "*flask_api*"
        } | Stop-Process -Force -ErrorAction SilentlyContinue
        Start-Sleep -Seconds 2
    } else {
        Write-Host "Exiting..." -ForegroundColor Yellow
        exit
    }
}

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

Write-Host ""
Write-Host "Starting Flask API server in background..." -ForegroundColor Green
Write-Host "Server will run on: http://localhost:5000" -ForegroundColor Green
Write-Host "Network access: http://$ipAddress:5000" -ForegroundColor Green
Write-Host ""

# Change to Flask directory and start in background
Set-Location $flaskDir

# Start Flask server in a new PowerShell window
$scriptBlock = {
    Set-Location $using:flaskDir
    python $using:flaskFile
}

Start-Process powershell -ArgumentList "-NoExit", "-Command", "& {$scriptBlock}" -WindowStyle Normal

Start-Sleep -Seconds 3

# Check if server started successfully
$portCheck = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue
if ($portCheck) {
    Write-Host "[SUCCESS] Flask API server started successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Server is running in a separate window." -ForegroundColor Cyan
    Write-Host "You can close this window, but keep the Flask API window open." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To stop the server, close the Flask API window or run:" -ForegroundColor Cyan
    Write-Host "  Get-Process python | Where-Object {`$_.CommandLine -like '*flask_api*'} | Stop-Process" -ForegroundColor Gray
} else {
    Write-Host "[WARNING] Flask API might not have started properly." -ForegroundColor Yellow
    Write-Host "Please check the Flask API window for errors." -ForegroundColor Yellow
}

Write-Host ""
Read-Host "Press Enter to exit"

