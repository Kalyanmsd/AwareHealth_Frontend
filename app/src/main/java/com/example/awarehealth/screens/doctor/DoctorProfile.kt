package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
fun DoctorProfile(navController: NavController) {

    /* -------- SAMPLE USER DATA -------- */
    val user = DoctorUser(
        name = "Dr. John Smith",
        specialty = "Cardiology",
        email = "johnsmith@hospital.com",
        phone = "+91 9876543210",
        experience = "8 Years"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header with back arrow
        Header(
            showBack = true,
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            /* -------- PROFILE CARD -------- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE9FFF4)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Icon
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                spotColor = Color(0x1A000000)
                            )
                            .background(Color(0xFFAEE4C1), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = Color(0xFF2D3748),
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name
                    Text(
                        text = user.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Specialty
                    Text(
                        text = "${user.specialty} Specialist",
                        fontSize = 16.sp,
                        color = Color(0xFF4A5568),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Edit Profile Button
                    Button(
                        onClick = { navController.navigate(Screen.EditDoctorProfile.route) },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFAEE4C1),
                            contentColor = Color(0xFF2D3748)
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Edit Profile",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* -------- DETAILS CARDS -------- */
            InfoCard(
                icon = Icons.Default.Email,
                label = "Email",
                value = user.email
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                icon = Icons.Default.Phone,
                label = "Phone",
                value = user.phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                icon = Icons.Default.AccessTime,
                label = "Experience",
                value = user.experience
            )

            Spacer(modifier = Modifier.height(32.dp))

            /* -------- THIS WEEK SECTION -------- */
            Text(
                text = "This Week",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                letterSpacing = (-0.3).sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Appointments Card
                StatCard(
                    value = "32",
                    label = "Appointments",
                    bg = Color(0xFFFFEAD6),
                    icon = Icons.Default.EmojiEvents,
                    modifier = Modifier.weight(1f)
                )

                // Completed Card
                StatCard(
                    value = "28",
                    label = "Completed",
                    bg = Color(0xFFE9FFF4),
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/* -------- INFO CARD -------- */
@Composable
fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF4A5568),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = Color(0xFF718096),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color(0xFF2D3748),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/* -------- STAT CARD -------- */
@Composable
fun StatCard(
    value: String,
    label: String,
    bg: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF4A5568),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/* -------- DATA CLASS -------- */
data class DoctorUser(
    val name: String,
    val specialty: String,
    val email: String,
    val phone: String,
    val experience: String
)
