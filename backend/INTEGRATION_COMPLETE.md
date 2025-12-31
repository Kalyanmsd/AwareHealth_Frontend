# 🔗 Complete Integration Guide: XAMPP + phpMyAdmin + Android App

## ✅ Current Status

From phpMyAdmin, I can see:
- ✅ Database: `awarehealth` exists
- ✅ All 7 tables created:
  - `appointments` (0 rows)
  - `diseases` (0 rows) ⚠️ **NEEDS DATA**
  - `doctors` (0 rows)
  - `health_articles` (0 rows)
  - `password_reset_tokens` (0 rows)
  - `symptoms` (0 rows)
  - `users` (7 rows) ✅

## 📋 Step 1: Insert Diseases Data

### In phpMyAdmin:

1. **Click on `awarehealth` database** (left sidebar)
2. **Click "SQL" tab** (top navigation)
3. **Copy and paste this SQL:**

```sql
-- Insert 20 diseases into the diseases table
INSERT INTO `diseases` (`id`, `name`, `category`, `severity`, `emoji`, `description`, `symptoms`, `causes`, `prevention`, `treatment`, `affected_population`, `duration`) VALUES

('disease-001', 'Common Cold', 'Infectious', 'Mild', '🤧', 'A viral infection of the upper respiratory tract, primarily affecting the nose and throat.', 
'["Runny nose", "Sneezing", "Cough", "Sore throat", "Mild fever", "Headache"]',
'["Rhinovirus", "Coronavirus", "Adenovirus", "Contact with infected person"]',
'["Wash hands frequently", "Avoid close contact with sick people", "Cover mouth when coughing", "Stay home when sick"]',
'["Rest", "Drink plenty of fluids", "Over-the-counter cold medicine", "Gargle with salt water"]',
'All age groups', '7-10 days'),

('disease-002', 'Influenza (Flu)', 'Infectious', 'Moderate', '🤒', 'A contagious respiratory illness caused by influenza viruses.', 
'["Fever", "Chills", "Muscle aches", "Fatigue", "Cough", "Sore throat", "Headache"]',
'["Influenza A or B virus", "Airborne droplets", "Contact with contaminated surfaces"]',
'["Annual flu vaccination", "Wash hands regularly", "Avoid touching face", "Stay away from sick people"]',
'["Antiviral medications", "Rest", "Fluids", "Pain relievers", "See doctor if severe"]',
'All age groups, especially elderly and children', '1-2 weeks'),

('disease-003', 'COVID-19', 'Infectious', 'Severe', '🦠', 'A respiratory illness caused by the SARS-CoV-2 virus.', 
'["Fever", "Cough", "Shortness of breath", "Loss of taste/smell", "Fatigue", "Body aches"]',
'["SARS-CoV-2 virus", "Close contact with infected person", "Airborne transmission"]',
'["Vaccination", "Wear masks", "Social distancing", "Wash hands", "Avoid crowded places"]',
'["Rest", "Isolation", "Monitor symptoms", "Seek medical help if severe", "Antiviral treatment if prescribed"]',
'All age groups', 'Varies (1-3 weeks)'),

('disease-004', 'Chickenpox', 'Infectious', 'Moderate', '🦠', 'A highly contagious viral infection caused by the varicella-zoster virus.', 
'["Itchy rash", "Fever", "Headache", "Loss of appetite", "Fatigue", "Blisters", "Body aches"]',
'["Varicella-zoster virus", "Direct contact with infected person", "Airborne droplets", "Contact with blisters"]',
'["Vaccination", "Avoid contact with infected people", "Good hygiene", "Isolate infected individuals"]',
'["Rest", "Calamine lotion for itching", "Antihistamines", "Pain relievers", "Antiviral medication if severe"]',
'Children, but can affect all ages', '10-21 days'),

('disease-005', 'Measles', 'Infectious', 'Severe', '🌡️', 'A highly contagious viral disease that causes fever, cough, and a distinctive rash.', 
'["High fever", "Cough", "Runny nose", "Red eyes", "Rash", "White spots in mouth", "Sore throat"]',
'["Measles virus", "Airborne transmission", "Contact with infected person", "Unvaccinated individuals"]',
'["MMR vaccination", "Avoid contact with infected people", "Isolation", "Good hygiene"]',
'["Rest", "Fluids", "Fever reducers", "Vitamin A supplements", "Medical supervision"]',
'Unvaccinated children and adults', '7-14 days'),

('disease-006', 'Diabetes Type 2', 'Chronic', 'Severe', '🍬', 'A chronic condition that affects how the body processes blood sugar.', 
'["Increased thirst", "Frequent urination", "Fatigue", "Blurred vision", "Slow healing wounds"]',
'["Obesity", "Family history", "Lack of exercise", "Poor diet", "Age"]',
'["Maintain healthy weight", "Regular exercise", "Balanced diet", "Regular checkups"]',
'["Medication", "Insulin therapy", "Diet management", "Exercise", "Blood sugar monitoring"]',
'Adults, especially over 45', 'Lifelong management'),

('disease-007', 'Hypertension', 'Chronic', 'Moderate', '❤️', 'High blood pressure, a long-term medical condition.', 
'["Headaches", "Shortness of breath", "Dizziness", "Chest pain", "Vision problems"]',
'["Age", "Family history", "Obesity", "Lack of exercise", "High salt intake", "Stress"]',
'["Regular exercise", "Healthy diet", "Reduce salt", "Maintain healthy weight", "Limit alcohol"]',
'["Medication", "Lifestyle changes", "Regular monitoring", "Stress management"]',
'Adults, especially over 40', 'Lifelong management'),

('disease-008', 'Asthma', 'Respiratory', 'Moderate', '🌬️', 'A chronic respiratory condition causing breathing difficulties.', 
'["Wheezing", "Shortness of breath", "Chest tightness", "Coughing", "Difficulty breathing"]',
'["Allergies", "Air pollution", "Exercise", "Cold air", "Respiratory infections", "Genetics"]',
'["Avoid triggers", "Use inhaler as prescribed", "Avoid smoke", "Manage allergies", "Regular checkups"]',
'["Inhalers", "Medications", "Avoid triggers", "Breathing exercises", "Emergency plan"]',
'All age groups', 'Lifelong management'),

('disease-009', 'Bronchitis', 'Respiratory', 'Moderate', '😷', 'Inflammation of the lining of the bronchial tubes.', 
'["Persistent cough", "Mucus production", "Fatigue", "Shortness of breath", "Chest discomfort", "Mild fever"]',
'["Viral infections", "Bacterial infections", "Smoking", "Air pollution", "Dust", "Fumes"]',
'["Avoid smoking", "Wash hands", "Avoid irritants", "Get vaccinated", "Wear mask in polluted areas"]',
'["Rest", "Drink fluids", "Cough medicine", "Bronchodilators", "Antibiotics if bacterial"]',
'All age groups, especially smokers', '1-3 weeks'),

('disease-010', 'Pneumonia', 'Respiratory', 'Severe', '🫁', 'Infection that inflames air sacs in one or both lungs.', 
'["Chest pain", "Cough with phlegm", "Fever", "Shortness of breath", "Fatigue", "Nausea"]',
'["Bacteria", "Viruses", "Fungi", "Weakened immune system", "Smoking", "Chronic diseases"]',
'["Vaccination", "Wash hands", "Avoid smoking", "Healthy lifestyle", "Manage chronic conditions"]',
'["Antibiotics", "Antiviral medications", "Rest", "Fluids", "Oxygen therapy if needed", "Hospitalization if severe"]',
'All age groups, especially elderly and children', '1-3 weeks'),

('disease-011', 'Gastroenteritis', 'Digestive', 'Moderate', '🤢', 'Inflammation of the stomach and intestines.', 
'["Diarrhea", "Nausea", "Vomiting", "Abdominal cramps", "Fever", "Dehydration"]',
'["Viruses", "Bacteria", "Parasites", "Contaminated food/water", "Poor hygiene"]',
'["Wash hands frequently", "Cook food thoroughly", "Avoid contaminated water", "Practice good hygiene"]',
'["Rest", "Stay hydrated", "Oral rehydration solutions", "Avoid solid foods initially", "Gradual return to normal diet"]',
'All age groups', '1-3 days'),

('disease-012', 'Irritable Bowel Syndrome (IBS)', 'Digestive', 'Moderate', '💊', 'A common disorder affecting the large intestine.', 
'["Abdominal pain", "Bloating", "Gas", "Diarrhea", "Constipation", "Mucus in stool"]',
'["Food sensitivities", "Stress", "Hormonal changes", "Muscle contractions", "Nervous system abnormalities"]',
'["Identify trigger foods", "Manage stress", "Regular exercise", "Eat smaller meals", "Fiber supplements"]',
'["Diet modifications", "Stress management", "Medications", "Probiotics", "Therapy"]',
'Adults, especially women', 'Chronic condition'),

('disease-013', 'Eczema', 'Skin', 'Moderate', '🔴', 'A condition that makes your skin red and itchy.', 
'["Itchy skin", "Red patches", "Dry skin", "Cracks", "Blisters", "Thickened skin"]',
'["Genetics", "Environmental factors", "Allergens", "Irritants", "Stress", "Climate"]',
'["Moisturize regularly", "Avoid triggers", "Use gentle soaps", "Manage stress", "Wear soft fabrics"]',
'["Topical creams", "Antihistamines", "Moisturizers", "Avoid scratching", "Phototherapy"]',
'All age groups, especially children', 'Chronic condition'),

('disease-014', 'Psoriasis', 'Skin', 'Moderate', '🔴', 'A skin disease that causes red, itchy scaly patches.', 
'["Red patches", "Silvery scales", "Dry skin", "Itching", "Burning", "Thickened nails"]',
'["Genetics", "Immune system", "Stress", "Infections", "Medications", "Weather"]',
'["Moisturize", "Avoid triggers", "Manage stress", "Sun exposure (moderate)", "Healthy lifestyle"]',
'["Topical treatments", "Light therapy", "Oral medications", "Biologics", "Lifestyle changes"]',
'Adults, especially 15-35 years', 'Chronic condition'),

('disease-015', 'Depression', 'Mental Health', 'Severe', '😔', 'A mood disorder that causes persistent feelings of sadness.', 
'["Persistent sadness", "Loss of interest", "Fatigue", "Sleep problems", "Appetite changes", "Difficulty concentrating"]',
'["Genetics", "Brain chemistry", "Hormones", "Life events", "Trauma", "Medical conditions"]',
'["Regular exercise", "Healthy diet", "Adequate sleep", "Social support", "Stress management"]',
'["Therapy", "Medications", "Lifestyle changes", "Support groups", "Professional help"]',
'All age groups', 'Varies (weeks to months)'),

('disease-016', 'Anxiety Disorder', 'Mental Health', 'Moderate', '😰', 'Excessive worry or fear that interferes with daily activities.', 
'["Excessive worry", "Restlessness", "Fatigue", "Difficulty concentrating", "Irritability", "Sleep problems"]',
'["Genetics", "Brain chemistry", "Personality", "Life events", "Trauma", "Medical conditions"]',
'["Stress management", "Regular exercise", "Adequate sleep", "Limit caffeine", "Relaxation techniques"]',
'["Therapy", "Medications", "Lifestyle changes", "Support groups", "Mindfulness"]',
'All age groups', 'Varies'),

('disease-017', 'Migraine', 'Neurological', 'Moderate', '🤕', 'A neurological condition characterized by intense, debilitating headaches.', 
'["Severe headache", "Nausea", "Vomiting", "Sensitivity to light", "Sensitivity to sound", "Aura"]',
'["Genetics", "Hormonal changes", "Stress", "Food triggers", "Sleep changes", "Weather"]',
'["Identify triggers", "Regular sleep", "Manage stress", "Regular meals", "Stay hydrated"]',
'["Pain relievers", "Triptans", "Preventive medications", "Rest in dark room", "Cold compress"]',
'Adults, especially women', '4-72 hours per episode'),

('disease-018', 'Epilepsy', 'Neurological', 'Severe', '🧠', 'A neurological disorder marked by sudden recurrent episodes of sensory disturbance.', 
'["Seizures", "Temporary confusion", "Staring spells", "Uncontrollable movements", "Loss of consciousness"]',
'["Genetics", "Brain injury", "Brain conditions", "Infections", "Prenatal injury", "Developmental disorders"]',
'["Take medications as prescribed", "Get adequate sleep", "Manage stress", "Avoid triggers", "Regular checkups"]',
'["Antiepileptic medications", "Surgery", "Vagus nerve stimulation", "Ketogenic diet", "Lifestyle management"]',
'All age groups', 'Lifelong management'),

('disease-019', 'Conjunctivitis (Pink Eye)', 'Eye', 'Mild', '👁️', 'Inflammation or infection of the outer membrane of the eyeball.', 
'["Red eyes", "Itching", "Tearing", "Discharge", "Crusting", "Sensitivity to light"]',
'["Bacteria", "Viruses", "Allergies", "Irritants", "Contact lenses", "Contaminated objects"]',
'["Wash hands frequently", "Avoid touching eyes", "Don't share towels", "Replace contact lenses", "Avoid allergens"]',
'["Antibiotic eye drops (bacterial)", "Antiviral medications (viral)", "Antihistamines (allergic)", "Warm compresses", "Artificial tears"]',
'All age groups', '1-2 weeks'),

('disease-020', 'Cataracts', 'Eye', 'Moderate', '👁️', 'Clouding of the normally clear lens of the eye, leading to vision impairment.', 
'["Cloudy vision", "Blurred vision", "Difficulty seeing at night", "Sensitivity to light", "Double vision", "Fading colors"]',
'["Aging", "Diabetes", "Smoking", "UV exposure", "Eye injury", "Genetics", "Medications"]',
'["Wear sunglasses", "Quit smoking", "Manage diabetes", "Eat healthy", "Regular eye exams"]',
'["Surgery", "New eyeglasses", "Brighter lighting", "Magnifying lenses", "Anti-glare sunglasses"]',
'Elderly, especially over 60', 'Progressive condition');
```

