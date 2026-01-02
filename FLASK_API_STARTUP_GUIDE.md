# 🚀 Flask API Automatic Startup Guide

This guide explains how to automatically start the Flask API server for AwareHealth.

## 📁 Available Scripts

### 1. **start_flask_api.bat** (Recommended for Windows)
- Simple batch file that starts Flask API in current window
- Easy to use - just double-click
- Shows server output directly
- **Best for**: Manual startup when needed

### 2. **start_flask_api.ps1** (PowerShell)
- PowerShell version with better error handling
- Checks for existing processes
- Shows colored output
- **Best for**: PowerShell users

### 3. **start_flask_api_background.ps1** (Background Mode)
- Starts Flask API in a separate window
- Runs in background
- **Best for**: Keeping server running while working

### 4. **start_all_services.bat** (All-in-One)
- Checks XAMPP Apache status
- Starts Flask API automatically
- **Best for**: Starting all services at once

---

## 🎯 Quick Start

### Option 1: Double-Click (Easiest)
1. Double-click `start_flask_api.bat`
2. Keep the window open
3. Server runs on `http://localhost:5000`

### Option 2: Command Line
```batch
start_flask_api.bat
```

### Option 3: PowerShell
```powershell
.\start_flask_api.ps1
```

### Option 4: Start All Services
```batch
start_all_services.bat
```

---

## ⚙️ Configuration

### Update Flask API Path

If your Flask API is in a different location, edit the script files and update this line:

**In .bat files:**
```batch
set FLASK_DIR=C:\xampp\htdocs\AwareHealth\aimodel\aware_health
```

**In .ps1 files:**
```powershell
$flaskDir = "C:\xampp\htdocs\AwareHealth\aimodel\aware_health"
```

---

## 🧪 Verify Flask API is Running

### Test in Browser:
```
http://localhost:5000/health
```

**Expected Response:**
```json
{
  "status": "success",
  "model_status": "loaded",
  "message": "API is running"
}
```

### Test from Phone (Same Wi-Fi):
```
http://172.20.10.2:5000/health
```
(Replace with your computer's IP address)

---

## 🛑 Stop Flask API Server

### Method 1: Close the Window
- Close the command window where Flask API is running
- Or press `Ctrl+C` in the window

### Method 2: PowerShell Command
```powershell
Get-Process python | Where-Object {$_.CommandLine -like "*flask_api*"} | Stop-Process
```

### Method 3: Kill by Port
```powershell
Get-NetTCPConnection -LocalPort 5000 | Select-Object -ExpandProperty OwningProcess | Stop-Process
```

---

## 🔍 Troubleshooting

### Issue: "Flask API directory not found"
**Solution**: Update the `FLASK_DIR` variable in the script to match your Flask API location.

### Issue: "Port 5000 is already in use"
**Solution**: 
1. Another Flask API instance might be running
2. Stop existing instance using the methods above
3. Or change Flask port in `flask_api.py` to a different port (e.g., 5001)

### Issue: "Python is not installed"
**Solution**: 
1. Install Python from https://www.python.org/
2. Make sure Python is added to PATH
3. Restart terminal/command prompt

### Issue: "Module not found" error
**Solution**: Install required Python packages:
```bash
pip install flask flask-cors pandas scikit-learn nltk
```

---

## 📱 Android App Integration

The Android app is configured to connect to:
```
http://172.20.10.2:5000/
```

Make sure:
1. ✅ Flask API is running (use one of the scripts above)
2. ✅ Phone and computer are on same Wi-Fi network
3. ✅ Windows Firewall allows port 5000
4. ✅ IP address matches your computer's current IP

---

## 🔄 Auto-Start on Windows Startup

To automatically start Flask API when Windows starts:

1. **Create a shortcut** to `start_flask_api.bat`
2. Press `Win + R` and type `shell:startup`
3. **Copy the shortcut** to the Startup folder
4. Flask API will start automatically when Windows boots

---

## 📝 Notes

- **Keep the Flask API window open** while developing
- The server must be running for the Android app chatbot to work
- If you see connection errors in the app, verify Flask API is running
- Port 5000 must be accessible (check firewall settings)

---

## ✅ Quick Checklist

Before testing the Android app:

- [ ] XAMPP Apache is running (port 80)
- [ ] Flask API is running (port 5000)
- [ ] Test `http://localhost:5000/health` in browser
- [ ] Phone and computer on same Wi-Fi
- [ ] IP address in app matches computer's IP
- [ ] Firewall allows ports 80 and 5000

---

**Happy Coding! 🚀**

