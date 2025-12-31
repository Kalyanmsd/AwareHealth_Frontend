-- =====================================================
-- Additional Diseases for AwareHealth
-- Add more comprehensive disease data
-- =====================================================

-- Insert more diseases into the diseases table
INSERT INTO `diseases` (`id`, `name`, `category`, `severity`, `emoji`, `description`, `symptoms`, `causes`, `prevention`, `treatment`, `affected_population`, `duration`) VALUES

-- Infectious Diseases
('disease-007', 'Chickenpox', 'Infectious', 'Moderate', '🦠', 'A highly contagious viral infection caused by the varicella-zoster virus, characterized by an itchy rash and blisters.', 
'["Itchy rash", "Fever", "Headache", "Loss of appetite", "Fatigue", "Blisters", "Body aches"]',
'["Varicella-zoster virus", "Direct contact with infected person", "Airborne droplets", "Contact with blisters"]',
'["Vaccination", "Avoid contact with infected people", "Good hygiene", "Isolate infected individuals"]',
'["Rest", "Calamine lotion for itching", "Antihistamines", "Pain relievers", "Antiviral medication if severe"]',
'Children, but can affect all ages', '10-21 days'),

('disease-008', 'Measles', 'Infectious', 'Severe', '🌡️', 'A highly contagious viral disease that causes fever, cough, and a distinctive rash.', 
'["High fever", "Cough", "Runny nose", "Red eyes", "Rash", "White spots in mouth", "Sore throat"]',
'["Measles virus", "Airborne transmission", "Contact with infected person", "Unvaccinated individuals"]',
'["MMR vaccination", "Avoid contact with infected people", "Isolation", "Good hygiene"]',
'["Rest", "Fluids", "Fever reducers", "Vitamin A supplements", "Medical supervision"]',
'Unvaccinated children and adults', '7-14 days'),

('disease-009', 'Mumps', 'Infectious', 'Moderate', '😷', 'A viral infection that causes painful swelling of the salivary glands.', 
'["Swollen salivary glands", "Fever", "Headache", "Muscle aches", "Fatigue", "Loss of appetite", "Pain when chewing"]',
'["Mumps virus", "Airborne droplets", "Direct contact", "Sharing utensils"]',
'["MMR vaccination", "Avoid contact with infected people", "Good hygiene", "Isolation"]',
'["Rest", "Pain relievers", "Cold compress", "Soft foods", "Fluids"]',
'Unvaccinated children and young adults', '7-10 days'),

('disease-010', 'Rubella (German Measles)', 'Infectious', 'Moderate', '🔴', 'A contagious viral infection that causes a red rash, especially dangerous for pregnant women.', 
'["Mild fever", "Rash", "Swollen lymph nodes", "Headache", "Runny nose", "Red eyes", "Joint pain"]',
'["Rubella virus", "Airborne transmission", "Contact with infected person", "Unvaccinated individuals"]',
'["MMR vaccination", "Avoid contact with infected people", "Isolation", "Pregnant women should avoid exposure"]',
'["Rest", "Pain relievers", "Fluids", "Medical supervision for pregnant women"]',
'Unvaccinated individuals, especially pregnant women', '3-7 days'),

('disease-011', 'Tuberculosis (TB)', 'Infectious', 'Severe', '🫁', 'A serious bacterial infection that primarily affects the lungs but can spread to other organs.', 
'["Persistent cough", "Chest pain", "Coughing up blood", "Fatigue", "Fever", "Night sweats", "Weight loss", "Loss of appetite"]',
'["Mycobacterium tuberculosis", "Airborne transmission", "Close contact with infected person", "Weakened immune system"]',
'["BCG vaccination", "Avoid close contact with TB patients", "Good ventilation", "Early detection and treatment"]',
'["Antibiotic treatment (6-9 months)", "Directly Observed Therapy", "Regular monitoring", "Isolation if contagious"]',
'People with weakened immune systems, close contacts', '6-9 months treatment'),

('disease-012', 'Hepatitis A', 'Infectious', 'Moderate', '🟡', 'A liver infection caused by the hepatitis A virus, usually spread through contaminated food or water.', 
'["Fatigue", "Nausea", "Vomiting", "Abdominal pain", "Jaundice", "Dark urine", "Loss of appetite", "Fever"]',
'["Hepatitis A virus", "Contaminated food/water", "Poor sanitation", "Close contact with infected person"]',
'["Hepatitis A vaccination", "Wash hands frequently", "Safe food handling", "Avoid contaminated water"]',
'["Rest", "Fluids", "Avoid alcohol", "Healthy diet", "Medical monitoring"]',
'All age groups, especially in areas with poor sanitation', '2-6 weeks'),

