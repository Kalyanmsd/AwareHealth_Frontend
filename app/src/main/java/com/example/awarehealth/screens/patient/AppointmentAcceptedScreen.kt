package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

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
    
    // Clean and validate parameters - remove any placeholder-like strings
    fun cleanValue(value: String?): String? {
        if (value == null || value.isEmpty()) return null
        // Remove placeholder-like patterns
        if (value.startsWith("{") && value.endsWith("}")) return null
        if (value.contains("{") || value.contains("}")) return null
        return value.trim()
    }
    
    // Format date to user-friendly format (e.g., "Jan 15, 2024")
    fun formatDate(dateStr: String?): String {
        if (dateStr == null || dateStr.isEmpty() || dateStr == "Not specified") return "Not specified"
        
        return try {
            // Try parsing YYYY-MM-DD format
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateStr)
            
            if (date != null) {
                // Check if date is in the past
                val today = Calendar.getInstance()
                today.set(Calendar.HOUR_OF_DAY, 0)
                today.set(Calendar.MINUTE, 0)
                today.set(Calendar.SECOND, 0)
                today.set(Calendar.MILLISECOND, 0)
                
                val appointmentCal = Calendar.getInstance()
                appointmentCal.time = date
                appointmentCal.set(Calendar.HOUR_OF_DAY, 0)
                appointmentCal.set(Calendar.MINUTE, 0)
                appointmentCal.set(Calendar.SECOND, 0)
                appointmentCal.set(Calendar.MILLISECOND, 0)
                
                // If date is in the past, return today's date instead
                if (appointmentCal.before(today)) {
                    outputFormat.format(today.time)
                } else {
                    outputFormat.format(date)
                }
            } else {
                dateStr
            }
        } catch (e: Exception) {
            // If parsing fails, return original string
            dateStr
        }
    }
    
    // Format time to user-friendly format (e.g., "10:00 AM")
    fun formatTime(timeStr: String?): String {
        if (timeStr == null || timeStr.isEmpty() || timeStr == "Not specified") return "Not specified"
        
        return try {
            // Try parsing HH:MM:SS or HH:MM format
            val inputFormat = if (timeStr.length == 8) {
                SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            } else {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            }
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val time = inputFormat.parse(timeStr)
            
            if (time != null) {
                outputFormat.format(time)
            } else {
                timeStr
            }
        } catch (e: Exception) {
            // If parsing fails, return original string
            timeStr
        }
    }
    
    // Store in local state with proper fallbacks and formatting
    val finalDoctorName = remember { 
        cleanValue(doctorName)?.takeIf { it.isNotEmpty() } ?: "Dr. Doctor"
    }
    val finalDoctorSpecialty = remember { 
        cleanValue(doctorSpecialty)?.takeIf { it.isNotEmpty() } ?: "General Physician"
    }
    val rawDate = cleanValue(appointmentDate)?.takeIf { it.isNotEmpty() }
    val finalAppointmentDate = remember { 
        formatDate(rawDate)
    }
    val rawTime = cleanValue(appointmentTime)?.takeIf { it.isNotEmpty() }
    val finalAppointmentTime = remember { 
        formatTime(rawTime)
    }
    val finalLocation = remember { 
        cleanValue(location)?.takeIf { it.isNotEmpty() } ?: "Saveetha Hospital"
    }
    
    // Log final values
    android.util.Log.d("AppointmentAcceptedScreen", "=== Final Display Values ===")
    android.util.Log.d("AppointmentAcceptedScreen", "Final Doctor Name: '$finalDoctorName'")
    android.util.Log.d("AppointmentAcceptedScreen", "Final Doctor Specialty: '$finalDoctorSpecialty'")
    android.util.Log.d("AppointmentAcceptedScreen", "Final Appointment Date: '$finalAppointmentDate'")
    android.util.Log.d("AppointmentAcceptedScreen", "Final Appointment Time: '$finalAppointmentTime'")
    android.util.Log.d("AppointmentAcceptedScreen", "Final Location: '$finalLocation'")
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
            Spacer(modifier = Modifier.height(20.dp))

            /* SUCCESS CIRCLE - Enhanced Design */
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        spotColor = Color(0xFFAEE4C1).copy(alpha = 0.5f)
                    )
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFAEE4C1),
                                Color(0xFF7FD4A3)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Confirmed",
                    modifier = Modifier.size(72.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* TITLE */
            Text(
                text = "Appointment Confirmed",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            /* SUBTITLE */
            Text(
                text = "Your appointment has been confirmed by the doctor",
                fontSize = 16.sp,
                color = Color(0xFF718096),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            /* DOCTOR INFO CARD - Enhanced */
            AppointmentInfoCard(
                backgroundColor = Color(0xFFE9FFF4),
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Person,
                iconColor = Color(0xFF7FD4A3)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF7FD4A3),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = finalDoctorName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748),
                                lineHeight = 26.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = finalDoctorSpecialty,
                                fontSize = 15.sp,
                                color = Color(0xFF4A5568),
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* DATE & TIME CARDS - Enhanced */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppointmentInfoCard(
                    backgroundColor = Color(0xFFFFEAD6),
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CalendarToday,
                    iconColor = Color(0xFFFF8C42)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFFFF8C42),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Date",
                                fontSize = 13.sp,
                                color = Color(0xFF718096),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = finalAppointmentDate,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 24.sp
                        )
                    }
                }

                AppointmentInfoCard(
                    backgroundColor = Color(0xFFE3F2FD),
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Schedule,
                    iconColor = Color(0xFF2196F3)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Time",
                                fontSize = 13.sp,
                                color = Color(0xFF718096),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = finalAppointmentTime,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* LOCATION CARD - Enhanced */
            AppointmentInfoCard(
                backgroundColor = Color(0xFFF3E8FF),
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.LocationOn,
                iconColor = Color(0xFF9C27B0)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF9C27B0),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Location",
                                fontSize = 13.sp,
                                color = Color(0xFF718096),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = finalLocation.split(",").firstOrNull() ?: finalLocation,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748),
                                lineHeight = 24.sp
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* ARRIVAL NOTICE - Enhanced */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(18.dp))
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF7FD4A3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Please arrive 10 minutes early",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
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
    icon: ImageVector? = null,
    iconColor: Color = Color(0xFF4A5568),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}
