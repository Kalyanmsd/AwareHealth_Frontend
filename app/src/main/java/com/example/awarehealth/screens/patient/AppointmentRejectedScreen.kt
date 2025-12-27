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
fun AppointmentRejectedScreen(
    onBack: () -> Unit,
    onBookAgain: () -> Unit,
    onGoHome: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .padding(horizontal = 24.dp)
    ) {

        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                modifier = Modifier.clickable { onBack() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* ---------- ICON ---------- */
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFFFE8E8), CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text("✖", fontSize = 36.sp, color = Color(0xFFE53935))
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* ---------- TITLE ---------- */
        Text(
            text = "Appointment Declined",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D3748),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Unfortunately, this appointment couldn't be confirmed",
            color = Color(0xFF718096),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- REASON ---------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFE8E8), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "Reason",
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Doctor not available at the requested time. Please try booking a different time slot.",
                color = Color(0xFF4A5568)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- WHAT CAN YOU DO ---------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9FFF4), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "What can you do?",
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(12.dp))

            BulletItem("Book another appointment with a different time")
            BulletItem("Try consulting another available doctor")
            BulletItem("Use our AI chatbot for immediate guidance")
        }

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- ACTION BUTTONS (FIXED) ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(modifier = Modifier.weight(1f)) {
                ActionButton(
                    text = "Book Again",
                    background = Color(0xFFAEE4C1),
                    onClick = onBookAgain
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                ActionButton(
                    text = "Go Home",
                    background = Color(0xFFF3F3F3),
                    onClick = onGoHome
                )
            }
        }
    }
}

/* ---------- COMPONENTS ---------- */

@Composable
private fun BulletItem(text: String) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("•", fontSize = 18.sp, color = Color(0xFF4A5568))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color(0xFF4A5568))
    }
}

@Composable
private fun ActionButton(
    text: String,
    background: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D3748)
        )
    }
}