('disease-013', 'Hepatitis B', 'Infectious', 'Severe', '🟠', 'A serious liver infection caused by the hepatitis B virus that can become chronic.', 
'["Fatigue", "Nausea", "Vomiting", "Abdominal pain", "Jaundice", "Dark urine", "Joint pain", "Fever"]',
'["Hepatitis B virus", "Blood contact", "Sexual contact", "Mother to child", "Sharing needles"]',
'["Hepatitis B vaccination", "Safe sex practices", "Avoid sharing needles", "Blood screening"]',
'["Antiviral medications", "Regular monitoring", "Liver function tests", "Avoid alcohol"]',
'All age groups, especially healthcare workers', 'Acute: 2-6 months, Chronic: lifelong'),

('disease-014', 'Malaria', 'Infectious', 'Severe', '🦟', 'A life-threatening disease caused by parasites transmitted through mosquito bites.', 
'["High fever", "Chills", "Sweating", "Headache", "Nausea", "Vomiting", "Fatigue", "Muscle aches"]',
'["Plasmodium parasites", "Mosquito bites", "Anopheles mosquitoes", "Travel to endemic areas"]',
'["Antimalarial medication", "Mosquito nets", "Insect repellent", "Protective clothing", "Avoid mosquito-prone areas"]',
'["Antimalarial drugs", "Hospitalization if severe", "Fluids", "Blood tests", "Medical supervision"]',
'People in tropical regions, travelers', 'Varies (can be fatal if untreated)'),

('disease-015', 'Dengue Fever', 'Infectious', 'Severe', '🩸', 'A mosquito-borne viral infection that causes severe flu-like symptoms and can be fatal.', 
'["High fever", "Severe headache", "Pain behind eyes", "Joint and muscle pain", "Nausea", "Vomiting", "Rash", "Bleeding"]',
'["Dengue virus", "Aedes mosquito bites", "Tropical and subtropical regions", "Standing water"]',
'["Eliminate mosquito breeding sites", "Use mosquito nets", "Insect repellent", "Protective clothing", "Avoid mosquito-prone areas"]',
'["Rest", "Fluids", "Pain relievers (avoid aspirin)", "Medical monitoring", "Hospitalization if severe"]',
'People in tropical regions, travelers', '1-2 weeks'),

-- Chronic Diseases
('disease-016', 'Heart Disease', 'Chronic', 'Severe', '❤️', 'A range of conditions that affect the heart, including coronary artery disease and heart failure.', 
'["Chest pain", "Shortness of breath", "Fatigue", "Irregular heartbeat", "Swelling in legs", "Dizziness", "Nausea"]',
'["High blood pressure", "High cholesterol", "Smoking", "Diabetes", "Obesity", "Family history", "Lack of exercise"]',
'["Regular exercise", "Healthy diet", "Quit smoking", "Manage blood pressure", "Control cholesterol", "Maintain healthy weight"]',
'["Medications", "Lifestyle changes", "Surgery if needed", "Cardiac rehabilitation", "Regular monitoring"]',
'Adults, especially over 50', 'Lifelong management'),

('disease-017', 'Chronic Kidney Disease', 'Chronic', 'Severe', '🫘', 'A progressive loss of kidney function over time, leading to kidney failure.', 
'["Fatigue", "Swelling", "Nausea", "Loss of appetite", "Changes in urination", "Muscle cramps", "Itching", "High blood pressure"]',
'["Diabetes", "High blood pressure", "Glomerulonephritis", "Polycystic kidney disease", "Long-term medication use"]',
'["Control blood pressure", "Manage diabetes", "Healthy diet", "Regular exercise", "Avoid excessive painkillers", "Regular checkups"]',
'["Medications", "Dialysis", "Kidney transplant", "Diet modifications", "Blood pressure control"]',
'Adults with diabetes or hypertension', 'Lifelong management'),

