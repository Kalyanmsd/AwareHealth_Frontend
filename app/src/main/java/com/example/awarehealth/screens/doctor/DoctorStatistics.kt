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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.roundToInt

@Composable
fun DoctorStatistics(navController: NavController) {

    /* -------- SAMPLE DATA -------- */
    val appointments = listOf(
        AppointmentStatus("1", "accepted"),
        AppointmentStatus("2", "pending"),
        AppointmentStatus("3", "critical"),
        AppointmentStatus("4", "accepted"),
        AppointmentStatus("5", "rejected")
    )

    var selectedPeriod by remember { mutableStateOf("week") }

    val pending = appointments.count { it.status == "pending" }
    val accepted = appointments.count { it.status == "accepted" }
    val rejected = appointments.count { it.status == "rejected" }
    val critical = appointments.count { it.status == "critical" }
    val total = appointments.size

    val weeklyData = listOf(
        DayData("Mon", 8),
        DayData("Tue", 12),
        DayData("Wed", 10),
        DayData("Thu", 15),
        DayData("Fri", 14),
        DayData("Sat", 6),
        DayData("Sun", 4)
    )

    val maxPatients = weeklyData.maxOf { it.patients }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {

            /* -------- BACK -------- */
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

            Text(
                text = "Statistics & Analytics",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            /* -------- PERIOD SELECT -------- */
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("week", "month", "year").forEach { period ->
                    Text(
                        text = "This ${period.substring(0,1).uppercase()}${period.substring(1)}",
                        modifier = Modifier
                            .background(
                                if (selectedPeriod == period)
                                    Color(0xFFAEE4C1)
                                else
                                    Color(0xFFF3F3F3),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedPeriod = period }
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        color = Color(0xFF2D3748),
                        fontSize = 14.sp,
                        fontWeight = if (selectedPeriod == period) FontWeight.SemiBold else FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* -------- STATS -------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = "Appointments",
                    value = total.toString(),
                    icon = Icons.Default.CalendarToday,
                    bg = Color(0xFFFFEAD6),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Completed",
                    value = accepted.toString(),
                    icon = Icons.Default.CheckCircle,
                    bg = Color(0xFFE9FFF4),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* -------- WEEKLY FLOW -------- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3F3F3)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Weekly Patient Flow",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weeklyData.forEach {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(
                                            ((it.patients.toFloat() / maxPatients) * 140).dp
                                        )
                                        .background(
                                            Color(0xFFAEE4C1),
                                            RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = it.day,
                                    fontSize = 13.sp,
                                    color = Color(0xFF4A5568),
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = it.patients.toString(),
                                    fontSize = 14.sp,
                                    color = Color(0xFF2D3748),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* -------- BREAKDOWN -------- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Appointment Status",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BreakdownItem("Accepted", accepted, total, Color(0xFFAEE4C1))
                    Spacer(modifier = Modifier.height(12.dp))
                    BreakdownItem("Pending", pending, total, Color(0xFFFFEAD6))
                    Spacer(modifier = Modifier.height(12.dp))
                    BreakdownItem("Critical", critical, total, Color(0xFFE53935))
                    Spacer(modifier = Modifier.height(12.dp))
                    BreakdownItem("Rejected", rejected, total, Color(0xFFF3F3F3))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* -------- PERFORMANCE -------- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEAD6)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Performance Metrics",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // First row with proper spacing
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Metric(
                            value = "4.8/5.0",
                            label = "Rating",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Metric(
                            value = "92%",
                            label = "Success",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Second row with proper spacing
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Metric(
                            value = "28 min",
                            label = "Response",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Metric(
                            value = "156",
                            label = "Patients",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/* -------- COMPONENTS -------- */

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = bg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF4A5568),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFF4A5568),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BreakdownItem(label: String, count: Int, total: Int, color: Color) {
    val percent = if (total == 0) 0 else ((count.toFloat() / total) * 100).roundToInt()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )
            Text(
                text = "$count ($percent%)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(Color(0xFFE2E8F0), RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .height(12.dp)
                    .background(color, RoundedCornerShape(50))
            )
        }
    }
}

@Composable
fun Metric(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF4A5568),
            fontWeight = FontWeight.Medium
        )
    }
}

/* -------- DATA -------- */
data class AppointmentStatus(val id: String, val status: String)
data class DayData(val day: String, val patients: Int)
