package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppointmentAcceptedScreen(
    onBack: () -> Unit,
    onViewAll: () -> Unit,
    onGoHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .padding(16.dp)
    ) {

        TextButton(onClick = onBack) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* SUCCESS CIRCLE */
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFAEE4C1), RoundedCornerShape(40.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "✓",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Appointment Confirmed",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Your appointment has been confirmed by the doctor",
            fontSize = 14.sp,
            color = Color(0xFF718096),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        /* DOCTOR INFO */
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9FFF4)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Dr. Michael Chen",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3748)
                )
                Text(
                    "Dermatology",
                    fontSize = 13.sp,
                    color = Color(0xFF4A5568)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* DATE & TIME */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEAD6)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Date", fontSize = 12.sp, color = Color(0xFF718096))
                    Text("2024-12-16", fontWeight = FontWeight.Medium)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEAD6)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Time", fontSize = 12.sp, color = Color(0xFF718096))
                    Text("2:30 PM", fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* LOCATION */
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Location", fontSize = 12.sp, color = Color(0xFF718096))
                Text("Medical Center", fontWeight = FontWeight.Medium)
                Text(
                    "123 Health Street, Room 204",
                    fontSize = 13.sp,
                    color = Color(0xFF4A5568)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Please arrive 10 minutes early",
                fontSize = 13.sp,
                color = Color(0xFF2D5016)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        /* ACTION BUTTONS */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onViewAll,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F3F3))
            ) {
                Text("View All", color = Color(0xFF2D3748))
            }

            Button(
                onClick = onGoHome,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAEE4C1))
            ) {
                Text("Go Home", color = Color(0xFF2D3748))
            }
        }
    }
}