('disease-018', 'Arthritis', 'Chronic', 'Moderate', '🦴', 'Inflammation of one or more joints, causing pain and stiffness that worsens with age.', 
'["Joint pain", "Stiffness", "Swelling", "Redness", "Reduced range of motion", "Fatigue", "Morning stiffness"]',
'["Age", "Obesity", "Joint injuries", "Genetics", "Autoimmune disorders", "Infection", "Occupation"]',
'["Maintain healthy weight", "Regular exercise", "Protect joints", "Healthy diet", "Avoid repetitive stress"]',
'["Pain relievers", "Anti-inflammatory drugs", "Physical therapy", "Exercise", "Joint protection", "Surgery if severe"]',
'Adults, especially over 65', 'Lifelong management'),

('disease-019', 'Osteoporosis', 'Chronic', 'Moderate', '🦴', 'A condition that weakens bones, making them fragile and more likely to break.', 
'["Bone fractures", "Loss of height", "Stooped posture", "Back pain", "Bone pain", "Weak grip strength"]',
'["Age", "Gender (women more at risk)", "Hormonal changes", "Low calcium intake", "Lack of exercise", "Smoking", "Alcohol"]',
'["Adequate calcium intake", "Vitamin D", "Regular weight-bearing exercise", "Quit smoking", "Limit alcohol", "Bone density tests"]',
'["Calcium and vitamin D supplements", "Bisphosphonates", "Hormone therapy", "Exercise", "Fall prevention"]',
'Postmenopausal women, elderly', 'Lifelong management'),

('disease-020', 'Chronic Obstructive Pulmonary Disease (COPD)', 'Respiratory', 'Severe', '💨', 'A group of lung diseases that block airflow and make breathing difficult.', 
'["Shortness of breath", "Chronic cough", "Wheezing", "Chest tightness", "Excessive mucus", "Fatigue", "Frequent respiratory infections"]',
'["Smoking", "Air pollution", "Occupational exposure", "Genetics", "Age", "Respiratory infections"]',
'["Quit smoking", "Avoid air pollution", "Wear protective masks", "Regular exercise", "Vaccinations", "Avoid irritants"]',
'["Bronchodilators", "Inhaled steroids", "Oxygen therapy", "Pulmonary rehabilitation", "Lung transplant if severe"]',
'Smokers, elderly, people with occupational exposure', 'Lifelong management'),

-- Respiratory Diseases
('disease-021', 'Pneumonia', 'Respiratory', 'Severe', '🫁', 'An infection that inflames air sacs in one or both lungs, which may fill with fluid.', 
'["Chest pain", "Cough with phlegm", "Fever", "Chills", "Shortness of breath", "Fatigue", "Nausea", "Confusion (in elderly)"]',
'["Bacteria", "Viruses", "Fungi", "Weakened immune system", "Age", "Chronic diseases", "Smoking"]',
'["Vaccination (pneumococcal, flu)", "Good hygiene", "Quit smoking", "Healthy lifestyle", "Avoid sick people"]',
'["Antibiotics (if bacterial)", "Antiviral (if viral)", "Rest", "Fluids", "Fever reducers", "Hospitalization if severe"]',
'Elderly, children, people with weakened immune systems', '1-3 weeks'),

('disease-022', 'Bronchitis', 'Respiratory', 'Moderate', '🌬️', 'Inflammation of the lining of bronchial tubes, which carry air to and from the lungs.', 
'["Persistent cough", "Mucus production", "Fatigue", "Shortness of breath", "Chest discomfort", "Slight fever", "Chills"]',
'["Viruses", "Bacteria", "Smoking", "Air pollution", "Dust", "Fumes", "Weakened immune system"]',
'["Quit smoking", "Avoid irritants", "Wash hands", "Wear masks", "Avoid sick people", "Vaccinations"]',
'["Rest", "Fluids", "Cough medicine", "Bronchodilators", "Antibiotics if bacterial", "Humidifier"]',
'Smokers, people with respiratory conditions', '1-3 weeks'),

('disease-023', 'Sinusitis', 'Respiratory', 'Moderate', '👃', 'Inflammation of the sinuses, often caused by infection or allergies.', 
'["Facial pain", "Nasal congestion", "Thick nasal discharge", "Loss of smell", "Cough", "Fever", "Fatigue", "Headache"]',
'["Viral infection", "Bacterial infection", "Allergies", "Nasal polyps", "Deviated septum", "Weak immune system"]',
'["Avoid allergens", "Use humidifier", "Nasal irrigation", "Good hygiene", "Avoid smoking", "Manage allergies"]',
'["Nasal decongestants", "Saline nasal spray", "Antibiotics if bacterial", "Pain relievers", "Steam inhalation"]',
'All age groups, especially people with allergies', '1-4 weeks'),

