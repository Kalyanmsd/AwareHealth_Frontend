package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookingPromptScreen(
    hospitalName: String? = null,
    onBack: () -> Unit,
    onBookAppointment: () -> Unit,
    onContinueChat: () -> Unit
) {

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
            color = Color(0xFF4A5568),
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFAEE4C1), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📅", fontSize = 36.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ready to Book?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = hospitalName?.let {
                        "Book an appointment at $it"
                    } ?: "Consult with experienced doctors for proper treatment",
                    color = Color(0xFF4A5568)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- BENEFITS ---------- */
        BookingBenefitCard("🗓️", "Easy Scheduling", "Book appointments at your convenient time", Color(0xFFE9FFF4))
        BookingBenefitCard("👨‍⚕️", "Verified Doctors", "Consult qualified professionals", Color(0xFFAEE4C1))
        BookingBenefitCard("⏱️", "Quick Response", "Get confirmation within minutes", Color(0xFFFFEAD6))

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- HOW IT WORKS ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F3F3), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "How It Works",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )

                Spacer(modifier = Modifier.height(12.dp))

                StepItem(1, "Select your preferred doctor")
                StepItem(2, "Pick date and time")
                StepItem(3, "Confirm appointment")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- ACTION BUTTONS ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFAEE4C1), RoundedCornerShape(18.dp))
                .clickable { onBookAppointment() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Book Appointment Now", fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F3F3), RoundedCornerShape(18.dp))
                .clickable { onContinueChat() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Continue Chatting")
        }
    }
}

/* ---------- SMALL COMPONENTS ---------- */

@Composable
private fun BookingBenefitCard(
    icon: String,
    title: String,
    desc: String,
    bg: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontWeight = FontWeight.Medium)
            Text(desc, fontSize = 13.sp, color = Color(0xFF4A5568))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun StepItem(step: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color(0xFFAEE4C1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(step.toString())
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp)
    }
    Spacer(modifier = Modifier.height(10.dp))
}

