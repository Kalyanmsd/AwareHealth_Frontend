# PowerShell script to remove Flask API automatic startup

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Remove Flask API Auto-Start" -ForegroundColor Cyan
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

$taskName = "AwareHealth_FlaskAPI_AutoStart"

# Check if task exists
$existingTask = Get-ScheduledTask -TaskName $taskName -ErrorAction SilentlyContinue

if (-not $existingTask) {
    Write-Host "[INFO] Auto-start task not found. Nothing to remove." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 0
}

# Remove the task
try {
    Unregister-ScheduledTask -TaskName $taskName -Confirm:$false
    Write-Host "[SUCCESS] Auto-start task removed successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Flask API will no longer start automatically." -ForegroundColor Yellow
    Write-Host ""
} catch {
    Write-Host "[ERROR] Failed to remove task: $_" -ForegroundColor Red
}

Read-Host "Press Enter to exit"