-- Digestive Diseases
('disease-024', 'Gastroenteritis (Stomach Flu)', 'Digestive', 'Moderate', '🤢', 'Inflammation of the stomach and intestines, usually caused by a viral or bacterial infection.', 
'["Diarrhea", "Nausea", "Vomiting", "Abdominal cramps", "Fever", "Dehydration", "Loss of appetite", "Headache"]',
'["Viruses (norovirus, rotavirus)", "Bacteria", "Parasites", "Contaminated food/water", "Poor hygiene"]',
'["Wash hands frequently", "Safe food handling", "Cook food thoroughly", "Avoid contaminated water", "Good hygiene"]',
'["Rest", "Fluids and electrolytes", "BRAT diet", "Avoid dairy", "Medical attention if severe dehydration"]',
'All age groups, especially children and elderly', '1-3 days'),

('disease-025', 'Irritable Bowel Syndrome (IBS)', 'Digestive', 'Moderate', '🫄', 'A common disorder that affects the large intestine, causing cramping, abdominal pain, and changes in bowel habits.', 
'["Abdominal pain", "Bloating", "Gas", "Diarrhea", "Constipation", "Mucus in stool", "Cramping"]',
'["Food sensitivities", "Stress", "Hormones", "Nervous system issues", "Muscle contractions", "Inflammation"]',
'["Identify trigger foods", "Manage stress", "Regular exercise", "Adequate fiber", "Small frequent meals", "Probiotics"]',
'["Diet modifications", "Stress management", "Medications", "Fiber supplements", "Probiotics", "Therapy"]',
'Adults, especially women', 'Chronic condition'),

('disease-026', 'Gastritis', 'Digestive', 'Moderate', '🍽️', 'Inflammation of the stomach lining, which can be acute or chronic.', 
'["Stomach pain", "Nausea", "Vomiting", "Bloating", "Loss of appetite", "Indigestion", "Feeling full quickly"]',
'["H. pylori infection", "Regular use of pain relievers", "Excessive alcohol", "Stress", "Autoimmune disorders", "Bile reflux"]',
'["Avoid irritants", "Limit alcohol", "Avoid NSAIDs", "Manage stress", "Eat smaller meals", "Avoid spicy foods"]',
'["Antacids", "H2 blockers", "Proton pump inhibitors", "Antibiotics if H. pylori", "Diet modifications"]',
'Adults, especially those using NSAIDs regularly', 'Varies (can be chronic)'),

('disease-027', 'Peptic Ulcer', 'Digestive', 'Moderate', '🩹', 'Sores that develop on the lining of the stomach, small intestine, or esophagus.', 
'["Burning stomach pain", "Feeling of fullness", "Bloating", "Nausea", "Vomiting", "Heartburn", "Intolerance to fatty foods"]',
'["H. pylori infection", "Regular use of NSAIDs", "Smoking", "Excessive alcohol", "Stress", "Spicy foods"]',
'["Avoid NSAIDs", "Quit smoking", "Limit alcohol", "Manage stress", "Treat H. pylori", "Healthy diet"]',
'["Antibiotics (if H. pylori)", "Proton pump inhibitors", "H2 blockers", "Antacids", "Lifestyle changes"]',
'Adults, especially those using NSAIDs', '4-8 weeks treatment'),

-- Seasonal Diseases
('disease-028', 'Seasonal Allergies (Hay Fever)', 'Seasonal', 'Mild', '🌸', 'An allergic reaction to pollen from trees, grasses, and weeds that occurs at certain times of the year.', 
'["Sneezing", "Runny nose", "Itchy eyes", "Watery eyes", "Nasal congestion", "Itchy throat", "Cough", "Fatigue"]',
'["Pollen from trees", "Grass pollen", "Weed pollen", "Mold spores", "Dust mites", "Pet dander"]',
'["Avoid allergens", "Stay indoors on high pollen days", "Use air purifier", "Keep windows closed", "Shower after being outside", "Wear mask"]',
'["Antihistamines", "Nasal sprays", "Eye drops", "Decongestants", "Allergy shots", "Avoid allergens"]',
'All age groups, especially in spring and fall', 'Seasonal (spring/fall)'),

