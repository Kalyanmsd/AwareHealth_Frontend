# Appointment Booking Integration - Complete Guide

## ✅ What Was Done

### Backend Changes

1. **Created `backend/api/appointments.php`**
   - `POST /appointments` - Create new appointment
   - `GET /appointments?userId=xxx` - Get appointments for a user (patient or doctor)
   - `GET /appointments/{id}` - Get single appointment
   - `PUT /appointments/{id}` - Update appointment status
   - `DELETE /appointments/{id}` - Delete appointment
   - Validates patient and doctor existence
   - Returns appointment data with proper error handling

2. **Created `backend/api/doctors.php`**
   - `GET /doctors` - Get all doctors
   - `GET /doctors/{id}` - Get single doctor
   - Joins with users table to get doctor names

3. **Updated `backend/api/index.php`**
   - Added routing for `appointments` endpoint
   - Added routing for `doctors` endpoint

### Frontend Changes

1. **Updated `NavGraph.kt`**
   - Added `userData` state to store UserData from login/registration
   - Updated `User` data class to include `id` field
   - Updated appointment creation to use API instead of local state
   - Added coroutine scope for API calls
   - Updated login/register callbacks to accept and store UserData

2. **Updated `LoginScreen.kt`**
   - Changed `onLoginSuccess` callback to pass `UserData`

3. **Updated `RegisterScreen.kt`**
   - Changed `onRegisterSuccess` callback to pass `UserData`

4. **Updated `PatientHomeScreen.kt`**
   - Updated `User` data class to include optional `id` field

## 📋 Database Structure

The `appointments` table should have these columns:
- `id` (VARCHAR 36) - Primary key
- `patient_id` (VARCHAR 36) - Foreign key to users table
- `doctor_id` (VARCHAR 36) - Foreign key to doctors table
- `date` (DATE) - Appointment date
- `time` (TIME) - Appointment time
- `symptoms` (TEXT) - Patient symptoms description
- `status` (VARCHAR 50) - Status: 'pending', 'accepted', 'rejected', 'cancelled', 'completed'
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## 🚀 Next Steps

### 1. Copy Backend Files to XAMPP

```powershell
cd C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend
.\COPY_TO_XAMPP_AUTO.ps1
```

Or manually copy:
- `backend/api/appointments.php` → `C:\xampp\htdocs\AwareHealth\api\appointments.php`
- `backend/api/doctors.php` → `C:\xampp\htdocs\AwareHealth\api\doctors.php`
- `backend/api/index.php` → `C:\xampp\htdocs\AwareHealth\api\index.php`

### 2. Restart Apache

1. Open XAMPP Control Panel
2. Stop Apache (if running)
3. Start Apache again

### 3. Test API Endpoints

Test in browser or PowerShell:

**Create Appointment:**
```powershell
$body = @{
    patientId = "user-uuid-here"
    doctorId = "doctor-uuid-here"
    date = "2024-12-25"
    time = "10:00:00"
    symptoms = "Headache and fever"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost/AwareHealth/api/appointments" `
    -Method POST `
    -Body $body `
    -ContentType "application/json" | Select-Object -ExpandProperty Content
```

**Get Appointments:**
```
http://localhost/AwareHealth/api/appointments?userId=user-uuid-here
```

**Get All Doctors:**
```
http://localhost/AwareHealth/api/doctors
```

### 4. Rebuild Android App

1. Open Android Studio
2. Build → Clean Project
3. Build → Rebuild Project
4. Run the app on your device/emulator

### 5. Test Appointment Booking Flow

1. **Login/Register** as a patient
2. Navigate to **Select Doctor** screen
3. Select a doctor
4. Select date and time
5. Fill in symptoms in the summary screen
6. Confirm appointment
7. Check phpMyAdmin to verify appointment was created in the `appointments` table

## 📝 API Endpoints

### POST /appointments

**Request:**
```json
{
  "patientId": "user-uuid",
  "doctorId": "doctor-uuid",
  "date": "2024-12-25",
  "time": "10:00:00",
  "symptoms": "Headache and fever"
}
```

**Response (Success - 201):**
```json
{
  "success": true,
  "message": "Appointment created successfully",
  "appointment": {
    "id": "appointment-uuid",
    "patientId": "user-uuid",
    "doctorId": "doctor-uuid",
    "date": "2024-12-25",
    "time": "10:00:00",
    "symptoms": "Headache and fever",
    "status": "pending"
  }
}
```

### GET /appointments?userId=xxx

**Response (Success - 200):**
```json
{
  "success": true,
  "appointments": [
    {
      "id": "appointment-uuid",
      "patientId": "user-uuid",
      "doctorId": "doctor-uuid",
      "date": "2024-12-25",
      "time": "10:00:00",
      "symptoms": "Headache and fever",
      "status": "pending"
    }
  ],
  "count": 1
}
```

### PUT /appointments/{id}

**Request:**
```json
{
  "status": "accepted"
}
```

**Response (Success - 200):**
```json
{
  "success": true,
  "message": "Appointment updated successfully",
  "appointment": {
    "id": "appointment-uuid",
    "patientId": "user-uuid",
    "doctorId": "doctor-uuid",
    "date": "2024-12-25",
    "time": "10:00:00",
    "symptoms": "Headache and fever",
    "status": "accepted"
  }
}
```

## 🐛 Troubleshooting

### Appointment Not Created

**Problem**: Appointment creation fails or doesn't appear in database

**Solutions**:
1. Check Logcat in Android Studio:
   - Filter by "NavGraph"
   - Look for error messages about API calls

2. Verify API endpoint works:
   - Test POST request in browser/Postman
   - Check Apache error logs: `C:\xampp\apache\logs\error.log`

3. Verify user IDs:
   - Make sure patient and doctor IDs exist in database
   - Check `users` and `doctors` tables

4. Check database connection:
   - Verify `config.php` has correct database credentials
   - Test database connection in phpMyAdmin

### "User not found" Error

**Problem**: API returns "Patient not found" or "Doctor not found"

**Solutions**:
1. Verify user exists in `users` table:
   ```sql
   SELECT id, name, user_type FROM users WHERE id = 'user-id-here';
   ```

2. Verify doctor exists:
   ```sql
   SELECT d.id, u.name FROM doctors d 
   JOIN users u ON d.user_id = u.id 
   WHERE d.id = 'doctor-id-here';
   ```

3. Check user ID is being passed correctly from frontend

### Network Error

**Problem**: "Cannot connect to server" error

**Solutions**:
1. Check XAMPP Apache is running
2. Verify IP address in `RetrofitClient.kt` matches your computer's IP
3. Check `network_security_config.xml` allows your IP
4. Test API in browser first

## ✅ Integration Checklist

- [x] Backend appointments.php created
- [x] Backend doctors.php created
- [x] Backend index.php routing updated
- [x] NavGraph updated to use API
- [x] LoginScreen updated to pass UserData
- [x] RegisterScreen updated to pass UserData
- [x] User data class updated with id field
- [ ] Backend files copied to XAMPP
- [ ] Apache restarted
- [ ] API endpoints tested
- [ ] Android app rebuilt
- [ ] Appointment booking tested end-to-end
- [ ] Verified appointments appear in phpMyAdmin

## 🎯 Summary

The appointment booking system is now fully integrated with the backend database. When a patient books an appointment:

1. The app sends appointment data to `POST /appointments`
2. The backend validates patient and doctor exist
3. The appointment is saved to the `appointments` table
4. The appointment appears in phpMyAdmin
5. The app shows success screen

All appointment data is now stored in MySQL and can be retrieved, updated, or deleted through the API endpoints!

