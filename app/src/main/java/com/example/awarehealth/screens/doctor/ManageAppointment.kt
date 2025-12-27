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
import androidx.compose.runtime.*
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
fun ManageAppointment(
    navController: NavController,
    appointmentId: String
) {

    /* -------- SAMPLE DATA (replace later) -------- */
    val appointments = listOf(
        Appointment(
            id = "1",
            patientName = "John Smith",
            patientId = "P001",
            date = "2024-12-15",
            time = "10:00 AM",
            symptoms = "Fever and headache",
            status = "pending"
        )
    )

    val appointment = appointments.find { it.id == appointmentId }

    var notes by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var action by remember { mutableStateOf<String?>(null) }

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
        // Header with menu and logo
        Header(
            showMenu = true,
            onMenuClick = { navController.navigate(Screen.DoctorMenu.route) }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Back button
            Row(
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF4A5568),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    color = Color(0xFF4A5568),
                    fontWeight = FontWeight.Medium
                )
            }

            // Title
            Text(
                text = "Manage Appointment",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Appointment Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9FFF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Patient Name
                    Text(
                        text = appointment.patientName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Date and Time
                    Text(
                        text = "${appointment.date} at ${appointment.time}",
                        fontSize = 16.sp,
                        color = Color(0xFF4A5568),
                        fontWeight = FontWeight.Medium
                    )

                    // Symptoms Section
                    appointment.symptoms?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFFFFDF7),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Symptoms:",
                                fontSize = 13.sp,
                                color = Color(0xFF718096),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = it,
                                fontSize = 15.sp,
                                color = Color(0xFF2D3748),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Additional Notes Section
            Text(
                text = "Additional Notes (Optional)",
                fontSize = 16.sp,
                color = Color(0xFF2D3748),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Add any notes or recommendations...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAEE4C1),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            // Accept Appointment Button (Green, White text, White checkmark)
            Button(
                onClick = {
                    action = "accept"
                    showConfirmDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Accept Appointment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mark as Critical Button (Red, White text, White warning icon)
            Button(
                onClick = {
                    action = "critical"
                    showConfirmDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Mark as Critical",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reject Appointment Button (Light gray/white, Dark text, Dark X icon)
            Button(
                onClick = {
                    action = "reject"
                    showConfirmDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F3F3),
                    contentColor = Color(0xFF2D3748)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF2D3748)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Reject Appointment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Confirm Dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "Confirm Action",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to ${action} this appointment for ${appointment.patientName}?",
                    color = Color(0xFF4A5568)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        navController.navigate(Screen.DoctorHome.route) {
                            popUpTo(0) { inclusive = false }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (action == "critical") Color(0xFFE53935)
                        else Color(0xFFAEE4C1),
                        contentColor = if (action == "critical") Color.White
                        else Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirm", fontWeight = FontWeight.Medium)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF4A5568)
                    )
                ) {
                    Text("Cancel", fontWeight = FontWeight.Medium)
                }
            },
            containerColor = Color(0xFFFFFDF7),
            shape = RoundedCornerShape(24.dp)
        )
    }
}

/* -------- DATA CLASS -------- */
data class Appointment(
    val id: String,
    val patientName: String,
    val patientId: String = "",
    val date: String,
    val time: String,
    val status: String,
    val symptoms: String? = null
)
