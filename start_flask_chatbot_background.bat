@echo off
REM Batch script to start Flask Chatbot API in background
REM This creates a separate window for the Flask server

echo ========================================
echo    Starting Flask Chatbot API
echo ========================================
echo.

REM Deploy Flask API first
powershell -ExecutionPolicy Bypass -Command "if (-not (Test-Path 'C:\xampp\htdocs\AwareHealth\aimodel\aware_health')) { New-Item -ItemType Directory -Path 'C:\xampp\htdocs\AwareHealth\aimodel\aware_health' -Force | Out-Null }; Copy-Item -Path 'backend\flask_chatbot_api.py' -Destination 'C:\xampp\htdocs\AwareHealth\aimodel\aware_health\flask_chatbot_api.py' -Force"

REM Check if port 5000 is in use
netstat -ano | findstr :5000 >nul
if %errorlevel% == 0 (
    echo Port 5000 is already in use. Stopping existing processes...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :5000') do (
        taskkill /F /PID %%a >nul 2>&1
    )
    timeout /t 2 /nobreak >nul
)

REM Start Flask API in a new window
start "Flask Chatbot API" cmd /k "cd /d C:\xampp\htdocs\AwareHealth\aimodel\aware_health && python flask_chatbot_api.py"

echo.
echo Flask Chatbot API is starting in a new window...
echo.
echo Server will be available at:
echo   Local:    http://localhost:5000
echo   Network:  http://172.20.10.2:5000
echo   Health:   http://172.20.10.2:5000/health
echo   Chatbot:  http://172.20.10.2:5000/chatbot
echo.
echo The Flask API window will stay open while the server is running.
echo Close that window to stop the server.
echo.
pause

