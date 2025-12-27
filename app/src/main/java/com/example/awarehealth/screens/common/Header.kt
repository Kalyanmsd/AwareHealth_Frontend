package com.example.awarehealth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.awarehealth.ui.components.Logo

@Composable
fun Header(
    showBack: Boolean = false,
    showNotifications: Boolean = false,
    showMenu: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE9FFF4))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        /* -------- LEFT ICON -------- */
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            if (showBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF2D3748),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() }
                )
            } else if (showMenu) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF2D3748),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onMenuClick() }
                )
            }
        }

        /* -------- LOGO AND TEXT (CENTERED) -------- */
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Logo(size = 32.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "AwareHealth",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    letterSpacing = 0.3.sp
                )
            }
        }

        /* -------- RIGHT ICON -------- */
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            if (showNotifications) {
                Box {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF2D3748),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onNotificationClick() }
                    )

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFE53935), CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}
