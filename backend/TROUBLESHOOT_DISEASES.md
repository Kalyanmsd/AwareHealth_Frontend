# 🔍 Troubleshooting: Diseases Not Showing

## ✅ Quick Checklist

### 1. **Database Tables Created?**
- [ ] Open phpMyAdmin
- [ ] Select `awarehealth` database
- [ ] Check if `diseases` table exists
- [ ] Check if table has data (should have 50+ rows)

**If table doesn't exist or is empty:**
1. Import `backend/database/create_health_tables.sql`
2. Import `backend/database/add_more_diseases.sql`

### 2. **Backend Files Copied?**
- [ ] `backend/api/health.php` → `C:\xampp\htdocs\AwareHealth\api\health.php`
- [ ] `backend/api/index.php` → `C:\xampp\htdocs\AwareHealth\api\index.php`
- [ ] `backend/includes/functions.php` → `C:\xampp\htdocs\AwareHealth\includes\functions.php`

### 3. **API Endpoint Working?**
Test in browser:
```
http://localhost/AwareHealth/api/health/diseases
```

**Expected Response:**
```json
{
  "success": true,
  "diseases": [...],
  "count": 50
}
```

**If you get 404:**
- Check `index.php` has `case 'health':` routing
- Check Apache is running
- Check file paths are correct

**If you get 500 error:**
- Check error log: `C:\xampp\apache\logs\error.log`
- Check database connection in `config.php`
- Check `diseases` table exists

**If you get empty array:**
- Database table is empty
- Import SQL files

### 4. **App Configuration?**
- [ ] `BASE_URL` in `RetrofitClient.kt` is correct
- [ ] For physical device: Use your computer's IP (e.g., `http://192.168.1.10/AwareHealth/api/`)
- [ ] For emulator: Use `http://10.0.2.2/AwareHealth/api/`

### 5. **Check Logcat**
In Android Studio:
1. Open **Logcat**
2. Filter by: `HealthViewModel`
3. Look for:
   - "Loading diseases..."
   - "Response received: true/false"
   - "Loaded X diseases"
   - Any error messages

---

## 🐛 Common Issues

### Issue 1: "No diseases found" but database has data

**Possible causes:**
1. API endpoint not accessible
2. Network configuration wrong
3. Response format mismatch

**Solution:**
1. Test API in browser first
2. Check Logcat for error messages
3. Verify BASE_URL matches your network setup

### Issue 2: API returns 404

**Solution:**
1. Check `index.php` routing
2. Verify `health.php` exists in `api/` folder
3. Check Apache error logs

### Issue 3: API returns 500

**Solution:**
1. Check database connection
2. Check `diseases` table exists
3. Check error log: `C:\xampp\apache\logs\error.log`

### Issue 4: Empty array returned

**Solution:**
1. Import SQL files in phpMyAdmin
2. Verify data exists: `SELECT COUNT(*) FROM diseases;`
3. Should return 50+

---

## 📋 Step-by-Step Fix

### Step 1: Verify Database
```sql
-- Run in phpMyAdmin
SELECT COUNT(*) FROM diseases;
-- Should return 50 or more
```

### Step 2: Test API
1. Open browser
2. Go to: `http://localhost/AwareHealth/api/health/diseases`
3. Should see JSON with diseases

### Step 3: Check App Logs
1. Open Android Studio
2. Run app
3. Open Logcat
4. Filter: `HealthViewModel`
5. Look for error messages

### Step 4: Verify Network
1. Check BASE_URL in `RetrofitClient.kt`
2. For physical device: Use computer's IP
3. Device and computer must be on same Wi-Fi

---

## 🔧 Debug Commands

### Check if table exists:
```sql
SHOW TABLES LIKE 'diseases';
```

### Check table data:
```sql
SELECT id, name, category FROM diseases LIMIT 10;
```

### Check API response:
```bash
# In browser or Postman
GET http://localhost/AwareHealth/api/health/diseases
```

---

## ✅ Success Indicators

When working correctly:
- ✅ Browser shows JSON with diseases array
- ✅ Logcat shows "Loaded X diseases"
- ✅ App shows disease cards
- ✅ No error messages

---

## 📞 Still Not Working?

1. **Check error logs:**
   - Apache: `C:\xampp\apache\logs\error.log`
   - PHP: `C:\xampp\php\logs\php_error_log`
   - App: Android Studio Logcat

2. **Verify all files:**
   - All backend files in XAMPP
   - Database tables created
   - SQL data imported

3. **Test API directly:**
   - Use browser or Postman
   - Verify response format matches app expectations

