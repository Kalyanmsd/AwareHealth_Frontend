# 🔧 Fix: Server Error for Health API

## 🐛 Problem
Getting "server error" when trying to load diseases.

## ✅ Solutions Applied

### 1. **Added Better Error Handling**
- More specific error messages in `HealthViewModel.kt`
- Different messages for different error types (timeout, connection, etc.)

### 2. **Created Test Endpoint**
- New file: `backend/api/test_health.php`
- Test URL: `http://192.168.1.10/AwareHealth/api/test_health`
- Use this to verify API is accessible

### 3. **Updated Routing**
- Added `test_health` route in `index.php`

---

## 📋 Step-by-Step Fix

### Step 1: Test API in Browser

**Test 1 - Simple Health Check:**
```
http://192.168.1.10/AwareHealth/api/test_health
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Health API is working",
  "database_connected": true,
  "diseases_count": 50
}
```

**Test 2 - Diseases Endpoint:**
```
http://192.168.1.10/AwareHealth/api/health/diseases
```

**Expected Response:**
```json
{
  "success": true,
  "diseases": [...],
  "count": 50
}
```

### Step 2: Check Common Issues

#### Issue 1: 404 Not Found
**Cause:** Files not in XAMPP or routing wrong

**Fix:**
1. Copy files to XAMPP:
   - `backend/api/health.php` → `C:\xampp\htdocs\AwareHealth\api\health.php`
   - `backend/api/index.php` → `C:\xampp\htdocs\AwareHealth\api\index.php`
   - `backend/api/test_health.php` → `C:\xampp\htdocs\AwareHealth\api\test_health.php`

2. Restart Apache in XAMPP

#### Issue 2: 500 Internal Server Error
**Cause:** Database connection or query error

**Fix:**
1. Check error log: `C:\xampp\apache\logs\error.log`
2. Verify database exists: `awarehealth`
3. Verify table exists: `diseases`
4. Check `config.php` database credentials

#### Issue 3: Connection Timeout
**Cause:** Wrong IP address or network issue

**Fix:**
1. Check your computer's IP: Run `ipconfig` in CMD
2. Update `BASE_URL` in `RetrofitClient.kt` if IP changed
3. Ensure device and computer on same Wi-Fi
4. Check Windows Firewall isn't blocking

#### Issue 4: Empty Response
**Cause:** Database table is empty

**Fix:**
1. Import SQL files in phpMyAdmin:
   - `backend/database/create_health_tables.sql`
   - `backend/database/add_more_diseases.sql`

---

## 🔍 Debugging Steps

### 1. Check XAMPP Status
- ✅ Apache is running (green in XAMPP Control Panel)
- ✅ MySQL is running (green in XAMPP Control Panel)

### 2. Test in Browser First
Always test API in browser before testing in app:
```
http://192.168.1.10/AwareHealth/api/test_health
http://192.168.1.10/AwareHealth/api/health/diseases
```

### 3. Check Logcat
In Android Studio Logcat, filter by `HealthViewModel`:
- Look for error messages
- Check HTTP response codes
- See detailed error information

### 4. Check Apache Error Log
Open: `C:\xampp\apache\logs\error.log`
- Look for PHP errors
- Check for database connection errors
- See detailed error messages

---

## 🎯 Quick Fix Checklist

- [ ] XAMPP Apache is running
- [ ] XAMPP MySQL is running
- [ ] Files copied to `C:\xampp\htdocs\AwareHealth\api\`
- [ ] Database `awarehealth` exists
- [ ] Table `diseases` exists and has data
- [ ] Test endpoint works in browser: `http://192.168.1.10/AwareHealth/api/test_health`
- [ ] Diseases endpoint works in browser: `http://192.168.1.10/AwareHealth/api/health/diseases`
- [ ] `BASE_URL` in `RetrofitClient.kt` matches your IP
- [ ] Device and computer on same Wi-Fi network
- [ ] App rebuilt after changes

---

## 📞 Still Not Working?

### Check Error Logs:
1. **Apache Error Log:** `C:\xampp\apache\logs\error.log`
2. **PHP Error Log:** `C:\xampp\php\logs\php_error_log`
3. **Android Logcat:** Filter by `HealthViewModel`

### Verify Network:
1. Ping your computer from device: `ping 192.168.1.10`
2. Check firewall settings
3. Try using `localhost` if on emulator: `http://10.0.2.2/AwareHealth/api/`

### Test Database:
```sql
-- Run in phpMyAdmin
SELECT COUNT(*) FROM diseases;
-- Should return 50+
```

---

## ✅ Success Indicators

When working correctly:
- ✅ Browser test shows JSON response
- ✅ Logcat shows "Loaded X diseases"
- ✅ App displays disease cards
- ✅ No error messages

