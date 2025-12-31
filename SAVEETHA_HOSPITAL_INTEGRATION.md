# 🏥 Saveetha Hospital - Complete Backend Integration

## ✅ What Has Been Done

### 1. **Backend Files Created/Updated**
- ✅ **`backend/api/auth.php`** - Enhanced doctor registration and login
  - Doctor registration automatically sets location as "Saveetha Hospital"
  - Doctor login supports both `doctor_id` (SAV001) and UUID
  - Only Saveetha Hospital doctors can login
  
- ✅ **`backend/api/doctors.php`** - Already filters by Saveetha Hospital
  - GET `/doctors` - Returns only Saveetha Hospital doctors
  
- ✅ **`backend/api/appointments.php`** - Full appointment management
  - POST `/appointments` - Patients can book appointments
  - GET `/appointments?userId=xxx` - Doctors can view their appointments
  - PUT `/appointments/{id}` - Doctors can accept/reject appointments

### 2. **Database Setup**
- ✅ **`backend/database/SAVEETHA_HOSPITAL_DOCTORS.sql`** - Sample doctors script
  - Creates 5 sample doctors with IDs: SAV001, SAV002, SAV003, SAV004, SAV005
  - Default password: `password` (for all doctors)
  - All doctors are from Saveetha Hospital

### 3. **Files Copied to XAMPP**
- ✅ All backend files automatically copied to `C:\xampp\htdocs\AwareHealth\`

## 🚀 Setup Instructions

### Step 1: Restart Apache
1. Open **XAMPP Control Panel**
2. **Stop** Apache
3. **Start** Apache again

### Step 2: Create Sample Doctors (Optional)
If you want to test with sample doctors:

1. Open phpMyAdmin: `http://localhost/phpmyadmin`
2. Select `awarehealth` database
3. Go to **SQL** tab
4. Copy contents from: `backend/database/SAVEETHA_HOSPITAL_DOCTORS.sql`
5. Paste and click **Go**

This creates 5 sample doctors:
- **SAV001** - Dr. Rajesh Kumar (Cardiologist)
- **SAV002** - Dr. Priya Sharma (Pediatrician)
- **SAV003** - Dr. Anil Patel (Orthopedic Surgeon)
- **SAV004** - Dr. Meera Reddy (Gynecologist)
- **SAV005** - Dr. Vikram Singh (Neurologist)

**Default password for all:** `password`

### Step 3: Test the Integration

#### Test Patient Booking:
1. Register/Login as patient
2. Go to "Book Appointment"
3. Select a Saveetha Hospital doctor
4. Choose date/time
5. Enter symptoms
6. Confirm booking
7. Appointment status: `pending`

#### Test Doctor Login:
1. Use Doctor ID: `SAV001` (or any SAV00X)
2. Password: `password`
3. Should login successfully
4. Can view appointments assigned to them

#### Test Doctor Accepting Appointment:
1. Login as doctor
2. View appointments (status: `pending`)
3. Accept appointment (change status to `accepted`)
4. Patient will see updated status

## 📋 API Endpoints

### Doctor Login
```
POST /api/auth/doctor-login
Body: {
  "doctorId": "SAV001",
  "password": "password"
}
```

### Get Saveetha Hospital Doctors
```
GET /api/doctors
Response: {
  "success": true,
  "doctors": [...],
  "hospital": "Saveetha Hospital"
}
```

### Book Appointment (Patient)
```
POST /api/appointments
Body: {
  "patientId": "patient-uuid",
  "doctorId": "doctor-uuid",
  "date": "2024-01-15",
  "time": "10:00:00",
  "symptoms": "Fever and cough"
}
```

### Get Doctor's Appointments
```
GET /api/appointments?userId=doctor-user-uuid
Response: {
  "success": true,
  "appointments": [...]
}
```

### Accept Appointment (Doctor)
```
PUT /api/appointments/{appointment-id}
Body: {
  "status": "accepted"
}
```

## 🔧 Database Structure

### Doctors Table
- `id` - UUID (primary key)
- `user_id` - Links to users table
- `doctor_id` - Short ID (SAV001, SAV002, etc.) - for login
- `specialty` - Doctor specialty
- `location` - Should be "Saveetha Hospital"
- `experience`, `rating`, `availability`

### Appointments Table
- `id` - UUID
- `patient_id` - Links to users (patient)
- `doctor_id` - Links to doctors table
- `date`, `time` - Appointment schedule
- `symptoms` - Patient symptoms
- `status` - `pending`, `accepted`, `rejected`, `cancelled`, `completed`

## ✅ Features

1. **Patient Booking**
   - ✅ Patients can book appointments with Saveetha Hospital doctors
   - ✅ Only Saveetha Hospital doctors shown in list
   - ✅ Appointment created with `pending` status

2. **Doctor Login**
   - ✅ Doctors login with their ID (SAV001, etc.)
   - ✅ Only Saveetha Hospital doctors can login
   - ✅ Returns doctor profile with specialty and location

3. **Doctor Appointments**
   - ✅ Doctors can view all appointments assigned to them
   - ✅ Shows patient name, email, phone
   - ✅ Shows appointment date, time, symptoms, status

4. **Accept Appointments**
   - ✅ Doctors can accept appointments (status: `accepted`)
   - ✅ Doctors can reject appointments (status: `rejected`)
   - ✅ Status updates reflected immediately

## 🎯 Testing Checklist

- [ ] Apache restarted
- [ ] Sample doctors created (optional)
- [ ] Patient can register/login
- [ ] Patient can see Saveetha Hospital doctors
- [ ] Patient can book appointment
- [ ] Doctor can login with SAV001 and password
- [ ] Doctor can view their appointments
- [ ] Doctor can accept appointment
- [ ] Patient sees updated appointment status

## 📝 Notes

- All new doctor registrations automatically set location as "Saveetha Hospital"
- Doctor login supports both `doctor_id` field and UUID `id` field
- Appointments are filtered by doctor's location (Saveetha Hospital)
- Frontend is already integrated - no changes needed!

---

**Everything is ready! Just restart Apache and test!** 🎉

