# 🚀 Quick Setup: Flask API Auto-Start

## ✅ Easiest Method (No Admin Required)

### Option 1: Windows Startup Folder (Recommended)

1. **Create a shortcut:**
   - Right-click `ensure_flask_running.bat`
   - Select "Create shortcut"

2. **Move to Startup folder:**
   - Press `Win + R`
   - Type: `shell:startup`
   - Press Enter
   - **Copy** the shortcut you created into this folder

3. **Done!** Flask API will start automatically when you log in.

---

## 🔧 Method 2: Task Scheduler (Requires Admin)

### Step 1: Run Setup Script
**Right-click** `setup_flask_autostart.bat` → **"Run as Administrator"**

### Step 2: Verify
- Open Task Scheduler (`Win + R` → `taskschd.msc`)
- Look for: `AwareHealth_FlaskAPI_AutoStart`
- Should show "Ready"

---

## 🧪 Test It Works

### Test Immediately:
```powershell
# If using Task Scheduler:
Start-ScheduledTask -TaskName "AwareHealth_FlaskAPI_AutoStart"

# Or just run:
.\ensure_flask_running.bat
```

### Check Flask API:
- Open browser: `http://localhost:5000/health`
- Should see JSON response ✅

---

## 📱 After Setup

Flask API will automatically:
- ✅ Start when Windows boots (Task Scheduler method)
- ✅ Start when you log in (Both methods)
- ✅ Run in minimized window
- ✅ Be ready for your Android app

---

## ❌ Remove Auto-Start

### Startup Folder Method:
- Delete the shortcut from `shell:startup` folder

### Task Scheduler Method:
- Right-click `remove_flask_autostart.ps1` → "Run as Administrator"

---

**Choose the method that works best for you!** 🎉

