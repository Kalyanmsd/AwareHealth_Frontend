# Auto-Fix Doctors API - PowerShell Script
# This script automatically fixes everything

Write-Host "🔧 Auto-Fixing Doctors API..." -ForegroundColor Cyan
Write-Host ""

# Step 1: Check if XAMPP is running
Write-Host "Step 1: Checking XAMPP..." -ForegroundColor Yellow
$apache = Get-Process -Name "httpd" -ErrorAction SilentlyContinue
if (-not $apache) {
    Write-Host "⚠️  Apache not running. Please start XAMPP Apache." -ForegroundColor Red
    Write-Host "   Opening XAMPP Control Panel..." -ForegroundColor Yellow
    Start-Process "C:\xampp\xampp-control.exe" -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 3
} else {
    Write-Host "✅ Apache is running" -ForegroundColor Green
}

# Step 2: Call the fix endpoint
Write-Host ""
Write-Host "Step 2: Running automatic fix..." -ForegroundColor Yellow

$fixUrl = "http://localhost/AwareHealth/backend/api/fix_doctors_complete.php"
try {
    $response = Invoke-WebRequest -Uri $fixUrl -UseBasicParsing -TimeoutSec 30
    Write-Host "✅ Fix script executed successfully" -ForegroundColor Green
    Write-Host ""
    Write-Host "Opening results in browser..." -ForegroundColor Cyan
    Start-Process $fixUrl
} catch {
    Write-Host "❌ Error calling fix script: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please manually open: $fixUrl" -ForegroundColor Yellow
}

# Step 3: Test API
Write-Host ""
Write-Host "Step 3: Testing API endpoint..." -ForegroundColor Yellow
Start-Sleep -Seconds 2

$apiUrl = "http://localhost/AwareHealth/backend/api/get_doctors.php"
try {
    $apiResponse = Invoke-WebRequest -Uri $apiUrl -UseBasicParsing -TimeoutSec 10
    $json = $apiResponse.Content | ConvertFrom-Json
    
    if ($json.success -and $json.doctors) {
        Write-Host "✅ API Test PASSED!" -ForegroundColor Green
        Write-Host "   Found $($json.doctors.Count) available doctors" -ForegroundColor Green
        Write-Host ""
        Write-Host "Your Android app should now work!" -ForegroundColor Green
    } else {
        Write-Host "⚠️  API returned success=false" -ForegroundColor Yellow
        Write-Host "   Message: $($json.message)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ API Test FAILED" -ForegroundColor Red
    Write-Host "   Error: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please check:" -ForegroundColor Yellow
    Write-Host "   1. XAMPP Apache is running" -ForegroundColor Yellow
    Write-Host "   2. Database is set up" -ForegroundColor Yellow
    Write-Host "   3. Open: $apiUrl" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Done! Check the browser window for detailed results." -ForegroundColor Cyan
Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')

