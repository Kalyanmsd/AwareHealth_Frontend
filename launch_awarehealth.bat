@echo off
REM ========================================
REM    AwareHealth Development Launcher
REM    Automatically starts all required services
REM ========================================
echo.

echo Starting AwareHealth Development Environment...
echo.

REM Start Flask API automatically
echo [1/2] Ensuring Flask API is running...
call ensure_flask_running.bat

echo.
echo [2/2] Checking XAMPP Apache...
netstat -ano | findstr :80 >nul
if %errorlevel% == 0 (
    echo [OK] XAMPP Apache is running
) else (
    echo [WARNING] XAMPP Apache is not running
    echo Please start Apache from XAMPP Control Panel
)

echo.
echo ========================================
echo    Ready for Development!
echo ========================================
echo.
echo Services Status:
echo - Flask API: Check the Flask API window
echo - XAMPP Apache: Should be running (port 80)
echo.
echo You can now:
echo 1. Open Android Studio
echo 2. Build and run the app
echo 3. Test the chatbot
echo.
echo Keep the Flask API window open!
echo.
pause

