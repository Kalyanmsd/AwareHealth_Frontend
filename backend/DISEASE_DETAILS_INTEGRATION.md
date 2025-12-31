# Disease Details Integration - Complete Guide

## ✅ What Was Done

### Frontend Changes

1. **Created DiseaseDetailsViewModel** (`app/src/main/java/com/example/awarehealth/viewmodel/DiseaseDetailsViewModel.kt`)
   - Fetches disease data from API using `AppRepository`
   - Handles loading, error, and success states
   - Provides retry functionality

2. **Updated DiseaseDetailsScreen** (`app/src/main/java/com/example/awarehealth/screens/health/DiseaseDetailsScreen.kt`)
   - Removed hardcoded disease data (the large `when` statement)
   - Now fetches disease data from API via ViewModel
   - Displays loading spinner while fetching
   - Shows error message with retry button on failure
   - Converts `DiseaseData` from API to `DiseaseDetails` format for UI

3. **Updated AppRepository** (`app/src/main/java/com/example/awarehealth/data/AppRepository.kt`)
   - Added `getDisease(id: String)` function to fetch single disease

4. **Updated NavGraph** (`app/src/main/java/com/example/awarehealth/navigation/NavGraph.kt`)
   - Added `repository` parameter to `DiseaseDetailsScreen` call

### Backend Status

✅ **Backend is already configured correctly!**

- The API endpoint `/health/diseases/{id}` already exists in `backend/api/health.php`
- It returns disease data in the correct format:
  ```json
  {
    "success": true,
    "disease": {
      "id": "disease-001",
      "name": "Common Cold",
      "category": "Infectious",
      "severity": "Mild",
      "emoji": "🤧",
      "description": "...",
      "symptoms": ["...", "..."],
      "causes": ["...", "..."],
      "prevention": ["...", "..."],
      "treatment": ["...", "..."],
      "affectedPopulation": "All age groups",
      "duration": "7-10 days"
    }
  }
  ```

## 📋 Database Requirements

Make sure your `diseases` table has all these columns:
- `id` (VARCHAR)
- `name` (VARCHAR)
- `category` (VARCHAR)
- `severity` (VARCHAR)
- `emoji` (VARCHAR)
- `description` (TEXT)
- `symptoms` (JSON/TEXT) - JSON array
- `causes` (JSON/TEXT) - JSON array
- `prevention` (JSON/TEXT) - JSON array
- `treatment` (JSON/TEXT) - JSON array
- `affected_population` (VARCHAR)
- `duration` (VARCHAR)

## 🚀 Next Steps

### 1. Copy Backend Files to XAMPP

Run the PowerShell script to copy all backend files:
```powershell
cd C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend
.\COPY_TO_XAMPP_AUTO.ps1
```

Or manually copy:
- All files from `backend/api/` → `C:\xampp\htdocs\AwareHealth\api\`
- All files from `backend/includes/` → `C:\xampp\htdocs\AwareHealth\includes\`
- `backend/config.php` → `C:\xampp\htdocs\AwareHealth\config.php`

### 2. Ensure Database Has Data

1. Open phpMyAdmin: `http://localhost/phpmyadmin`
2. Select `awarehealth` database
3. Go to `diseases` table
4. Verify you have disease records with IDs like `disease-001`, `disease-002`, etc.

If you need to insert diseases, use:
- `backend/database/INSERT_DISEASES_DATA.sql` - Contains 20 sample diseases
- Or run the SQL in phpMyAdmin

### 3. Restart Apache

1. Open XAMPP Control Panel
2. Stop Apache (if running)
3. Start Apache again

### 4. Test API Endpoint

Test in browser:
```
http://localhost/AwareHealth/api/health/diseases/disease-001
```

Or use PowerShell:
```powershell
Invoke-WebRequest -Uri "http://localhost/AwareHealth/api/health/diseases/disease-001" -Method GET | Select-Object -ExpandProperty Content
```

You should see JSON response with disease data.

