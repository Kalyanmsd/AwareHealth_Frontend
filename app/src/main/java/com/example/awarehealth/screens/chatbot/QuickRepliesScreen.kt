package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODELS ---------- */

data class QuickReply(
    val text: String,
    val emoji: String
)

data class QuickReplyCategory(
    val title: String,
    val color: Color,
    val replies: List<QuickReply>
)

/* ---------- SCREEN ---------- */

@Composable
fun QuickRepliesScreen(
    onBack: () -> Unit,
    onQuickReply: (String) -> Unit,
    onCustomChat: () -> Unit
) {

    val categories = listOf(
        QuickReplyCategory(
            title = "Common Symptoms",
            color = Color(0xFFE9FFF4),
            replies = listOf(
                QuickReply("I have a fever", "🤒"),
                QuickReply("Headache", "🤕"),
                QuickReply("Cough and cold", "🤧"),
                QuickReply("Stomach pain", "😷")
            )
        ),
        QuickReplyCategory(
            title = "Urgent Issues",
            color = Color(0xFFFFEAD6),
            replies = listOf(
                QuickReply("Severe chest pain", "⚠️"),
                QuickReply("Difficulty breathing", "😰"),
                QuickReply("High fever (above 103°F)", "🌡️"),
                QuickReply("Severe injury", "🩹")
            )
        ),
        QuickReplyCategory(
            title = "General Health",
            color = Color(0xFFAEE4C1),
            replies = listOf(
                QuickReply("Preventive care tips", "🛡️"),
                QuickReply("Vaccination schedule", "💉"),
                QuickReply("Healthy lifestyle", "🏃"),
                QuickReply("Nutrition advice", "🥗")
            )
        ),
        QuickReplyCategory(
            title = "Appointments",
            color = Color(0xFFF3F3F3),
            replies = listOf(
                QuickReply("Book a doctor", "👨‍⚕️"),
                QuickReply("View my appointments", "📅"),
                QuickReply("Cancel appointment", "❌"),
                QuickReply("Reschedule", "🔄")
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        /* ---------- BACK ---------- */
        Text(
            text = "← Back",
            fontSize = 18.sp,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Text("⚡", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Quick Replies",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Get instant answers with pre-made questions",
                    color = Color(0xFF4A5568)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- CATEGORIES ---------- */
        categories.forEach { category ->

            Text(
                category.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                category.replies.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { reply ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        category.color,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onQuickReply(reply.text) }
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Text(reply.emoji, fontSize = 26.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        reply.text,
                                        fontSize = 14.sp,
                                        color = Color(0xFF2D3748)
                                    )
                                }
                            }
                        }

                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        /* ---------- FOOTER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F3F3), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Can't find what you're looking for?",
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAEE4C1), RoundedCornerShape(20.dp))
                        .clickable { onCustomChat() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Start Custom Chat",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
