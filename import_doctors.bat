@echo off
echo Opening Doctors Table Import Page...
echo.
echo This will automatically create the doctors table and import sample data.
echo.
start http://localhost/AwareHealth/backend/api/auto_import_doctors.php
echo.
echo Browser should open automatically.
echo If not, manually open: http://localhost/AwareHealth/backend/api/auto_import_doctors.php
echo.
pause

