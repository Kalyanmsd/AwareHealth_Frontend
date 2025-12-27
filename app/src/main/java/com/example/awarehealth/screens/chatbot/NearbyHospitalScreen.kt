package com.example.awarehealth.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */
data class Hospital(
    val id: String,
    val name: String,
    val distance: String,
    val address: String,
    val rating: Double,
    val phone: String,
    val openNow: Boolean,
    val specialty: String
)

/* ---------- SCREEN ---------- */
@Composable
fun NearbyHospitalsScreen(
    onBack: () -> Unit,
    onBookAppointment: (String) -> Unit
) {

    val context = LocalContext.current
    var selectedHospitalId by remember { mutableStateOf<String?>(null) }

    val hospitals = listOf(
        Hospital("1", "City General Hospital", "1.2 km", "123 Main Street", 4.5, "5551234567", true, "Multi-specialty"),
        Hospital("2", "Medicare Clinic", "2.5 km", "456 Oak Avenue", 4.2, "5552345678", true, "General Physician"),
        Hospital("3", "Care Plus Hospital", "3.8 km", "789 Park Road", 4.7, "5553456789", false, "Emergency Care"),
        Hospital("4", "HealthFirst Medical Center", "5.0 km", "321 Hill Street", 4.4, "5554567890", true, "Multi-specialty")
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
            /* ---------- HEADER CARD ---------- */
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        )
                        .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Nearby Hospitals",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Healthcare facilities near you",
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            /* ---------- HOSPITAL LIST ---------- */
            items(hospitals) { hospital ->
                HospitalCard(
                    hospital = hospital,
                    isSelected = selectedHospitalId == hospital.id,
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${hospital.phone}")
                        context.startActivity(intent)
                    },
                    onDirections = {
                        selectedHospitalId = if (selectedHospitalId == hospital.id) null else hospital.id
                    },
                    onBookAppointment = {
                        onBookAppointment(hospital.name)
                    }
                )
            }

            /* ---------- EMERGENCY SECTION ---------- */
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        )
                        .background(Color(0xFFFFEAD6), RoundedCornerShape(20.dp))
                        .padding(22.dp)
                ) {
                    Column {
                        Text(
                            text = "Emergency Services",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 26.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                context.startActivity(
                                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
                                )
                            }
                        ) {
                            Text("📞", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Emergency: 911",
                                fontSize = 16.sp,
                                color = Color(0xFF2D3748),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                context.startActivity(
                                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001234567"))
                                )
                            }
                        ) {
                            Text("🚑", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Ambulance: 1800-123-4567",
                                fontSize = 16.sp,
                                color = Color(0xFF2D3748),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun HospitalCard(
    hospital: Hospital,
    isSelected: Boolean,
    onCall: () -> Unit,
    onDirections: () -> Unit,
    onBookAppointment: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            // Hospital Name
            Text(
                text = hospital.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            
            // Specialty
            Text(
                text = hospital.specialty,
                fontSize = 15.sp,
                color = Color(0xFF4A5568),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            // Address
            Text(
                text = hospital.address,
                fontSize = 14.sp,
                color = Color(0xFF718096),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Distance and Status Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = hospital.distance,
                        fontSize = 15.sp,
                        color = Color(0xFF2D3748),
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = if (hospital.openNow) "Open Now" else "Closed",
                    fontSize = 15.sp,
                    color = if (hospital.openNow) Color(0xFF2D7A46) else Color(0xFFE53935),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onCall,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFAEE4C1),
                        contentColor = Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Call",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onDirections,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFEAD6),
                        contentColor = Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Directions",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Book Appointment Button (shown when selected)
            if (isSelected) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBookAppointment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFAEE4C1),
                        contentColor = Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Book Appointment",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
