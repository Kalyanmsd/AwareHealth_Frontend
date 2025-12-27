package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awarehealth.ui.components.Header
import com.example.awarehealth.navigation.Screen

@Composable
fun PendingRequests(navController: NavController) {
    // Sample data matching the image
    val pendingAppointments = listOf(
        Appointment(
            id = "1",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-15",
            time = "10:00 AM",
            status = "pending",
            symptoms = "Fever and headache"
        ),
        Appointment(
            id = "2",
            patientName = "Lisa Anderson",
            patientId = "P005",
            date = "2024-12-20",
            time = "4:30 PM",
            status = "pending",
            symptoms = "Persistent cough and sore throat"
        ),
        Appointment(
            id = "3",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-22",
            time = "2:00 PM",
            status = "pending",
            symptoms = "Annual health checkup"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header
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
                text = "Pending Requests",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "${pendingAppointments.size} appointment requests waiting",
                fontSize = 14.sp,
                color = Color(0xFF718096)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Appointment Cards
            if (pendingAppointments.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pendingAppointments.forEach { appointment ->
                        PendingRequestCard(
                            appointment = appointment,
                            onClick = {
                                navController.navigate(Screen.ManageAppointment.createRoute(appointment.id))
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
                            Color(0xFFF3F3F3),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFF718096),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No pending requests",
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
fun PendingRequestCard(
    appointment: Appointment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEAD6)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Patient Name and Status
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

                // Status Badge
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFF3F3F3),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "Pending",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4A5568)
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

            // Symptoms
            appointment.symptoms?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Symptoms: $it",
                    fontSize = 14.sp,
                    color = Color(0xFF4A5568),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

