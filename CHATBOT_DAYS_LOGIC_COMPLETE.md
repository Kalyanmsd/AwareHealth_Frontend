# ✅ Chatbot Days Logic Update Complete!

## Changes Made

**Requirement**: After asking "how many days are you suffering from", implement logic based on days:
- **Days >= 3** (more than 3 days): Show Saveetha Hospital number to book appointment
- **Days < 3** (less than 3 days): Advise to avoid being alone and take care, follow prevention tips

## What Was Changed

### 1. Backend Chatbot API (`chatbot.php`)

**Before:**
- Generic hospital recommendation for days >= 3
- No specific care advice for days < 3

**After:**

#### Days >= 3 (3 or more days):
```php
$hospitalInfo = "\n\n⚠️ IMPORTANT: Since you've been suffering for more than 3 days, please book an appointment:\n\n" .
                "🏥 Saveetha Hospital\n" .
                "📞 Phone: +91-44-2680 1580 / +91-44-2680 1581\n" .
                "📍 Location: Saveetha Nagar, Thandalam, Chennai - 602 105\n\n" .
                "Please call to book your appointment for immediate medical consultation.";
```

**Response includes:**
- ✅ Saveetha Hospital name
- ✅ Phone numbers (2 numbers)
- ✅ Full address
- ✅ Clear call-to-action

#### Days < 3 (less than 3 days):
```php
$careAdvice = "\n\n💡 Important Reminders:\n" .
              "• Avoid being alone during this time\n" .
              "• Take proper rest and care\n" .
              "• Follow the prevention tips shared earlier\n" .
              "• Monitor your symptoms closely\n" .
              "• If symptoms worsen, consult a doctor immediately\n\n" .
              "Take care and get well soon! 🌟";
```

**Response includes:**
- ✅ Reminder to avoid being alone
- ✅ Rest and care advice
- ✅ Prevention tips reminder
- ✅ Symptom monitoring advice
- ✅ Warning for symptom worsening

### 2. System Prompt (`chatbot.php`)

**Updated rules:**
```php
If the user has been experiencing symptoms for 3 or more days, you MUST:
- Provide Saveetha Hospital contact information (phone number and address)
- Suggest booking an appointment immediately
- Emphasize the importance of seeking timely medical care

If the user has been experiencing symptoms for less than 3 days, you MUST:
- Advise them to avoid being alone
- Recommend taking proper rest and care
- Suggest following prevention tips
- Monitor symptoms closely
```

### 3. Frontend Chat Logic (`ChatWindowScreen.kt`)

**Updated "completed" state handling:**

#### Days >= 3:
```kotlin
if (days != null && days >= 3) {
    val messageText = symptomResponse.message ?: (
        "Since you've been suffering for $days days, please book an appointment:\n\n" +
        "🏥 Saveetha Hospital\n" +
        "📞 Phone: +91-44-2680 1580 / +91-44-2680 1581\n" +
        "📍 Location: Saveetha Nagar, Thandalam, Chennai - 602 105\n\n" +
        "Please call to book your appointment for immediate medical consultation."
    )
    // Add message to chat
}
```

#### Days < 3:
```kotlin
else {
    val messageText = symptomResponse.message ?: (
        "Since you've been suffering for less than 3 days, please follow these important reminders:\n\n" +
        "💡 Important Reminders:\n" +
        "• Avoid being alone during this time\n" +
        "• Take proper rest and care\n" +
        "• Follow the prevention tips shared earlier\n" +
        "• Monitor your symptoms closely\n" +
        "• If symptoms worsen, consult a doctor immediately\n\n" +
        "Take care and get well soon! 🌟"
    )
    // Add message with prevention tips
}
```

## Logic Flow

1. **User enters symptoms** → Chatbot asks about disease
2. **User provides disease** → Chatbot asks "From how many days are you suffering from this disease?"
3. **User replies with days**:
   - **Days >= 3** → Show Saveetha Hospital contact info
   - **Days < 3** → Show care advice + prevention tips

## Example Responses

### Example 1: Days >= 3 (e.g., "5 days")

**Bot Response:**
```
[AI-generated health advice]

⚠️ IMPORTANT: Since you've been suffering for more than 3 days, please book an appointment:

🏥 Saveetha Hospital
📞 Phone: +91-44-2680 1580 / +91-44-2680 1581
📍 Location: Saveetha Nagar, Thandalam, Chennai - 602 105

Please call to book your appointment for immediate medical consultation.
```

### Example 2: Days < 3 (e.g., "2 days")

**Bot Response:**
```
[AI-generated health advice]

💡 Important Reminders:
• Avoid being alone during this time
• Take proper rest and care
• Follow the prevention tips shared earlier
• Monitor your symptoms closely
• If symptoms worsen, consult a doctor immediately

Take care and get well soon! 🌟
```

## Files Updated

- ✅ `backend/chatbot.php` - Updated response logic
- ✅ `C:\xampp\htdocs\AwareHealth\chatbot.php` - Deployed
- ✅ `app/src/main/java/com/example/awarehealth/screens/chatbot/ChatWindowScreen.kt` - Updated frontend logic

## Testing

### Test 1: Days >= 3
1. Open chatbot
2. Enter symptoms
3. Provide disease name
4. Reply "5 days" (or any number >= 3)
5. Should see Saveetha Hospital contact information

### Test 2: Days < 3
1. Open chatbot
2. Enter symptoms
3. Provide disease name
4. Reply "2 days" (or any number < 3)
5. Should see care advice with prevention tips reminder

## Saveetha Hospital Contact Information

- **Name**: Saveetha Hospital
- **Phone 1**: +91-44-2680 1580
- **Phone 2**: +91-44-2680 1581
- **Location**: Saveetha Nagar, Thandalam, Chennai - 602 105

## Status

- ✅ Days >= 3: Shows Saveetha Hospital number and address
- ✅ Days < 3: Shows care advice (avoid being alone, take care, prevention tips)
- ✅ Frontend logic updated
- ✅ Backend logic updated
- ✅ Deployed to XAMPP
- ✅ Ready to test!

**The chatbot now provides appropriate responses based on days of suffering! 🎉**

