# 🚀 Quick Setup Instructions

## Step 1: Start XAMPP Services

1. Open **XAMPP Control Panel**
2. Start **Apache** (click "Start" - should turn green)
3. Start **MySQL** (click "Start" - should turn green)

## Step 2: Run Database Setup

**Option A: Double-click this file:**
```
setup_doctors.bat
```

**Option B: Run manually:**
```
C:\xampp\php\php.exe setup_doctors_database.php
```

This will:
- ✅ Create the database
- ✅ Create the doctors table
- ✅ Insert 5 sample doctors

## Step 3: Copy Files to XAMPP (if needed)

If your files are not already in `C:\xampp\htdocs\AwareHealth\`:

1. Copy the entire `backend` folder to:
   ```
   C:\xampp\htdocs\AwareHealth\backend\
   ```

2. Or create the API endpoint directly at:
   ```
   C:\xampp\htdocs\AwareHealth\api\get_doctors.php
   ```
   (Copy `backend/api/get_doctors_simple.php` and rename it)

## Step 4: Test API

Open in browser:
```
http://localhost/AwareHealth/backend/api/get_doctors_simple.php
```

Or:
```
http://localhost/AwareHealth/api/get_doctors.php
```

You should see JSON with doctors list.

## Step 5: Refresh Android App

1. Open your AwareHealth app
2. Go to "Select Doctor" screen
3. Pull down to refresh OR tap the refresh button
4. You should now see the doctors list!

---

## Troubleshooting

### "Failed to connect" error:
- ✅ Check XAMPP MySQL is running (green in Control Panel)
- ✅ Check XAMPP Apache is running (green in Control Panel)

### "Not Found" error:
- ✅ Files need to be in `C:\xampp\htdocs\AwareHealth\`
- ✅ Check the URL matches your file location

### "No doctors available":
- ✅ Run `setup_doctors.bat` to set up database
- ✅ Check database has doctors: Open phpMyAdmin → Select `awarehealth` → Browse `doctors` table

