package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awarehealth.ui.components.Header
import com.example.awarehealth.navigation.Screen

@Composable
fun AppointmentDetails(
    navController: NavController,
    appointmentId: String
) {

    /* -------- SAMPLE DATA -------- */
    val appointments = listOf(
        // Critical appointments (matching CriticalAppointments IDs)
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
        ),
        // Accepted appointments (matching AcceptedRequests IDs - using different IDs to avoid conflict)
        Appointment(
            id = "4",
            patientName = "Emma Wilson",
            patientId = "P002",
            date = "2024-12-16",
            time = "2:30 PM",
            status = "accepted",
            symptoms = "Regular checkup"
        ),
        Appointment(
            id = "5",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-18",
            time = "3:00 PM",
            status = "accepted",
            symptoms = "Follow-up appointment"
        ),
        // Pending appointments (matching PendingRequests IDs)
        Appointment(
            id = "6",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-15",
            time = "10:00 AM",
            status = "pending",
            symptoms = "Fever and headache"
        ),
        Appointment(
            id = "7",
            patientName = "Lisa Anderson",
            patientId = "P005",
            date = "2024-12-20",
            time = "4:30 PM",
            status = "pending",
            symptoms = "Persistent cough and sore throat"
        ),
        Appointment(
            id = "8",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-22",
            time = "2:00 PM",
            status = "pending",
            symptoms = "Annual health checkup"
        )
    )

    val appointment = appointments.find { it.id == appointmentId }

    if (appointment == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFDF7)),
            contentAlignment = Alignment.Center
        ) {
            Text("Appointment not found", color = Color(0xFF4A5568))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header with menu and notification bell
        Header(
            showMenu = true,
            showNotifications = true,
            onMenuClick = { navController.navigate(Screen.DoctorMenu.route) },
            onNotificationClick = { /* Handle notification click */ }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // Back button with improved styling
            Row(
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF4A5568),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Back",
                    fontSize = 17.sp,
                    color = Color(0xFF4A5568),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Appointment Details Card (Top Card with pink background) - Enhanced
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color(0x1A000000)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3F3)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Title and CRITICAL Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Appointment Details",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2D3748),
                            letterSpacing = (-0.5).sp
                        )

                        // CRITICAL Badge - Enhanced
                        if (appointment.status == "critical") {
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        spotColor = Color(0x33E53935)
                                    )
                                    .background(
                                        Color(0xFFE53935),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "CRITICAL",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    // Alert Banner (only for critical) - Enhanced
                    if (appointment.status == "critical") {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(14.dp),
                                    spotColor = Color(0x33E53935)
                                )
                                .background(
                                    Color(0xFFE53935),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "Critical condition - Requires immediate attention",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Patient Information Card - Enhanced
            InfoCard(
                icon = Icons.Default.Person,
                title = "Patient Information"
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = appointment.patientName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    letterSpacing = (-0.3).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ID: ${appointment.patientId}",
                    fontSize = 15.sp,
                    color = Color(0xFF718096),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Time Card - Enhanced
            InfoCard(
                icon = Icons.Default.CalendarToday,
                title = "Date & Time"
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = appointment.date,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Date",
                            fontSize = 13.sp,
                            color = Color(0xFF718096),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Vertical divider - Enhanced
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(50.dp)
                            .background(Color(0xFFE2E8F0))
                            .padding(horizontal = 12.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = appointment.time,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Time",
                            fontSize = 13.sp,
                            color = Color(0xFF718096),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Symptoms Card - Enhanced
            appointment.symptoms?.let {
                InfoCard(
                    icon = Icons.Default.Description,
                    title = "Symptoms"
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748),
                        lineHeight = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Contact Card - Enhanced with divider
            InfoCard(
                icon = Icons.Default.Phone,
                title = "Contact"
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // Phone number
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color(0xFF4A5568),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "+1 (555) 123-4567",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )
                }

                // Horizontal divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE2E8F0))
                        .padding(vertical = 8.dp)
                )

                // Email
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFF4A5568),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "patient@example.com",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/* -------- REUSABLE INFO CARD - Enhanced -------- */
@Composable
fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0x1A000000)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF4A5568),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    color = Color(0xFF718096),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.2.sp
                )
            }
            content()
        }
    }
}