### 5. Rebuild Android App

1. Open Android Studio
2. Build → Clean Project
3. Build → Rebuild Project
4. Run the app on your device/emulator

### 6. Test in App

1. Navigate to "Disease Database" screen
2. Tap on any disease
3. The disease details screen should:
   - Show loading spinner briefly
   - Display disease information from database
   - Show all fields: name, category, severity, description, symptoms, causes, prevention, treatment, affected population, duration

## 🐛 Troubleshooting

### "Unknown Disease" Still Showing

**Problem**: Screen shows "Unknown Disease" instead of actual data

**Solutions**:
1. Check Logcat in Android Studio:
   - Filter by "DiseaseDetailsViewModel"
   - Look for error messages about API calls
   
2. Verify API endpoint works:
   - Test in browser: `http://YOUR_IP/AwareHealth/api/health/diseases/DISEASE_ID`
   - Replace `YOUR_IP` with your computer's IP (e.g., `172.20.10.2`)
   - Replace `DISEASE_ID` with actual disease ID from database

3. Check RetrofitClient BASE_URL:
   - File: `app/src/main/java/com/example/awarehealth/data/RetrofitClient.kt`
   - Should be: `http://YOUR_IP/AwareHealth/api/`
   - Make sure IP matches your computer's current IP

4. Verify disease ID format:
   - The disease ID passed to the screen should match IDs in database
   - Example: If database has `disease-001`, the app should navigate with `disease-001`

### Network Error

**Problem**: "Cannot connect to server" error

**Solutions**:
1. Check XAMPP Apache is running
2. Verify IP address in `RetrofitClient.kt` matches your computer's IP
3. Check `network_security_config.xml` allows your IP
4. Test API in browser first to confirm it's accessible

### Database Error

**Problem**: API returns "Disease not found"

**Solutions**:
1. Verify disease exists in database:
   ```sql
   SELECT id, name FROM diseases WHERE id = 'disease-001';
   ```
2. Check disease ID format matches exactly (case-sensitive)
3. Verify JSON columns are valid JSON arrays

## 📝 API Endpoint Details

### GET /health/diseases/{id}

**Request**:
- Method: GET
- URL: `http://YOUR_IP/AwareHealth/api/health/diseases/{id}`
- Example: `http://172.20.10.2/AwareHealth/api/health/diseases/disease-001`

**Response (Success)**:
```json
{
  "success": true,
  "disease": {
    "id": "disease-001",
    "name": "Common Cold",
    "category": "Infectious",
    "severity": "Mild",
    "emoji": "🤧",
    "description": "A viral infection...",
    "symptoms": ["Runny nose", "Sneezing", "Cough"],
    "causes": ["Rhinovirus", "Contact with infected person"],
    "prevention": ["Wash hands frequently", "Avoid close contact"],
    "treatment": ["Rest", "Drink fluids", "Over-the-counter medicine"],
    "affectedPopulation": "All age groups",
    "duration": "7-10 days"
  }
}
```

**Response (Error - Not Found)**:
```json
{
  "success": false,
  "message": "Disease not found"
}
```

## ✅ Integration Checklist

- [x] DiseaseDetailsViewModel created
- [x] DiseaseDetailsScreen updated to use ViewModel
- [x] AppRepository.getDisease() added
- [x] NavGraph updated to pass repository
- [x] Backend endpoint verified (already exists)
- [ ] Backend files copied to XAMPP
- [ ] Apache restarted
- [ ] Database has disease data
- [ ] API endpoint tested in browser
- [ ] Android app rebuilt
- [ ] App tested on device/emulator

## 🎯 Summary

The disease details screen is now fully integrated with the backend database. When a user taps on a disease in the disease list, the app will:

1. Fetch disease details from the API endpoint `/health/diseases/{id}`
2. Display a loading spinner while fetching
3. Show the disease information once loaded
4. Handle errors gracefully with a retry option

All disease data is now dynamic and comes from your MySQL database!

