package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awarehealth.ui.components.Header
import com.example.awarehealth.navigation.Screen

@Composable
fun CriticalAppointments(navController: NavController) {

    /* -------- SAMPLE DATA (replace later) -------- */
    val criticalAppointments = listOf(
        Appointment(
            id = "1",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-15",
            time = "10:00 AM",
            status = "critical",
            symptoms = "Fever and headache"
        ),
        Appointment(
            id = "2",
            patientName = "David Brown",
            patientId = "P003",
            date = "2024-12-14",
            time = "11:00 AM",
            status = "critical",
            symptoms = "Severe chest pain"
        ),
        Appointment(
            id = "3",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-19",
            time = "5:00 PM",
            status = "critical",
            symptoms = "Severe asthma attack, difficulty breathing"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header with back arrow and notification bell
        Header(
            showBack = true,
            showNotifications = true,
            onBackClick = { navController.popBackStack() },
            onNotificationClick = { /* Handle notification click */ }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Title
            Text(
                text = "Critical Condition",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Urgent cases requiring immediate attention",
                fontSize = 14.sp,
                color = Color(0xFF718096)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Alert Banner
            if (criticalAppointments.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFFFE8E8),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${criticalAppointments.size} critical cases require attention",
                        color = Color(0xFFE53935),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Critical Appointment Cards
            if (criticalAppointments.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    criticalAppointments.forEach { appointment ->
                        CriticalAppointmentCard(
                            appointment = appointment,
                            onClick = {
                                navController.navigate(
                                    Screen.AppointmentDetails.createRoute(appointment.id)
                                )
                            }
                        )
                    }
                }
            } else {
                // Empty State
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .background(
                            Color(0xFFE9FFF4),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFF718096),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No critical cases at the moment",
                            fontSize = 16.sp,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CriticalAppointmentCard(
    appointment: Appointment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE8E8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Red left border indicator
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFE53935))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {
                // Patient Name and Critical Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = appointment.patientName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Patient ID: ${appointment.patientId}",
                            fontSize = 13.sp,
                            color = Color(0xFF718096)
                        )
                    }

                    // Critical Badge
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFE53935),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "CRITICAL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date and Time Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF4A5568),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = appointment.date,
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFF4A5568),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = appointment.time,
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Symptoms/Reason
                appointment.symptoms?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color(0xFFE53935),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
