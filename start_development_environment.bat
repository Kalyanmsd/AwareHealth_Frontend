@echo off
echo ========================================
echo    Starting AwareHealth Development Environment
echo ========================================
echo.

REM Check XAMPP Apache
echo [1/3] Checking XAMPP Apache...
netstat -ano | findstr :80 >nul
if %errorlevel% == 0 (
    echo [OK] XAMPP Apache is running on port 80
) else (
    echo [WARNING] XAMPP Apache is not running on port 80
    echo Please start Apache from XAMPP Control Panel
    echo.
)

echo.

REM Check and start Flask API
echo [2/3] Checking Flask API...
call ensure_flask_running.bat

echo.

REM Get IP address
echo [3/3] Network Configuration...
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /c:"IPv4 Address"') do (
    set IP=%%a
    set IP=!IP:~1!
    goto :found_ip
)
:found_ip

if defined IP (
    echo.
    echo ========================================
    echo    Services Ready!
    echo ========================================
    echo.
    echo XAMPP API: http://%IP%/AwareHealth/api/
    echo Flask API: http://%IP%:5000/health
    echo.
    echo You can now:
    echo 1. Build and run the Android app
    echo 2. Test the chatbot
    echo 3. Keep Flask API window open
    echo.
) else (
    echo Could not detect IP address
)

echo.
pause

