@echo off
echo ========================================
echo   Copy Files to XAMPP
echo ========================================
echo.

set XAMPP_PATH=C:\xampp\htdocs\AwareHealth
set SOURCE_PATH=%~dp0backend

echo Copying files to XAMPP...
echo From: %SOURCE_PATH%
echo To:   %XAMPP_PATH%
echo.

REM Check if XAMPP path exists
if not exist "%XAMPP_PATH%" (
    echo Creating directory: %XAMPP_PATH%
    mkdir "%XAMPP_PATH%"
)

REM Copy backend folder
echo Copying backend folder...
xcopy "%SOURCE_PATH%" "%XAMPP_PATH%\backend\" /E /I /Y /Q

if errorlevel 1 (
    echo ERROR: Failed to copy files!
    pause
    exit /b 1
)

echo.
echo ✅ Files copied successfully!
echo.
echo Next steps:
echo   1. Make sure XAMPP Apache is running
echo   2. Make sure XAMPP MySQL is running
echo   3. Open: http://localhost/AwareHealth/backend/api/get_doctors_auto_setup.php
echo   4. You should see doctors list in JSON format
echo   5. Refresh your Android app!
echo.
pause

