@echo off
REM Batch script to ensure Flask API is running
REM This script checks if Flask API is running and starts it if needed

set FLASK_DIR=C:\xampp\htdocs\AwareHealth\aimodel\aware_health
set FLASK_FILE=flask_api.py

REM Check if Flask API directory exists
if not exist "%FLASK_DIR%" (
    echo [ERROR] Flask API directory not found: %FLASK_DIR%
    pause
    exit /b 1
)

REM Check if Flask API file exists
if not exist "%FLASK_DIR%\%FLASK_FILE%" (
    echo [ERROR] Flask API file not found: %FLASK_DIR%\%FLASK_FILE%
    pause
    exit /b 1
)

REM Check if port 5000 is in use (Flask API running)
netstat -ano | findstr :5000 >nul
if %errorlevel% == 0 (
    echo [INFO] Flask API is already running on port 5000
    exit /b 0
)

REM Flask API is not running, start it
echo [INFO] Flask API is not running. Starting now...

REM Start Flask API in a new window
start "Flask API Server (Auto-Started)" cmd /k "cd /d %FLASK_DIR% && echo ======================================== && echo    Flask API Server (Auto-Started) && echo ======================================== && echo. && echo Server running on: http://localhost:5000 && echo Network access: http://172.20.10.2:5000 && echo. && echo Keep this window open while developing! && echo. && python %FLASK_FILE% && pause"

REM Wait a moment for Flask to start
timeout /t 3 /nobreak >nul

REM Verify Flask started
netstat -ano | findstr :5000 >nul
if %errorlevel% == 0 (
    echo [SUCCESS] Flask API started successfully!
) else (
    echo [WARNING] Flask API might not have started. Please check the Flask API window.
)

