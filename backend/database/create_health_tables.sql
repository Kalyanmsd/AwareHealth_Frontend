-- =====================================================
-- Health Information Database Tables
-- For AwareHealth App
-- =====================================================

-- =====================================================
-- 1. DISEASES TABLE
-- Stores disease information
-- =====================================================
CREATE TABLE IF NOT EXISTS `diseases` (
  `id` VARCHAR(36) PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `category` VARCHAR(100) NOT NULL DEFAULT 'General',
  `severity` VARCHAR(50) NOT NULL DEFAULT 'Mild',
  `emoji` VARCHAR(10) DEFAULT '🏥',
  `description` TEXT NOT NULL,
  `symptoms` TEXT NOT NULL COMMENT 'JSON array of symptoms',
  `causes` TEXT DEFAULT NULL COMMENT 'JSON array of causes',
  `prevention` TEXT DEFAULT NULL COMMENT 'JSON array of prevention tips',
  `treatment` TEXT DEFAULT NULL COMMENT 'JSON array of treatment options',
  `affected_population` VARCHAR(255) DEFAULT NULL,
  `duration` VARCHAR(100) DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_category` (`category`),
  INDEX `idx_severity` (`severity`),
  INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. SYMPTOMS TABLE
-- Stores symptom information
-- =====================================================
CREATE TABLE IF NOT EXISTS `symptoms` (
  `id` VARCHAR(36) PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `emoji` VARCHAR(10) DEFAULT '🤒',
  `definition` TEXT NOT NULL,
  `normal_range` VARCHAR(100) DEFAULT NULL,
  `fever_range` VARCHAR(100) DEFAULT NULL,
  `severity_info` TEXT DEFAULT NULL COMMENT 'JSON object with severity levels',
  `possible_causes` TEXT DEFAULT NULL COMMENT 'JSON array of possible causes',
  `what_to_do` TEXT DEFAULT NULL COMMENT 'JSON array of actions',
  `when_to_seek_help` TEXT DEFAULT NULL COMMENT 'JSON array of warning signs',
  `associated_symptoms` TEXT DEFAULT NULL COMMENT 'JSON array of related symptoms',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. PREVENTION TIPS TABLE
-- Stores prevention tips for diseases
-- =====================================================
CREATE TABLE IF NOT EXISTS `prevention_tips` (
  `id` VARCHAR(36) PRIMARY KEY,
  `disease_id` VARCHAR(36) NOT NULL,
  `category` VARCHAR(100) DEFAULT 'General' COMMENT 'Hygiene, Nutrition, Exercise, Mental Health, etc.',
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `priority` INT DEFAULT 0 COMMENT 'Higher number = higher priority',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`disease_id`) REFERENCES `diseases`(`id`) ON DELETE CASCADE,
  INDEX `idx_disease_id` (`disease_id`),
  INDEX `idx_category` (`category`),
  INDEX `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 4. HEALTH ARTICLES TABLE
-- Stores health awareness articles
-- =====================================================
CREATE TABLE IF NOT EXISTS `health_articles` (
  `id` VARCHAR(36) PRIMARY KEY,
  `title` VARCHAR(255) NOT NULL,
  `category` VARCHAR(100) DEFAULT 'General',
  `summary` TEXT NOT NULL,
  `content` TEXT NOT NULL,
  `image_url` VARCHAR(500) DEFAULT NULL,
  `author` VARCHAR(255) DEFAULT 'AwareHealth Team',
  `published_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_featured` BOOLEAN DEFAULT FALSE,
  INDEX `idx_category` (`category`),
  INDEX `idx_published` (`published_at`),
  INDEX `idx_featured` (`is_featured`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 5. VACCINATION REMINDERS TABLE
-- Stores vaccination schedule and reminders
-- =====================================================
CREATE TABLE IF NOT EXISTS `vaccination_reminders` (
  `id` VARCHAR(36) PRIMARY KEY,
  `user_id` VARCHAR(36) NOT NULL,
  `vaccine_name` VARCHAR(255) NOT NULL,
  `dose_number` INT DEFAULT 1,
  `scheduled_date` DATE NOT NULL,
  `completed_date` DATE DEFAULT NULL,
  `is_completed` BOOLEAN DEFAULT FALSE,
  `reminder_sent` BOOLEAN DEFAULT FALSE,
  `notes` TEXT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_scheduled_date` (`scheduled_date`),
  INDEX `idx_completed` (`is_completed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- SAMPLE DATA INSERTION
-- =====================================================

-- Insert sample diseases
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

('disease-004', 'Diabetes Type 2', 'Chronic', 'Severe', '🍬', 'A chronic condition that affects how the body processes blood sugar.', 
'["Increased thirst", "Frequent urination", "Fatigue", "Blurred vision", "Slow healing wounds"]',
'["Obesity", "Family history", "Lack of exercise", "Poor diet", "Age"]',
'["Maintain healthy weight", "Regular exercise", "Balanced diet", "Regular checkups"]',
'["Medication", "Insulin therapy", "Diet management", "Exercise", "Blood sugar monitoring"]',
'Adults, especially over 45', 'Lifelong management'),

('disease-005', 'Hypertension', 'Chronic', 'Moderate', '❤️', 'High blood pressure, a long-term medical condition.', 
'["Headaches", "Shortness of breath", "Dizziness", "Chest pain", "Vision problems"]',
'["Age", "Family history", "Obesity", "Lack of exercise", "High salt intake", "Stress"]',
'["Regular exercise", "Healthy diet", "Reduce salt", "Maintain healthy weight", "Limit alcohol"]',
'["Medication", "Lifestyle changes", "Regular monitoring", "Stress management"]',
'Adults, especially over 40', 'Lifelong management'),

('disease-006', 'Asthma', 'Respiratory', 'Moderate', '🌬️', 'A chronic respiratory condition causing breathing difficulties.', 
'["Wheezing", "Shortness of breath", "Chest tightness", "Coughing", "Difficulty breathing"]',
'["Allergies", "Air pollution", "Exercise", "Cold air", "Respiratory infections", "Genetics"]',
'["Avoid triggers", "Use inhaler as prescribed", "Avoid smoke", "Manage allergies", "Regular checkups"]',
'["Inhalers", "Medications", "Avoid triggers", "Breathing exercises", "Emergency plan"]',
'All age groups', 'Lifelong management');

-- Insert sample symptoms
INSERT INTO `symptoms` (`id`, `name`, `emoji`, `definition`, `normal_range`, `fever_range`, `severity_info`, `possible_causes`, `what_to_do`, `when_to_seek_help`, `associated_symptoms`) VALUES
('symptom-001', 'Fever', '🌡️', 'An elevated body temperature above the normal range, usually above 98.6°F (37°C).', 
'98.6°F (37°C)', 'Above 100.4°F (38°C)',
'{"mild": "100-101°F", "moderate": "101-103°F", "severe": "Above 103°F"}',
'["Infection", "Inflammation", "Vaccination", "Heat exposure", "Medication"]',
'["Rest", "Drink fluids", "Take fever reducers", "Cool compress", "Monitor temperature"]',
'["Fever above 103°F", "Fever lasting more than 3 days", "Severe headache", "Stiff neck", "Rash"]',
'["Chills", "Sweating", "Headache", "Fatigue", "Body aches"]'),

('symptom-002', 'Cough', '😷', 'A reflex action to clear the airways of mucus and irritants.', 
'None', 'N/A',
'{"mild": "Occasional", "moderate": "Frequent but manageable", "severe": "Persistent and disruptive"}',
'["Common cold", "Flu", "Allergies", "Asthma", "Smoking", "Air pollution"]',
'["Stay hydrated", "Use cough drops", "Honey and warm water", "Avoid irritants", "Rest"]',
'["Cough with blood", "Cough lasting more than 3 weeks", "Difficulty breathing", "Chest pain"]',
'["Sore throat", "Runny nose", "Fever", "Chest congestion"]'),

('symptom-003', 'Headache', '🤕', 'Pain in the head or upper neck, varying in intensity and duration.', 
'None', 'N/A',
'{"mild": "Manageable pain", "moderate": "Affects daily activities", "severe": "Debilitating pain"}',
'["Stress", "Dehydration", "Lack of sleep", "Eye strain", "Sinus infection", "Migraine"]',
'["Rest in dark room", "Stay hydrated", "Pain relievers", "Cold compress", "Relaxation techniques"]',
'["Sudden severe headache", "Headache with fever", "Headache after injury", "Vision changes"]',
'["Nausea", "Sensitivity to light", "Dizziness", "Neck stiffness"]');

-- Insert sample prevention tips
INSERT INTO `prevention_tips` (`id`, `disease_id`, `category`, `title`, `description`, `priority`) VALUES
('tip-001', 'disease-001', 'Hygiene', 'Wash Hands Frequently', 'Wash your hands with soap and water for at least 20 seconds, especially after being in public places.', 10),
('tip-002', 'disease-001', 'Hygiene', 'Avoid Touching Face', 'Avoid touching your eyes, nose, and mouth with unwashed hands to prevent virus entry.', 9),
('tip-003', 'disease-001', 'Nutrition', 'Boost Immune System', 'Eat a balanced diet rich in fruits, vegetables, and vitamins to strengthen your immune system.', 8),
('tip-004', 'disease-002', 'Hygiene', 'Get Annual Flu Shot', 'Get vaccinated annually to protect against the most common flu strains.', 10),
('tip-005', 'disease-002', 'Hygiene', 'Practice Good Hygiene', 'Cover your mouth when coughing or sneezing, and dispose of tissues properly.', 9),
('tip-006', 'disease-003', 'Hygiene', 'Wear Masks in Public', 'Wear a mask in crowded or indoor public spaces to reduce transmission risk.', 10),
('tip-007', 'disease-003', 'Hygiene', 'Maintain Social Distance', 'Keep at least 6 feet distance from others, especially in public places.', 9),
('tip-008', 'disease-004', 'Nutrition', 'Eat Balanced Diet', 'Focus on whole grains, lean proteins, fruits, and vegetables. Limit processed foods.', 10),
('tip-009', 'disease-004', 'Exercise', 'Regular Physical Activity', 'Aim for at least 150 minutes of moderate exercise per week.', 9),
('tip-010', 'disease-005', 'Exercise', 'Regular Exercise', 'Engage in at least 30 minutes of moderate exercise most days of the week.', 10),
('tip-011', 'disease-005', 'Nutrition', 'Reduce Salt Intake', 'Limit sodium intake to less than 2,300 mg per day to help control blood pressure.', 9);

-- Insert sample health articles
INSERT INTO `health_articles` (`id`, `title`, `category`, `summary`, `content`, `author`, `is_featured`) VALUES
('article-001', 'Understanding Your Immune System', 'General', 'Learn how your immune system works and how to keep it strong.', 
'Your immune system is your body\'s defense mechanism against infections and diseases. It consists of various cells, tissues, and organs that work together to protect you. To keep your immune system strong, maintain a healthy lifestyle with proper nutrition, regular exercise, adequate sleep, and stress management.', 
'AwareHealth Team', TRUE),

('article-002', 'The Importance of Regular Health Checkups', 'General', 'Regular health checkups can help detect problems early.', 
'Regular health checkups are essential for maintaining good health. They help detect potential health issues early when they are easier to treat. Schedule annual checkups with your doctor, and don\'t skip recommended screenings based on your age and risk factors.', 
'AwareHealth Team', TRUE),

('article-003', 'Managing Stress for Better Health', 'Mental Health', 'Learn effective strategies to manage stress and improve your well-being.', 
'Chronic stress can have negative effects on both your physical and mental health. Practice stress management techniques such as deep breathing, meditation, regular exercise, and maintaining a healthy work-life balance. Don\'t hesitate to seek professional help if stress becomes overwhelming.', 
'AwareHealth Team', FALSE);

-- =====================================================
-- END OF SCRIPT
-- =====================================================

