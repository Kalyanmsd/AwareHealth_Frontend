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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val experience: String,
    val rating: Double,
    val availability: String,
    val location: String,
    val color: Color
)

/* ---------- SCREEN ---------- */

@Composable
fun SelectDoctorScreen(
    onBack: () -> Unit,
    onDoctorSelected: (Doctor) -> Unit
) {

    val doctors = listOf(
        Doctor(
            id = "1",
            name = "Dr. Sarah Johnson",
            specialty = "Cardiology",
            experience = "10 years",
            rating = 4.8,
            availability = "Available Today",
            location = "City Hospital",
            color = Color(0xFFFFEAD6)
        ),
        Doctor(
            id = "2",
            name = "Dr. Michael Chen",
            specialty = "Dermatology",
            experience = "8 years",
            rating = 4.9,
            availability = "Available Tomorrow",
            location = "Medical Center",
            color = Color(0xFFE9FFF4)
        ),
        Doctor(
            id = "3",
            name = "Dr. Emily Rodriguez",
            specialty = "Pediatrics",
            experience = "12 years",
            rating = 4.7,
            availability = "Available Today",
            location = "Children's Hospital",
            color = Color(0xFFF3F3F3)
        ),
        Doctor(
            id = "4",
            name = "Dr. James Wilson",
            specialty = "Orthopedics",
            experience = "15 years",
            rating = 4.9,
            availability = "Next Week",
            location = "Bone & Joint Clinic",
            color = Color(0xFFAEE4C1)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .verticalScroll(rememberScrollState())
    ) {

        /* ---------- HEADER (BACK) ---------- */
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
                text = "Select Doctor",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Choose a specialist for your appointment",
                color = Color(0xFF718096)
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- DOCTOR LIST ---------- */
            doctors.forEach { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onClick = { onDoctorSelected(doctor) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/* ---------- COMPONENT ---------- */

@Composable
fun DoctorCard(
    doctor: Doctor,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(doctor.color, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row {

            /* Avatar */
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFFFFDF7), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🩺", fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = doctor.name,
                    fontSize = 18.sp,
                    color = Color(0xFF2D3748)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${doctor.specialty} • ${doctor.experience}",
                    fontSize = 14.sp,
                    color = Color(0xFF4A5568)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = doctor.rating.toString(),
                        color = Color(0xFF2D3748),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text("📍", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = doctor.location,
                        fontSize = 14.sp,
                        color = Color(0xFF4A5568)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⏰", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = doctor.availability,
                        fontSize = 14.sp,
                        color = Color(0xFF4A5568)
                    )
                }
            }
        }
    }
}


