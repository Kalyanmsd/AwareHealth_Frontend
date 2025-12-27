package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */
data class DaysOption(
    val value: String,
    val label: String,
    val emoji: String,
    val description: String
)

/* ---------- SCREEN ---------- */
@Composable
fun DaysQuestionScreen(
    symptoms: String,
    onBack: () -> Unit,
    onContinue: (String) -> Unit
) {

    var selectedDays by remember { mutableStateOf("") }

    val daysOptions = listOf(
        DaysOption("1-2", "1-2 days", "☀️", "Recent onset"),
        DaysOption("3-5", "3-5 days", "☁️", "Few days"),
        DaysOption("6-7", "6-7 days", "⛅", "About a week"),
        DaysOption("7+", "More than a week", "⚠️", "Persistent symptoms")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header is handled in NavGraph

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            /* ---------- HEADER CARD ---------- */
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
                    verticalAlignment = Alignment.Top
                ) {
                    // Red calendar icon
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color(0xFFE53935), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "JUL",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "17",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 28.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Duration of Symptoms",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (symptoms.isNotEmpty()) 
                                "How long have you been experiencing $symptoms?"
                            else
                                "How long have you been experiencing symptoms?",
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- OPTIONS ---------- */
            daysOptions.forEach { option ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = if (selectedDays == option.value) 4.dp else 2.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = Color.Black.copy(alpha = if (selectedDays == option.value) 0.12f else 0.05f)
                        )
                        .background(
                            if (selectedDays == option.value)
                                Color(0xFFAEE4C1)
                            else
                                Color.White,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedDays = option.value }
                        .padding(22.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(option.emoji, fontSize = 40.sp)
                            Spacer(modifier = Modifier.width(18.dp))
                            Column {
                                Text(
                                    text = option.label,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2D3748),
                                    lineHeight = 24.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = option.description,
                                    fontSize = 14.sp,
                                    color = Color(0xFF4A5568),
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        if (selectedDays == option.value) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = Color(0xFF22C55E),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- TIP SECTION ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(18.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(Color(0xFFFFEAD6), RoundedCornerShape(18.dp))
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text("💡", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Tip: The duration of symptoms helps in better diagnosis and treatment recommendations.",
                        fontSize = 14.sp,
                        color = Color(0xFF2D3748),
                        lineHeight = 22.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- CONTINUE BUTTON ---------- */
            Button(
                onClick = { onContinue(selectedDays) },
                enabled = selectedDays.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = if (selectedDays.isNotEmpty()) 4.dp else 0.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    disabledContainerColor = Color(0xFFE2E8F0),
                    contentColor = Color(0xFF2D3748),
                    disabledContentColor = Color(0xFF718096)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Continue to Diagnosis",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


