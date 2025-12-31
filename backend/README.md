# AwareHealth Chatbot API - Backend Files

This folder contains the PHP backend API for the AwareHealth chatbot feature.

## 📁 Files Overview

### Core API Files

1. **`config.php`** - Configuration file with OpenAI API key
   - ⚠️ **Action Required**: Add your OpenAI API key here
   - Location: Should be copied to `C:\xampp\htdocs\AwareHealth\config.php`

2. **`chatbot.php`** - Direct endpoint version
   - Endpoint: `/chatbot.php`
   - Request format: `{message: String, days: Int}`
   - Response format: `{success: Boolean, response: String, days: Int, recommends_hospital: Boolean}`
   - Use this if you want direct access or are updating your Android app

3. **`api/chatbot/message.php`** - Android-compatible version ⭐ **RECOMMENDED**
   - Endpoint: `/api/chatbot/message` (matches your Android app)
   - Request format: `{message: String, conversationId: String?}` (Android-compatible)
   - Response format: `{success: Boolean, response: String, conversationId: String}`
   - Automatically extracts "days" from message content
   - ✅ **Use this version** if you want it to work with existing Android code

### Documentation Files

4. **`QUICK_START.md`** - ⚡ Quick 5-minute setup guide
   - Start here for fastest setup

5. **`CHATBOT_SETUP_GUIDE.md`** - 📖 Detailed setup and integration guide
   - Complete step-by-step instructions
   - Troubleshooting guide
   - File deployment instructions

6. **`CHATBOT_API_README.md`** - 📚 Full API documentation
   - API endpoint details
   - Request/response examples
   - cURL testing commands

---

## 🚀 Quick Setup

### Step 1: Configure API Key
Edit `config.php` and add your OpenAI API key.

### Step 2: Copy Files to XAMPP
```
Copy to: C:\xampp\htdocs\AwareHealth\
  - config.php
  - api/chatbot/message.php (recommended)
```

### Step 3: Start XAMPP Apache
Start Apache server in XAMPP Control Panel.

### Step 4: Test
Your Android app is already configured to use: `http://10.0.2.2/AwareHealth/api/chatbot/message`

---

## 📋 Which File to Use?

### Use `api/chatbot/message.php` if:
- ✅ You want it to work with existing Android app code (no changes needed)
- ✅ You want the endpoint at `/api/chatbot/message`
- ✅ You want automatic "days" extraction from message content

### Use `chatbot.php` if:
- ✅ You're updating your Android app to use the new format with explicit "days" parameter
- ✅ You want the endpoint at `/chatbot.php`
- ✅ You want full control over the "days" parameter

---

## 🔗 Android App Integration

Your Android app (`RetrofitClient.kt`) is already configured with:
- **BASE_URL**: `http://10.0.2.2/AwareHealth/api/` (emulator)
- **Endpoint**: `chatbot/message`
- **Full URL**: `http://10.0.2.2/AwareHealth/api/chatbot/message`

✅ Using `api/chatbot/message.php` will work immediately without Android code changes.

---

## 📚 Documentation Guide

1. **New to this?** → Read `QUICK_START.md`
2. **Need detailed setup?** → Read `CHATBOT_SETUP_GUIDE.md`
3. **Need API reference?** → Read `CHATBOT_API_README.md`

---

## ⚠️ Important Notes

1. **PHP files run on XAMPP, NOT in Android Studio**
   - Android Studio is for Android app development
   - XAMPP runs the PHP backend server
   - Android app connects to XAMPP via HTTP

2. **File Locations**
   - **Development**: Files are in this `backend/` folder (Android Studio project)
   - **Deployment**: Files must be copied to `C:\xampp\htdocs\AwareHealth\` (XAMPP)

3. **API Key Security**
   - Never commit `config.php` with a real API key to public repositories
   - Keep your OpenAI API key secure

---

## 🆘 Need Help?

1. Check `CHATBOT_SETUP_GUIDE.md` for troubleshooting
2. Check XAMPP Apache is running
3. Test API in browser first: `http://localhost/AwareHealth/api/chatbot/message.php`
4. Check Android Logcat for API response errors
5. Verify BASE_URL in `RetrofitClient.kt` matches your setup

---

**Ready to get started?** → Open `QUICK_START.md` 🚀

