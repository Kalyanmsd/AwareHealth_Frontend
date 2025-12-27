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

/* ---------- MODEL ---------- */

data class Patient(
    val name: String,
    val email: String,
    val phone: String
)

/* ---------- SCREEN ---------- */

@Composable
fun PatientProfileScreen(
    patient: Patient,
    totalAppointments: Int,
    completedAppointments: Int,
    onEditProfile: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .padding(24.dp)
    ) {

        /* ---------- PROFILE HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Color(0xFFAEE4C1), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 40.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    patient.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )

                Text("Patient", color = Color(0xFF4A5568))

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .background(Color(0xFFAEE4C1), RoundedCornerShape(50.dp))
                        .clickable { onEditProfile() }
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✏️ Edit Profile", fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- CONTACT INFO ---------- */
        InfoCard("📧 Email", patient.email)
        InfoCard("📞 Phone", patient.phone)

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- STATS ---------- */
        Text(
            "Statistics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "📅 Total Appointments",
                value = totalAppointments.toString(),
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "✅ Completed",
                value = completedAppointments.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/* ---------- COMPONENTS ---------- */

@Composable
private fun InfoCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F3F3), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontSize = 13.sp, color = Color(0xFF718096))
            Text(value, fontWeight = FontWeight.Medium)
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFFFEAD6), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontSize = 13.sp, color = Color(0xFF718096))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
