# PowerShell script to set up Flask API automatic startup via Windows Task Scheduler
# This will make Flask API start automatically when Windows boots or user logs in

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Flask API Auto-Start Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if running as Administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $isAdmin) {
    Write-Host "[ERROR] This script requires Administrator privileges!" -ForegroundColor Red
    Write-Host "Please right-click and select 'Run as Administrator'" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Configuration
$flaskDir = "C:\xampp\htdocs\AwareHealth\aimodel\aware_health"
$flaskFile = "flask_api.py"
$flaskPath = Join-Path $flaskDir $flaskFile
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$autoStartScript = Join-Path $scriptDir "auto_start_flask_api_service.ps1"
$taskName = "AwareHealth_FlaskAPI_AutoStart"
$taskDescription = "Automatically starts Flask API server for AwareHealth on system startup"

# Check if Flask API directory exists
if (-not (Test-Path $flaskDir)) {
    Write-Host "[ERROR] Flask API directory not found: $flaskDir" -ForegroundColor Red
    Write-Host "Please update the flaskDir variable in this script." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Flask API file exists
if (-not (Test-Path $flaskPath)) {
    Write-Host "[ERROR] Flask API file not found: $flaskPath" -ForegroundColor Red
    Write-Host "Please verify the Flask API location." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[INFO] Flask API found at: $flaskPath" -ForegroundColor Green
Write-Host ""

# Create the auto-start service script if it doesn't exist
if (-not (Test-Path $autoStartScript)) {
    Write-Host "[INFO] Creating auto-start service script..." -ForegroundColor Yellow
    
    $serviceScriptContent = @"
# Auto-start Flask API service script
# This script is called by Windows Task Scheduler

`$flaskDir = "$flaskDir"
`$flaskFile = "$flaskFile"

# Wait a bit for system to fully boot
Start-Sleep -Seconds 10

# Check if Flask API is already running
`$portInUse = Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue

if (`$portInUse) {
    exit 0
}

# Get current IP address dynamically
try {
    `$ipAddress = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object { 
        `$_.IPAddress -like "192.168.*" -or 
        `$_.IPAddress -like "172.*" -or 
        `$_.IPAddress -like "10.*" 
    } | Select-Object -First 1).IPAddress
} catch {
    `$ipAddress = "YOUR_IP"
}

if (-not `$ipAddress) {
    `$ipAddress = "YOUR_IP"
}

# Change to Flask directory
Set-Location `$flaskDir

# Start Flask API in a minimized window
`$scriptBlock = {
    param(`$flaskDir, `$flaskFile, `$ipAddr)
    Set-Location `$flaskDir
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   Flask API Server (Auto-Started)" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Server running on: http://localhost:5000" -ForegroundColor Green
    Write-Host "Network access: http://`$ipAddr:5000" -ForegroundColor Green
    Write-Host ""
    Write-Host "Started automatically on system boot" -ForegroundColor Yellow
    Write-Host ""
    python `$flaskFile
}

Start-Process powershell -ArgumentList "-NoExit", "-Command", "& {`$scriptBlock} -flaskDir '`$flaskDir' -flaskFile '`$flaskFile' -ipAddr '`$ipAddress'" -WindowStyle Minimized
"@
    
    Set-Content -Path $autoStartScript -Value $serviceScriptContent
    Write-Host "[SUCCESS] Auto-start service script created!" -ForegroundColor Green
    Write-Host ""
}

# Check if task already exists
$existingTask = Get-ScheduledTask -TaskName $taskName -ErrorAction SilentlyContinue

if ($existingTask) {
    Write-Host "[INFO] Task already exists. Updating..." -ForegroundColor Yellow
    Unregister-ScheduledTask -TaskName $taskName -Confirm:$false
}

# Create the scheduled task action
$action = New-ScheduledTaskAction -Execute "powershell.exe" -Argument "-ExecutionPolicy Bypass -WindowStyle Hidden -File `"$autoStartScript`""

# Create trigger: At startup
$trigger = New-ScheduledTaskTrigger -AtStartup

# Create trigger: At logon (when user logs in)
$trigger2 = New-ScheduledTaskTrigger -AtLogOn

# Create settings
$settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries -StartWhenAvailable -RunOnlyIfNetworkAvailable

# Create principal (run as current user)
$principal = New-ScheduledTaskPrincipal -UserId "$env:USERDOMAIN\$env:USERNAME" -LogonType Interactive -RunLevel Highest

# Register the task
try {
    Register-ScheduledTask -TaskName $taskName -Action $action -Trigger @($trigger, $trigger2) -Settings $settings -Principal $principal -Description $taskDescription | Out-Null
    Write-Host "[SUCCESS] Scheduled task created successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Task Name: $taskName" -ForegroundColor Cyan
    Write-Host "Description: $taskDescription" -ForegroundColor Cyan
    Write-Host "Triggers: At Startup, At Logon" -ForegroundColor Cyan
    Write-Host ""
} catch {
    Write-Host "[ERROR] Failed to create scheduled task: $_" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Verify task was created
$task = Get-ScheduledTask -TaskName $taskName -ErrorAction SilentlyContinue
if ($task) {
    Write-Host "[SUCCESS] Task verified and ready!" -ForegroundColor Green
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   Setup Complete!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Flask API will now start automatically:" -ForegroundColor Yellow
    Write-Host "  - When Windows boots" -ForegroundColor White
    Write-Host "  - When you log in" -ForegroundColor White
    Write-Host ""
    Write-Host "To test immediately, run:" -ForegroundColor Yellow
    Write-Host "  Start-ScheduledTask -TaskName `"$taskName`"" -ForegroundColor White
    Write-Host ""
    Write-Host "To remove auto-start later, run:" -ForegroundColor Yellow
    Write-Host "  Unregister-ScheduledTask -TaskName `"$taskName`" -Confirm:`$false" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host "[ERROR] Task was not created properly." -ForegroundColor Red
}

Read-Host "Press Enter to exit"

