package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODELS ---------- */

data class Appointment(
    val id: String,
    val doctorName: String,
    val patientName: String,
    val date: String,
    val time: String,
    val status: String
)

data class User(
    val name: String,
    val id: String = ""
)

/* ---------- SCREEN ---------- */

@Composable
fun PatientHomeScreen(
    user: User,
    appointments: List<Appointment>,
    onMenuClick: () -> Unit,
    onBookAppointment: () -> Unit,
    onChatbot: () -> Unit,
    onHealthInfo: () -> Unit,
    onMyAppointments: () -> Unit,
    onViewAll: () -> Unit
) {

    val patientAppointments =
        appointments.filter { it.patientName == user.name }

    val upcomingCount =
        patientAppointments.count {
            it.status == "pending" || it.status == "accepted"
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header is handled in NavGraph

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- WELCOME SECTION ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                    .padding(26.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hello, ${user.name.split(" ")[0]}! 👋",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "How are you feeling today?",
                        fontSize = 16.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- STATS CARDS ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    "Upcoming",
                    upcomingCount.toString(),
                    Color(0xFFFFEAD6),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    "Completed",
                    "12",
                    Color(0xFFAEE4C1),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ---------- QUICK ACTIONS ---------- */
            Text(
                text = "Quick Actions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionCard(
                    icon = Icons.Default.CalendarToday,
                    label = "Book Appointment",
                    date = "JUL 17",
                    background = Color(0xFFFFEAD6),
                    onClick = onBookAppointment,
                    modifier = Modifier.weight(1f)
                )
                ActionCard(
                    icon = Icons.Default.SmartToy,
                    label = "AI Chatbot",
                    date = null,
                    background = Color(0xFFE9FFF4),
                    onClick = onChatbot,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionCard(
                    icon = Icons.Default.Info,
                    label = "Health Info",
                    date = null,
                    background = Color(0xFFF3F3F3),
                    onClick = onHealthInfo,
                    modifier = Modifier.weight(1f)
                )
                ActionCard(
                    icon = Icons.Default.Favorite,
                    label = "My Appointments",
                    date = null,
                    background = Color(0xFFAEE4C1),
                    onClick = onMyAppointments,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            /* ---------- RECENT APPOINTMENTS ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Appointments",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = "View All",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D7FF9),
                    modifier = Modifier
                        .clickable { onViewAll() }
                        .padding(vertical = 4.dp, horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            patientAppointments.take(3).forEach { apt ->
                AppointmentItem(apt)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/* ---------- COMPONENTS ---------- */

@Composable
fun StatCard(title: String, value: String, background: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(background, RoundedCornerShape(20.dp))
            .padding(22.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = Color(0xFF4A5568),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 40.sp
            )
        }
    }
}

@Composable
fun ActionCard(
    icon: ImageVector,
    label: String,
    date: String?,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(150.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(background, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (date != null) {
                // Calendar icon with date
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = Color(0xFF2D3748)
                        )
                        Text(
                            text = date,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 12.sp
                        )
                    }
                }
            } else {
                // Regular icon
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(42.dp),
                    tint = Color(0xFF2D3748)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748),
                maxLines = 2,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AppointmentItem(apt: Appointment) {

    val bgColor =
        if (apt.status == "critical") Color(0xFFFFE8E8)
        else Color(0xFFF3F3F3)

    val borderColor =
        if (apt.status == "critical") Color(0xFFE53935)
        else Color(0xFFAEE4C1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(bgColor, RoundedCornerShape(18.dp))
            .border(2.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(20.dp)
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
                    text = apt.doctorName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3748),
                    maxLines = 1,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${apt.date} at ${apt.time}",
                    fontSize = 14.sp,
                    color = Color(0xFF718096),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .background(
                        when (apt.status) {
                            "accepted" -> Color(0xFFAEE4C1)
                            "pending" -> Color(0xFFFFEAD6)
                            "critical" -> Color(0xFFE53935)
                            else -> Color.Transparent
                        },
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = apt.status.replaceFirstChar { it.uppercase() },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (apt.status == "critical") Color.White else Color(0xFF2D3748),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