('disease-029', 'Heat Stroke', 'Seasonal', 'Severe', '☀️', 'A life-threatening condition that occurs when the body overheats, usually as a result of prolonged exposure to high temperatures.', 
'["High body temperature", "Altered mental state", "Nausea", "Vomiting", "Flushed skin", "Rapid breathing", "Racing heart", "Headache"]',
'["Prolonged exposure to heat", "Dehydration", "Strenuous activity in heat", "Age (elderly and children)", "Certain medications"]',
'["Stay hydrated", "Avoid excessive heat", "Wear light clothing", "Take breaks in shade", "Avoid strenuous activity in heat", "Use sunscreen"]',
'["Immediate cooling", "Emergency medical care", "IV fluids", "Hospitalization", "Monitor vital signs"]',
'People exposed to extreme heat, elderly, athletes', 'Requires immediate treatment'),

('disease-030', 'Hypothermia', 'Seasonal', 'Severe', '❄️', 'A dangerous drop in body temperature, usually caused by prolonged exposure to cold temperatures.', 
'["Shivering", "Slurred speech", "Slow breathing", "Weak pulse", "Clumsiness", "Drowsiness", "Confusion", "Loss of consciousness"]',
'["Prolonged exposure to cold", "Wet clothing", "Wind", "Poor clothing", "Age (elderly and infants)", "Alcohol consumption"]',
'["Dress warmly", "Stay dry", "Limit time in cold", "Wear layers", "Avoid alcohol in cold", "Seek shelter"]',
'["Remove wet clothing", "Warm gradually", "Emergency medical care", "Warm drinks", "Body-to-body contact", "Hospitalization"]',
'People exposed to extreme cold, elderly, homeless', 'Requires immediate treatment'),

-- Mental Health
('disease-031', 'Depression', 'Mental Health', 'Severe', '😔', 'A mood disorder that causes persistent feelings of sadness, loss of interest, and affects daily functioning.', 
'["Persistent sadness", "Loss of interest", "Fatigue", "Changes in sleep", "Changes in appetite", "Difficulty concentrating", "Feelings of worthlessness", "Thoughts of suicide"]',
'["Genetics", "Brain chemistry", "Hormones", "Life events", "Trauma", "Chronic illness", "Substance abuse"]',
'["Regular exercise", "Healthy diet", "Adequate sleep", "Stress management", "Social support", "Avoid alcohol/drugs"]',
'["Therapy", "Medications (antidepressants)", "Lifestyle changes", "Support groups", "Hospitalization if severe"]',
'All age groups, especially teens and adults', 'Varies (can be chronic)'),

('disease-032', 'Anxiety Disorders', 'Mental Health', 'Moderate', '😰', 'A group of mental health conditions characterized by excessive worry, fear, and anxiety that interfere with daily life.', 
'["Excessive worry", "Restlessness", "Fatigue", "Difficulty concentrating", "Irritability", "Muscle tension", "Sleep problems", "Panic attacks"]',
'["Genetics", "Brain chemistry", "Personality", "Life events", "Trauma", "Medical conditions", "Substance abuse"]',
'["Stress management", "Regular exercise", "Adequate sleep", "Limit caffeine", "Avoid alcohol", "Therapy", "Relaxation techniques"]',
'["Therapy (CBT)", "Medications", "Lifestyle changes", "Support groups", "Relaxation techniques", "Avoid triggers"]',
'All age groups, especially young adults', 'Varies (can be chronic)'),

-- Skin Conditions
('disease-033', 'Eczema (Atopic Dermatitis)', 'Skin', 'Moderate', '🔴', 'A chronic skin condition that causes inflamed, itchy, and red skin.', 
'["Itchy skin", "Red patches", "Dry skin", "Cracked skin", "Blisters", "Swelling", "Thickened skin"]',
'["Genetics", "Immune system dysfunction", "Environmental triggers", "Allergens", "Irritants", "Stress", "Climate"]',
'["Moisturize regularly", "Avoid triggers", "Use gentle soaps", "Avoid scratching", "Manage stress", "Wear soft fabrics"]',
'["Topical corticosteroids", "Moisturizers", "Antihistamines", "Avoid triggers", "Light therapy", "Immunosuppressants if severe"]',
'Children and adults, especially those with allergies', 'Chronic condition'),

('disease-034', 'Psoriasis', 'Skin', 'Moderate', '🟥', 'A chronic autoimmune condition that causes rapid skin cell growth, resulting in thick, scaly patches.', 
'["Red patches", "Silvery scales", "Dry cracked skin", "Itching", "Burning", "Soreness", "Thickened nails", "Swollen joints"]',
'["Immune system dysfunction", "Genetics", "Triggers (stress, infections)", "Medications", "Weather", "Injury to skin"]',
'["Moisturize", "Avoid triggers", "Manage stress", "Avoid alcohol", "Protect skin", "Sunlight exposure (moderate)"]',
'["Topical treatments", "Light therapy", "Oral medications", "Biologics", "Lifestyle changes", "Stress management"]',
'Adults, especially 15-35 years', 'Chronic condition'),

