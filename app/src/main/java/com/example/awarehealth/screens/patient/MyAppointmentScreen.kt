package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .verticalScroll(rememberScrollState())
    ) {

        /* ---------- HEADER ---------- */
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

            Text(
                text = "My Appointments",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredAppointments.isNotEmpty()) {

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    filteredAppointments.forEach { apt ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    when (apt.status) {
                                        "accepted" -> Color(0xFFE9FFF4)
                                        "pending" -> Color(0xFFFFEAD6)
                                        "critical" -> Color(0xFFFFE8E8)
                                        else -> Color(0xFFF3F3F3)
                                    },
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    // ✅ THIS IS THE FIX
                                    if (apt.status == "accepted") {
                                        onAcceptedClick()
                                    }
                                }
                                .padding(16.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column {
                                    Text(
                                        text = apt.doctorName,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF2D3748)
                                    )
                                    Text(
                                        text = "${apt.date} at ${apt.time}",
                                        fontSize = 13.sp,
                                        color = Color(0xFF718096)
                                    )
                                }

                                Text(
                                    text = apt.status.uppercase(),
                                    fontSize = 12.sp,
                                    color = Color(0xFF2D3748)
                                )
                            }
                        }
                    }
                }

            } else {
                Spacer(modifier = Modifier.height(40.dp))
                Text("No appointments found")
            }
        }
    }
}
