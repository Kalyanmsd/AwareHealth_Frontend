@echo off
echo ========================================
echo    AwareHealth Services Status
echo ========================================
echo.

REM Check XAMPP Apache (port 80)
echo Checking XAMPP Apache (Port 80)...
netstat -ano | findstr :80 >nul
if %errorlevel% == 0 (
    echo [RUNNING] XAMPP Apache is active on port 80
    echo   URL: http://localhost/AwareHealth/api/
) else (
    echo [STOPPED] XAMPP Apache is not running
    echo   Action: Start Apache from XAMPP Control Panel
)
echo.

REM Check Flask API (port 5000)
echo Checking Flask API (Port 5000)...
netstat -ano | findstr :5000 >nul
if %errorlevel% == 0 (
    echo [RUNNING] Flask API is active on port 5000
    echo   URL: http://localhost:5000/health
) else (
    echo [STOPPED] Flask API is not running
    echo   Action: Run start_flask_api.bat
)
echo.

REM Get IP address
echo Getting network IP address...
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /c:"IPv4 Address"') do (
    set IP=%%a
    set IP=!IP:~1!
    goto :found_ip
)
:found_ip

if defined IP (
    echo.
    echo Network Access:
    echo   XAMPP: http://%IP%/AwareHealth/api/
    echo   Flask API: http://%IP%:5000/health
) else (
    echo   Could not detect IP address
)

echo.
echo ========================================
pause