('disease-035', 'Acne', 'Skin', 'Mild', '🔴', 'A common skin condition that occurs when hair follicles become clogged with oil and dead skin cells.', 
'["Pimples", "Blackheads", "Whiteheads", "Cysts", "Redness", "Inflammation", "Scarring"]',
'["Excess oil production", "Clogged pores", "Bacteria", "Hormones", "Genetics", "Certain medications", "Diet"]',
'["Wash face regularly", "Avoid picking", "Use non-comedogenic products", "Manage stress", "Healthy diet", "Avoid excessive washing"]',
'["Topical treatments", "Oral medications", "Antibiotics", "Hormonal therapy", "Isotretinoin if severe", "Professional treatments"]',
'Teenagers and young adults', 'Varies (can persist into adulthood)'),

-- Neurological
('disease-036', 'Migraine', 'Neurological', 'Moderate', '🤕', 'A neurological condition characterized by severe, recurring headaches, often accompanied by other symptoms.', 
'["Severe headache", "Nausea", "Vomiting", "Sensitivity to light", "Sensitivity to sound", "Aura", "Dizziness", "Fatigue"]',
'["Genetics", "Hormonal changes", "Stress", "Certain foods", "Weather changes", "Sleep patterns", "Medications"]',
'["Identify triggers", "Regular sleep", "Manage stress", "Regular meals", "Avoid triggers", "Regular exercise", "Stay hydrated"]',
'["Pain relievers", "Triptans", "Preventive medications", "Rest in dark room", "Cold compress", "Avoid triggers"]',
'Adults, especially women', 'Varies (can be chronic)'),

('disease-037', 'Epilepsy', 'Neurological', 'Severe', '⚡', 'A neurological disorder characterized by recurrent, unprovoked seizures.', 
'["Seizures", "Temporary confusion", "Staring spells", "Uncontrollable movements", "Loss of consciousness", "Fear", "Anxiety"]',
'["Genetics", "Brain injury", "Brain conditions", "Infections", "Prenatal injury", "Developmental disorders"]',
'["Take medications as prescribed", "Get adequate sleep", "Avoid triggers", "Wear medical alert bracelet", "Avoid alcohol", "Manage stress"]',
'["Antiepileptic medications", "Surgery if needed", "Vagus nerve stimulation", "Ketogenic diet", "Lifestyle modifications"]',
'All age groups', 'Lifelong management'),

-- Eye Conditions
('disease-038', 'Conjunctivitis (Pink Eye)', 'Eye', 'Mild', '👁️', 'Inflammation of the conjunctiva, the thin clear tissue covering the white part of the eye.', 
'["Red eyes", "Itchy eyes", "Watery eyes", "Discharge", "Crusty eyelids", "Sensitivity to light", "Blurred vision"]',
'["Bacteria", "Viruses", "Allergies", "Irritants", "Contact lenses", "Poor hygiene"]',
'["Wash hands frequently", "Avoid touching eyes", "Don't share towels", "Replace contact lenses", "Avoid allergens"]',
'["Antibiotic eye drops (if bacterial)", "Antihistamines (if allergic)", "Warm compress", "Artificial tears", "Avoid contact lenses"]',
'All age groups, especially children', '1-2 weeks'),

-- Additional Common Conditions
('disease-039', 'Urinary Tract Infection (UTI)', 'General', 'Moderate', '💧', 'An infection in any part of the urinary system, most commonly in the bladder and urethra.', 
'["Burning urination", "Frequent urination", "Urgent urination", "Cloudy urine", "Blood in urine", "Pelvic pain", "Fever"]',
'["Bacteria (E. coli)", "Sexual activity", "Poor hygiene", "Holding urine", "Dehydration", "Menopause", "Catheters"]',
'["Drink plenty of water", "Urinate frequently", "Wipe front to back", "Urinate after sex", "Good hygiene", "Cranberry juice"]',
'["Antibiotics", "Pain relievers", "Increase fluid intake", "Avoid irritants", "Warm compress"]',
'Women, especially sexually active', '3-7 days with treatment'),

