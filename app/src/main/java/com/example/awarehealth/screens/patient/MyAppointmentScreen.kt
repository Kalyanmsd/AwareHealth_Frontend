package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyAppointmentsScreen(
    user: User,
    appointments: List<Appointment>,
    onBack: () -> Unit,
    onAcceptedClick: () -> Unit   // ✅ IMPORTANT
) {

    var filter by remember { mutableStateOf("all") }

    val patientAppointments =
        appointments.filter { it.patientName == user.name }

    val filteredAppointments =
        if (filter == "all") patientAppointments
        else patientAppointments.filter { it.status == filter }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header is handled in NavGraph

        if (filteredAppointments.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                /* ---------- TITLE SECTION ---------- */
                item {
                    Text(
                        text = "My Appointments",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 38.sp
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                }

                /* ---------- APPOINTMENT LIST ---------- */
                items(filteredAppointments) { apt ->
                    AppointmentCard(
                        appointment = apt,
                        onClick = {
                            if (apt.status == "accepted") {
                                onAcceptedClick()
                            }
                        }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "My Appointments",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 38.sp
                )
                Spacer(modifier = Modifier.height(120.dp))
                Text(
                    text = "No appointments found",
                    fontSize = 18.sp,
                    color = Color(0xFF718096),
                    lineHeight = 26.sp
                )
            }
        }
    }
}

@Composable
private fun AppointmentCard(
    appointment: Appointment,
    onClick: () -> Unit
) {
    val cardColor = when (appointment.status) {
        "accepted" -> Color(0xFFE9FFF4)
        "pending" -> Color(0xFFFFEAD6)
        "critical" -> Color(0xFFFFE8E8)
        "rejected" -> Color(0xFFFFE8E8)
        else -> Color(0xFFF3F3F3)
    }
    
    val statusColor = when (appointment.status) {
        "accepted" -> Color(0xFF2D7A46)
        "pending" -> Color(0xFFE67E22)
        "critical" -> Color(0xFFE53935)
        "rejected" -> Color(0xFFE53935)
        else -> Color(0xFF718096)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(cardColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = appointment.doctorName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${appointment.date} at ${appointment.time}",
                    fontSize = 15.sp,
                    color = Color(0xFF718096),
                    lineHeight = 22.sp
                )
            }

            Text(
                text = appointment.status.replaceFirstChar { it.uppercase() },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = statusColor
            )
        }
    }
}
