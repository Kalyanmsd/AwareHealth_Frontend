package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppointmentAcceptedScreen(
    doctorName: String? = null,
    doctorSpecialty: String? = null,
    appointmentDate: String? = null,
    appointmentTime: String? = null,
    location: String? = null,
    onBack: () -> Unit,
    onViewAll: () -> Unit,
    onGoHome: () -> Unit
) {
    // Log received parameters for debugging
    android.util.Log.d("AppointmentAcceptedScreen", "=== Received Parameters ===")
    android.util.Log.d("AppointmentAcceptedScreen", "Doctor Name: '$doctorName'")
    android.util.Log.d("AppointmentAcceptedScreen", "Doctor Specialty: '$doctorSpecialty'")
    android.util.Log.d("AppointmentAcceptedScreen", "Appointment Date: '$appointmentDate'")
    android.util.Log.d("AppointmentAcceptedScreen", "Appointment Time: '$appointmentTime'")
    android.util.Log.d("AppointmentAcceptedScreen", "Location: '$location'")
    
    // Store in local state to ensure they persist
    val finalDoctorName = remember { doctorName ?: "Dr. Doctor" }
    val finalDoctorSpecialty = remember { doctorSpecialty ?: "General Physician" }
    val finalAppointmentDate = remember { appointmentDate ?: "N/A" }
    val finalAppointmentTime = remember { appointmentTime ?: "N/A" }
    val finalLocation = remember { location ?: "Saveetha Hospital" }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBack,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF2D3748)
                )
            ) {
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            /* SUCCESS CIRCLE */
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = Color(0xFFAEE4C1).copy(alpha = 0.4f)
                    )
                    .background(Color(0xFFAEE4C1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* TITLE */
            Text(
                text = "Appointment Confirmed",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            /* SUBTITLE */
            Text(
                text = "Your appointment has been confirmed by the doctor",
                fontSize = 15.sp,
                color = Color(0xFF718096),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            /* DOCTOR INFO CARD */
            AppointmentInfoCard(
                backgroundColor = Color(0xFFE9FFF4),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = finalDoctorName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = finalDoctorSpecialty,
                        fontSize = 14.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* DATE & TIME CARDS */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppointmentInfoCard(
                    backgroundColor = Color(0xFFFFEAD6),
                    modifier = Modifier.weight(1f)
                ) {
                    Column {
                        Text(
                            text = "Date",
                            fontSize = 12.sp,
                            color = Color(0xFF718096),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = finalAppointmentDate,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3748),
                            lineHeight = 22.sp
                        )
                    }
                }

                AppointmentInfoCard(
                    backgroundColor = Color(0xFFFFEAD6),
                    modifier = Modifier.weight(1f)
                ) {
                    Column {
                        Text(
                            text = "Time",
                            fontSize = 12.sp,
                            color = Color(0xFF718096),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = finalAppointmentTime,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3748),
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* LOCATION CARD */
            AppointmentInfoCard(
                backgroundColor = Color(0xFFF3E8FF), // Light purple
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Location",
                        fontSize = 12.sp,
                        color = Color(0xFF718096),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = finalLocation.split(",").firstOrNull() ?: finalLocation,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748),
                        lineHeight = 22.sp
                    )
                    if (finalLocation.contains(",")) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = finalLocation.split(",").getOrNull(1)?.trim() ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* ARRIVAL NOTICE */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Please arrive 10 minutes early",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        /* ACTION BUTTONS */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onViewAll,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F3F3),
                    contentColor = Color(0xFF2D3748)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "View All",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onGoHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color(0xFFAEE4C1).copy(alpha = 0.3f)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    contentColor = Color(0xFF2D3748)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Go Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun AppointmentInfoCard(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}
