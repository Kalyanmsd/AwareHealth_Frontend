@echo off
echo ========================================
echo    Starting Flask API Server
echo ========================================
echo.

REM Get the current IP address
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /c:"IPv4 Address"') do (
    set IP=%%a
    set IP=!IP:~1!
    goto :found_ip
)
:found_ip

REM Flask API directory path
set FLASK_DIR=C:\xampp\htdocs\AwareHealth\aimodel\aware_health
set FLASK_FILE=flask_api.py

echo Checking if Flask API is already running...
netstat -ano | findstr :5000 >nul
if %errorlevel% == 0 (
    echo.
    echo [WARNING] Port 5000 is already in use!
    echo Flask API might already be running.
    echo.
    echo Do you want to continue anyway? (Y/N)
    set /p CONTINUE=
    if /i not "%CONTINUE%"=="Y" exit /b
)

echo.
echo Flask API Directory: %FLASK_DIR%
echo Flask API File: %FLASK_FILE%
echo Server will run on: http://localhost:5000
echo Network access: http://%IP%:5000
echo.

REM Check if directory exists
if not exist "%FLASK_DIR%" (
    echo [ERROR] Flask API directory not found!
    echo Expected location: %FLASK_DIR%
    echo.
    echo Please update the FLASK_DIR variable in this script.
    pause
    exit /b 1
)

REM Check if Flask file exists
if not exist "%FLASK_DIR%\%FLASK_FILE%" (
    echo [ERROR] Flask API file not found!
    echo Expected file: %FLASK_DIR%\%FLASK_FILE%
    echo.
    pause
    exit /b 1
)

REM Change to Flask directory
cd /d "%FLASK_DIR%"

echo Starting Flask API server...
echo.
echo [IMPORTANT] Keep this window open while the server is running!
echo Press Ctrl+C to stop the server.
echo.
echo ========================================
echo.

REM Start Flask server
python %FLASK_FILE%

echo.
echo Flask API server stopped.
pause

