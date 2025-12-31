# 🔧 Troubleshooting "Failed to load doctors" Error

## 🔍 Quick Diagnosis

### Step 1: Test API Directly

**Open in browser:**
```
http://172.20.10.2/AwareHealth/api/test_get_doctors.php
```

**OR:**
```
http://localhost/AwareHealth/api/test_get_doctors.php
```

**This will show:**
- ✅ Database connection status
- ✅ If doctors table exists
- ✅ Total doctors count
- ✅ Available doctors count
- ✅ Sample doctors data
- ✅ Specific error messages

---

## 🐛 Common Issues & Solutions

### Issue 1: Doctors Table is Empty

**Symptom:** API returns `count: 0` or empty doctors array

**Solution:**
1. Open phpMyAdmin: `http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=awarehealth`
2. Run this SQL to insert sample doctors:

```sql
INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `experience`, `available_days`, `available_time`, `status`) VALUES
('Dr. Rajesh Kumar', 'Cardiology', 'Saveetha Hospital', '15 years', 'Monday, Wednesday, Friday', '09:00 AM - 05:00 PM', 'Available'),
('Dr. Priya Sharma', 'Pediatrics', 'Saveetha Hospital', '12 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM', 'Available'),
('Dr. Anil Patel', 'Orthopedics', 'Saveetha Hospital', '18 years', 'Monday, Wednesday, Friday', '08:00 AM - 04:00 PM', 'Available');
```

**OR if your table doesn't have `status` column:**

```sql
INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `experience`, `available_days`, `available_time`) VALUES
('Dr. Rajesh Kumar', 'Cardiology', 'Saveetha Hospital', '15 years', 'Monday, Wednesday, Friday', '09:00 AM - 05:00 PM'),
('Dr. Priya Sharma', 'Pediatrics', 'Saveetha Hospital', '12 years', 'Tuesday, Thursday, Saturday', '10:00 AM - 06:00 PM'),
('Dr. Anil Patel', 'Orthopedics', 'Saveetha Hospital', '18 years', 'Monday, Wednesday, Friday', '08:00 AM - 04:00 PM');
```

---

### Issue 2: All Doctors Have Status = 'Unavailable'

**Symptom:** API returns empty array even though doctors exist

**Solution:**
1. Update doctor status to 'Available':

```sql
UPDATE doctors SET status = 'Available' WHERE id IN (1, 2, 3);
```

**OR if you want all doctors available:**

```sql
UPDATE doctors SET status = 'Available';
```

---

### Issue 3: Doctors Table Doesn't Exist

**Symptom:** API returns "Doctors table does not exist"

**Solution:**
1. Create the table:

```sql
CREATE TABLE IF NOT EXISTS `doctors` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `specialization` VARCHAR(255) NOT NULL,
  `hospital` VARCHAR(255) NOT NULL DEFAULT 'Saveetha Hospital',
  `experience` VARCHAR(100) NOT NULL,
  `available_days` VARCHAR(255) NOT NULL,
  `available_time` VARCHAR(255) NOT NULL,
  `status` VARCHAR(50) DEFAULT 'Available',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

2. Insert sample doctors (see Issue 1)

---

### Issue 4: Network Connection Error

**Symptom:** "Cannot connect to server" or "Failed to connect"

**Solutions:**
1. **Check XAMPP Apache is running:**
   - Open XAMPP Control Panel
   - Ensure Apache is "Running" (green)

2. **Check IP Address:**
   - Open `RetrofitClient.kt`
   - Verify BASE_URL: `http://172.20.10.2/AwareHealth/api/`
   - If your IP changed, update it

3. **Check Wi-Fi:**
   - Phone and PC must be on same Wi-Fi network
   - Test: Open `http://172.20.10.2/AwareHealth/api/test_get_doctors.php` on phone browser

4. **Check Firewall:**
   - Windows Firewall might be blocking
   - Temporarily disable to test

---

### Issue 5: API Endpoint Not Found (404)

**Symptom:** "Not Found" or 404 error

**Solutions:**
1. **Check file exists:**
   - Verify: `C:\xampp\htdocs\AwareHealth\api\get_doctors.php` exists

2. **Check .htaccess (if using routing):**
   - If using `index.php` routing, ensure route is registered

3. **Test direct access:**
   - Open: `http://localhost/AwareHealth/api/get_doctors.php`
   - Should return JSON

---

## ✅ Step-by-Step Fix

### 1. Test API First
```
http://172.20.10.2/AwareHealth/api/test_get_doctors.php
```

### 2. Check Response
- If `success: false` → Fix database/table issue
- If `success: true` but `count: 0` → Insert doctors
- If `available_doctors: 0` → Update status to 'Available'

### 3. Verify Doctors in Database
```sql
SELECT * FROM doctors;
```

### 4. Check Status Column
```sql
SELECT id, name, status FROM doctors;
```

### 5. Update Status if Needed
```sql
UPDATE doctors SET status = 'Available';
```

### 6. Test Again
```
http://172.20.10.2/AwareHealth/api/get_doctors.php
```

### 7. Test in App
- Open "Select Doctor" screen
- Should show doctors now

---

## 🧪 Quick Test Commands

### Test 1: Check API Works
```bash
curl http://172.20.10.2/AwareHealth/api/test_get_doctors.php
```

### Test 2: Check Doctors Endpoint
```bash
curl http://172.20.10.2/AwareHealth/api/get_doctors.php
```

### Test 3: Check Database
```sql
-- In phpMyAdmin SQL tab
SELECT COUNT(*) as total FROM doctors;
SELECT COUNT(*) as available FROM doctors WHERE status = 'Available';
```

---

## 📱 App-Side Checks

### Check Logcat
```
adb logcat | grep DoctorsViewModel
```

**Look for:**
- "Loading doctors from API..."
- "Response received: true/false"
- "Success: true/false"
- Error messages

### Common Logcat Errors:
- `Failed to connect` → Network issue
- `404 Not Found` → API endpoint issue
- `500 Internal Server Error` → Backend error
- `success: false` → Check API response message

---

## 🎯 Most Likely Fix

**90% of cases:** Doctors table is empty or all doctors have `status != 'Available'`

**Quick Fix:**
```sql
-- Insert sample doctors
INSERT INTO `doctors` (`name`, `specialization`, `hospital`, `experience`, `available_days`, `available_time`, `status`) VALUES
('Dr. Test Doctor', 'General Medicine', 'Saveetha Hospital', '10 years', 'Monday to Friday', '09:00 AM - 05:00 PM', 'Available');

-- OR update existing
UPDATE doctors SET status = 'Available' LIMIT 5;
```

**Then test:**
```
http://172.20.10.2/AwareHealth/api/test_get_doctors.php
```

---

**Run the test endpoint first to diagnose the exact issue!** 🔍

