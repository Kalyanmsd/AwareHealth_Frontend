# ✅ Flask API Auto-Start Setup Complete!

## 🎉 What Was Set Up

Flask API will now **automatically start** when you log in to Windows!

---

## ✅ What Was Created

### 1. **Startup Shortcut**
- **Location**: `C:\Users\chilu\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup\AwareHealth_FlaskAPI.lnk`
- **Target**: `ensure_flask_running.bat`
- **Effect**: Flask API starts automatically when you log in

### 2. **Auto-Start Scripts**
- `setup_startup_folder.bat` - Easy setup (already run ✅)
- `setup_flask_autostart.bat` - Task Scheduler setup (optional, requires admin)
- `ensure_flask_running.bat` - The script that actually starts Flask API

---

## 🧪 Test It Now

### Option 1: Test Immediately
The Flask API should be starting now in the background. Check:

1. **Look for Flask API window** (might be minimized)
2. **Test in browser**: `http://localhost:5000/health`
3. **Check port**: `netstat -ano | findstr :5000`

### Option 2: Test After Restart
1. **Restart your computer**
2. **Log in**
3. **Flask API should start automatically** ✅
4. **Test**: `http://localhost:5000/health`

---

## 📱 How It Works

### When You Log In:
1. Windows runs all programs in Startup folder
2. `AwareHealth_FlaskAPI.lnk` shortcut runs
3. `ensure_flask_running.bat` executes
4. Script checks if Flask API is running (port 5000)
5. If not running → Starts Flask API automatically
6. Flask API runs in a separate window

### The Script:
- ✅ Checks if Flask API is already running (avoids duplicates)
- ✅ Starts Flask API if not running
- ✅ Shows Flask API in a separate window
- ✅ Displays server status and IP address

---

## 🔍 Verify Flask API is Running

### Method 1: Check the Window
- Look for "Flask API Server (Auto-Started)" window
- Should show: "Server running on: http://localhost:5000"

### Method 2: Test in Browser
```
http://localhost:5000/health
```
Should return:
```json
{
  "status": "success",
  "model_status": "loaded",
  "message": "API is running"
}
```

### Method 3: Check Port
```powershell
netstat -ano | findstr :5000
```
Should show port 5000 is LISTENING

### Method 4: Test from Phone
```
http://172.20.10.2:5000/health
```
(Replace with your computer's IP address)

---

## 🎯 Daily Workflow

### Now You Can:
1. **Boot your computer** → Flask API starts automatically ✅
2. **Open Android Studio** → Build and run app
3. **Test chatbot** → Works immediately! ✅

**No manual steps needed!** 🎉

---

## 🛠️ Manual Control

### Start Flask API Manually (if needed):
```batch
start_flask_api.bat
```

### Stop Flask API:
1. Find the Flask API window
2. Press `Ctrl + C` or close the window

### Check Status:
```powershell
netstat -ano | findstr :5000
```

---

## ❌ Remove Auto-Start (if needed)

### Method 1: Delete Shortcut
1. Press `Win + R`
2. Type: `shell:startup`
3. Delete `AwareHealth_FlaskAPI.lnk`

### Method 2: Use Script
```batch
remove_startup_shortcut.bat
```
(If you create this script)

---

## 🔧 Troubleshooting

### Problem: Flask API doesn't start automatically

**Solution 1**: Check Startup folder
- Press `Win + R` → `shell:startup`
- Verify `AwareHealth_FlaskAPI.lnk` exists

**Solution 2**: Check Python is installed
```powershell
python --version
```

**Solution 3**: Check Flask API file exists
```
C:\xampp\htdocs\AwareHealth\aimodel\aware_health\flask_api.py
```

**Solution 4**: Run manually to see errors
```batch
ensure_flask_running.bat
```

### Problem: Flask API starts but stops immediately

**Check**: Python dependencies
```powershell
cd C:\xampp\htdocs\AwareHealth\aimodel\aware_health
python flask_api.py
```
Look for import errors.

---

## ✅ Summary

**Status**: ✅ **AUTO-START CONFIGURED**

- ✅ Shortcut created in Startup folder
- ✅ Flask API will start automatically on login
- ✅ No manual steps needed
- ✅ Ready for Android app development

**Next Steps**:
1. Restart your computer (or log out/in) to test
2. Verify Flask API starts automatically
3. Test in Android app - chatbot should work! 🎉

---

## 🚀 Enjoy Automatic Flask API!

Your Flask API will now start automatically every time you log in, making development seamless! 🎉

