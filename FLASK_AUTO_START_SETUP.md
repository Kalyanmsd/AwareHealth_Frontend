# 🚀 Flask API Automatic Startup - Complete Setup Guide

## ✅ What This Does

This setup will make the Flask API **automatically start** when:
- Windows boots up
- You log in to Windows

No manual steps needed! The Flask API will be ready whenever you need it.

---

## 🎯 Quick Setup (2 Steps)

### Step 1: Run the Setup Script

**Right-click** `setup_flask_autostart.bat` and select **"Run as Administrator"**

Or double-click it (it will prompt for admin if needed).

### Step 2: Done!

That's it! Flask API will now start automatically.

---

## 📋 What Gets Created

### 1. **Windows Scheduled Task**
- **Name**: `AwareHealth_FlaskAPI_AutoStart`
- **Triggers**: 
  - At Windows startup
  - At user logon
- **Action**: Starts Flask API in a minimized window

### 2. **Auto-Start Service Script**
- **File**: `auto_start_flask_api_service.ps1`
- **Purpose**: Script that Task Scheduler runs to start Flask API
- **Location**: Same folder as setup script

---

## 🔍 Verify It's Working

### Method 1: Check Task Scheduler
1. Press `Win + R`
2. Type `taskschd.msc` and press Enter
3. Look for task: `AwareHealth_FlaskAPI_AutoStart`
4. Should show "Ready" status

### Method 2: Test Immediately
Open PowerShell as Administrator and run:
```powershell
Start-ScheduledTask -TaskName "AwareHealth_FlaskAPI_AutoStart"
```

Then check if Flask API is running:
- Look for a minimized PowerShell window
- Or test: `http://localhost:5000/health` in browser

### Method 3: Restart Your Computer
After restart, Flask API should start automatically in a minimized window.

---

## 🛠️ Manual Control

### Start Flask API Manually (Even with Auto-Start)
Just double-click: `start_flask_api.bat`

### Stop Flask API
1. Find the Flask API window (check minimized windows)
2. Press `Ctrl + C` in that window
3. Or close the window

### Check if Flask API is Running
```powershell
netstat -ano | findstr :5000
```
If you see output, Flask API is running.

---

## ❌ Remove Auto-Start

If you want to remove automatic startup:

**Right-click** `remove_flask_autostart.ps1` and select **"Run as Administrator"**

Or run in PowerShell (as Admin):
```powershell
Unregister-ScheduledTask -TaskName "AwareHealth_FlaskAPI_AutoStart" -Confirm:$false
```

---

## 🔧 Troubleshooting

### Problem: Flask API doesn't start automatically

**Solution 1**: Check Task Scheduler
1. Open Task Scheduler (`taskschd.msc`)
2. Find `AwareHealth_FlaskAPI_AutoStart`
3. Check "Last Run Result" - should be "0x0" (success)
4. If error, check "History" tab for details

**Solution 2**: Check Python is in PATH
```powershell
python --version
```
If not found, install Python and add to PATH.

**Solution 3**: Check Flask API file exists
```
C:\xampp\htdocs\AwareHealth\aimodel\aware_health\flask_api.py
```

**Solution 4**: Run task manually to see errors
```powershell
Start-ScheduledTask -TaskName "AwareHealth_FlaskAPI_AutoStart"
```
Check the minimized window for error messages.

### Problem: Flask API starts but stops immediately

**Check 1**: Python dependencies
```powershell
cd C:\xampp\htdocs\AwareHealth\aimodel\aware_health
python flask_api.py
```
Look for import errors or missing packages.

**Check 2**: Port 5000 already in use
```powershell
netstat -ano | findstr :5000
```
If another process is using port 5000, stop it first.

### Problem: Task Scheduler shows error

**Check**: Task Scheduler History
1. Open Task Scheduler
2. Select `AwareHealth_FlaskAPI_AutoStart`
3. Click "History" tab
4. Look for error messages
5. Common issues:
   - Python not found → Install Python
   - File not found → Check Flask API path
   - Permission denied → Run setup as Administrator

---

## 📱 Testing After Auto-Start

### 1. Wait for System to Boot
Give Windows 10-15 seconds after boot/login for Flask API to start.

### 2. Test in Browser
Open: `http://localhost:5000/health`

Should see:
```json
{
  "status": "success",
  "model_status": "loaded",
  "message": "API is running"
}
```

### 3. Test from Phone
Open on phone browser: `http://172.20.10.2:5000/health`
(Replace with your computer's IP address)

### 4. Test in Android App
- Build and run AwareHealth app
- Navigate to Chatbot
- Type a disease name
- Should get AI response ✅

---

## 🎯 Best Practices

### Daily Workflow:
1. **Boot your computer** → Flask API starts automatically
2. **Open Android Studio** → Build and run app
3. **Test chatbot** → Works immediately! ✅

### If Flask API Doesn't Start:
1. Check minimized windows (look for PowerShell/Flask API)
2. Run `start_flask_api.bat` manually
3. Check Task Scheduler for errors

### When Developing:
- Keep Flask API window open (restore from minimized)
- Monitor logs for debugging
- Restart Flask API if needed: `Ctrl + C` then run `start_flask_api.bat`

---

## ✅ Summary

**After Setup:**
- ✅ Flask API starts automatically on boot/login
- ✅ Runs in minimized window
- ✅ Ready for Android app immediately
- ✅ No manual steps needed

**To Remove:**
- Run `remove_flask_autostart.ps1` as Administrator

**To Re-Enable:**
- Run `setup_flask_autostart.bat` as Administrator again

---

## 🚀 Enjoy Automatic Flask API!

Your Flask API will now start automatically, making development seamless! 🎉

