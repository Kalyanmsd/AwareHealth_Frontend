# 🔧 Fix: "Endpoint not found" for Diseases

## 🐛 Problem
Getting error: `{"success":false,"message":"Endpoint not found"}` when trying to access diseases list.

## ✅ Solution

### Step 1: Copy Updated Files to XAMPP

Copy these files to `C:\xampp\htdocs\AwareHealth\api\`:

1. **`backend/api/health.php`** (UPDATED - better routing logic)
2. **`backend/api/index.php`** (UPDATED - added test_diseases route)
3. **`backend/api/test_diseases.php`** (NEW - direct test endpoint)

### Step 2: Restart Apache

1. Open **XAMPP Control Panel**
2. **Stop** Apache
3. **Start** Apache again

### Step 3: Test in Browser

Test these URLs in your browser:

**Test 1: Direct test endpoint**
```
http://172.20.10.2/AwareHealth/api/test_diseases.php
```

**Test 2: Health API endpoint**
```
http://172.20.10.2/AwareHealth/api/health/diseases
```

**Expected Response:**
```json
{
  "success": true,
  "diseases": [...],
  "count": 50
}
```

### Step 4: Check Error Logs

If still not working, check:
- `C:\xampp\apache\logs\error.log`
- Look for "Health API" entries
- Check what segments are being parsed

### Step 5: Verify Database

Make sure the `diseases` table exists and has data:

1. Open **phpMyAdmin**
2. Select **`awarehealth`** database
3. Check **`diseases`** table exists
4. Check it has rows (should have 50 diseases)

### Step 6: Rebuild App

1. **Build → Rebuild Project** in Android Studio
2. **Run the app**
3. **Check Logcat** for detailed error messages

---

## 🔍 Debugging

### Check What's Happening

The API logs will show:
```
Health API - Path segments: ["health","diseases"]
Health API - Resolved endpoint: 'diseases'
Health API - Full request: /AwareHealth/api/health/diseases
```

### Common Issues

**Issue 1: Still getting "Endpoint not found"**
- ✅ Check if `health.php` is in `C:\xampp\htdocs\AwareHealth\api\`
- ✅ Verify Apache is restarted
- ✅ Check error log for actual segments
- ✅ Test `test_diseases.php` directly

**Issue 2: Empty diseases array**
- ✅ Check if `diseases` table exists
- ✅ Check if table has data
- ✅ Verify database connection in `config.php`

**Issue 3: Network error**
- ✅ Check IP address is `172.20.10.2` in `RetrofitClient.kt`
- ✅ Verify XAMPP Apache is running
- ✅ Test URL in phone browser

---

## 📱 Quick Test from Phone

1. Open browser on your phone
2. Go to: `http://172.20.10.2/AwareHealth/api/test_diseases.php`
3. You should see JSON with diseases
4. If this works, the app should work too

---

## ✅ Success Indicators

After fixing, you should see:
- ✅ Diseases list loads in app
- ✅ No "Endpoint not found" error
- ✅ Can search and filter diseases
- ✅ Can view disease details

