@echo off
echo ========================================
echo   Setting Up API Endpoint
echo ========================================
echo.

REM Create api directory if needed
if not exist "C:\xampp\htdocs\AwareHealth\api" (
    echo Creating api directory...
    mkdir "C:\xampp\htdocs\AwareHealth\api"
)

REM Copy the auto-setup API file
echo Copying API file...
copy /Y "backend\api\get_doctors_auto_setup.php" "C:\xampp\htdocs\AwareHealth\api\get_doctors.php"

if errorlevel 1 (
    echo ERROR: Failed to copy API file!
    pause
    exit /b 1
)

echo ✅ API file copied successfully!
echo.
echo API endpoint is now at:
echo   http://localhost/AwareHealth/api/get_doctors.php
echo.
echo Testing API...
timeout /t 2 /nobreak >nul
start http://localhost/AwareHealth/api/get_doctors.php
echo.
echo ========================================
echo   Setup Complete!
echo ========================================
echo.
echo Next steps:
echo   1. Make sure XAMPP MySQL is running (green in Control Panel)
echo   2. Refresh your Android app
echo   3. You should now see doctors list!
echo.
pause

