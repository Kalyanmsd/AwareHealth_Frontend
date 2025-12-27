package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awarehealth.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun DoctorMenu(
    navController: NavController,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()

    /* -------- OVERLAY -------- */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onClose() }
    )

    /* -------- SIDE DRAWER -------- */
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color(0xFFFFFDF7))
    ) {

        /* -------- HEADER -------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9FFF4))
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "AwareHealth",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color(0xFF2D3748)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFAEE4C1), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = Color(0xFF2D3748),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Dr. John Smith",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )
                    Text(
                        text = "Cardiologist",
                        fontSize = 12.sp,
                        color = Color(0xFF4A5568)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        /* -------- MENU ITEMS -------- */
        MenuItem(
            icon = Icons.Default.Home,
            label = "Home"
        ) {
            onClose()
            navController.navigate(Screen.DoctorHome.route) {
                popUpTo(Screen.DoctorHome.route) { inclusive = true }
            }
        }

        MenuItem(
            icon = Icons.Default.CalendarMonth,
            label = "Appointments"
        ) {
            onClose()
            // Small delay to ensure menu closes before navigation
            scope.launch {
                delay(100)
                navController.navigate(Screen.PendingRequests.route)
            }
        }

        MenuItem(
            icon = Icons.Default.TrendingUp,
            label = "Statistics"
        ) {
            onClose()
            navController.navigate(Screen.DoctorStatistics.route)
        }

        MenuItem(
            icon = Icons.Default.Notifications,
            label = "Notifications"
        ) {
            onClose()
            scope.launch {
                delay(100)
                navController.navigate(Screen.DoctorNotifications.route)
            }
        }

        MenuItem(
            icon = Icons.Default.Person,
            label = "Profile"
        ) {
            onClose()
            navController.navigate(Screen.DoctorProfile.route)
        }

        MenuItem(
            icon = Icons.Default.Logout,
            label = "Logout"
        ) {
            onClose()
            // Navigate to login screen
            navController.navigate(Screen.Login.createRoute("doctor")) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}

/* -------- MENU ITEM -------- */
@Composable
fun MenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF4A5568)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF2D3748)
        )
    }
}
