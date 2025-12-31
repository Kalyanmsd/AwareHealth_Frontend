# 🔗 Complete Integration Guide: Database ↔ Backend ↔ App

## ✅ Step 1: Verify Tables Created

You've already completed this! ✅
- All 7 tables are created in phpMyAdmin
- `diseases` table exists with 0 rows

---

## ✅ Step 2: Insert Diseases Data

### In phpMyAdmin:

1. **Select `awarehealth` database**
2. **Click "SQL" tab**
3. **Copy and paste this code:**

```sql
-- Insert 20 sample diseases
INSERT INTO `diseases` (`id`, `name`, `category`, `severity`, `emoji`, `description`, `symptoms`, `causes`, `prevention`, `treatment`, `affected_population`, `duration`) VALUES
('disease-001', 'Common Cold', 'Infectious', 'Mild', '🤧', 'A viral infection of the upper respiratory tract, primarily affecting the nose and throat.', '["Runny nose", "Sneezing", "Cough", "Sore throat", "Mild fever", "Headache"]', '["Rhinovirus", "Coronavirus", "Adenovirus", "Contact with infected person"]', '["Wash hands frequently", "Avoid close contact with sick people", "Cover mouth when coughing", "Stay home when sick"]', '["Rest", "Drink plenty of fluids", "Over-the-counter cold medicine", "Gargle with salt water"]', 'All age groups', '7-10 days'),
('disease-002', 'Influenza (Flu)', 'Infectious', 'Moderate', '🤒', 'A contagious respiratory illness caused by influenza viruses.', '["Fever", "Chills", "Muscle aches", "Fatigue", "Cough", "Sore throat", "Headache"]', '["Influenza A or B virus", "Airborne droplets", "Contact with contaminated surfaces"]', '["Annual flu vaccination", "Wash hands regularly", "Avoid touching face", "Stay away from sick people"]', '["Antiviral medications", "Rest", "Fluids", "Pain relievers", "See doctor if severe"]', 'All age groups, especially elderly and children', '1-2 weeks'),
('disease-003', 'COVID-19', 'Infectious', 'Severe', '🦠', 'A respiratory illness caused by the SARS-CoV-2 virus.', '["Fever", "Cough", "Shortness of breath", "Loss of taste/smell", "Fatigue", "Body aches"]', '["SARS-CoV-2 virus", "Close contact with infected person", "Airborne transmission"]', '["Vaccination", "Wear masks", "Social distancing", "Wash hands", "Avoid crowded places"]', '["Rest", "Isolation", "Monitor symptoms", "Seek medical help if severe", "Antiviral treatment if prescribed"]', 'All age groups', 'Varies (1-3 weeks)'),
('disease-004', 'Chickenpox', 'Infectious', 'Moderate', '🦠', 'A highly contagious viral infection caused by the varicella-zoster virus.', '["Itchy rash", "Fever", "Headache", "Loss of appetite", "Fatigue", "Blisters", "Body aches"]', '["Varicella-zoster virus", "Direct contact with infected person", "Airborne droplets", "Contact with blisters"]', '["Vaccination", "Avoid contact with infected people", "Good hygiene", "Isolate infected individuals"]', '["Rest", "Calamine lotion for itching", "Antihistamines", "Pain relievers", "Antiviral medication if severe"]', 'Children, but can affect all ages', '10-21 days'),
('disease-005', 'Measles', 'Infectious', 'Severe', '🌡️', 'A highly contagious viral disease that causes fever, cough, and a distinctive rash.', '["High fever", "Cough", "Runny nose", "Red eyes", "Rash", "White spots in mouth", "Sore throat"]', '["Measles virus", "Airborne transmission", "Contact with infected person", "Unvaccinated individuals"]', '["MMR vaccination", "Avoid contact with infected people", "Isolation", "Good hygiene"]', '["Rest", "Fluids", "Fever reducers", "Vitamin A supplements", "Medical supervision"]', 'Unvaccinated children and adults', '7-14 days'),
('disease-006', 'Diabetes Type 2', 'Chronic', 'Severe', '🍬', 'A chronic condition that affects how the body processes blood sugar.', '["Increased thirst", "Frequent urination", "Fatigue", "Blurred vision", "Slow healing wounds"]', '["Obesity", "Family history", "Lack of exercise", "Poor diet", "Age"]', '["Maintain healthy weight", "Regular exercise", "Balanced diet", "Regular checkups"]', '["Medication", "Insulin therapy", "Diet management", "Exercise", "Blood sugar monitoring"]', 'Adults, especially over 45', 'Lifelong management'),
('disease-007', 'Hypertension', 'Chronic', 'Moderate', '❤️', 'High blood pressure, a long-term medical condition.', '["Headaches", "Shortness of breath", "Dizziness", "Chest pain", "Vision problems"]', '["Age", "Family history", "Obesity", "Lack of exercise", "High salt intake", "Stress"]', '["Regular exercise", "Healthy diet", "Reduce salt", "Maintain healthy weight", "Limit alcohol"]', '["Medication", "Lifestyle changes", "Regular monitoring", "Stress management"]', 'Adults, especially over 40', 'Lifelong management'),
('disease-008', 'Asthma', 'Respiratory', 'Moderate', '🌬️', 'A chronic respiratory condition causing breathing difficulties.', '["Wheezing", "Shortness of breath", "Chest tightness", "Coughing", "Difficulty breathing"]', '["Allergies", "Air pollution", "Exercise", "Cold air", "Respiratory infections", "Genetics"]', '["Avoid triggers", "Use inhaler as prescribed", "Avoid smoke", "Manage allergies", "Regular checkups"]', '["Inhalers", "Medications", "Avoid triggers", "Breathing exercises", "Emergency plan"]', 'All age groups', 'Lifelong management'),
('disease-009', 'Bronchitis', 'Respiratory', 'Moderate', '😷', 'Inflammation of the lining of the bronchial tubes.', '["Persistent cough", "Mucus production", "Fatigue", "Shortness of breath", "Chest discomfort", "Mild fever"]', '["Viral infections", "Bacterial infections", "Smoking", "Air pollution", "Dust", "Fumes"]', '["Avoid smoking", "Wash hands", "Avoid irritants", "Get vaccinated", "Wear mask in polluted areas"]', '["Rest", "Drink fluids", "Cough medicine", "Bronchodilators", "Antibiotics if bacterial"]', 'All age groups, especially smokers', '1-3 weeks'),
('disease-010', 'Pneumonia', 'Respiratory', 'Severe', '🫁', 'Infection that inflames air sacs in one or both lungs.', '["Chest pain", "Cough with phlegm", "Fever", "Shortness of breath", "Fatigue", "Nausea"]', '["Bacteria", "Viruses", "Fungi", "Weakened immune system", "Smoking", "Chronic diseases"]', '["Vaccination", "Wash hands", "Avoid smoking", "Healthy lifestyle", "Manage chronic conditions"]', '["Antibiotics", "Antiviral medications", "Rest", "Fluids", "Oxygen therapy if needed", "Hospitalization if severe"]', 'All age groups, especially elderly and children', '1-3 weeks'),
('disease-011', 'Gastroenteritis', 'Digestive', 'Moderate', '🤢', 'Inflammation of the stomach and intestines.', '["Diarrhea", "Nausea", "Vomiting", "Abdominal cramps", "Fever", "Dehydration"]', '["Viruses", "Bacteria", "Parasites", "Contaminated food/water", "Poor hygiene"]', '["Wash hands frequently", "Cook food thoroughly", "Avoid contaminated water", "Practice good hygiene"]', '["Rest", "Stay hydrated", "Oral rehydration solutions", "Avoid solid foods initially", "Gradual return to normal diet"]', 'All age groups', '1-3 days'),
('disease-012', 'Irritable Bowel Syndrome (IBS)', 'Digestive', 'Moderate', '💊', 'A common disorder affecting the large intestine.', '["Abdominal pain", "Bloating", "Gas", "Diarrhea", "Constipation", "Mucus in stool"]', '["Food sensitivities", "Stress", "Hormonal changes", "Muscle contractions", "Nervous system abnormalities"]', '["Identify trigger foods", "Manage stress", "Regular exercise", "Eat smaller meals", "Fiber supplements"]', '["Diet modifications", "Stress management", "Medications", "Probiotics", "Therapy"]', 'Adults, especially women', 'Chronic condition'),
('disease-013', 'Eczema', 'Skin', 'Moderate', '🔴', 'A condition that makes your skin red and itchy.', '["Itchy skin", "Red patches", "Dry skin", "Cracks", "Blisters", "Thickened skin"]', '["Genetics", "Environmental factors", "Allergens", "Irritants", "Stress", "Climate"]', '["Moisturize regularly", "Avoid triggers", "Use gentle soaps", "Manage stress", "Wear soft fabrics"]', '["Topical creams", "Antihistamines", "Moisturizers", "Avoid scratching", "Phototherapy"]', 'All age groups, especially children', 'Chronic condition'),
('disease-014', 'Psoriasis', 'Skin', 'Moderate', '🔴', 'A skin disease that causes red, itchy scaly patches.', '["Red patches", "Silvery scales", "Dry skin", "Itching", "Burning", "Thickened nails"]', '["Genetics", "Immune system", "Stress", "Infections", "Medications", "Weather"]', '["Moisturize", "Avoid triggers", "Manage stress", "Sun exposure (moderate)", "Healthy lifestyle"]', '["Topical treatments", "Light therapy", "Oral medications", "Biologics", "Lifestyle changes"]', 'Adults, especially 15-35 years', 'Chronic condition'),
('disease-015', 'Depression', 'Mental Health', 'Severe', '😔', 'A mood disorder that causes persistent feelings of sadness.', '["Persistent sadness", "Loss of interest", "Fatigue", "Sleep problems", "Appetite changes", "Difficulty concentrating"]', '["Genetics", "Brain chemistry", "Hormones", "Life events", "Trauma", "Medical conditions"]', '["Regular exercise", "Healthy diet", "Adequate sleep", "Social support", "Stress management"]', '["Therapy", "Medications", "Lifestyle changes", "Support groups", "Professional help"]', 'All age groups', 'Varies (weeks to months)'),
('disease-016', 'Anxiety Disorder', 'Mental Health', 'Moderate', '😰', 'Excessive worry or fear that interferes with daily activities.', '["Excessive worry", "Restlessness", "Fatigue", "Difficulty concentrating", "Irritability", "Sleep problems"]', '["Genetics", "Brain chemistry", "Personality", "Life events", "Trauma", "Medical conditions"]', '["Stress management", "Regular exercise", "Adequate sleep", "Limit caffeine", "Relaxation techniques"]', '["Therapy", "Medications", "Lifestyle changes", "Support groups", "Mindfulness"]', 'All age groups', 'Varies'),
('disease-017', 'Migraine', 'Neurological', 'Moderate', '🤕', 'A neurological condition characterized by intense headaches.', '["Severe headache", "Nausea", "Vomiting", "Sensitivity to light", "Sensitivity to sound", "Aura"]', '["Genetics", "Hormonal changes", "Stress", "Food triggers", "Sleep changes", "Weather"]', '["Identify triggers", "Regular sleep", "Manage stress", "Regular meals", "Stay hydrated"]', '["Pain relievers", "Triptans", "Preventive medications", "Rest in dark room", "Cold compress"]', 'Adults, especially women', '4-72 hours per episode'),
('disease-018', 'Epilepsy', 'Neurological', 'Severe', '🧠', 'A neurological disorder marked by sudden recurrent episodes of seizures.', '["Seizures", "Temporary confusion", "Staring spells", "Uncontrollable movements", "Loss of consciousness"]', '["Genetics", "Brain injury", "Brain conditions", "Infections", "Prenatal injury", "Developmental disorders"]', '["Take medications as prescribed", "Get adequate sleep", "Manage stress", "Avoid triggers", "Regular checkups"]', '["Antiepileptic medications", "Surgery", "Vagus nerve stimulation", "Ketogenic diet", "Lifestyle management"]', 'All age groups', 'Lifelong management'),
('disease-019', 'Conjunctivitis (Pink Eye)', 'Eye', 'Mild', '👁️', 'Inflammation or infection of the outer membrane of the eyeball.', '["Red eyes", "Itching", "Tearing", "Discharge", "Crusting", "Sensitivity to light"]', '["Bacteria", "Viruses", "Allergies", "Irritants", "Contact lenses", "Contaminated objects"]', '["Wash hands frequently", "Avoid touching eyes", "Don't share towels", "Replace contact lenses", "Avoid allergens"]', '["Antibiotic eye drops (bacterial)", "Antiviral medications (viral)", "Antihistamines (allergic)", "Warm compresses", "Artificial tears"]', 'All age groups', '1-2 weeks'),
('disease-020', 'Cataracts', 'Eye', 'Moderate', '👁️', 'Clouding of the normally clear lens of the eye.', '["Cloudy vision", "Blurred vision", "Difficulty seeing at night", "Sensitivity to light", "Double vision", "Fading colors"]', '["Aging", "Diabetes", "Smoking", "UV exposure", "Eye injury", "Genetics", "Medications"]', '["Wear sunglasses", "Quit smoking", "Manage diabetes", "Eat healthy", "Regular eye exams"]', '["Surgery", "New eyeglasses", "Brighter lighting", "Magnifying lenses", "Anti-glare sunglasses"]', 'Elderly, especially over 60', 'Progressive condition');
```

