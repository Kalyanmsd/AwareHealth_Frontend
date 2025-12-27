package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
fun DoctorHome(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header
        Header(
            showMenu = true,
            showNotifications = true,
            onMenuClick = { navController.navigate(Screen.DoctorMenu.route) },
            onNotificationClick = { /* Handle notification click */ }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // Welcome Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9FFF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Welcome, Dr. Sarah Johnson! 👨‍⚕️",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Cardiology Specialist",
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Statistics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Today Patients Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEAD6)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = Color(0xFFE67E22),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "8",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2D3748),
                            lineHeight = 40.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Today Patients",
                            fontSize = 13.sp,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // This Week Appointments Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE9FFF4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF27AE60),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "32",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2D3748),
                            lineHeight = 40.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "This Week Appointments",
                            fontSize = 13.sp,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Quick Actions Section
            Text(
                text = "Quick Actions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Quick Actions Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Pending Card
                    QuickActionCard(
                        icon = Icons.Default.AccessTime,
                        label = "Pending",
                        count = 3,
                        backgroundColor = Color(0xFFFFEAD6),
                        iconTint = Color(0xFFE67E22),
                        onClick = { navController.navigate("pending_requests") }
                    )

                    // Critical Card
                    QuickActionCard(
                        icon = Icons.Default.Warning,
                        label = "Critical",
                        count = 2,
                        backgroundColor = Color(0xFFFFE8E8),
                        iconTint = Color(0xFFE53935),
                        onClick = { navController.navigate("critical_appointments") }
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Accepted Card
                    QuickActionCard(
                        icon = Icons.Default.CheckCircle,
                        label = "Accepted",
                        count = 2,
                        backgroundColor = Color(0xFFE9FFF4),
                        iconTint = Color(0xFF27AE60),
                        onClick = { navController.navigate("accepted_requests") }
                    )

                    // Statistics Card
                    QuickActionCard(
                        icon = Icons.Default.BarChart,
                        label = "Statistics",
                        backgroundColor = Color(0xFFE9FFF4),
                        iconTint = Color(0xFF27AE60),
                        onClick = { navController.navigate("doctor_statistics") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Recent Requests Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Requests",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "View All",
                    fontSize = 14.sp,
                    color = Color(0xFF27AE60),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.navigate("pending_requests") }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Recent Request Card
            RecentRequestCard(
                patientName = "John Smith",
                date = "2024-12-15",
                time = "10:00 AM",
                reason = "Fever and headache",
                status = "pending"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    count: Int? = null,
    backgroundColor: Color,
    iconTint: Color = Color(0xFF4A5568),
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(26.dp)
                )
                if (count != null) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(Color(0xFF2D3748), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = count.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )
        }
    }
}

@Composable
fun RecentRequestCard(
    patientName: String,
    date: String,
    time: String,
    reason: String,
    status: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = patientName,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
                Box(
                    modifier = Modifier
                        .background(
                            when (status) {
                                "pending" -> Color(0xFFFFEAD6)
                                "accepted" -> Color(0xFFE9FFF4)
                                "critical" -> Color(0xFFFFE8E8)
                                else -> Color(0xFFF3F3F3)
                            },
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = status.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (status) {
                            "pending" -> Color(0xFFE67E22)
                            "accepted" -> Color(0xFF27AE60)
                            "critical" -> Color(0xFFE53935)
                            else -> Color(0xFF4A5568)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF718096),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "$date at $time",
                    fontSize = 14.sp,
                    color = Color(0xFF718096),
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF4A5568),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = reason,
                    fontSize = 14.sp,
                    color = Color(0xFF4A5568),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
