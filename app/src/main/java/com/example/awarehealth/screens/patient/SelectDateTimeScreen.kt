package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .verticalScroll(rememberScrollState())
    ) {

        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                modifier = Modifier.clickable { onBack() }
            )
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            /* ---------- TITLE ---------- */
            Text(
                text = "Select Date & Time",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Booking with ${selectedDoctor?.name ?: ""}",
                color = Color(0xFF718096)
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- DATE SELECTION ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📅", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Date",
                    fontSize = 18.sp,
                    color = Color(0xFF2D3748)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 8.dp)
            ) {
                dates.forEach { (date, day, num) ->
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .background(
                                if (selectedDate == date)
                                    Color(0xFFAEE4C1)
                                else
                                    Color(0xFFF3F3F3),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedDate = date }
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(day, fontSize = 13.sp, color = Color(0xFF4A5568))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                num,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2D3748)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ---------- TIME SELECTION ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⏰", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Time",
                    fontSize = 18.sp,
                    color = Color(0xFF2D3748)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                timeSlots.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { time ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selectedTime == time)
                                            Color(0xFFAEE4C1)
                                        else
                                            Color(0xFFF3F3F3),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedTime = time }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = time,
                                    fontSize = 14.sp,
                                    color = Color(0xFF2D3748)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        if (row.size < 3) {
                            repeat(3 - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ---------- CONTINUE BUTTON ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFAEE4C1), RoundedCornerShape(20.dp))
                    .clickable {
                        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                            onContinue(
                                SelectedDateTime(
                                    date = selectedDate,
                                    time = selectedTime
                                )
                            )
                        }
                    }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