4. **Click "Go"**
5. **Verify:** You should see "20 rows affected"

---

## 📋 Step 2: Verify Backend Files in XAMPP

Make sure these files are in `C:\xampp\htdocs\AwareHealth\`:

### Required Files:
```
C:\xampp\htdocs\AwareHealth\
├── api\
│   ├── index.php
│   ├── health.php
│   ├── auth.php
│   ├── simple_diseases.php
│   └── debug_health.php
├── config.php
├── includes\
│   ├── database.php
│   ├── functions.php
│   └── smtp_email.php
└── .htaccess (optional)
```

### Copy Files Command (PowerShell):
```powershell
# Copy backend files to XAMPP
Copy-Item -Path "C:\Users\chilu\AndroidStudioProjects\AwareHealth2\backend\*" -Destination "C:\xampp\htdocs\AwareHealth\" -Recurse -Force
```

---

## 📋 Step 3: Test API Endpoint

### Test in Browser:

1. **Open browser**
2. **Go to:** `http://localhost/AwareHealth/api/health/diseases`
3. **Expected:** JSON response with diseases array

**OR**

1. **Go to:** `http://localhost/AwareHealth/api/simple_diseases.php`
2. **Expected:** JSON response with diseases

### Test from Phone Browser:

1. **Find your computer's IP:** Run `ipconfig` in Command Prompt
2. **Look for:** IPv4 Address (e.g., `172.20.10.2`)
3. **Open browser on phone**
4. **Go to:** `http://172.20.10.2/AwareHealth/api/simple_diseases.php`
5. **Expected:** JSON response with diseases

