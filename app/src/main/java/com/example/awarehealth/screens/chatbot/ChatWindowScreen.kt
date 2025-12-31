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
    val time: String
)

/* ---------- SCREEN ---------- */

@Composable
fun ChatWindowScreen(
    repository: AppRepository,
    onBack: () -> Unit,
    onDaysQuestion: () -> Unit = {},
    onQuickReplies: () -> Unit = {},
    onDiseaseResponse: () -> Unit = {},
    onBookingPrompt: () -> Unit = {}
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
                            
                            // Send to API
                            scope.launch {
                                val response = viewModel.sendMessage(
                                    message = userInput,
                                    conversationId = uiState.conversationId
                                )
                                
                                if (response != null && response.success) {
                                    // Add bot response to UI
                                    messages.add(
                                        ChatMessage(
                                            id = (System.currentTimeMillis() + 1).toString(),
                                            text = response.response,
                                            sender = "bot",
                                            time = "Now"
                                        )
                                    )
                                } else {
                                    // Show error message - prioritize ViewModel error state
                                    val errorMsg = if (uiState.error != null) {
                                        uiState.error!!
                                    } else if (response != null && !response.success) {
                                        response.response
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
