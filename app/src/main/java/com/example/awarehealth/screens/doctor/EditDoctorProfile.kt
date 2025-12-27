package com.example.awarehealth.ui.doctor

import androidx.compose.foundation.background
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
import com.example.awarehealth.ui.components.Header
import com.example.awarehealth.navigation.Screen

@Composable
fun EditDoctorProfile(navController: NavController) {

    var name by remember { mutableStateOf("Dr. John Smith") }
    var email by remember { mutableStateOf("johnsmith@hospital.com") }
    var phone by remember { mutableStateOf("+91 9876543210") }
    var specialty by remember { mutableStateOf("Cardiology") }
    var experience by remember { mutableStateOf("8 Years") }

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
            // Title
            Text(
                text = "Edit Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Update your professional information",
                fontSize = 15.sp,
                color = Color(0xFF718096),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Field
            ProfileField(
                label = "Full Name",
                value = name,
                onValueChange = { name = it },
                icon = Icons.Default.Person,
                placeholder = "Enter your full name"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            ProfileField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                icon = Icons.Default.Email,
                placeholder = "Enter your email"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Phone Field
            ProfileField(
                label = "Phone",
                value = phone,
                onValueChange = { phone = it },
                icon = Icons.Default.Phone,
                placeholder = "Enter your phone number"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Specialty Field
            ProfileField(
                label = "Specialty",
                value = specialty,
                onValueChange = { specialty = it },
                icon = Icons.Default.MedicalServices,
                placeholder = "Enter your specialty"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Experience Field
            ProfileField(
                label = "Experience",
                value = experience,
                onValueChange = { experience = it },
                icon = Icons.Default.AccessTime,
                placeholder = "Enter years of experience"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Save Changes Button
            Button(
                onClick = {
                    // Save profile changes
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    contentColor = Color(0xFF2D3748)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Save Changes",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/* -------- PROFILE FIELD COMPONENT -------- */
@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color(0xFF2D3748),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF718096)) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF4A5568),
                    modifier = Modifier.size(22.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAEE4C1),
                unfocusedBorderColor = Color(0xFFE2E8F0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                color = Color(0xFF2D3748),
                fontWeight = FontWeight.Medium
            ),
            singleLine = true
        )
    }
}
