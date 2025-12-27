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
import androidx.compose.material.icons.filled.MoreVert
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

data class QuickTopic(
    val emoji: String,
    val label: String,
    val query: String
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

    val quickTopics = listOf(
        QuickTopic("🤒", "Fever", "I have a fever"),
        QuickTopic("🤧", "Cold & Flu", "Cold symptoms"),
        QuickTopic("😷", "Cough", "I have a cough"),
        QuickTopic("🤕", "Headache", "Headache relief")
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
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    )
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dark blue square icon with stars
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFF1E3A5F), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✨", fontSize = 32.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "AI Health Assistant",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Get instant health guidance",
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- SEARCH FIELD ---------- */
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                placeholder = {
                    Text(
                        "Search or ask a question...",
                        color = Color(0xFF718096),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF718096)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFE8F5),
                    unfocusedContainerColor = Color(0xFFFFE8F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- QUICK TOPICS ---------- */
            Text(
                "Quick Topics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                quickTopics.forEach { topic ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(
                                elevation = 2.dp,
                                shape = CircleShape,
                                spotColor = Color.Black.copy(alpha = 0.05f)
                            )
                            .background(Color.White, CircleShape)
                            .clickable { onNewChat(topic.query) }
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(topic.emoji, fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                topic.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2D3748),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ---------- RECENT CONVERSATIONS ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Conversations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
                Text(
                    "Settings",
                    fontSize = 15.sp,
                    color = Color(0xFF4A5568),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onSettings() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            conversations.forEach { chat ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = Color.Black.copy(alpha = 0.08f)
                        )
                        .background(
                            if (chat.unread)
                                Color(0xFFE9FFF4)
                            else
                                Color.White,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onOpenChat(chat.id) }
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                chat.title,
                                fontSize = 16.sp,
                                fontWeight = if (chat.unread)
                                    FontWeight.Bold
                                else
                                    FontWeight.SemiBold,
                                color = Color(0xFF2D3748),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                chat.time,
                                fontSize = 12.sp,
                                color = Color(0xFF718096),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            chat.lastMessage,
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 20.sp,
                            maxLines = 2
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
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
                    elevation = 6.dp,
                    shape = CircleShape,
                    spotColor = Color.Black.copy(alpha = 0.15f)
                )
                .background(Color(0xFFAEE4C1), CircleShape)
                .clickable { onNewChat(null) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "New Chat",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
