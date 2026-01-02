# 🚀 AwareHealth Startup Scripts

This folder contains scripts to automatically start and manage the AwareHealth services.

## 📋 Available Scripts

| Script | Description | Usage |
|--------|-------------|-------|
| `start_flask_api.bat` | Start Flask API server | Double-click or run from command line |
| `start_flask_api.ps1` | PowerShell version with better error handling | Run in PowerShell: `.\start_flask_api.ps1` |
| `start_flask_api_background.ps1` | Start Flask API in separate window | Run in PowerShell |
| `start_all_services.bat` | Start all services (XAMPP check + Flask) | Double-click to start everything |
| `check_services_status.bat` | Check if services are running | Double-click to check status |

---

## 🎯 Quick Start

### Start Flask API Only:
```batch
start_flask_api.bat
```

### Start All Services:
```batch
start_all_services.bat
```

### Check Service Status:
```batch
check_services_status.bat
```

---

## 📖 Detailed Guide

See **[FLASK_API_STARTUP_GUIDE.md](FLASK_API_STARTUP_GUIDE.md)** for complete documentation.

---

## ✅ What You Need

1. **Python** installed and in PATH
2. **Flask API** located at: `C:\xampp\htdocs\AwareHealth\aimodel\aware_health\flask_api.py`
3. **XAMPP** installed (for Apache)

---

## 🔧 Customization

If your Flask API is in a different location, edit the scripts and update:
- `FLASK_DIR` variable in `.bat` files
- `$flaskDir` variable in `.ps1` files

---

## 🆘 Troubleshooting

**Problem**: Script says "Flask API directory not found"  
**Solution**: Update the path in the script to match your Flask API location

**Problem**: Port 5000 already in use  
**Solution**: Stop existing Flask API process or change port in flask_api.py

**Problem**: Python not found  
**Solution**: Install Python and add it to PATH

---

## 📱 Testing

After starting Flask API:

1. **Test locally**: http://localhost:5000/health
2. **Test from phone**: http://[YOUR_IP]:5000/health
3. **Check in app**: Open chatbot in AwareHealth app

---

**Ready to go! 🎉**

