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
                text = "Hello! I'm your AI Health Assistant. How can I help you today?",
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
                                    
                                    // Handle different conversation states separately
                                    when (conversationState) {
                                        "waiting_for_disease", "asking_prevention" -> {
                                            // Initial state: Show symptoms ONLY in structured format (not in message text)
                                            // Extract just the question part from message
                                            val messageText = symptomResponse.message?.let { msg ->
                                                // Find the question part (usually after symptoms list)
                                                val questionPart = msg.split("\n\n").lastOrNull { 
                                                    it.contains("Can I provide", ignoreCase = true) ||
                                                    it.contains("prevention", ignoreCase = true)
                                                } ?: msg.takeIf { 
                                                    it.contains("Can I provide", ignoreCase = true)
                                                }
                                                questionPart ?: "Can I provide prevention tips?"
                                            } ?: "Can I provide prevention tips?"
                                            
                                            // First message: Symptoms (structured only)
                                            messages.add(
                                                ChatMessage(
                                                    id = (System.currentTimeMillis() + 1).toString(),
                                                    text = "Here are the common symptoms:",
                                                    sender = "bot",
                                                    time = "Now",
                                                    symptoms = symptomResponse.symptoms, // Show ONLY in structured format
                                                    preventionTips = null,
                                                    foodRecommendations = null
                                                )
                                            )
                                            
                                            // Second message: Question (separate)
                                            if (conversationState == "asking_prevention") {
                                                kotlinx.coroutines.delay(300)
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 2).toString(),
                                                        text = messageText,
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
                                            // After prevention tips: Show prevention tips ONLY in structured format
                                            // Extract just the question part
                                            val messageText = symptomResponse.message?.let { msg ->
                                                msg.split("\n\n").lastOrNull { 
                                                    it.contains("Can I provide", ignoreCase = true) ||
                                                    it.contains("food", ignoreCase = true)
                                                } ?: msg.takeIf { 
                                                    it.contains("Can I provide", ignoreCase = true)
                                                }
                                                ?: "Can I provide what type of food you have to take?"
                                            } ?: "Can I provide what type of food you have to take?"
                                            
                                            // First message: Prevention tips (structured only)
                                            messages.add(
                                                ChatMessage(
                                                    id = (System.currentTimeMillis() + 1).toString(),
                                                    text = "Here are the prevention tips:",
                                                    sender = "bot",
                                                    time = "Now",
                                                    symptoms = null,
                                                    preventionTips = symptomResponse.prevention_tips, // Show ONLY in structured format
                                                    foodRecommendations = null
                                                )
                                            )
                                            
                                            // Second message: Food question (separate)
                                            kotlinx.coroutines.delay(300)
                                            messages.add(
                                                ChatMessage(
                                                    id = (System.currentTimeMillis() + 2).toString(),
                                                    text = messageText,
                                                    sender = "bot",
                                                    time = "Now",
                                                    symptoms = null,
                                                    preventionTips = null,
                                                    foodRecommendations = null
                                                )
                                            )
                                        }
                                        
                                        "asking_days" -> {
                                            // After food recommendations: Show food in separate message, then ask days
                                            // First message: Food recommendations
                                            if (symptomResponse.food_recommendations?.isNotEmpty() == true) {
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = "Here are the food recommendations:",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = symptomResponse.food_recommendations
                                                    )
                                                )
                                                
                                                // Second message: Days question (separate)
                                                kotlinx.coroutines.delay(300)
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 2).toString(),
                                                        text = symptomResponse.message?.takeIf { 
                                                            it.contains("days", ignoreCase = true) ||
                                                            it.contains("suffering", ignoreCase = true)
                                                        } ?: "From how many days are you suffering from this disease?",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            } else {
                                                // If no food recommendations, just ask days
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = symptomResponse.message?.takeIf { 
                                                            it.contains("days", ignoreCase = true) ||
                                                            it.contains("suffering", ignoreCase = true)
                                                        } ?: "From how many days are you suffering from this disease?",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            }
                                        }
                                        
                                        "completed" -> {
                                            // After days provided: Show response based on days
                                            val days = symptomResponse.days_suffering
                                            
                                            if (days != null && days >= 3) {
                                                // 3+ days: Show message and navigate
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = symptomResponse.message ?: "Since you've been suffering for more than 3 days, I recommend booking an appointment with a doctor.",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                                kotlinx.coroutines.delay(1000)
                                                onNavigateToSelectDoctor()
                                            } else {
                                                // 1-2 days: Show care message
                                                messages.add(
                                                    ChatMessage(
                                                        id = (System.currentTimeMillis() + 1).toString(),
                                                        text = "Please take care of your health. Rest well, stay hydrated, and avoid being alone - reach out to family or friends for support. If symptoms worsen, please consult a doctor.",
                                                        sender = "bot",
                                                        time = "Now",
                                                        symptoms = null,
                                                        preventionTips = null,
                                                        foodRecommendations = null
                                                    )
                                                )
                                            }
                                        }
                                        
                                        else -> {
                                            // Fallback: Show message with structured data if available
                                            messages.add(
                                                ChatMessage(
                                                    id = (System.currentTimeMillis() + 1).toString(),
                                                    text = symptomResponse.message ?: "I've analyzed your symptoms.",
                                                    sender = "bot",
                                                    time = "Now",
                                                    symptoms = symptomResponse.symptoms,
                                                    preventionTips = symptomResponse.prevention_tips,
                                                    foodRecommendations = symptomResponse.food_recommendations
                                                )
                                            )
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
                                    // Show error message
                                    val errorMsg = if (uiState.error != null) {
                                        uiState.error!!
                                    } else if (chatResponse != null && !chatResponse.success) {
                                        chatResponse.response
                                    } else {
                                        "Unable to connect to server. Please check:\n1. Your internet connection\n2. Server is running\n3. Try again in a moment"
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
