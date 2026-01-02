# 🔧 Flask API Update Required

## 📋 New Conversation Flow

The Android app now expects the Flask API to follow this exact flow:

### Step 1: User Types Disease
**User**: "fever"

**Flask API Response**:
```json
{
  "success": true,
  "conversation_state": "asking_prevention",
  "disease_name": "fever",
  "message": "Here are the common symptoms of fever:\n1. Elevated body temperature...\n\nCan I provide prevention tips?",
  "symptoms": ["Elevated body temperature", "Chills", "Sweating", ...],
  "prevention_tips": null,
  "food_recommendations": null
}
```

### Step 2: User Says "Yes" to Prevention Tips
**User**: "yes"

**Flask API Response**:
```json
{
  "success": true,
  "conversation_state": "asking_food",
  "disease_name": "fever",
  "message": "Here are the prevention tips:\n1. Wash hands frequently...\n\nCan I provide what type of food you have to take?",
  "symptoms": [...],
  "prevention_tips": ["Wash hands frequently", "Avoid close contact", ...],
  "food_recommendations": null
}
```

### Step 3: User Says "Yes" to Food Recommendations
**User**: "yes"

**Flask API Response**:
```json
{
  "success": true,
  "conversation_state": "asking_days",
  "disease_name": "fever",
  "message": "Here are the food recommendations:\n1. Clear broths...\n\nFrom how many days are you suffering from?",
  "symptoms": [...],
  "prevention_tips": [...],
  "food_recommendations": ["Clear broths and soups", "Herbal teas", ...]
}
```

### Step 4: User Provides Days
**User**: "5 days" or "2"

**Flask API Response** (if days >= 3):
```json
{
  "success": true,
  "conversation_state": "completed",
  "disease_name": "fever",
  "message": "Thank you for providing that information. You've been experiencing symptoms for 5 day(s).\n\nSince you've been suffering for more than 3 days, I recommend booking an appointment with a doctor.",
  "symptoms": [...],
  "prevention_tips": [...],
  "food_recommendations": [...],
  "days_suffering": 5,
  "suggest_appointment": true
}
```

**Flask API Response** (if days <= 2):
```json
{
  "success": true,
  "conversation_state": "completed",
  "disease_name": "fever",
  "message": "Thank you for providing that information. You've been experiencing symptoms for 2 day(s).",
  "symptoms": [...],
  "prevention_tips": [...],
  "food_recommendations": [...],
  "days_suffering": 2,
  "suggest_appointment": false
}
```

---

## 🔄 Conversation States

The Flask API should use these conversation states:

1. **`waiting_for_disease`**: Initial state, waiting for disease name
2. **`asking_prevention`**: After showing symptoms, asking if user wants prevention tips
3. **`asking_food`**: After showing prevention tips, asking if user wants food recommendations
4. **`asking_days`**: After showing food recommendations, asking for days
5. **`completed`**: After user provides days, conversation complete

---

## ✅ What Android App Does

### When `conversation_state == "asking_days"`:
- App waits for user to provide days
- Shows food recommendations (if available)

### When `conversation_state == "completed"`:
- If `days_suffering >= 3`:
  - Shows message
  - **Automatically navigates to SelectDoctor screen** for booking
- If `days_suffering <= 2`:
  - Shows message: "Please take care, avoid being alone..."
  - Does NOT navigate to booking

---

## 📝 Flask API Implementation Notes

### Key Points:
1. **Don't show food recommendations automatically** - only after user says "yes"
2. **Ask "Can I provide what type of food you have to take?"** after prevention tips
3. **Extract days from user input** (e.g., "5 days", "2", "three days")
4. **Set `suggest_appointment: true`** only if days >= 3
5. **Set `conversation_state: "completed"`** after days are provided

### Example Python Code:
```python
# After prevention tips shown
if conversation_state == "asking_prevention" and user_says_yes:
    conversation_state = "asking_food"
    message = "Here are the prevention tips...\n\nCan I provide what type of food you have to take?"
    food_recommendations = None  # Don't show yet

# After food recommendations shown
if conversation_state == "asking_food" and user_says_yes:
    conversation_state = "asking_days"
    message = "Here are the food recommendations...\n\nFrom how many days are you suffering from?"
    food_recommendations = get_food_recommendations(disease_name)

# After days provided
if conversation_state == "asking_days":
    days = extract_days(user_message)
    conversation_state = "completed"
    if days >= 3:
        suggest_appointment = True
        message = f"Since you've been suffering for {days} days, I recommend booking an appointment."
    else:
        suggest_appointment = False
        message = f"Thank you for providing that information. You've been experiencing symptoms for {days} day(s)."
```

---

## 🧪 Testing

### Test Flow:
1. User: "fever" → Should show symptoms, ask about prevention
2. User: "yes" → Should show prevention tips, ask about food
3. User: "yes" → Should show food recommendations, ask about days
4. User: "5 days" → Should navigate to SelectDoctor screen
5. User: "2 days" → Should show care message, NOT navigate

---

## 📍 Flask API File Location

Update this file:
```
C:\xampp\htdocs\AwareHealth\aimodel\aware_health\flask_api.py
```

---

## ✅ Summary

The Android app is ready! You just need to update the Flask API to:
1. ✅ Ask about food recommendations (don't show automatically)
2. ✅ Ask about days after food recommendations
3. ✅ Return `days_suffering` in response
4. ✅ Set `suggest_appointment: true` only if days >= 3

The Android app will automatically:
- ✅ Show food recommendations only when appropriate
- ✅ Navigate to SelectDoctor when days >= 3
- ✅ Show care message when days <= 2

