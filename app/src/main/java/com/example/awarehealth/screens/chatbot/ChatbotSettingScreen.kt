package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatbotSettingsScreen(
    onBack: () -> Unit,
    onTerms: () -> Unit,
    onHelp: () -> Unit
) {

    var soundEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("English") }
    var showClearDialog by remember { mutableStateOf(false) }

    val languages = listOf("English", "Spanish", "French", "Hindi", "Chinese")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .padding(24.dp)
    ) {

        /* ---------- BACK ---------- */
        Text(
            text = "← Back",
            fontSize = 18.sp,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Chatbot Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- PREFERENCES ---------- */
        SectionCard("Preferences", Color(0xFFE9FFF4)) {

            ToggleItem(
                title = "Sound Effects",
                subtitle = "Play sounds for messages",
                checked = soundEnabled
            ) { soundEnabled = !soundEnabled }

            ToggleItem(
                title = "Notifications",
                subtitle = "Receive chat notifications",
                checked = notificationsEnabled
            ) { notificationsEnabled = !notificationsEnabled }

            ToggleItem(
                title = "Dark Mode",
                subtitle = "Coming soon",
                checked = darkMode,
                enabled = false
            ) { darkMode = !darkMode }
        }

        /* ---------- LANGUAGE ---------- */
        SectionCard("Language", Color(0xFFF3F3F3)) {
            Column {
                languages.forEach { lang ->
                    Text(
                        text = if (lang == language) "✓ $lang" else lang,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { language = lang }
                            .padding(10.dp)
                    )
                }
            }
        }

        /* ---------- DATA & PRIVACY ---------- */
        SectionCard("Data & Privacy", Color(0xFFFFEAD6)) {

            ActionItem("Terms & Conditions") { onTerms() }

            ActionItem(
                title = "Clear Chat History",
                titleColor = Color(0xFFE53935)
            ) {
                showClearDialog = true
            }
        }

        /* ---------- HELP ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFAEE4C1), RoundedCornerShape(20.dp))
                .clickable { onHelp() }
                .padding(16.dp)
        ) {
            Column {
                Text("Help & Support", fontWeight = FontWeight.Medium)
                Text(
                    "Get help with chatbot features",
                    fontSize = 12.sp,
                    color = Color(0xFF4A5568)
                )
            }
        }
    }

    /* ---------- CLEAR HISTORY DIALOG ---------- */
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            confirmButton = {
                Text(
                    "Clear All",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFFE53935), RoundedCornerShape(12.dp))
                        .clickable { showClearDialog = false }
                        .padding(10.dp)
                )
            },
            dismissButton = {
                Text(
                    "Cancel",
                    modifier = Modifier
                        .clickable { showClearDialog = false }
                        .padding(10.dp)
                )
            },
            title = { Text("Clear Chat History?") },
            text = {
                Text(
                    "This will permanently delete all chat conversations. This action cannot be undone."
                )
            }
        )
    }
}

/* ---------- REUSABLE COMPONENTS ---------- */

@Composable
private fun SectionCard(
    title: String,
    bgColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun ToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onToggle() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title)
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF718096))
        }
        Text(if (checked) "ON" else "OFF")
    }
}

@Composable
private fun ActionItem(
    title: String,
    titleColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Text(
        text = title,
        color = titleColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    )
}
