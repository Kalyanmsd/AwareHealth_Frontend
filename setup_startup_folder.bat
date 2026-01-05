@echo off
REM Simple script to add Flask API to Windows Startup folder
REM This doesn't require Administrator privileges

echo ========================================
echo    Flask API Startup Folder Setup
echo ========================================
echo.
echo This will add Flask API to Windows Startup folder
echo so it starts automatically when you log in.
echo.
echo [NOTE] This does NOT require Administrator privileges.
echo.

REM Get the current script directory
set SCRIPT_DIR=%~dp0
set STARTUP_SCRIPT=%SCRIPT_DIR%ensure_flask_running.bat

REM Get Windows Startup folder path
for /f "tokens=*" %%a in ('powershell -Command "[Environment]::GetFolderPath('Startup')"') do set STARTUP_FOLDER=%%a

echo Current directory: %SCRIPT_DIR%
echo Startup folder: %STARTUP_FOLDER%
echo.
echo Creating shortcut in Startup folder...
echo.

REM Create shortcut using PowerShell
powershell -Command "$WshShell = New-Object -ComObject WScript.Shell; $Shortcut = $WshShell.CreateShortcut('%STARTUP_FOLDER%\AwareHealth_FlaskAPI.lnk'); $Shortcut.TargetPath = '%STARTUP_SCRIPT%'; $Shortcut.WorkingDirectory = '%SCRIPT_DIR%'; $Shortcut.Description = 'Auto-start Flask API for AwareHealth'; $Shortcut.Save(); Write-Host 'Shortcut created successfully'"

if %errorlevel% == 0 (
    echo [SUCCESS] Shortcut created in Startup folder!
    echo.
    echo Flask API will now start automatically when you log in.
    echo.
    echo To test: Log out and log back in, or restart your computer.
    echo.
) else (
    echo [ERROR] Failed to create shortcut.
    echo.
    echo Please try manually:
    echo 1. Right-click ensure_flask_running.bat
    echo 2. Select "Create shortcut"
    echo 3. Press Win+R, type: shell:startup
    echo 4. Copy the shortcut there
    echo.
)

pause