('disease-040', 'Anemia', 'General', 'Moderate', '🩸', 'A condition in which you lack enough healthy red blood cells to carry adequate oxygen to body tissues.', 
'["Fatigue", "Weakness", "Pale skin", "Shortness of breath", "Dizziness", "Cold hands and feet", "Headache", "Irregular heartbeat"]',
'["Iron deficiency", "Vitamin B12 deficiency", "Chronic diseases", "Blood loss", "Pregnancy", "Genetics", "Bone marrow problems"]',
'["Iron-rich diet", "Vitamin B12", "Folate", "Regular checkups", "Manage underlying conditions", "Avoid blood loss"]',
'["Iron supplements", "Vitamin supplements", "Diet changes", "Treat underlying cause", "Blood transfusion if severe"]',
'All age groups, especially women and children', 'Varies based on cause'),

('disease-041', 'Thyroid Disorders', 'General', 'Moderate', '🦋', 'Conditions affecting the thyroid gland, including hypothyroidism and hyperthyroidism.', 
'["Fatigue", "Weight changes", "Mood changes", "Hair loss", "Temperature sensitivity", "Irregular heartbeat", "Muscle weakness", "Sleep problems"]',
'["Autoimmune disorders", "Iodine deficiency", "Genetics", "Radiation exposure", "Medications", "Pregnancy", "Age"]',
'["Adequate iodine", "Regular checkups", "Manage stress", "Healthy diet", "Avoid excessive soy", "Regular exercise"]',
'["Hormone replacement (hypothyroidism)", "Antithyroid medications (hyperthyroidism)", "Radioactive iodine", "Surgery", "Lifestyle changes"]',
'Adults, especially women', 'Lifelong management'),

('disease-042', 'Obesity', 'General', 'Severe', '⚖️', 'A complex disease involving excessive body fat that increases the risk of other health problems.', 
'["Excessive body weight", "Difficulty breathing", "Fatigue", "Joint pain", "Sleep apnea", "High blood pressure", "Diabetes risk"]',
'["Poor diet", "Lack of exercise", "Genetics", "Medical conditions", "Medications", "Age", "Lifestyle", "Psychological factors"]',
'["Healthy diet", "Regular exercise", "Portion control", "Limit processed foods", "Adequate sleep", "Stress management"]',
'["Diet modifications", "Exercise program", "Behavioral therapy", "Medications", "Surgery if severe", "Support groups"]',
'All age groups', 'Lifelong management'),

('disease-043', 'Sleep Disorders', 'General', 'Moderate', '😴', 'Conditions that affect the quality, timing, or amount of sleep, resulting in daytime impairment.', 
'["Difficulty falling asleep", "Waking frequently", "Daytime sleepiness", "Irritability", "Difficulty concentrating", "Mood changes", "Snoring"]',
'["Stress", "Medical conditions", "Medications", "Substance use", "Irregular schedule", "Environment", "Age", "Genetics"]',
'["Regular sleep schedule", "Create sleep-friendly environment", "Limit screens before bed", "Avoid caffeine", "Regular exercise", "Manage stress"]',
'["Sleep hygiene", "Medications", "CPAP (for sleep apnea)", "Therapy", "Lifestyle changes", "Treat underlying conditions"]',
'All age groups', 'Varies based on type'),

('disease-044', 'Vitamin D Deficiency', 'General', 'Mild', '☀️', 'A condition where the body doesn't have enough vitamin D, essential for bone health and immune function.', 
'["Fatigue", "Bone pain", "Muscle weakness", "Mood changes", "Hair loss", "Slow wound healing", "Frequent infections"]',
'["Lack of sun exposure", "Dark skin", "Age", "Diet", "Obesity", "Kidney/liver disease", "Malabsorption"]',
'["Sun exposure (15-20 min daily)", "Vitamin D supplements", "Fatty fish", "Fortified foods", "Egg yolks", "Regular checkups"]',
'["Vitamin D supplements", "Diet changes", "Sun exposure", "Treat underlying conditions", "Regular monitoring"]',
'All age groups, especially elderly and dark-skinned', 'Varies with treatment'),

('disease-045', 'Food Poisoning', 'Digestive', 'Moderate', '🍗', 'Illness caused by consuming contaminated food or beverages containing harmful bacteria, viruses, or toxins.', 
'["Nausea", "Vomiting", "Diarrhea", "Abdominal cramps", "Fever", "Dehydration", "Weakness", "Headache"]',
'["Contaminated food", "Improper food handling", "Undercooked food", "Cross-contamination", "Poor hygiene", "Unsafe water"]',
'["Cook food thoroughly", "Wash hands", "Avoid cross-contamination", "Refrigerate properly", "Avoid risky foods", "Safe water"]',
'["Rest", "Fluids and electrolytes", "Avoid solid foods initially", "BRAT diet", "Medical attention if severe"]',
'All age groups', '1-3 days'),

