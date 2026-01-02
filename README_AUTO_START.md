# 🚀 Automatic Flask API Startup - Complete Guide

## ✅ Solution Implemented

The Flask API will now **automatically start** when needed, ensuring the chatbot always works!

---

## 🎯 Quick Start (Easiest Method)

### Option 1: Use the Launcher (Recommended)
**Just double-click this file before opening Android Studio:**
```
launch_awarehealth.bat
```

This will:
- ✅ Check if Flask API is running
- ✅ Start Flask API automatically if not running
- ✅ Check XAMPP Apache status
- ✅ Show you everything is ready

---

## 📋 Available Scripts

### 1. **`launch_awarehealth.bat`** ⭐ **USE THIS ONE**
- **Purpose**: Main launcher - starts everything automatically
- **When to use**: Before opening Android Studio
- **How**: Just double-click it!

### 2. **`ensure_flask_running.bat`**
- **Purpose**: Checks and starts Flask API if not running
- **When to use**: When you need to ensure Flask is running
- **How**: Double-click or call from other scripts

### 3. **`auto_start_flask_api.ps1`**
- **Purpose**: PowerShell version with better error handling
- **When to use**: If you prefer PowerShell
- **How**: Run in PowerShell: `.\auto_start_flask_api.ps1`

### 4. **`start_development_environment.bat`**
- **Purpose**: Starts all services and shows status
- **When to use**: When setting up development environment
- **How**: Double-click

---

## 🔄 Automatic Startup Methods

### Method 1: Manual Launch (Before Development)
1. Double-click `launch_awarehealth.bat`
2. Flask API window will open automatically
3. Open Android Studio
4. Build and run your app
5. **Chatbot will work!** ✅

### Method 2: Windows Startup (Auto-start on Boot)
1. Right-click `launch_awarehealth.bat`
2. Select "Create shortcut"
3. Press `Win + R` and type `shell:startup`
4. Copy the shortcut to the Startup folder
5. Flask API will start automatically when Windows boots

### Method 3: Android Studio Pre-Build (Advanced)
You can configure Android Studio to run the script before building:
1. Go to: **File → Settings → Build, Execution, Deployment → Build Tools → Gradle**
2. Add to "Run before build":
   ```
   cmd /c ensure_flask_running.bat
   ```

---

## 🧪 How It Works

### When You Run `launch_awarehealth.bat`:

1. **Checks Flask API Status**
   - Tests if port 5000 is in use
   - If running → Shows "already running"
   - If not running → Starts it automatically

2. **Starts Flask API**
   - Opens Flask API in a new window
   - Window shows server status
   - Server runs on `http://localhost:5000`

3. **Verifies Startup**
   - Waits 3 seconds
   - Checks if port 5000 is now active
   - Confirms success

---

## ✅ Verification

### Check Flask API is Running:

**Method 1: Check the Window**
- Look for "Flask API Server" window
- Should show "Server running on: http://localhost:5000"

**Method 2: Test in Browser**
- Open: `http://localhost:5000/health`
- Should see JSON response:
  ```json
  {
    "status": "success",
    "model_status": "loaded",
    "message": "API is running"
  }
  ```

**Method 3: Check Port**
- Run: `netstat -ano | findstr :5000`
- Should show port 5000 is LISTENING

---

## 🔧 Troubleshooting

### Problem: Flask API doesn't start automatically

**Solution 1**: Check Python is installed
```powershell
python --version
```
If not found, install Python and add to PATH.

**Solution 2**: Check Flask API file exists
```
C:\xampp\htdocs\AwareHealth\aimodel\aware_health\flask_api.py
```
If missing, verify the path in the script.

**Solution 3**: Check permissions
- Right-click script → Run as Administrator
- Or check if Python can access the Flask directory

### Problem: Flask API starts but chatbot still doesn't work

**Check 1**: Network connection
- Phone and computer on same Wi-Fi
- IP address matches: `172.20.10.2`

**Check 2**: Firewall
- Windows Firewall might block port 5000
- Allow Python through firewall

**Check 3**: Flask API response
- Test: `http://172.20.10.2:5000/health` from phone browser
- Should return JSON response

---

## 📱 Testing the Chatbot

### After Flask API is Running:

1. **Build Android App** in Android Studio
2. **Run on Device/Emulator**
3. **Navigate to Chatbot** screen
4. **Type a disease name** (e.g., "fever", "headache")
5. **See AI response** with:
   - Main message
   - Identified Symptoms
   - Prevention Tips

---

## 🎯 Best Practice Workflow

### Daily Development Routine:

1. **Start your day:**
   ```
   Double-click: launch_awarehealth.bat
   ```

2. **Verify services:**
   - Flask API window is open
   - XAMPP Apache is running (green in XAMPP Control Panel)

3. **Open Android Studio:**
   - Build and run your app
   - Test chatbot functionality

4. **End of day:**
   - Close Flask API window (Ctrl+C or close window)
   - Or leave it running if you want

---

## ✅ Status Checklist

Before testing chatbot:

- [ ] Flask API is running (check window or port 5000)
- [ ] XAMPP Apache is running (port 80)
- [ ] Phone and computer on same Wi-Fi
- [ ] IP address is `172.20.10.2` (check with `ipconfig`)
- [ ] Test `http://localhost:5000/health` works
- [ ] Test `http://172.20.10.2:5000/health` from phone browser

---

## 🚀 Summary

**The Flask API will now start automatically!**

- ✅ Use `launch_awarehealth.bat` before development
- ✅ Flask API starts automatically if not running
- ✅ Chatbot will work seamlessly
- ✅ No manual steps needed after first setup

**Just double-click and go! 🎉**

