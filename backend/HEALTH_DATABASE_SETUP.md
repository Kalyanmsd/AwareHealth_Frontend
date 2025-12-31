# 🏥 Health Database Setup Guide

## 📋 Overview

This guide explains how to set up the health information database tables for your AwareHealth app.

---

## ✅ Required Tables

The health screens need these database tables:

1. **`diseases`** - Disease information
2. **`symptoms`** - Symptom details
3. **`prevention_tips`** - Prevention tips for diseases
4. **`health_articles`** - Health awareness articles
5. **`vaccination_reminders`** - Vaccination schedules

---

## 📝 Step-by-Step Setup

### Step 1: Open phpMyAdmin

1. **Open XAMPP Control Panel**
2. **Start MySQL** (if not running)
3. **Click "Admin"** next to MySQL
4. **Select database:** `awarehealth`

### Step 2: Import SQL Script

1. **Click "SQL" tab** in phpMyAdmin
2. **Open the file:** `backend/database/create_health_tables.sql`
3. **Copy all SQL code**
4. **Paste into SQL tab**
5. **Click "Go"** button

**OR**

1. **Click "Import" tab**
2. **Choose file:** `backend/database/create_health_tables.sql`
3. **Click "Go"**

### Step 3: Verify Tables Created

Check these tables exist:
- ✅ `diseases`
- ✅ `symptoms`
- ✅ `prevention_tips`
- ✅ `health_articles`
- ✅ `vaccination_reminders`

### Step 4: Copy Backend Files

Copy these files to XAMPP:

1. **`backend/api/health.php`** → `C:\xampp\htdocs\AwareHealth\api\health.php`
2. **`backend/api/index.php`** → `C:\xampp\htdocs\AwareHealth\api\index.php` (updated)

---

## 📊 Table Structures

### 1. Diseases Table

**Fields:**
- `id` - Unique identifier
- `name` - Disease name
- `category` - Category (Infectious, Chronic, Respiratory, etc.)
- `severity` - Severity level (Mild, Moderate, Severe)
- `emoji` - Emoji icon
- `description` - Disease description
- `symptoms` - JSON array of symptoms
- `causes` - JSON array of causes
- `prevention` - JSON array of prevention tips
- `treatment` - JSON array of treatment options
- `affected_population` - Who is affected
- `duration` - How long it lasts

### 2. Symptoms Table

**Fields:**
- `id` - Unique identifier
- `name` - Symptom name
- `emoji` - Emoji icon
- `definition` - Symptom definition
- `normal_range` - Normal range
- `fever_range` - Fever range
- `severity_info` - JSON object with severity levels
- `possible_causes` - JSON array of causes
- `what_to_do` - JSON array of actions
- `when_to_seek_help` - JSON array of warning signs
- `associated_symptoms` - JSON array of related symptoms

### 3. Prevention Tips Table

**Fields:**
- `id` - Unique identifier
- `disease_id` - Reference to disease
- `category` - Tip category (Hygiene, Nutrition, Exercise, etc.)
- `title` - Tip title
- `description` - Tip description
- `priority` - Priority level (higher = more important)

### 4. Health Articles Table

**Fields:**
- `id` - Unique identifier
- `title` - Article title
- `category` - Article category
- `summary` - Article summary
- `content` - Full article content
- `image_url` - Article image URL
- `author` - Article author
- `published_at` - Publication date
- `is_featured` - Featured article flag

### 5. Vaccination Reminders Table

**Fields:**
- `id` - Unique identifier
- `user_id` - Reference to user
- `vaccine_name` - Vaccine name
- `dose_number` - Dose number
- `scheduled_date` - Scheduled date
- `completed_date` - Completion date
- `is_completed` - Completion status
- `reminder_sent` - Reminder sent flag
- `notes` - Additional notes

---

## 🔌 API Endpoints

### Get All Diseases
```
GET /api/health/diseases
GET /api/health/diseases?category=Infectious
GET /api/health/diseases?search=cold
```

### Get Single Disease
```
GET /api/health/diseases/{id}
```

### Get Symptoms
```
GET /api/health/symptoms
GET /api/health/symptoms/{id}
```

### Get Prevention Tips
```
GET /api/health/prevention-tips?disease_id={id}
GET /api/health/prevention-tips?disease_id={id}&category=Hygiene
```

### Get Health Articles
```
GET /api/health/health-articles
GET /api/health/health-articles?featured=true
```

---

## ✅ Sample Data Included

The SQL script includes sample data for:
- ✅ 6 diseases (Common Cold, Flu, COVID-19, Diabetes, Hypertension, Asthma)
- ✅ 3 symptoms (Fever, Cough, Headache)
- ✅ 11 prevention tips
- ✅ 3 health articles

---

## 🧪 Testing

### Test 1: Check Tables
1. **Open phpMyAdmin**
2. **Select `awarehealth` database**
3. **Check tables exist**

### Test 2: Test API
1. **Open browser**
2. **Go to:** `http://localhost/AwareHealth/api/health/diseases`
3. **Should see JSON with diseases**

### Test 3: Test from App
1. **Open Health Info screen in app**
2. **Should load diseases from database**

---

## 📝 Adding More Data

### Add New Disease:
```sql
INSERT INTO diseases (id, name, category, severity, emoji, description, symptoms, causes, prevention, treatment, affected_population, duration)
VALUES ('disease-007', 'Disease Name', 'Category', 'Severity', '🏥', 'Description', 
'["Symptom 1", "Symptom 2"]', 
'["Cause 1", "Cause 2"]', 
'["Prevention 1", "Prevention 2"]', 
'["Treatment 1", "Treatment 2"]', 
'Affected Population', 'Duration');
```

### Add New Symptom:
```sql
INSERT INTO symptoms (id, name, emoji, definition, normal_range, fever_range, severity_info, possible_causes, what_to_do, when_to_seek_help, associated_symptoms)
VALUES ('symptom-004', 'Symptom Name', '🤒', 'Definition', 'Normal Range', 'Fever Range',
'{"mild": "Description", "moderate": "Description", "severe": "Description"}',
'["Cause 1", "Cause 2"]',
'["Action 1", "Action 2"]',
'["Warning 1", "Warning 2"]',
'["Symptom 1", "Symptom 2"]');
```

---

## ✅ Checklist

- [ ] SQL script imported successfully
- [ ] All 5 tables created
- [ ] Sample data inserted
- [ ] `health.php` copied to XAMPP
- [ ] `index.php` updated in XAMPP
- [ ] API endpoints tested
- [ ] App can fetch health data

---

## 🆘 Troubleshooting

### Tables Not Created
- Check MySQL is running
- Check SQL syntax errors
- Verify database name is `awarehealth`

### API Not Working
- Check `health.php` is in `C:\xampp\htdocs\AwareHealth\api\`
- Check `index.php` includes health routing
- Check error logs: `C:\xampp\apache\logs\error.log`

### No Data Showing
- Verify sample data was inserted
- Check table names match exactly
- Test API endpoint directly in browser

---

## 📍 File Locations

**SQL Script:**
- `backend/database/create_health_tables.sql`

**API Files:**
- `backend/api/health.php`
- `backend/api/index.php` (updated)

**Copy to:**
- `C:\xampp\htdocs\AwareHealth\api\health.php`
- `C:\xampp\htdocs\AwareHealth\api\index.php`

