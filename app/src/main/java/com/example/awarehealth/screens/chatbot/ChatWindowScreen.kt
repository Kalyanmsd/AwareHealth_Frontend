package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/* ---------- MODEL ---------- */

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: String, // "user" | "bot"
    val time: String
)

/* ---------- SCREEN ---------- */

@Composable
fun ChatWindowScreen(
    onBack: () -> Unit,
    onDaysQuestion: () -> Unit = {},
    onQuickReplies: () -> Unit = {},
    onDiseaseResponse: () -> Unit = {},
    onBookingPrompt: () -> Unit = {}
) {

    val scope = rememberCoroutineScope()

    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "1",
                text = "Hello! I'm your AI Health Assistant. How can I help you today?",
                sender = "bot",
                time = "Now"
            )
        )
    }
    
    // Track conversation state
    var conversationState by remember { mutableStateOf("initial") } // "initial", "symptoms_discussed", "medicines_discussed", "ready_for_duration"
    var mentionedSymptoms by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header is handled in NavGraph

        /* ---------- CHAT LIST ---------- */
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = false
        ) {
            items(messages) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        if (msg.sender == "user")
                            Arrangement.End
                        else
                            Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .shadow(
                                elevation = 1.dp,
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = if (msg.sender == "user") 20.dp else 4.dp,
                                    bottomEnd = if (msg.sender == "user") 4.dp else 20.dp
                                ),
                                spotColor = Color.Black.copy(alpha = 0.05f)
                            )
                            .background(
                                if (msg.sender == "user")
                                    Color(0xFFAEE4C1)
                                else
                                    Color(0xFFF3F3F3),
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = if (msg.sender == "user") 20.dp else 4.dp,
                                    bottomEnd = if (msg.sender == "user") 4.dp else 20.dp
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Column {
                            Text(
                                text = msg.text,
                                fontSize = 15.sp,
                                color = if (msg.sender == "user")
                                    Color(0xFF2D3748)
                                else
                                    Color(0xFF2D3748),
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = msg.time,
                                fontSize = 11.sp,
                                color = Color(0xFF718096),
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }

            if (isTyping) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .shadow(
                                    elevation = 1.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    spotColor = Color.Black.copy(alpha = 0.05f)
                                )
                                .background(Color(0xFFF3F3F3), RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    "Bot is typing",
                                    fontSize = 14.sp,
                                    color = Color(0xFF4A5568)
                                )
                                Text("...", fontSize = 14.sp, color = Color(0xFF4A5568))
                            }
                        }
                    }
                }
            }
        }

        /* ---------- INPUT BAR ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    spotColor = Color.Black.copy(alpha = 0.1f)
                )
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Type your message...",
                            fontSize = 16.sp,
                            color = Color(0xFF2D3748),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF3F3F3),
                        unfocusedContainerColor = Color(0xFFF3F3F3),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xFF2D3748),
                        unfocusedTextColor = Color(0xFF2D3748),
                        focusedPlaceholderColor = Color(0xFF2D3748),
                        unfocusedPlaceholderColor = Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF2D3748),
                        fontWeight = FontWeight.Normal
                    )
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = CircleShape,
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        )
                        .background(
                            if (inputText.isNotBlank())
                                Color(0xFFAEE4C1)
                            else
                                Color(0xFFE2E8F0),
                            CircleShape
                        )
                        .clickable(enabled = inputText.isNotBlank()) {
                            messages.add(
                                ChatMessage(
                                    id = System.currentTimeMillis().toString(),
                                    text = inputText,
                                    sender = "user",
                                    time = "Now"
                                )
                            )

                            val userInput = inputText
                            inputText = ""
                            isTyping = true

                            scope.launch {
                                delay(1500)
                                val botReply = generateBotReply(
                                    userInput, 
                                    conversationState,
                                    mentionedSymptoms
                                )
                                
                                // Update conversation state based on bot response
                                when (botReply.action) {
                                    "symptoms_discussed" -> {
                                        conversationState = "symptoms_discussed"
                                        mentionedSymptoms = botReply.symptoms ?: ""
                                    }
                                    "medicines_discussed" -> {
                                        conversationState = "medicines_discussed"
                                    }
                                    "ready_for_duration" -> {
                                        conversationState = "ready_for_duration"
                                    }
                                    else -> {}
                                }
                                
                                messages.add(
                                    ChatMessage(
                                        id = System.currentTimeMillis().toString(),
                                        text = botReply.text,
                                        sender = "bot",
                                        time = "Now"
                                    )
                                )
                                isTyping = false
                                
                                // Only navigate after proper conversation flow
                                when (botReply.action) {
                                    "days_question" -> {
                                        delay(500) // Small delay before navigation
                                        onDaysQuestion()
                                    }
                                    "quick_replies" -> onQuickReplies()
                                    "disease_response" -> onDiseaseResponse()
                                    "booking_prompt" -> onBookingPrompt()
                                    else -> {}
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank())
                            Color.White
                        else
                            Color(0xFF718096),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

/* ---------- BOT LOGIC ---------- */

