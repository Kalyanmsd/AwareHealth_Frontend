# 🔧 Troubleshooting: Diseases Not Showing

## 🐛 Problem
Files are in XAMPP but diseases list is still not showing in the app.

## ✅ Step-by-Step Fix

### Step 1: Test Database Connection

Open in browser:
```
http://172.20.10.2/AwareHealth/api/debug_health.php
```

**Check these:**
- ✅ `database.connection` = "SUCCESS"
- ✅ `tables.diseases` = "EXISTS"
- ✅ `tables.diseases_count` > 0 (should be 50)
- ✅ `data.sample_diseases` has data

**If database connection fails:**
- Check MySQL is running in XAMPP
- Check database name is `awarehealth`
- Check username is `root` and password is empty

**If table doesn't exist:**
- Import `backend/database/create_all_tables_complete.sql` in phpMyAdmin
- Import `backend/database/create_health_tables.sql` for sample data
- Import `backend/database/add_more_diseases.sql` for more diseases

### Step 2: Test Simple Endpoint

Open in browser:
```
http://172.20.10.2/AwareHealth/api/simple_diseases.php
```

**Expected:** JSON with `success: true` and `diseases` array

**If this fails:**
- Check database connection
- Check `diseases` table exists
- Check table has data

### Step 3: Test Health API Endpoint

Open in browser:
```
http://172.20.10.2/AwareHealth/api/health/diseases
```

**Expected:** JSON with `success: true` and `diseases` array

**If this fails:**
- Check `health.php` is in `C:\xampp\htdocs\AwareHealth\api\`
- Check `index.php` routing is correct
- Check Apache error log: `C:\xampp\apache\logs\error.log`

### Step 4: Check Apache Error Log

Open file:
```
C:\xampp\apache\logs\error.log
```

**Look for:**
- "Health API" entries
- Database connection errors
- PHP errors

### Step 5: Verify Files in XAMPP

Check these files exist:
- ✅ `C:\xampp\htdocs\AwareHealth\api\index.php`
- ✅ `C:\xampp\htdocs\AwareHealth\api\health.php`
- ✅ `C:\xampp\htdocs\AwareHealth\api\simple_diseases.php`
- ✅ `C:\xampp\htdocs\AwareHealth\api\debug_health.php`
- ✅ `C:\xampp\htdocs\AwareHealth\config.php`
- ✅ `C:\xampp\htdocs\AwareHealth\includes\database.php`
- ✅ `C:\xampp\htdocs\AwareHealth\includes\functions.php`

### Step 6: Restart Apache

1. **XAMPP Control Panel**
2. **Stop** Apache
3. **Wait 5 seconds**
4. **Start** Apache again

### Step 7: Test from Phone Browser

1. Open browser on your phone
2. Go to: `http://172.20.10.2/AwareHealth/api/simple_diseases.php`
3. You should see JSON data

**If this works:** Backend is fine, issue is in app
**If this fails:** Backend issue, check network/firewall

### Step 8: Check App Configuration

Verify in `RetrofitClient.kt`:
```kotlin
private const val BASE_URL = "http://172.20.10.2/AwareHealth/api/"
```

### Step 9: Check Logcat

In Android Studio:
1. Open **Logcat**
2. Filter by: `HealthViewModel`
3. Look for:
   - "Loading diseases"
   - "Response received"
   - Error messages

### Step 10: Rebuild App

1. **Build → Clean Project**
2. **Build → Rebuild Project**
3. **Run** the app

---

## 🔍 Common Issues

### Issue 1: "Endpoint not found"
**Fix:**
- Copy `health.php` to XAMPP
- Restart Apache
- Check `index.php` routing

### Issue 2: Empty diseases array
**Fix:**
- Check database has data
- Run SQL: `SELECT COUNT(*) FROM diseases;`
- Import sample data if empty

### Issue 3: Network error
**Fix:**
- Check IP is `172.20.10.2`
- Check phone and computer on same Wi-Fi
- Check firewall allows port 80
- Test URL in phone browser

### Issue 4: Database connection failed
**Fix:**
- Check MySQL is running
- Check `config.php` has correct credentials
- Check database `awarehealth` exists

### Issue 5: Table doesn't exist
**Fix:**
- Import `create_all_tables_complete.sql`
- Check in phpMyAdmin: `awarehealth` → `diseases` table

---

## ✅ Success Checklist

- [ ] `debug_health.php` shows database connection success
- [ ] `debug_health.php` shows diseases table exists
- [ ] `debug_health.php` shows diseases count > 0
- [ ] `simple_diseases.php` returns JSON with diseases
- [ ] `health/diseases` endpoint works in browser
- [ ] Phone browser can access `simple_diseases.php`
- [ ] App BASE_URL is `172.20.10.2`
- [ ] Apache is running
- [ ] MySQL is running
- [ ] Files are in XAMPP
- [ ] App rebuilt and running

---

## 📞 Still Not Working?

1. **Check debug_health.php output** - shows exactly what's wrong
2. **Check Apache error log** - shows PHP errors
3. **Check Logcat** - shows app-side errors
4. **Test each endpoint** - isolate the problem

