# 🚨 QUICK FIX: "Endpoint not found" Error

## ⚡ Immediate Solution

The error "Endpoint not found" means the routing isn't working. Here's the quickest fix:

### Option 1: Use Simplified Endpoint (FASTEST)

1. **Copy this file to XAMPP:**
   - `backend/api/health_simple.php` → `C:\xampp\htdocs\AwareHealth\api\health_simple.php`

2. **Update App to use simple endpoint:**
   - Change API endpoint in `ApiService.kt` from `health/diseases` to `health_simple`

3. **Test:**
   ```
   http://192.168.1.10/AwareHealth/api/health_simple
   ```

### Option 2: Fix Routing (RECOMMENDED)

1. **Copy ALL files to XAMPP:**
   ```
   backend/api/health.php → C:\xampp\htdocs\AwareHealth\api\health.php
   backend/api/index.php → C:\xampp\htdocs\AwareHealth\api\index.php
   backend/includes/functions.php → C:\xampp\htdocs\AwareHealth\includes\functions.php
   ```

2. **Restart Apache** in XAMPP

3. **Test in browser:**
   ```
   http://192.168.1.10/AwareHealth/api/health/diseases
   ```

4. **Check error log:**
   - `C:\xampp\apache\logs\error.log`
   - Look for "Health API" entries

---

## 🔍 Debugging Steps

### Step 1: Verify Files Exist
Check these files exist in XAMPP:
- ✅ `C:\xampp\htdocs\AwareHealth\api\index.php`
- ✅ `C:\xampp\htdocs\AwareHealth\api\health.php`
- ✅ `C:\xampp\htdocs\AwareHealth\includes\functions.php`

### Step 2: Test Direct Access
Try accessing health.php directly:
```
http://192.168.1.10/AwareHealth/api/health.php
```
(Should show error or work if routing is bypassed)

### Step 3: Check Apache Error Log
Open: `C:\xampp\apache\logs\error.log`
Look for:
- PHP errors
- "Health API" log entries
- Path segment information

### Step 4: Test Simple Endpoint
```
http://192.168.1.10/AwareHealth/api/health_simple
```
This should work if database is set up.

---

## 🎯 Most Likely Issues

### Issue 1: Files Not Copied
**Fix:** Copy all backend files to XAMPP `htdocs` folder

### Issue 2: Apache Not Restarted
**Fix:** Stop and start Apache in XAMPP

### Issue 3: Wrong File Paths
**Fix:** Verify files are in correct locations

### Issue 4: Database Not Set Up
**Fix:** Import SQL files in phpMyAdmin

---

## ✅ Success Test

When working, this URL should return JSON:
```
http://192.168.1.10/AwareHealth/api/health/diseases
```

Response should be:
```json
{
  "success": true,
  "diseases": [...],
  "count": 50
}
```

---

## 📝 Files to Copy

1. `backend/api/health.php`
2. `backend/api/index.php`
3. `backend/api/health_simple.php` (for testing)
4. `backend/includes/functions.php`
5. `backend/includes/database.php`
6. `backend/config.php`

All to: `C:\xampp\htdocs\AwareHealth\`