('disease-046', 'Mononucleosis (Mono)', 'Infectious', 'Moderate', '😷', 'A viral infection caused by the Epstein-Barr virus, often called the "kissing disease".', 
'["Extreme fatigue", "Sore throat", "Fever", "Swollen lymph nodes", "Body aches", "Headache", "Rash", "Enlarged spleen"]',
'["Epstein-Barr virus", "Close contact", "Kissing", "Sharing utensils", "Coughing", "Sneezing"]',
'["Avoid close contact with infected people", "Don't share utensils", "Good hygiene", "Rest", "Avoid contact sports"]',
'["Rest", "Fluids", "Pain relievers", "Gargle with salt water", "Avoid contact sports", "Medical monitoring"]',
'Teenagers and young adults', '2-4 weeks'),

('disease-047', 'Strep Throat', 'Infectious', 'Moderate', '🦠', 'A bacterial infection that causes inflammation and pain in the throat.', 
'["Severe sore throat", "Difficulty swallowing", "Fever", "Swollen lymph nodes", "Red spots on roof of mouth", "Headache", "Rash", "Nausea"]',
'["Streptococcus bacteria", "Close contact", "Airborne droplets", "Sharing utensils", "Touching contaminated surfaces"]',
'["Wash hands frequently", "Avoid close contact", "Don't share utensils", "Cover mouth when coughing", "Good hygiene"]',
'["Antibiotics", "Pain relievers", "Rest", "Fluids", "Gargle with salt water", "Throat lozenges"]',
'Children and teenagers', '3-7 days with treatment'),

('disease-048', 'Tonsillitis', 'Infectious', 'Moderate', '🦠', 'Inflammation of the tonsils, usually caused by a viral or bacterial infection.', 
'["Sore throat", "Swollen tonsils", "Difficulty swallowing", "Fever", "Bad breath", "Headache", "Stiff neck", "Voice changes"]',
'["Viruses", "Bacteria (streptococcus)", "Close contact", "Airborne droplets", "Weakened immune system"]',
'["Good hygiene", "Avoid close contact", "Don't share utensils", "Wash hands", "Strengthen immune system"]',
'["Antibiotics (if bacterial)", "Pain relievers", "Rest", "Fluids", "Gargle with salt water", "Surgery if chronic"]',
'Children and teenagers', '3-7 days'),

('disease-049', 'Lyme Disease', 'Infectious', 'Severe', '🕷️', 'A bacterial infection transmitted through the bite of infected black-legged ticks.', 
'["Bull's-eye rash", "Fever", "Chills", "Fatigue", "Body aches", "Headache", "Swollen lymph nodes", "Joint pain"]',
'["Borrelia burgdorferi bacteria", "Tick bites", "Outdoor activities", "Areas with deer", "Tall grass", "Wooded areas"]',
'["Use insect repellent", "Wear protective clothing", "Check for ticks", "Remove ticks promptly", "Avoid tick habitats", "Treat pets"]',
'["Antibiotics", "Early treatment important", "Long-term antibiotics if chronic", "Pain relievers", "Medical monitoring"]',
'People in tick-infested areas, outdoor enthusiasts', 'Varies (can be chronic if untreated)'),

('disease-050', 'Shingles', 'Infectious', 'Moderate', '🔴', 'A viral infection that causes a painful rash, caused by the varicella-zoster virus (same virus that causes chickenpox).', 
'["Painful rash", "Blisters", "Burning sensation", "Itching", "Fever", "Headache", "Fatigue", "Sensitivity to touch"]',
'["Varicella-zoster virus reactivation", "Previous chickenpox", "Age", "Weakened immune system", "Stress", "Certain medications"]',
'["Shingles vaccination", "Maintain healthy immune system", "Manage stress", "Avoid contact with unvaccinated people"]',
'["Antiviral medications", "Pain relievers", "Topical treatments", "Cool compress", "Calamine lotion", "Early treatment important"]',
'Adults over 50, people with weakened immune systems', '2-4 weeks');

-- =====================================================
-- END OF ADDITIONAL DISEASES
-- =====================================================

