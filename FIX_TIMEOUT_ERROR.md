# ⚡ FIX TIMEOUT ERROR - Quick Solution

## 🚨 Current Error
**"Network error: timeout"**

This means the app can't connect to your server within 30 seconds.

---

## ✅ IMMEDIATE FIX (Do This First)

### Step 1: Check XAMPP is Running

1. **Open XAMPP Control Panel**
2. **Apache** → Must be **GREEN** ✅
3. **MySQL** → Must be **GREEN** ✅
4. **If RED** → Click **Start** button

**⚠️ CRITICAL:** Both must be running!

---

### Step 2: Test Server from Phone Browser

1. **Open browser on your phone**
2. **Type this URL:**
   ```
   http://192.168.1.11/AwareHealth/api/test_connection.php
   ```
3. **Expected:** You should see JSON response immediately
4. **If timeout/error** → Server not accessible (see Step 3)
5. **If works** → Server is OK, rebuild app (Step 4)

---

### Step 3: Check Windows Firewall

If Step 2 didn't work:

1. **Press `Windows + R`**
2. **Type:** `firewall.cpl` → Press Enter
3. **Click:** "Allow an app or feature through Windows Defender Firewall"
4. **Click:** "Change settings" (top right)
5. **Find:** "Apache HTTP Server" or "httpd.exe"
6. **Check both:** ✅ Private ✅ Public
7. **If not found:**
   - Click "Allow another app"
   - Browse → `C:\xampp\apache\bin\httpd.exe`
   - Add → Check both boxes

---

### Step 4: Rebuild App

1. **Android Studio:**
   - **Build** → **Clean Project**
   - **Build** → **Rebuild Project**
   - Wait 2-5 minutes

2. **Uninstall old app from phone**

3. **Install new version:**
   - Click **Run** button (▶️)

---

### Step 5: Verify Same Wi-Fi

**CRITICAL:** Phone and computer must be on **same Wi-Fi network!**

1. **Phone:** Settings → Wi-Fi → Check network name
2. **Computer:** Check Wi-Fi network name
3. **Must match!** If different, connect phone to same Wi-Fi

---

## 🔍 Quick Diagnostic

### Test These URLs in Phone Browser:

1. **Test connection:**
   ```
   http://192.168.1.11/AwareHealth/api/test_connection.php
   ```
   ✅ Should work immediately

2. **Test auth endpoint:**
   ```
   http://192.168.1.11/AwareHealth/api/auth/login
   ```
   ⚠️ Might show error (needs POST), but should connect

3. **If both timeout:**
   - XAMPP not running
   - Firewall blocking
   - Wrong IP address
   - Different Wi-Fi networks

---

## ✅ Success Checklist

- [ ] XAMPP Apache is GREEN (running)
- [ ] XAMPP MySQL is GREEN (running)
- [ ] Test URL works in phone browser
- [ ] Firewall allows Apache
- [ ] Phone and computer on same Wi-Fi
- [ ] Rebuilt app
- [ ] Login works! ✅

---

## 🆘 Still Timing Out?

### Check Android Studio Logcat

1. **Open Logcat** (bottom panel)
2. **Filter by:** `AuthViewModel` or `Retrofit`
3. **Look for red errors**
4. **Common errors:**

   **"UnknownHostException"**
   - Wrong IP address
   - Different Wi-Fi networks

   **"ConnectException"**
   - Apache not running
   - Firewall blocking

   **"SocketTimeoutException"**
   - Server too slow
   - Network issues

---

## 🎯 Most Common Issues

1. **XAMPP not running** → Start Apache & MySQL
2. **Firewall blocking** → Allow Apache through firewall
3. **Different Wi-Fi** → Connect to same network
4. **Wrong IP** → Already fixed (192.168.1.11)

**The timeout usually means XAMPP Apache is not running!** Check Step 1 first!

