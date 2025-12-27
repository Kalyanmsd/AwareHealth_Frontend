package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.awarehealth.ui.components.Logo

@Composable
fun UserTypeSelection(
    onUserSelected: (String) -> Unit   // "patient" or "doctor"
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {

        // Header with Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Logo(size = 24.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AwareHealth",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome Back!",
                fontSize = 28.sp,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select your account type to continue",
                fontSize = 16.sp,
                color = Color(0xFF718096)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // PATIENT
            UserTypeCard(
                backgroundColor = Color(0xFFFFEAD6),
                icon = Icons.Filled.Person,
                title = "I'm a Patient",
                description = "Book appointments & consult with doctors"
            ) {
                onUserSelected("patient")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DOCTOR
            UserTypeCard(
                backgroundColor = Color(0xFFE9FFF4),
                icon = Icons.Filled.Favorite,
                title = "I'm a Doctor",
                description = "Manage appointments & patient care"
            ) {
                onUserSelected("doctor")
            }
        }
    }
}

@Composable
fun UserTypeCard(
    backgroundColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .background(backgroundColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFFFFDF7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF2D3748),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 22.sp,
            color = Color(0xFF2D3748)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            color = Color(0xFF4A5568)
        )
    }
}
