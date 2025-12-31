# ⚡ QUICK FIX - Network Connection

## ✅ What I Just Fixed

**Updated BASE_URL from `172.20.10.2` to `192.168.1.11`**

Your computer's IP address changed! The app was trying to connect to the old IP.

---

## 🚀 Next Steps (Do This Now)

### Step 1: Make Sure XAMPP is Running

1. **Open XAMPP Control Panel**
2. **Apache** → Should be **GREEN** ✅
3. **MySQL** → Should be **GREEN** ✅
4. **If RED** → Click **Start** button

### Step 2: Test Connection from Phone

1. **Open browser on your phone**
2. **Type this URL:**
   ```
   http://192.168.1.11/AwareHealth/api/test_connection.php
   ```
3. **Should see:**
   ```json
   {
     "success": true,
     "message": "Backend is reachable!",
     ...
   }
   ```
4. **If you see this** → Network is working! ✅
5. **If error** → Check XAMPP is running (Step 1)

### Step 3: Rebuild and Reinstall App

1. **Android Studio:**
   - **Build** → **Clean Project**
   - **Build** → **Rebuild Project**
   - Wait for build to finish (2-5 minutes)

2. **Uninstall old app from phone:**
   - Long press AwareHealth icon
   - Uninstall

3. **Install new version:**
   - Click **Run** button (▶️) in Android Studio
   - App will install automatically

### Step 4: Test Login

1. **Open app on phone**
2. **Try to login**
3. **Should work now!** ✅

---

## 🔍 If Still Not Working

### Check These:

1. **Same Wi-Fi?**
   - Phone and computer must be on **same Wi-Fi network**
   - Check Wi-Fi name on both devices

2. **Firewall blocking?**
   - Windows might be blocking Apache
   - Press `Windows + R` → Type `firewall.cpl`
   - Allow "Apache HTTP Server" through firewall

3. **XAMPP Apache running?**
   - Must be GREEN in XAMPP Control Panel
   - If not, click Start

---

## 📱 Alternative: Use Emulator

If physical device keeps having issues:

1. **Use Android Emulator** (AVD)
2. **Change BASE_URL to:**
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2/AwareHealth/api/"
   ```
3. **Emulator automatically connects to localhost**

---

## ✅ Success Checklist

- [ ] XAMPP Apache is GREEN (running)
- [ ] XAMPP MySQL is GREEN (running)
- [ ] Test URL works in phone browser: `http://192.168.1.11/AwareHealth/api/test_connection.php`
- [ ] Rebuilt app in Android Studio
- [ ] Uninstalled old app from phone
- [ ] Installed new app
- [ ] Login works! ✅

---

## 🎯 Most Common Issues

1. **XAMPP not running** → Start Apache & MySQL
2. **Wrong IP** → Already fixed! (Now using 192.168.1.11)
3. **Different Wi-Fi** → Connect phone to same Wi-Fi as computer
4. **Firewall** → Allow Apache through Windows Firewall

**The IP address was the main issue - it's now fixed!** 🎉

