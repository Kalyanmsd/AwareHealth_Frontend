package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppointmentSummaryScreen(
    selectedDoctor: Doctor?,
    selectedDateTime: SelectedDateTime?,
    user: User,
    onBack: () -> Unit,
    onConfirm: (String) -> Unit
) {

    var symptoms by remember { mutableStateOf("") }

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
                text = "Appointment Summary",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Review your appointment details",
                color = Color(0xFF718096)
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- DOCTOR INFO ---------- */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEAD6), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFFFFFDF7), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🩺", fontSize = 26.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = selectedDoctor?.name ?: "",
                            color = Color(0xFF2D3748),
                            fontSize = 18.sp
                        )
                        Text(
                            text = selectedDoctor?.specialty ?: "",
                            color = Color(0xFF4A5568)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* ---------- DATE & TIME (FIXED) ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard(
                        title = "Date",
                        value = selectedDateTime?.date ?: "",
                        background = Color(0xFFE9FFF4),
                        icon = "📅"
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard(
                        title = "Time",
                        value = selectedDateTime?.time ?: "",
                        background = Color(0xFFAEE4C1),
                        icon = "⏰"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* ---------- SYMPTOMS ---------- */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F3F3), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📄", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Describe Your Symptoms",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2D3748)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = symptoms,
                    onValueChange = { symptoms = it },
                    placeholder = { Text("Tell us what you're experiencing...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ---------- CONFIRM BUTTON ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFAEE4C1), RoundedCornerShape(20.dp))
                    .clickable { onConfirm(symptoms) }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Confirm Appointment",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/* ---------- CARD (NO WEIGHT HERE) ---------- */

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    background: Color,
    icon: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, fontSize = 13.sp, color = Color(0xFF718096))
        Text(value, color = Color(0xFF2D3748))
    }
}
