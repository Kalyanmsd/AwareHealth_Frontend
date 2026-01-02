@echo off
echo ========================================
echo    Starting All AwareHealth Services
echo ========================================
echo.

REM Check if XAMPP Apache is running
echo Checking XAMPP Apache...
netstat -ano | findstr :80 >nul
if %errorlevel% == 0 (
    echo [OK] XAMPP Apache is running on port 80
) else (
    echo [WARNING] XAMPP Apache is not running on port 80
    echo Please start XAMPP Apache from XAMPP Control Panel
    echo.
    pause
)

echo.

REM Check if Flask API is running
echo Checking Flask API...
netstat -ano | findstr :5000 >nul
if %errorlevel% == 0 (
    echo [OK] Flask API is already running on port 5000
    echo.
    echo All services are running!
    pause
    exit /b
) else (
    echo [INFO] Flask API is not running. Starting now...
    echo.
)

REM Start Flask API in a new window
echo Starting Flask API server...
start "Flask API Server" cmd /k "%~dp0start_flask_api.bat"

echo.
echo Flask API server is starting in a new window...
echo.
echo ========================================
echo    Services Status:
echo ========================================
echo [OK] XAMPP Apache - Port 80
echo [STARTING] Flask API - Port 5000
echo.
echo Keep both windows open while developing!
echo.
pause

