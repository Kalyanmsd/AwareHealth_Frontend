# 📋 Manual Guide: Copy Files to XAMPP

## Method 1: Using File Explorer (Easiest)

### Step 1: Open Both Folders

1. **Open Project Folder:**
   - Navigate to: `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend`

2. **Open XAMPP Folder:**
   - Navigate to: `C:\xampp\htdocs\AwareHealth`
   - If this folder doesn't exist, create it:
     - Go to `C:\xampp\htdocs`
     - Right-click → New → Folder
     - Name it: `AwareHealth`

### Step 2: Create Subdirectories in XAMPP

In `C:\xampp\htdocs\AwareHealth`, create these folders:
- `api` (if doesn't exist)
- `includes` (if doesn't exist)
- `database` (if doesn't exist)

### Step 3: Copy Files

#### Copy Config File:
- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\config.php`
- **To:** `C:\xampp\htdocs\AwareHealth\config.php`

#### Copy API Files:
- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\api\index.php`
- **To:** `C:\xampp\htdocs\AwareHealth\api\index.php`

- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\api\health.php`
- **To:** `C:\xampp\htdocs\AwareHealth\api\health.php`

- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\api\health_simple.php`
- **To:** `C:\xampp\htdocs\AwareHealth\api\health_simple.php`

- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\api\auth.php`
- **To:** `C:\xampp\htdocs\AwareHealth\api\auth.php`

#### Copy Include Files:
- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\includes\database.php`
- **To:** `C:\xampp\htdocs\AwareHealth\includes\database.php`

- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\includes\functions.php`
- **To:** `C:\xampp\htdocs\AwareHealth\includes\functions.php`

- **From:** `C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\includes\smtp_email.php`
- **To:** `C:\xampp\htdocs\AwareHealth\includes\smtp_email.php`

### Step 4: Verify Files

Check that these files exist in XAMPP:
```
C:\xampp\htdocs\AwareHealth\
├── config.php
├── api\
│   ├── index.php
│   ├── health.php
│   ├── health_simple.php
│   └── auth.php
└── includes\
    ├── database.php
    ├── functions.php
    └── smtp_email.php
```

---

## Method 2: Using PowerShell Script (Automated)

### Step 1: Open PowerShell

1. Press `Windows Key + X`
2. Select "Windows PowerShell" or "Terminal"
3. Navigate to project folder:
   ```powershell
   cd C:\Users\chilu\AndroidStudioProjects\AwareHealth2
   ```

### Step 2: Run the Script

```powershell
.\COPY_TO_XAMPP.ps1
```

If you get an execution policy error, run:
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```
Then run the script again.

---

## Method 3: Using Command Prompt

### Step 1: Open Command Prompt

1. Press `Windows Key + R`
2. Type `cmd` and press Enter

### Step 2: Copy Files Using Commands

```cmd
REM Create directories
mkdir C:\xampp\htdocs\AwareHealth\api
mkdir C:\xampp\htdocs\AwareHealth\includes

REM Copy config
copy "C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\config.php" "C:\xampp\htdocs\AwareHealth\config.php"

REM Copy API files
copy "C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\api\*.php" "C:\xampp\htdocs\AwareHealth\api\"

REM Copy include files
copy "C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\includes\*.php" "C:\xampp\htdocs\AwareHealth\includes\"
```

---

## ✅ After Copying Files

### Step 1: Restart Apache

1. Open **XAMPP Control Panel**
2. Click **Stop** next to Apache (if it's running)
3. Wait a few seconds
4. Click **Start** next to Apache
5. Make sure it turns green

### Step 2: Test in Browser

Open your browser and test:
```
http://localhost/AwareHealth/api/health_simple
```

You should see JSON response with diseases.

### Step 3: Test Main Endpoint

```
http://localhost/AwareHealth/api/health/diseases
```

---

## 🔍 Troubleshooting

### Issue: "File not found" error
- **Fix:** Check file paths are correct
- Verify files exist in source location

### Issue: "Permission denied"
- **Fix:** Run PowerShell/CMD as Administrator
- Or manually copy files using File Explorer

### Issue: Files copied but API still not working
- **Fix:** Restart Apache in XAMPP
- Check Apache error log: `C:\xampp\apache\logs\error.log`

---

## 📝 Quick Checklist

- [ ] Created `C:\xampp\htdocs\AwareHealth` folder
- [ ] Created `api` and `includes` subfolders
- [ ] Copied `config.php` to root
- [ ] Copied all files from `backend\api\` to `api\`
- [ ] Copied all files from `backend\includes\` to `includes\`
- [ ] Restarted Apache in XAMPP
- [ ] Tested in browser

---

## 🎯 File Structure After Copying

```
C:\xampp\htdocs\AwareHealth\
│
├── config.php
│
├── api\
│   ├── index.php
│   ├── health.php
│   ├── health_simple.php
│   ├── auth.php
│   └── test_health.php
│
└── includes\
    ├── database.php
    ├── functions.php
    └── smtp_email.php
```

---

## 💡 Tips

1. **Always restart Apache** after copying files
2. **Test in browser first** before testing in app
3. **Check error logs** if something doesn't work
4. **Keep files in sync** - update XAMPP when you change backend files


