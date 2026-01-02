package com.example.awarehealth.ui.screens

/* ---------- IMPORTS (VERY IMPORTANT) ---------- */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODELS ---------- */

data class ChatConversation(
    val id: String,
    val title: String,
    val lastMessage: String,
    val time: String,
    val unread: Boolean
)


/* ---------- SCREEN ---------- */

@Composable
fun ChatbotMainScreen(
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    onNewChat: (String?) -> Unit,
    onSettings: () -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val conversations = listOf(
        ChatConversation(
            "1",
            "Fever and Headache",
            "How many days have you had these symptoms?",
            "2 mins ago",
            true
        ),
        ChatConversation(
            "2",
            "Common Cold Symptoms",
            "This looks like a common cold...",
            "1 hour ago",
            false
        ),
        ChatConversation(
            "3",
            "Stomach Pain",
            "Please describe the pain location.",
            "2 days ago",
            false
        )
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header is handled in NavGraph

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            /* ---------- AI HEALTH ASSISTANT CARD ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = Color(0xFFAEE4C1).copy(alpha = 0.3f)
                    )
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE9FFF4),
                                Color(0xFFF0FDF4)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Enhanced icon with gradient background
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = Color.Black.copy(alpha = 0.1f)
                            )
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1E3A5F),
                                        Color(0xFF2D4A6F)
                                    )
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✨", fontSize = 36.sp)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "AI Health Assistant",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 30.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Get instant health guidance and personalized care recommendations",
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ---------- SEARCH FIELD ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .clickable { onNewChat(null) }
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        if (it.isNotBlank()) {
                            onNewChat(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Ask me anything about your health...",
                            color = Color(0xFF718096),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFFAEE4C1),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xFF2D3748),
                        unfocusedTextColor = Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF2D3748),
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            /* ---------- RECENT CONVERSATIONS ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Conversations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
                Text(
                    "Settings",
                    fontSize = 15.sp,
                    color = Color(0xFFAEE4C1),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onSettings() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (conversations.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "💬",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No conversations yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4A5568)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Start a new chat to get health guidance",
                            fontSize = 14.sp,
                            color = Color(0xFF718096)
                        )
                    }
                }
            } else {
                conversations.forEach { chat ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(22.dp),
                                spotColor = if (chat.unread) 
                                    Color(0xFFAEE4C1).copy(alpha = 0.2f)
                                else
                                    Color.Black.copy(alpha = 0.06f)
                            )
                            .background(
                                if (chat.unread)
                                    Color(0xFFE9FFF4)
                                else
                                    Color.White,
                                RoundedCornerShape(22.dp)
                            )
                            .clickable { onOpenChat(chat.id) }
                            .padding(22.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    chat.title,
                                    fontSize = 17.sp,
                                    fontWeight = if (chat.unread)
                                        FontWeight.Bold
                                    else
                                        FontWeight.SemiBold,
                                    color = Color(0xFF2D3748),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    chat.time,
                                    fontSize = 13.sp,
                                    color = Color(0xFF718096),
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                chat.lastMessage,
                                fontSize = 15.sp,
                                color = Color(0xFF4A5568),
                                lineHeight = 22.sp,
                                maxLines = 2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        /* ---------- FLOATING ACTION BUTTON ---------- */
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(64.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = Color(0xFFAEE4C1).copy(alpha = 0.4f)
                )
                .background(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFAEE4C1),
                            Color(0xFF8DD4B0)
                        )
                    ),
                    shape = CircleShape
                )
                .clickable { onNewChat(null) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Chat",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
