package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

data class SelectedDateTime(
    val date: String,
    val time: String
)

/* ---------- SCREEN ---------- */

@Composable
fun SelectDateTimeScreen(
    selectedDoctor: Doctor?,   // ✅ USING EXISTING Doctor MODEL
    onBack: () -> Unit,
    onContinue: (SelectedDateTime) -> Unit
) {

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    val dates = listOf(
        Triple("2024-12-13", "Fri", "13"),
        Triple("2024-12-14", "Sat", "14"),
        Triple("2024-12-15", "Sun", "15"),
        Triple("2024-12-16", "Mon", "16"),
        Triple("2024-12-17", "Tue", "17")
    )

    val timeSlots = listOf(
        "09:00 AM", "10:00 AM", "11:00 AM",
        "12:00 PM", "02:00 PM", "03:00 PM",
        "04:00 PM", "05:00 PM"
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
            /* ---------- TITLE SECTION ---------- */
            Text(
                text = "Select Date & Time",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Booking with ${selectedDoctor?.name ?: "Doctor"}",
                fontSize = 15.sp,
                color = Color(0xFF718096),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- DATE SELECTION ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📅", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Select Date",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 26.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 8.dp)
            ) {
                dates.forEach { (date, day, num) ->
                    Box(
                        modifier = Modifier
                            .width(88.dp)
                            .shadow(
                                elevation = if (selectedDate == date) 4.dp else 2.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = Color.Black.copy(alpha = if (selectedDate == date) 0.12f else 0.05f)
                            )
                            .background(
                                if (selectedDate == date)
                                    Color(0xFFAEE4C1)
                                else
                                    Color.White,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedDate = date }
                            .padding(vertical = 18.dp, horizontal = 16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = day,
                                fontSize = 14.sp,
                                color = Color(0xFF4A5568),
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = num,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748),
                                lineHeight = 30.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ---------- TIME SELECTION ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⏰", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Select Time",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 26.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                timeSlots.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { time ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(
                                        elevation = if (selectedTime == time) 4.dp else 2.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        spotColor = Color.Black.copy(alpha = if (selectedTime == time) 0.12f else 0.05f)
                                    )
                                    .background(
                                        if (selectedTime == time)
                                            Color(0xFFAEE4C1)
                                        else
                                            Color.White,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { selectedTime = time }
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = time,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF2D3748),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                        if (row.size < 3) {
                            repeat(3 - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            /* ---------- CONTINUE BUTTON ---------- */
            Button(
                onClick = {
                    if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                        onContinue(
                            SelectedDateTime(
                                date = selectedDate,
                                time = selectedTime
                            )
                        )
                    }
                },
                enabled = selectedDate.isNotEmpty() && selectedTime.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) 4.dp else 0.dp,
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
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
