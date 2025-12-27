package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppointmentSuccessScreen(
    onGoHome: () -> Unit,
    onMyAppointments: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        /* ---------- SUCCESS ICON ---------- */
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(Color(0xFFAEE4C1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("✔", fontSize = 56.sp, color = Color(0xFF2D3748))
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- TITLE ---------- */
        Text(
            text = "Appointment Booked!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D3748),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        /* ---------- DESCRIPTION ---------- */
        Text(
            text = "Your appointment has been successfully scheduled. The doctor will review and confirm shortly.",
            color = Color(0xFF718096),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- WHAT'S NEXT CARD ---------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {

            Text(
                text = "What's Next?",
                color = Color(0xFF2D3748),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            SuccessStep("1", "Wait for doctor confirmation (usually within 24 hours)")
            SuccessStep("2", "You'll receive a notification once confirmed")
            SuccessStep("3", "Arrive 10 minutes early on the appointment day")
        }

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- ACTION BUTTONS (FIXED) ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ActionButton(
                modifier = Modifier.weight(1f),
                text = "My Appointments",
                background = Color(0xFFFFEAD6),
                onClick = onMyAppointments
            )

            ActionButton(
                modifier = Modifier.weight(1f),
                text = "Go Home",
                background = Color(0xFFAEE4C1),
                onClick = onGoHome
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

/* ---------- COMPONENTS ---------- */

@Composable
private fun SuccessStep(number: String, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Color(0xFFAEE4C1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = Color(0xFF2D3748), fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(text, color = Color(0xFF4A5568))
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    background: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(background, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF2D3748),
            fontWeight = FontWeight.Medium
        )
    }
}
