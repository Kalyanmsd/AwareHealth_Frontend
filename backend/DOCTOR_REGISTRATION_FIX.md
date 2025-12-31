# Doctor Registration JSON Error Fix

## Problem
When registering as a doctor, you get a JSON parsing error: "Use JsonReader.setLenient(true) to accept malformed JSON"

## Root Cause
The error occurs because:
1. The `doctors` table structure might not match what the code expects (missing `user_id` column)
2. PHP errors/warnings might be output before JSON, causing malformed JSON
3. Output buffering issues causing non-JSON content in response

## Solution Applied

### 1. Fixed `backend/api/auth.php`
- Added output buffering to prevent any output before JSON
- Improved error handling for doctor record creation
- Added fallback logic to handle both table structures (with/without `user_id` column)
- Better error logging

### 2. Fixed `backend/includes/functions.php`
- Enhanced `sendResponse()` function to:
  - Clear output buffer before sending JSON
  - Handle JSON encoding errors gracefully
  - Set proper content-type header with charset

### 3. Created Database Fix Scripts

**Option 1: Add user_id column to existing table (Recommended)**
Run in phpMyAdmin:
```sql
-- Add user_id column if it doesn't exist
ALTER TABLE `doctors` 
ADD COLUMN `user_id` VARCHAR(36) DEFAULT NULL AFTER `id`,
ADD INDEX `idx_user_id` (`user_id`);
```

**Option 2: Recreate doctors table (if you don't have data)**
Use `backend/database/CREATE_DOCTORS_TABLE_CORRECT.sql`

## Next Steps

### 1. Fix Database Structure

**In phpMyAdmin:**
1. Select `awarehealth` database
2. Click on SQL tab
3. Run this SQL:
```sql
-- Check if user_id column exists
SHOW COLUMNS FROM doctors LIKE 'user_id';

-- If it doesn't exist, add it:
ALTER TABLE `doctors` 
ADD COLUMN `user_id` VARCHAR(36) DEFAULT NULL AFTER `id`,
ADD INDEX `idx_user_id` (`user_id`);
```

### 2. Copy Updated Files to XAMPP

Copy these files to XAMPP:
- `backend/api/auth.php` → `C:\xampp\htdocs\AwareHealth\api\auth.php`
- `backend/includes/functions.php` → `C:\xampp\htdocs\AwareHealth\includes\functions.php`

### 3. Restart Apache

1. Open XAMPP Control Panel
2. Stop Apache
3. Start Apache

### 4. Test Doctor Registration

1. Rebuild Android app
2. Try registering as a doctor again
3. Check phpMyAdmin to verify:
   - User created in `users` table
   - Doctor record created in `doctors` table

## Expected Database Structure

**doctors table should have:**
- `id` (VARCHAR 36) - Primary key
- `user_id` (VARCHAR 36) - Foreign key to users.id (NEW - must add this)
- `specialty` (VARCHAR 255) - Doctor specialty
- `experience` (VARCHAR 100) - Optional
- `rating` (DECIMAL 3,2) - Optional
- `availability` (VARCHAR 255) - Optional
- `location` (VARCHAR 255) - Optional
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## Verification

After applying the fix, test by:
1. Registering as a doctor in the app
2. Checking that registration succeeds without JSON error
3. Verifying in phpMyAdmin:
   ```sql
   SELECT u.id, u.name, u.email, d.id as doctor_id, d.user_id 
   FROM users u 
   LEFT JOIN doctors d ON d.user_id = u.id 
   WHERE u.user_type = 'doctor';
   ```

## Troubleshooting

### Still Getting JSON Error?

1. **Check Apache error log:**
   - `C:\xampp\apache\logs\error.log`
   - Look for PHP errors

2. **Test API directly:**
   ```powershell
   $body = @{
       name = "Dr. Test"
       email = "doctor@test.com"
       password = "test123"
       phone = "1234567890"
       userType = "doctor"
   } | ConvertTo-Json

   Invoke-WebRequest -Uri "http://localhost/AwareHealth/api/auth/register" `
       -Method POST `
       -Body $body `
       -ContentType "application/json" | Select-Object -ExpandProperty Content
   ```

3. **Check PHP error reporting:**
   - Make sure `display_errors` is OFF in production
   - Check `error_reporting` setting in php.ini

4. **Verify table structure:**
   ```sql
   DESCRIBE doctors;
   ```
   Should show `user_id` column

### Doctor Record Not Created?

- Check error logs for specific error
- Verify `doctors` table exists
- Try the SQL fix above to add `user_id` column

