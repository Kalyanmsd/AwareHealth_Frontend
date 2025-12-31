# ⚡ INSTANT FIX - Doctors Not Showing

## 🚀 Quick Solution (30 seconds)

### Step 1: Open This URL in Your Phone Browser

```
http://192.168.1.11/AwareHealth/api/quick_setup_doctors.php
```

**OR on your computer browser:**
```
http://localhost/AwareHealth/api/quick_setup_doctors.php
```

### Step 2: You Should See

```json
{
  "success": true,
  "message": "Doctors setup complete!",
  "doctors_count": 5,
  ...
}
```

### Step 3: Refresh App

1. **Open app** on phone
2. **Go to Select Doctor screen**
3. **Tap refresh button** (🔄) or go back and return
4. **Doctors should appear!** ✅

---

## ✅ That's It!

If you see `"doctors_count": 5` → Doctors are ready!

---

## 🔍 If Still Not Showing

### Test API Directly

Open in phone browser:
```
http://192.168.1.11/AwareHealth/api/doctors
```

**Should see:**
```json
{
  "success": true,
  "doctors": [
    {
      "id": "DOC001",
      "name": "Dr. Rajesh Kumar",
      "specialty": "Cardiology",
      ...
    },
    ...
  ],
  "count": 5
}
```

**If empty array `[]`** → Run quick_setup_doctors.php again

**If error** → Check XAMPP Apache is running

---

## 🎯 Alternative: Use Auto Setup

If quick_setup doesn't work, use full setup:

```
http://192.168.1.11/AwareHealth/api/auto_setup_doctors.php
```

This shows a detailed HTML page with setup progress.

---

## ✅ Success Checklist

- [ ] Opened quick_setup_doctors.php in browser
- [ ] Saw "success": true and "doctors_count": 5
- [ ] Tested /api/doctors - shows 5 doctors
- [ ] Refreshed app Select Doctor screen
- [ ] Doctors appear! ✅

**Most likely issue:** Doctors not in database yet. Run quick_setup_doctors.php!