data class BotReply(
    val text: String,
    val action: String = "", // "days_question", "quick_replies", "disease_response", "booking_prompt", "symptoms_discussed", "medicines_discussed", "ready_for_duration"
    val symptoms: String? = null
)

private fun generateBotReply(
    userInput: String,
    conversationState: String,
    mentionedSymptoms: String
): BotReply {
    val lowerInput = userInput.lowercase()
    
    // Check if user mentions days/duration
    val daysPattern = Regex("""(\d+)\s*(day|days|d)\s*(ago|for)?""")
    val daysMatch = daysPattern.find(lowerInput)
    var mentionedDays = daysMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
    
    // Also check for text numbers and "more than" patterns
    if (lowerInput.contains("more than") || lowerInput.contains("over") || lowerInput.contains("above")) {
        val moreThanPattern = Regex("""(more than|over|above)\s*(\d+)""")
        val moreThanMatch = moreThanPattern.find(lowerInput)
        val moreThanDays = moreThanMatch?.groupValues?.get(2)?.toIntOrNull()
        if (moreThanDays != null && moreThanDays >= 3) {
            mentionedDays = moreThanDays
        }
    }
    
    // Check for duration keywords
    val hasDurationKeywords = lowerInput.contains("day") || 
                             lowerInput.contains("days") || 
                             lowerInput.contains("week") || 
                             lowerInput.contains("weeks") ||
                             lowerInput.contains("how long") ||
                             lowerInput.contains("since") ||
                             lowerInput.contains("duration") ||
                             lowerInput.contains("ago") ||
                             lowerInput.contains("for")
    
    // Check for symptom keywords
    val hasSymptoms = lowerInput.contains("fever") || 
                     lowerInput.contains("headache") || 
                     lowerInput.contains("cough") || 
                     lowerInput.contains("pain") || 
                     lowerInput.contains("ache") || 
                     lowerInput.contains("symptom") ||
                     lowerInput.contains("symptoms") ||
                     lowerInput.contains("sick") || 
                     lowerInput.contains("ill") ||
                     lowerInput.contains("nausea") ||
                     lowerInput.contains("dizziness") ||
                     lowerInput.contains("fatigue") ||
                     lowerInput.contains("sore throat") ||
                     lowerInput.contains("runny nose")
    
    // Extract symptom text
    val symptomText = if (hasSymptoms) {
        val symptomKeywords = listOf("fever", "headache", "cough", "pain", "ache", "nausea", "dizziness", "fatigue", "sore throat", "runny nose")
        symptomKeywords.firstOrNull { lowerInput.contains(it) } ?: "symptoms"
    } else ""
    
    // Check for medicine/treatment keywords
    val hasMedicineKeywords = lowerInput.contains("medicine") || 
                             lowerInput.contains("medication") || 
                             lowerInput.contains("treatment") || 
                             lowerInput.contains("prescription") ||
                             lowerInput.contains("drug") ||
                             lowerInput.contains("pill") ||
                             lowerInput.contains("tablet") ||
                             lowerInput.contains("what should i take") ||
                             lowerInput.contains("what medicine") ||
                             lowerInput.contains("suggest medicine")
    
    // Check for yes/affirmative responses
    val isAffirmative = lowerInput.contains("yes") || 
                       lowerInput.contains("yeah") || 
                       lowerInput.contains("sure") || 
                       lowerInput.contains("okay") ||
                       lowerInput.contains("ok") ||
                       lowerInput.contains("alright") ||
                       lowerInput.contains("please") ||
                       lowerInput.contains("tell me")
    
    return when {
        // Booking/appointment related - always handle
        lowerInput.contains("doctor") || lowerInput.contains("appointment") || lowerInput.contains("book") -> 
            BotReply(
                "Would you like to book an appointment with a doctor?",
                "booking_prompt"
            )
        
        // If user mentions 3 or more days with symptoms -> navigate to DiseaseResponse
        mentionedDays >= 3 && hasSymptoms -> 
            BotReply(
                "I see you've been experiencing symptoms for $mentionedDays days. Let me provide you with detailed information about your condition.",
                "disease_response"
            )
        
        // Conversation flow based on state
        conversationState == "initial" && hasSymptoms -> {
            // First time mentioning symptoms - discuss in chat
            BotReply(
                "I understand you're experiencing ${if (symptomText.isNotEmpty()) symptomText else "symptoms"}. " +
                "Let me help you with this. Can you describe your symptoms in more detail? " +
                "For example, are you experiencing any fever, body aches, or other discomfort?",
                "symptoms_discussed",
                symptomText
            )
        }
        
        conversationState == "symptoms_discussed" && (hasMedicineKeywords || isAffirmative) -> {
            // After discussing symptoms, user asks about medicines or agrees to see suggestions
            BotReply(
                "Based on your symptoms, I can suggest some general medications that might help. " +
                "However, please remember that these are general suggestions and it's always best to consult with a healthcare professional. " +
                "Common over-the-counter options include pain relievers for headaches, decongestants for cold symptoms, and rest for recovery. " +
                "Would you like me to provide more specific recommendations?",
                "medicines_discussed"
            )
        }
        
        conversationState == "medicines_discussed" && (isAffirmative || lowerInput.contains("tell me") || lowerInput.contains("suggest")) -> {
            // After discussing medicines and user wants more info, ask about duration and navigate
            BotReply(
                "To provide you with the most accurate recommendations, I need to know how long you've been experiencing these symptoms. " +
                "Let me help you specify the duration.",
                "days_question"
            )
        }
        
        conversationState == "medicines_discussed" && (hasDurationKeywords || mentionedDays > 0) -> {
            // User provided duration after medicines discussion
            if (mentionedDays >= 3) {
                BotReply(
                    "I see you've been experiencing symptoms for $mentionedDays days. Let me provide you with detailed information about your condition.",
                    "disease_response"
                )
            } else {
                BotReply(
                    "I understand you've had symptoms for $mentionedDays days. For symptoms lasting less than 3 days, " +
                    "it's often a minor issue. However, to provide you with the best recommendations, " +
                    "let me help you specify the exact duration.",
                    "days_question"
                )
            }
        }
        
        conversationState == "medicines_discussed" -> {
            // After medicines discussion, ask about duration and navigate to DaysQuestionScreen
            BotReply(
                "To better assist you with accurate recommendations, I need to know how long you've been experiencing these symptoms. " +
                "Let me help you specify the duration.",
                "days_question"
            )
        }
        
        // If user mentions symptoms but less than 3 days -> provide general advice
        hasSymptoms && mentionedDays > 0 && mentionedDays < 3 -> 
            BotReply(
                "I understand you've had symptoms for $mentionedDays days. For symptoms lasting less than 3 days, " +
                "it's often a minor issue. Please monitor and rest. If symptoms persist or worsen, consult a doctor.",
                ""
            )
        
        // Quick reply suggestions for common conditions (only if explicitly asked)
        (lowerInput.contains("cold") || lowerInput.contains("flu")) && isAffirmative -> 
            BotReply(
                "Based on your symptoms, this looks like a common cold. Would you like to see quick reply options?",
                "quick_replies"
            )
        
        // Default response - continue conversation naturally
        else -> {
            when (conversationState) {
                "symptoms_discussed" -> 
                    BotReply("I'm here to help. Can you tell me more about your symptoms, or would you like to know about treatment options?")
                "medicines_discussed" -> 
                    BotReply("Is there anything else you'd like to know about your symptoms or treatment options?")
                "ready_for_duration" -> 
                    BotReply("To provide the best recommendations, please let me know how many days you've been experiencing these symptoms.")
                else -> 
                    BotReply("I understand your concern. Can you tell me more about what you're experiencing?")
            }
        }
    }
}
