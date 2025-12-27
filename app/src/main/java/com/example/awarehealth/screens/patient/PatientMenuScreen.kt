package com.example.awarehealth.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PatientMenu(
    userName: String,
    onClose: () -> Unit,
    onHome: () -> Unit,
    onChatbot: () -> Unit,
    onAppointments: () -> Unit,
    onHealthInfo: () -> Unit,
    onNotifications: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {

    /* ---------- Overlay ---------- */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onClose() }
    )

    /* ---------- Drawer ---------- */
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .shadow(
                elevation = 8.dp,
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            /* ---------- Header ---------- */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE9FFF4))
                    .padding(24.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AwareHealth",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close menu",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onClose() },
                        tint = Color(0xFF2D3748)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = CircleShape,
                                spotColor = Color.Black.copy(alpha = 0.1f)
                            )
                            .background(Color(0xFFAEE4C1), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF2D3748)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = userName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3748),
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Patient",
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            /* ---------- Menu Items ---------- */
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {

                MenuItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    onClick = onHome
                )
                MenuItem(
                    icon = Icons.Default.SmartToy,
                    label = "AI Chatbot",
                    onClick = onChatbot
                )
                MenuItem(
                    icon = Icons.Default.CalendarToday,
                    label = "My Appointments",
                    badgeCount = 17,
                    onClick = onAppointments
                )
                MenuItem(
                    icon = Icons.Default.Info,
                    label = "Health Info",
                    onClick = onHealthInfo
                )
                MenuItem(
                    icon = Icons.Default.Notifications,
                    label = "Notifications",
                    onClick = onNotifications
                )
                MenuItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    onClick = onProfile
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                MenuItem(
                    icon = Icons.Default.ExitToApp,
                    label = "Logout",
                    onClick = onLogout,
                    isDestructive = true
                )
            }
        }
    }
}

/* ---------- Menu Item ---------- */

@Composable
fun MenuItem(
    icon: ImageVector,
    label: String,
    badgeCount: Int? = null,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (isDestructive) Color(0xFFE53E3E) else Color(0xFF2D3748)
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDestructive) Color(0xFFE53E3E) else Color(0xFF2D3748),
            modifier = Modifier.weight(1f),
            lineHeight = 22.sp
        )
        
        if (badgeCount != null) {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFE53E3E),
                        CircleShape
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeCount.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
