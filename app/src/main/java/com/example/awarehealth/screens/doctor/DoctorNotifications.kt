package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

@Composable
fun DoctorNotifications(navController: NavController) {

    var notifications by remember {
        mutableStateOf(
            listOf(
                DoctorNotification(
                    "1",
                    "New Appointment Request",
                    "John Smith has requested an appointment for Dec 15, 2024",
                    "5 mins ago",
                    "appointment",
                    false
                ),
                DoctorNotification(
                    "2",
                    "Critical Patient Alert",
                    "David Brown marked as critical - Severe chest pain",
                    "15 mins ago",
                    "critical",
                    false
                ),
                DoctorNotification(
                    "3",
                    "Appointment Confirmed",
                    "Emma Wilson's appointment has been confirmed",
                    "1 hour ago",
                    "appointment",
                    true
                ),
                DoctorNotification(
                    "4",
                    "System Update",
                    "AwareHealth has been updated to version 2.0",
                    "2 hours ago",
                    "system",
                    true
                )
            )
        )
    }

    val unreadCount = notifications.count { !it.read }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .padding(16.dp)
    ) {

        /* -------- BACK -------- */
        Row(
            modifier = Modifier
                .clickable { navController.popBackStack() }
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back", color = Color(0xFF4A5568))
        }

        /* -------- HEADER -------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "Notifications",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )

                if (unreadCount > 0) {
                    Text(
                        text = "$unreadCount unread notification${if (unreadCount > 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = Color(0xFF718096)
                    )
                }
            }

            if (unreadCount > 0) {
                Button(
                    onClick = {
                        notifications = notifications.map {
                            it.copy(read = true)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFAEE4C1),
                        contentColor = Color(0xFF2D3748)
                    )
                ) {
                    Icon(Icons.Default.Done, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mark all read", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* -------- LIST -------- */
        if (notifications.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F3F3), RoundedCornerShape(24.dp))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color(0xFFCBD5E0),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("No notifications yet", color = Color(0xFF718096))
            }

        } else {

            notifications.forEach { notification ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            if (notification.read) Color(0xFFF3F3F3)
                            else Color(0xFFE9FFF4),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp)
                ) {

                    Row(verticalAlignment = Alignment.Top) {

                        Icon(
                            imageVector = when (notification.type) {
                                "critical" -> Icons.Default.Error
                                "appointment" -> Icons.Default.AccessTime
                                else -> Icons.Default.Notifications
                            },
                            contentDescription = null,
                            tint = when (notification.type) {
                                "critical" -> Color(0xFFE53935)
                                else -> Color(0xFF4A5568)
                            },
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = notification.title,
                                fontWeight = if (notification.read)
                                    FontWeight.Normal
                                else
                                    FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )

                            Text(
                                text = notification.message,
                                fontSize = 13.sp,
                                color = Color(0xFF4A5568)
                            )

                            Text(
                                text = notification.time,
                                fontSize = 11.sp,
                                color = Color(0xFF718096)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {

                            if (!notification.read) {
                                IconButton(
                                    onClick = {
                                        notifications = notifications.map {
                                            if (it.id == notification.id)
                                                it.copy(read = true)
                                            else it
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Done,
                                        contentDescription = null,
                                        tint = Color(0xFF2D3748)
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    notifications =
                                        notifications.filter { it.id != notification.id }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color(0xFF2D3748)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* -------- DATA CLASS -------- */
data class DoctorNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: String,
    val read: Boolean
)
