# 🔧 Fix Network Connection Error

## 🚨 Current Error
```
Network error: failed to connect to /172.20.10.2 (port 80) 
from /192.168.1.9 (port 49138) after 60000ms
```

**Your phone IP:** 192.168.1.9  
**Server IP:** 172.20.10.2  
**Problem:** Cannot connect to server

---

## ✅ Step-by-Step Fix

### Step 1: Check XAMPP is Running

1. **Open XAMPP Control Panel**
2. **Check Apache:**
   - Should show **GREEN** "Running"
   - If **RED** "Stopped" → Click **Start** button
3. **Check MySQL:**
   - Should show **GREEN** "Running"
   - If **RED** "Stopped" → Click **Start** button

**⚠️ IMPORTANT:** Both must be GREEN before proceeding!

---

### Step 2: Find Your Current Computer IP Address

Your IP might have changed! Let's find the correct one:

**On Windows:**
1. Press `Windows Key + R`
2. Type: `cmd` and press Enter
3. Type: `ipconfig` and press Enter
4. Look for **"Wireless LAN adapter Wi-Fi"** or **"Ethernet adapter"**
5. Find **"IPv4 Address"** - This is your current IP!

**Example output:**
```
Wireless LAN adapter Wi-Fi:
   IPv4 Address. . . . . . . . . . . : 192.168.1.100  ← THIS IS YOUR IP
```

**Note it down!** (Might be different from 172.20.10.2)

---

### Step 3: Test Connection from Phone Browser

1. **Open browser on your phone** (Chrome, etc.)
2. **Type in address bar:**
   ```
   http://YOUR_IP_HERE/AwareHealth/api/test_connection.php
   ```
   Replace `YOUR_IP_HERE` with the IP from Step 2
   
   **Example:** If your IP is `192.168.1.100`, type:
   ```
   http://192.168.1.100/AwareHealth/api/test_connection.php
   ```

3. **Expected result:**
   - ✅ **Works:** You'll see JSON response → Network is OK!
   - ❌ **Doesn't work:** Continue to Step 4

---

### Step 4: Update App BASE_URL

If Step 3 worked, update the app with your new IP:

1. **Open:** `app/src/main/java/com/example/awarehealth/data/RetrofitClient.kt`
2. **Find line 27:**
   ```kotlin
   private const val BASE_URL = "http://172.20.10.2/AwareHealth/api/"
   ```
3. **Replace with your IP from Step 2:**
   ```kotlin
   private const val BASE_URL = "http://192.168.1.100/AwareHealth/api/"  // Use YOUR IP
   ```
4. **Save file**
5. **Rebuild app:**
   - Build → Clean Project
   - Build → Rebuild Project
   - Run app again

---

### Step 5: Check Windows Firewall

If Step 3 didn't work, firewall might be blocking:

1. **Press `Windows Key + R`**
2. **Type:** `firewall.cpl` and press Enter
3. **Click:** "Allow an app or feature through Windows Defender Firewall"
4. **Click:** "Change settings" (top right)
5. **Find:** "Apache HTTP Server" or "httpd.exe"
6. **Check both boxes:** ✅ Private ✅ Public
7. **If not found:** Click "Allow another app" → Browse → Find `C:\xampp\apache\bin\httpd.exe` → Add

---

### Step 6: Verify Same Wi-Fi Network

**CRITICAL:** Phone and computer MUST be on the same Wi-Fi!

1. **On phone:** Settings → Wi-Fi → Check network name
2. **On computer:** Check Wi-Fi network name
3. **Must match!** If different, connect phone to same Wi-Fi as computer

---

## 🔍 Quick Diagnostic Test

Run this test to see what's wrong:

1. **On phone browser, try these URLs one by one:**

   ```
   http://localhost/AwareHealth/api/test_connection.php
   ```
   (Won't work on phone, but tests if you typed correctly)

   ```
   http://192.168.1.1/AwareHealth/api/test_connection.php
   ```
   (Your router IP - might work)

   ```
   http://YOUR_COMPUTER_IP/AwareHealth/api/test_connection.php
   ```
   (Use IP from Step 2 - should work!)

2. **Which one works?** That's the IP you need in the app!

---

## ✅ Success Checklist

- [ ] XAMPP Apache is GREEN (running)
- [ ] XAMPP MySQL is GREEN (running)
- [ ] Found current computer IP address
- [ ] Tested URL in phone browser - WORKS
- [ ] Updated BASE_URL in RetrofitClient.kt with correct IP
- [ ] Rebuilt and reinstalled app
- [ ] Phone and computer on same Wi-Fi
- [ ] Firewall allows Apache

---

## 🆘 Still Not Working?

### Check Android Studio Logcat

1. **Open Logcat** (bottom panel)
2. **Filter by:** `AuthViewModel` or `Retrofit`
3. **Look for red errors**
4. **Copy the full error message**

### Common Issues:

**"UnknownHostException"**
- IP address is wrong
- Phone and computer on different networks

**"Connection refused"**
- Apache not running
- Wrong port (should be port 80)

**"Connection timed out"**
- Firewall blocking
- Wrong IP address

**"Cleartext traffic not permitted"**
- Network security config issue (should already be fixed)

---

## 📱 Alternative: Use Emulator

If physical device keeps having issues:

1. **Use Android Emulator instead**
2. **Change BASE_URL to:**
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2/AwareHealth/api/"  // For Emulator
   ```
3. **Emulator automatically connects to localhost**

---

## 🎯 Quick Fix Summary

1. ✅ Start XAMPP Apache & MySQL
2. ✅ Find your computer IP (`ipconfig`)
3. ✅ Test in phone browser: `http://YOUR_IP/AwareHealth/api/test_connection.php`
4. ✅ Update BASE_URL in RetrofitClient.kt
5. ✅ Rebuild app
6. ✅ Test login again

**Most common issue:** IP address changed or XAMPP not running!