4. **Click "Go"**
5. **You should see: "20 rows affected"**

---

## ✅ Step 3: Copy Backend Files to XAMPP

Copy these files/folders to `C:\xampp\htdocs\AwareHealth\`:

### Required Files:
```
backend/
├── api/
│   ├── index.php          → C:\xampp\htdocs\AwareHealth\api\index.php
│   ├── health.php         → C:\xampp\htdocs\AwareHealth\api\health.php
│   ├── auth.php           → C:\xampp\htdocs\AwareHealth\api\auth.php
│   └── test_connection.php → C:\xampp\htdocs\AwareHealth\api\test_connection.php
├── config.php             → C:\xampp\htdocs\AwareHealth\config.php
└── includes/
    ├── database.php       → C:\xampp\htdocs\AwareHealth\includes\database.php
    ├── functions.php      → C:\xampp\htdocs\AwareHealth\includes\functions.php
    └── smtp_email.php     → C:\xampp\htdocs\AwareHealth\includes\smtp_email.php
```

### Quick Copy Command (PowerShell):
```powershell
# Copy API files
Copy-Item "backend\api\*.php" -Destination "C:\xampp\htdocs\AwareHealth\api\" -Force

# Copy config
Copy-Item "backend\config.php" -Destination "C:\xampp\htdocs\AwareHealth\" -Force

