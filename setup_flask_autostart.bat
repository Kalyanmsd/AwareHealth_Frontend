@echo off
REM Batch file to set up Flask API automatic startup
REM This will run the PowerShell script with Administrator privileges

echo ========================================
echo    Flask API Auto-Start Setup
echo ========================================
echo.
echo This will set up Flask API to start automatically
echo when Windows boots or when you log in.
echo.
echo [NOTE] This requires Administrator privileges.
echo.

REM Check if running as Administrator
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] This script requires Administrator privileges!
    echo.
    echo Please right-click this file and select "Run as Administrator"
    echo.
    pause
    exit /b 1
)

echo [INFO] Running PowerShell setup script...
echo.

REM Run the PowerShell script
powershell.exe -ExecutionPolicy Bypass -File "%~dp0setup_flask_autostart.ps1"

pause

