package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
            color = Color(0xFFFFEAD6) // Light orange/peach
        ),
        Doctor(
            id = "4",
            name = "Dr. James Wilson",
            specialty = "Orthopedics",
            experience = "15 years",
            rating = 4.9,
            availability = "Next Week",
            location = "Bone & Joint Clinic",
            color = Color(0xFFE9FFF4) // Light green
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header is handled in NavGraph

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            /* ---------- TITLE SECTION ---------- */
            item {
                Column {
                    Text(
                        text = "Select Doctor",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 38.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Choose a specialist for your appointment",
                        fontSize = 15.sp,
                        color = Color(0xFF718096),
                        lineHeight = 22.sp
                    )
                }
            }

            /* ---------- DOCTOR LIST ---------- */
            items(doctors) { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onClick = { onDoctorSelected(doctor) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
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
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(doctor.color, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(22.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* Avatar with light green background */
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFE9FFF4), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🩺", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Doctor Name
                Text(
                    text = doctor.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Specialty and Experience
                Text(
                    text = "${doctor.specialty} • ${doctor.experience}",
                    fontSize = 15.sp,
                    color = Color(0xFF4A5568),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Rating and Location Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = doctor.rating.toString(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.width(18.dp))

                    Text("📍", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = doctor.location,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Availability Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⏰", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = doctor.availability,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568)
                    )
                }
            }
        }
    }
}