---

## 📋 Step 4: Verify App Configuration

### Check `RetrofitClient.kt`:

```kotlin
private const val BASE_URL = "http://172.20.10.2/AwareHealth/api/"
```

**Update IP if needed:**
- Run `ipconfig` in Command Prompt
- Find your IPv4 Address
- Update `BASE_URL` in `RetrofitClient.kt`

---

## 📋 Step 5: Test in App

1. **Rebuild app** in Android Studio
2. **Run app** on phone
3. **Navigate to Disease Database screen**
4. **Expected:** List of 20 diseases should appear

---

## ✅ Success Checklist

- [ ] Diseases data inserted (20 rows in `diseases` table)
- [ ] Backend files copied to `C:\xampp\htdocs\AwareHealth\`
- [ ] Apache is running in XAMPP
- [ ] MySQL is running in XAMPP
- [ ] API endpoint works in browser: `http://localhost/AwareHealth/api/health/diseases`
- [ ] API endpoint works from phone: `http://YOUR_IP/AwareHealth/api/simple_diseases.php`
- [ ] `BASE_URL` in app matches your computer's IP
- [ ] App rebuilt and running
- [ ] Diseases list shows in app

---

## 🐛 Troubleshooting

### Issue: "Endpoint not found"
- ✅ Check `health.php` is in `C:\xampp\htdocs\AwareHealth\api\`
- ✅ Restart Apache in XAMPP
- ✅ Check error log: `C:\xampp\apache\logs\error.log`

### Issue: Empty diseases list
- ✅ Check `diseases` table has rows: `SELECT COUNT(*) FROM diseases;`
- ✅ Test API in browser first
- ✅ Check database connection in `config.php`

### Issue: Network error in app
- ✅ Check IP address is correct
- ✅ Check phone and computer on same Wi-Fi
- ✅ Test URL in phone browser first
- ✅ Check firewall allows port 80

---

## 📞 Quick Test Commands

### Check Database:
```sql
SELECT COUNT(*) as total_diseases FROM diseases;
SELECT name, category FROM diseases LIMIT 5;
```

### Check API:
```
http://localhost/AwareHealth/api/debug_health.php
```

This will show database connection status and disease count.

