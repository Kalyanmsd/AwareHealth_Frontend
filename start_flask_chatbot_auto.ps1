# PowerShell script to automatically start Flask Chatbot API
# This script deploys and starts the Flask API, keeping it running

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Starting Flask Chatbot API" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Paths
$projectRoot = $PSScriptRoot
$sourceFile = Join-Path $projectRoot "backend\flask_chatbot_api.py"
$flaskDir = "C:\xampp\htdocs\AwareHealth\aimodel\aware_health"
$targetFile = Join-Path $flaskDir "flask_chatbot_api.py"
$flaskFile = "flask_chatbot_api.py"

# Step 1: Deploy Flask API
Write-Host "Step 1: Deploying Flask Chatbot API..." -ForegroundColor Yellow

# Create directory if it doesn't exist
if (-not (Test-Path $flaskDir)) {
    Write-Host "  Creating directory: $flaskDir" -ForegroundColor Gray
    New-Item -ItemType Directory -Path $flaskDir -Force | Out-Null
}

# Copy Flask API file
if (Test-Path $sourceFile) {
    Copy-Item -Path $sourceFile -Destination $targetFile -Force
    Write-Host "  [OK] Flask API deployed to: $targetFile" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] Source file not found: $sourceFile" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""

# Step 2: Check and stop existing Flask processes
Write-Host "Step 2: Checking for existing Flask processes..." -ForegroundColor Yellow

$portInUse = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host "  Port 5000 is in use. Stopping existing processes..." -ForegroundColor Yellow
    $processes = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique
    foreach ($pid in $processes) {
        try {
            Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
            Write-Host "  [OK] Stopped process: $pid" -ForegroundColor Green
        } catch {
            # Ignore errors
        }
    }
    Start-Sleep -Seconds 2
} else {
    Write-Host "  [OK] Port 5000 is available" -ForegroundColor Green
}

Write-Host ""

# Step 3: Check Python and packages
Write-Host "Step 3: Checking Python environment..." -ForegroundColor Yellow

try {
    $pythonVersion = python --version 2>&1
    Write-Host "  [OK] Python version: $pythonVersion" -ForegroundColor Green
} catch {
    Write-Host "  [ERROR] Python is not installed or not in PATH!" -ForegroundColor Red
    Write-Host "  Please install Python and add it to your PATH." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check and install required packages
Write-Host "  Checking Python packages..." -ForegroundColor Gray
$packages = @("flask", "flask-cors", "mysql-connector-python")
$missingPackages = @()

foreach ($package in $packages) {
    $packageName = $package.Replace('-', '_')
    if ($packageName -eq "flask_cors") { $packageName = "flask_cors" }
    if ($packageName -eq "mysql_connector_python") { $packageName = "mysql.connector" }
    
    $check = python -c "import $packageName; print('installed')" 2>&1
    if ($LASTEXITCODE -ne 0) {
        $missingPackages += $package
    }
}

if ($missingPackages.Count -gt 0) {
    Write-Host "  Installing missing packages: $($missingPackages -join ', ')" -ForegroundColor Yellow
    pip install $missingPackages
    Write-Host "  [OK] Packages installed" -ForegroundColor Green
} else {
    Write-Host "  [OK] All required packages are installed" -ForegroundColor Green
}

Write-Host ""

# Step 4: Start Flask API
Write-Host "Step 4: Starting Flask Chatbot API..." -ForegroundColor Yellow

# Get IP address
$ipAddress = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object {
    $_.InterfaceAlias -notlike "*Loopback*" -and 
    $_.IPAddress -notlike "169.254.*" -and
    $_.IPAddress -notlike "127.*"
} | Select-Object -First 1).IPAddress

if (-not $ipAddress) {
    $ipAddress = "localhost"
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Flask Chatbot API Starting" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Server Details:" -ForegroundColor Yellow
Write-Host "  Local:    http://localhost:5000" -ForegroundColor Green
Write-Host "  Network:  http://$ipAddress:5000" -ForegroundColor Green
Write-Host "  Health:   http://$ipAddress:5000/health" -ForegroundColor Green
Write-Host "  Chatbot:  http://$ipAddress:5000/chatbot" -ForegroundColor Green
Write-Host ""
Write-Host "[IMPORTANT] Keep this window open while the server is running!" -ForegroundColor Yellow
Write-Host "Press Ctrl+C to stop the server." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Change to Flask directory
Set-Location $flaskDir

# Start Flask server
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

