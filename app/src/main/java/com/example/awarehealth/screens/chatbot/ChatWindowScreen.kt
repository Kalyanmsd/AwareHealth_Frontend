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
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.viewmodel.ChatbotViewModel
import kotlinx.coroutines.launch

/* ---------- MODEL ---------- */

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: String, // "user" | "bot"
    val time: String,
    val symptoms: List<String>? = null, // Symptoms from AI response
    val preventionTips: List<String>? = null, // Prevention tips from AI response
    val foodRecommendations: List<String>? = null // Food recommendations from AI response
)

/* ---------- SCREEN ---------- */

@Composable
fun ChatWindowScreen(
    repository: AppRepository,
    onBack: () -> Unit,
    onDaysQuestion: () -> Unit = {},
    onQuickReplies: () -> Unit = {},
    onDiseaseResponse: () -> Unit = {},
    onBookingPrompt: () -> Unit = {},
    onNavigateToSelectDoctor: () -> Unit = {} // New callback for navigating to doctor selection
) {
    // Initialize ViewModel
    val viewModel: ChatbotViewModel = remember { ChatbotViewModel(repository) }
    val uiState by viewModel.uiState.collectAsState()
    
    val scope = rememberCoroutineScope()

    var inputText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "1",
                text = "Hello! I'm your AI Health Assistant. Please enter the disease name you'd like to know about.",
                sender = "bot",
                time = "Now"
            )
        )
    }
    
    // Show error if any
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            errorMessage = it
            viewModel.clearError()
        }
    }

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
                            
                            // Display symptoms if available
                            msg.symptoms?.takeIf { it.isNotEmpty() }?.let { symptoms ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "🔍 Identified Symptoms:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2D3748)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                symptoms.forEach { symptom ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "• ",
                                            fontSize = 14.sp,
                                            color = Color(0xFFAEE4C1),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = symptom,
                                            fontSize = 14.sp,
                                            color = Color(0xFF4A5568),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                            
                            // Display prevention tips if available
                            msg.preventionTips?.takeIf { it.isNotEmpty() }?.let { tips ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "💡 Prevention Tips:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2D3748)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                tips.forEach { tip ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "✓ ",
                                            fontSize = 14.sp,
                                            color = Color(0xFFAEE4C1),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = tip,
                                            fontSize = 14.sp,
                                            color = Color(0xFF4A5568),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                            
                            // Display food recommendations if available (only after user says yes)
                            msg.foodRecommendations?.takeIf { it.isNotEmpty() }?.let { foods ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "🍎 Food Recommendations:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2D3748)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                foods.forEach { food ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "• ",
                                            fontSize = 14.sp,
                                            color = Color(0xFFAEE4C1),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = food,
                                            fontSize = 14.sp,
                                            color = Color(0xFF4A5568),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                            
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

            if (uiState.isLoading) {
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
                                    "AI is thinking...",
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
                        .clickable(enabled = inputText.isNotBlank() && !uiState.isLoading) {
                            val userInput = inputText.trim()
                            if (userInput.isEmpty()) return@clickable
                            
                            // Add user message to UI
                            messages.add(
                                ChatMessage(
                                    id = System.currentTimeMillis().toString(),
                                    text = userInput,
                                    sender = "user",
                                    time = "Now"
                                )
                            )
                            
                            // Clear input
                            inputText = ""
                            errorMessage = null
                            
                            // Send to API - Try AI symptom checker first, then fallback to regular chatbot
                            scope.launch {
                                // Try AI symptom checker first
                                val (chatResponse, symptomResponse) = viewModel.sendMessageWithAI(
                                    message = userInput,
                                    conversationId = uiState.conversationId
                                )
                                
                                if (symptomResponse != null && symptomResponse.success) {
                                    val conversationState = symptomResponse.conversation_state
                                    
                                    // Track conversation progress to enforce correct flow (strict validation)
                                    val hasShownSymptoms = messages.any { it.symptoms != null && it.symptoms.isNotEmpty() }
                                    val hasAskedPrevention = messages.any { it.text.contains("Can I provide prevention tips", ignoreCase = true) }
                                    val hasShownPrevention = messages.any { it.preventionTips != null && it.preventionTips.isNotEmpty() }
                                    val hasAskedFood = messages.any { it.text.contains("Can I provide food recommendations", ignoreCase = true) }
                                    val hasShownFood = messages.any { it.foodRecommendations != null && it.foodRecommendations.isNotEmpty() }
                                    val hasAskedDays = messages.any { it.text.contains("how many days", ignoreCase = true) || it.text.contains("suffering from", ignoreCase = true) }
                                    
                                    // CRITICAL: If backend sends wrong state (e.g., asking_food before prevention), fix it
                                    // Enforce strict flow: symptoms -> prevention question -> prevention tips -> food question -> food recommendations -> days question
                                    if (conversationState == "asking_food" && !hasShownSymptoms) {
                                        // Backend skipped to food question - force correct flow
                                        if (symptomResponse.symptoms?.isNotEmpty() == true) {
                                            messages.add(
                                                ChatMessage(
                                                    id = (System.currentTimeMillis() + 1).toString(),
                                                    text = "Here are the symptoms of ${symptomResponse.disease_name ?: "this disease"}:",
                                                    sender = "bot",
                                                    time = "Now",
                                                    symptoms = symptomResponse.symptoms,
                                                    preventionTips = null,
                                                    foodRecommendations = null
                                                )
                                            )
                                            kotlinx.coroutines.delay(300)
                                            messages.add(
                                                ChatMessage(
                                                    id = (System.currentTimeMillis() + 2).toString(),
                                                    text = "Can I provide prevention tips?",
                                                    sender = "bot",
                                                    time = "Now",
                                                    symptoms = null,
                                                    preventionTips = null,
                                                    foodRecommendations = null
                                                )
                                            )
                                        }
                                        return@launch
                                    }
                                    
                                    // Handle different conversation states separately
                                    when (conversationState) {
                                        "waiting_for_disease" -> {
                                            // User just entered disease name - show ONLY symptoms (strict rule: nothing else)
                                            // Enforce: Only show symptoms, nothing else
                                            if (!hasShownSymptoms && symptomResponse.symptoms?.isNotEmpty() == true) {
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = "Here are the symptoms of ${symptomResponse.disease_name ?: "this disease"}:",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = symptomResponse.symptoms,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                                
                                                // After showing symptoms, ask about prevention tips (one by one)
                                                kotlinx.coroutines.delay(300)
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 2).toString(),
                                                        text = "Can I provide prevention tips?",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            }
                                        }
                                        
                                        "asking_prevention" -> {
                                            // User responded to prevention tips question
                                            // Enforce correct flow: Must have shown symptoms and asked prevention question first
                                            if (!hasShownSymptoms) {
                                                // Backend skipped symptoms - show them first
                                                if (symptomResponse.symptoms?.isNotEmpty() == true) {
                                                    messages.add(
                                                        ChatMessage(
                                                            id = (System.currentTimeMillis() + 1).toString(),
                                                            text = "Here are the symptoms of ${symptomResponse.disease_name ?: "this disease"}:",
                                                            sender = "bot",
                                                            time = "Now",
                                                            symptoms = symptomResponse.symptoms,
                                                            preventionTips = null,
                                                            foodRecommendations = null
                                                        )
                                                    )
                                                    kotlinx.coroutines.delay(300)
                                                    messages.add(
                                                        ChatMessage(
                                                            id = (System.currentTimeMillis() + 2).toString(),
                                                            text = "Can I provide prevention tips?",
                                                            sender = "bot",
                                                            time = "Now",
                                                            symptoms = null,
                                                            preventionTips = null,
                                                            foodRecommendations = null
                                                        )
                                                    )
                                                }
                                                return@launch
                                            }
                                            
                                            // Strict rule: Only show prevention tips if user said yes/provide/okay/sure/fine
                                            if (symptomResponse.prevention_tips?.isNotEmpty() == true && !hasShownPrevention) {
                                                // User confirmed - show ONLY prevention tips (no mixing with food)
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = "Here are the prevention tips:",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = symptomResponse.prevention_tips,
                                                        foodRecommendations = null
                                                    )
                                                )
                                                kotlinx.coroutines.delay(300)
                                            }
                                            
                                            // After prevention tips (or after user said no), ask about food recommendations
                                            // Strict rule: Ask questions one by one, don't mix
                                            // Only ask if we haven't asked already and we've shown prevention (or user said no)
                                            if (!hasAskedFood) {
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 2).toString(),
                                                        text = "Can I provide food recommendations?",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            }
                                        }
                                        
                                        "asking_food" -> {
                                            // User responded to food recommendations question
                                            // CRITICAL FIX: Backend might send BOTH prevention_tips AND food_recommendations in same response
                                            // This causes them to appear in same message bubble - we must prevent this!
                                            val hasBothPreventionAndFood = symptomResponse.prevention_tips?.isNotEmpty() == true && 
                                                                          symptomResponse.food_recommendations?.isNotEmpty() == true
                                            
                                            if (hasBothPreventionAndFood) {
                                                // Backend sent BOTH - this causes the bug where both appear in same message
                                                // Fix: Show ONLY food recommendations here (ignore prevention_tips)
                                                // Prevention tips should have been shown earlier when user confirmed prevention question
                                                if (!hasShownFood) {
                                                    messages.add(
                                                        ChatMessage(
                                                            id = (System.currentTimeMillis() + 1).toString(),
                                                            text = "Here are the food recommendations:",
                                                            sender = "bot",
                                                            time = "Now",
                                                            symptoms = null,
                                                            preventionTips = null, // CRITICAL: Ignore prevention_tips - show only food
                                                            foodRecommendations = symptomResponse.food_recommendations
                                                        )
                                                    )
                                                    kotlinx.coroutines.delay(300)
                                                }
                                                
                                                // Ask days question
                                                if (!hasAskedDays) {
                                                    messages.add(
                                                        ChatMessage(
                                                            id = (System.currentTimeMillis() + 2).toString(),
                                                            text = "From how many days are you suffering from this disease?",
                                                            sender = "bot",
                                                            time = "Now",
                                                            symptoms = null,
                                                            preventionTips = null,
                                                            foodRecommendations = null
                                                        )
                                                    )
                                                }
                                                return@launch
                                            }
                                            
                                            // Check if backend sent prevention_tips when it should send food_recommendations
                                            val hasPreventionInFoodState = symptomResponse.prevention_tips?.isNotEmpty() == true && 
                                                                          (symptomResponse.food_recommendations == null || symptomResponse.food_recommendations.isEmpty())
                                            
                                            if (hasPreventionInFoodState) {
                                                // Backend sent wrong data - it sent prevention tips when we're in food state
                                                // This is the bug shown in the image - backend shows prevention tips when user says yes to food
                                                // Fix: Ignore prevention_tips in this state, only show food_recommendations
                                                // If no food_recommendations, don't show anything, just ask days
                                                if (symptomResponse.food_recommendations?.isNotEmpty() == true && !hasShownFood) {
                                                    messages.add(
                                                        ChatMessage(
                                                            id = (System.currentTimeMillis() + 1).toString(),
                                                            text = "Here are the food recommendations:",
                                                            sender = "bot",
                                                            time = "Now",
                                                            symptoms = null,
                                                            preventionTips = null, // CRITICAL: Don't show prevention_tips here
                                                            foodRecommendations = symptomResponse.food_recommendations
                                                        )
                                                    )
                                                    kotlinx.coroutines.delay(300)
                                                }
                                                
                                                // Ask days question
                                                if (!hasAskedDays) {
                                                    messages.add(
                                                        ChatMessage(
                                                            id = (System.currentTimeMillis() + 2).toString(),
                                                            text = "From how many days are you suffering from this disease?",
                                                            sender = "bot",
                                                            time = "Now",
                                                            symptoms = null,
                                                            preventionTips = null,
                                                            foodRecommendations = null
                                                        )
                                                    )
                                                }
                                                return@launch
                                            }
                                            
                                            // Normal flow: Only show food recommendations if user said yes/okay/sure (permission given)
                                            if (symptomResponse.food_recommendations?.isNotEmpty() == true && !hasShownFood) {
                                                // User confirmed - show ONLY food recommendations (no mixing with prevention)
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = "Here are the food recommendations:",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null, // CRITICAL: Never show prevention tips in food state
                                                        foodRecommendations = symptomResponse.food_recommendations
                                                    )
                                                )
                                                kotlinx.coroutines.delay(300)
                                            }
                                            
                                            // After food recommendations (or after user said no), ask about days
                                            // Strict rule: Don't give hospital advice unless days are asked first
                                            if (!hasAskedDays) {
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 2).toString(),
                                                        text = "From how many days are you suffering from this disease?",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            }
                                        }
                                        
                                        "asking_days" -> {
                                            // User said no to food recommendations - ask about days
                                            // Extract days question from message or use default
                                            val daysQuestion = symptomResponse.message?.takeIf { 
                                                it.contains("days", ignoreCase = true) || 
                                                it.contains("suffering", ignoreCase = true)
                                            } ?: "From how many days are you suffering from this disease?"
                                            
                                            messages.add(
                                                ChatMessage(
                                                    id = (System.currentTimeMillis() + 1).toString(),
                                                    text = daysQuestion,
                                                    sender = "bot",
                                                    time = "Now",
                                                    symptoms = null,
                                                    preventionTips = null,
                                                    foodRecommendations = null
                                                )
                                            )
                                        }
                                        
                                        "completed" -> {
                                            // After days provided: Show response based on days
                                            val days = symptomResponse.days_suffering
                                            
                                            if (days != null && days > 3) {
                                                // More than 3 days: Always show hospital number to book appointment
                                                val hospitalInfo = "\n\n🏥 Saveetha Hospital\n" +
                                                    "📞 Phone: +91-44-2680 1580 / +91-44-2680 1581\n" +
                                                    "📍 Location: Saveetha Nagar, Thandalam, Chennai - 602 105\n\n" +
                                                    "Please call to book your appointment for immediate medical consultation."
                                                
                                                val messageText = if (symptomResponse.message != null && 
                                                                      symptomResponse.message!!.contains("Saveetha", ignoreCase = true)) {
                                                    // Flask already included hospital info
                                                    symptomResponse.message
                                                } else {
                                                    // Add hospital info to Flask message or use default
                                                    val baseMessage = symptomResponse.message ?: 
                                                        "Since you've been suffering for $days days, please book an appointment:"
                                                    baseMessage + hospitalInfo
                                                }
                                                
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = messageText,
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            } else {
                                                // 3 days or less: Advise care and prevention tips
                                                val messageText = symptomResponse.message ?: (
                                                    "Since you've been suffering for $days day(s), take care of your health. Rest well, stay hydrated, and don't be alone - reach out to family or friends for support. If symptoms worsen, please consult a doctor."
                                                )
                                                
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = messageText,
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = symptomResponse.prevention_tips,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            }
                                        }
                                        
                                        else -> {
                                            // Fallback: Handle unknown states
                                            // CRITICAL: NEVER mix prevention tips and food recommendations in one message
                                            // They must be in separate messages, shown at appropriate times
                                            
                                            // First, show symptoms if available and not shown yet
                                            if (!hasShownSymptoms && symptomResponse.symptoms?.isNotEmpty() == true) {
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = "Here are the symptoms of ${symptomResponse.disease_name ?: "this disease"}:",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = symptomResponse.symptoms,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                                kotlinx.coroutines.delay(300)
                                                
                                                // Then ask prevention question
                                                if (!hasAskedPrevention) {
                                                    messages.add(
                                                        ChatMessage(
                                                            id = (System.currentTimeMillis() + 2).toString(),
                                                            text = "Can I provide prevention tips?",
                                                            sender = "bot",
                                                            time = "Now",
                                                            symptoms = null,
                                                            preventionTips = null,
                                                            foodRecommendations = null
                                                        )
                                                    )
                                                }
                                            } else {
                                                // Just show the message if no symptoms
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = symptomResponse.message ?: "I've analyzed your information.",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null, // CRITICAL: Don't mix with food
                                                        foodRecommendations = null // CRITICAL: Don't mix with prevention
                                                    )
                                                )
                                            }
                                        }
                                    }
                                } else if (chatResponse != null && chatResponse.success) {
                                    // Regular chatbot responded
                                    messages.add(
                                        ChatMessage(
                                            id = (System.currentTimeMillis() + 1).toString(),
                                            text = chatResponse.response,
                                            sender = "bot",
                                            time = "Now"
                                        )
                                    )
                                } else {
                                    // Show error message with better troubleshooting
                                    val errorMsg = when {
                                        uiState.error != null -> {
                                            // Use error from ViewModel (includes Flask API or PHP API errors)
                                            uiState.error!!
                                        }
                                        chatResponse != null && !chatResponse.success -> {
                                            // Server returned error response
                                            chatResponse.response
                                        }
                                        symptomResponse != null && !symptomResponse.success -> {
                                            // Flask API error
                                            val flaskUrl = com.example.awarehealth.data.RetrofitClient.FLASK_BASE_URL_PUBLIC
                                            "Cannot connect to AI service.\n\nPlease check:\n1. Flask API is running (python flask_api.py)\n2. Phone and computer on same Wi-Fi\n3. Test: ${flaskUrl}health in phone browser\n4. Or use regular chatbot (fallback)"
                                        }
                                        else -> {
                                            // Generic fallback with helpful info
                                            val baseUrl = com.example.awarehealth.data.RetrofitClient.BASE_URL_PUBLIC
                                            val flaskUrl = com.example.awarehealth.data.RetrofitClient.FLASK_BASE_URL_PUBLIC
                                            "Unable to connect to server.\n\nPlease check:\n1. XAMPP Apache is running (port 80)\n2. Flask API is running (port 5000)\n3. Phone and computer on same Wi-Fi\n4. Test PHP: ${baseUrl}test_connection.php\n5. Test Flask: ${flaskUrl}health"
                                        }
                                    }
                                    
                                    messages.add(
                                        ChatMessage(
                                            id = (System.currentTimeMillis() + 1).toString(),
                                            text = errorMsg,
                                            sender = "bot",
                                            time = "Now"
                                        )
                                    )
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
