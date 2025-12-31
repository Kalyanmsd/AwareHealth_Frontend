# 🏥 Doctors & Appointment Booking Setup Guide

This guide will help you set up doctors in the Select Doctor screen so patients can book appointments.

## ✅ What's Already Implemented

1. **Select Doctor Screen** - UI is ready and displays doctors from API
2. **Doctors API** - Backend endpoint at `/api/doctors` that fetches Saveetha Hospital doctors
3. **Appointment Booking Flow** - Complete flow: Select Doctor → Select Date/Time → Summary → Book
4. **Pull-to-Refresh** - Added to Select Doctor screen for easy refresh

## 📋 Setup Steps

### Step 1: Set Up Doctors Database

You need to populate the database with Saveetha Hospital doctors. Choose one method:

#### Option A: Automatic Setup (Recommended)

1. **Open in browser:**
   ```
   http://localhost/AwareHealth/api/auto_setup_doctors.php
   ```
   OR if using physical device:
   ```
   http://172.20.10.2/AwareHealth/api/auto_setup_doctors.php
   ```

2. **This script will:**
   - Create/update `users` table
   - Create/update `doctors` table
   - Add 5 Saveetha Hospital doctors:
     - Dr. Rajesh Kumar (Cardiologist)
     - Dr. Priya Sharma (Pediatrician)
     - Dr. Anil Patel (Orthopedic Surgeon)
     - Dr. Meera Reddy (Gynecologist)
     - Dr. Vikram Singh (Neurologist)

3. **Verify success** - You should see a success message with a table showing all doctors

#### Option B: Manual SQL Setup

1. **Open phpMyAdmin:** `http://localhost/phpmyadmin`
2. **Select database:** `awarehealth`
3. **Click SQL tab**
4. **Copy and paste** the contents of:
   ```
   backend/database/SAVEETHA_HOSPITAL_DOCTORS.sql
   ```
5. **Click Go**
6. **Verify** - Check that 5 doctors were created

### Step 2: Verify API Endpoint

Test the doctors API endpoint:

1. **Open in browser:**
   ```
   http://localhost/AwareHealth/api/doctors
   ```
   OR if using physical device:
   ```
   http://172.20.10.2/AwareHealth/api/doctors
   ```

2. **Expected Response:**
   ```json
   {
     "success": true,
     "doctors": [
       {
         "id": "...",
         "name": "Dr. Rajesh Kumar",
         "specialty": "Cardiology",
         "experience": "15 years",
         "rating": 4.8,
         "availability": "Mon-Fri: 9 AM - 5 PM",
         "location": "Saveetha Hospital"
       },
       ...
     ],
     "count": 5,
     "hospital": "Saveetha Hospital"
   }
   ```

3. **If empty array** - Doctors not set up yet, go back to Step 1

### Step 3: Verify App Configuration

1. **Check BASE_URL** in `app/src/main/java/com/example/awarehealth/data/RetrofitClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://172.20.10.2/AwareHealth/api/" // For Physical Device
   // OR
   // private const val BASE_URL = "http://10.0.2.2/AwareHealth/api/" // For Emulator
   ```

2. **Make sure:**
   - XAMPP Apache is running
   - MySQL is running
   - Your device/emulator is on the same Wi-Fi network (for physical device)
   - Firewall allows port 80

### Step 4: Test in App

1. **Build and run** the app
2. **Navigate to:** Patient Home → Book Appointment → Select Doctor
3. **You should see:**
   - Loading indicator (briefly)
   - List of 5 Saveetha Hospital doctors
   - Each doctor card showing:
     - Name
     - Specialty
     - Experience
     - Rating
     - Location
     - Availability

4. **Test pull-to-refresh:**
   - Pull down on the doctor list
   - Should refresh and reload doctors

5. **Test appointment booking:**
   - Tap on a doctor
   - Select date and time
   - Fill in symptoms
   - Confirm appointment
   - Should see success message

## 🔍 Troubleshooting

### Issue: "No doctors available at Saveetha Hospital"

**Solutions:**
1. Run `auto_setup_doctors.php` (Step 1)
2. Check database has doctors:
   ```sql
   SELECT COUNT(*) FROM doctors WHERE location = 'Saveetha Hospital';
   ```
3. Verify API returns doctors (Step 2)
4. Check app logs in Logcat for errors

### Issue: "Failed to load doctors" or Network Error

**Solutions:**
1. **Check XAMPP:**
   - Apache is running
   - MySQL is running
   - Port 80 is not blocked

2. **Check Network:**
   - Device and computer on same Wi-Fi
   - BASE_URL matches your IP (check with `ipconfig` on Windows)
   - Firewall allows port 80

3. **Test API directly:**
   - Open `http://172.20.10.2/AwareHealth/api/doctors` in phone browser
   - Should see JSON response

4. **Check Logcat:**
   - Filter by "DoctorsViewModel"
   - Look for error messages

### Issue: Doctors not showing after setup

**Solutions:**
1. **Restart Apache** in XAMPP Control Panel
2. **Clear app data** and restart app
3. **Pull to refresh** on Select Doctor screen
4. **Check database:**
   ```sql
   SELECT d.id, u.name, d.location 
   FROM doctors d 
   JOIN users u ON d.user_id = u.id 
   WHERE d.location = 'Saveetha Hospital';
   ```

### Issue: Appointment booking fails

**Solutions:**
1. **Check appointments API:**
   ```
   http://localhost/AwareHealth/api/appointments
   ```

2. **Verify user is logged in** (patient ID must exist)

3. **Check database tables:**
   - `users` table exists
   - `doctors` table exists
   - `appointments` table exists

4. **Check Logcat** for appointment creation errors

## 📱 Appointment Booking Flow

The complete flow is:

1. **Select Doctor Screen** → Shows list of Saveetha Hospital doctors
2. **Select Doctor** → Tap on a doctor card
3. **Select Date/Time** → Choose appointment date and time slot
4. **Appointment Summary** → Review details and add symptoms
5. **Confirm** → Creates appointment via API
6. **Success Screen** → Shows confirmation

## ✅ Verification Checklist

- [ ] Doctors database set up (5 doctors in database)
- [ ] API endpoint returns doctors (`/api/doctors`)
- [ ] BASE_URL in app matches your setup
- [ ] XAMPP Apache and MySQL running
- [ ] App shows doctors in Select Doctor screen
- [ ] Pull-to-refresh works
- [ ] Appointment booking completes successfully

## 🎯 Quick Test

1. **Setup:** Run `auto_setup_doctors.php`
2. **Test API:** Open `http://172.20.10.2/AwareHealth/api/doctors` in browser
3. **Test App:** Open Select Doctor screen in app
4. **Book Appointment:** Select doctor → date/time → confirm

If all steps work, you're done! 🎉

## 📞 Need Help?

- Check Logcat for detailed error messages
- Verify database in phpMyAdmin
- Test API endpoints in browser first
- Ensure network connectivity between device and server

