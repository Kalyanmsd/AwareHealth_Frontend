# 🚀 Automatic Doctors Table Import

## Quick Start

Simply open this URL in your browser:
```
http://localhost/AwareHealth/backend/api/auto_import_doctors.php
```

Or double-click: `import_doctors.bat` (in project root)

## What It Does

The script automatically:
1. ✅ Creates `awarehealth` database if it doesn't exist
2. ✅ Creates `doctors` table with required columns
3. ✅ Inserts 5 sample doctors (3 with status='Available')
4. ✅ Verifies the import
5. ✅ Tests the API endpoint
6. ✅ Shows results in a nice HTML page

## Features

- **Automatic**: No manual SQL import needed
- **Safe**: Checks if data exists before inserting
- **Smart**: Can clear and re-import if needed
- **Complete**: Creates database, table, and data in one go

## Sample Doctors Imported

1. Dr. Rajesh Kumar - General Physician - Available
2. Dr. Priya Sharma - Cardiologist - Available
3. Dr. Anil Patel - Dermatologist - Available
4. Dr. Meera Singh - Pediatrician - Busy
5. Dr. Vikram Reddy - Orthopedic Surgeon - Available

## After Import

The API will be ready at:
- `http://localhost/AwareHealth/backend/api/get_doctors.php`
- `http://localhost/AwareHealth/backend/get_doctors.php`

Your Android app will automatically work once the import is complete!