# Copy includes
Copy-Item "backend\includes\*.php" -Destination "C:\xampp\htdocs\AwareHealth\includes\" -Force
```

---

## ✅ Step 4: Restart Apache

1. Open **XAMPP Control Panel**
2. **Stop** Apache
3. **Start** Apache again

---

## ✅ Step 5: Test Backend API

### Test 1: Check Connection
Open in browser:
```
http://localhost/AwareHealth/api/test_connection.php
```
**Expected:** `{"success":true,"message":"Connection successful"}`

### Test 2: Check Diseases Endpoint
Open in browser:
```
http://localhost/AwareHealth/api/health/diseases
```
**Expected:** JSON with `success: true` and `diseases` array with 20 items

### Test 3: Test from Phone Browser
On your phone (same Wi-Fi):
```
http://172.20.10.2/AwareHealth/api/health/diseases
```
**Expected:** Same JSON response

---

## ✅ Step 6: Verify App Configuration

In `app/src/main/java/com/example/awarehealth/data/RetrofitClient.kt`:
```kotlin
private const val BASE_URL = "http://172.20.10.2/AwareHealth/api/"
```

---

## ✅ Step 7: Rebuild and Test App

1. **Build → Clean Project** in Android Studio
2. **Build → Rebuild Project**
3. **Run** the app
4. **Navigate to Disease Database** screen
5. **You should see 20 diseases listed!**

---

## 🎯 Quick Checklist

- [ ] ✅ Tables created in phpMyAdmin (DONE)
- [ ] ⬜ Diseases data inserted (20 rows)
- [ ] ⬜ Backend files copied to XAMPP
- [ ] ⬜ Apache restarted
- [ ] ⬜ API tested in browser
- [ ] ⬜ App BASE_URL is correct
- [ ] ⬜ App rebuilt and tested

---

## 🐛 Troubleshooting

### If diseases don't show in app:

1. **Check database has data:**
   ```sql
   SELECT COUNT(*) FROM diseases;
   ```
   Should return: 20

2. **Test API directly:**
   ```
   http://172.20.10.2/AwareHealth/api/health/diseases
   ```
   Should return JSON with diseases

3. **Check Logcat in Android Studio:**
   - Filter by: `HealthViewModel`
   - Look for error messages

4. **Verify files are in XAMPP:**
   - Check `C:\xampp\htdocs\AwareHealth\api\health.php` exists
   - Check `C:\xampp\htdocs\AwareHealth\config.php` exists

---

## ✅ Success Indicators

After integration:
- ✅ 20 diseases visible in phpMyAdmin
- ✅ API returns diseases JSON
- ✅ App displays diseases list
- ✅ Search and filter work
- ✅ Disease details show correctly

---

## 📞 Next Steps

1. Insert diseases data (Step 2)
2. Copy backend files (Step 3)
3. Test API (Step 5)
4. Test app (Step 7)

Everything should work! 🎉

