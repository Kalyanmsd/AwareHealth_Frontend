package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.UserData
import com.example.awarehealth.viewmodel.ProfileViewModel

@Composable
fun EditPatientProfileScreen(
    repository: AppRepository,
    userData: UserData,
    onProfileUpdated: (UserData) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: ProfileViewModel = remember { ProfileViewModel(repository) }
    val uiState by viewModel.uiState.collectAsState()
    
    var name by remember { mutableStateOf(userData.name) }
    var email by remember { mutableStateOf(userData.email) }
    var phone by remember { mutableStateOf(userData.phone ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Observe UI state changes
    LaunchedEffect(uiState) {
        isLoading = uiState.isLoading
        uiState.error?.let {
            errorMessage = it
            viewModel.clearError()
        }
        if (uiState.isSuccess && uiState.updatedUser != null) {
            onProfileUpdated(uiState.updatedUser!!)
            onBack() // Navigate back after successful update
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF2D3748)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Title
            Text(
                text = "Update Your Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                letterSpacing = (-0.5).sp
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // Subtitle
            Text(
                text = "Update your personal information",
                fontSize = 15.sp,
                color = Color(0xFF718096),
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Error Message
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFE53E3E),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
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
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Save Changes Button
            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()) {
                        errorMessage = null
                        viewModel.updateProfile(
                            userId = userData.id,
                            name = name.trim(),
                            email = email.trim(),
                            phone = phone.trim(),
                            onSuccess = { updatedUser ->
                                onProfileUpdated(updatedUser)
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    } else {
                        errorMessage = "Please fill in all fields"
                    }
                },
                enabled = !isLoading && name.isNotBlank() && email.isNotBlank() && phone.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    contentColor = Color(0xFF2D3748),
                    disabledContainerColor = Color(0xFFE2E8F0),
                    disabledContentColor = Color(0xFFA0AEC0)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF2D3748),
                        strokeWidth = 2.dp
                    )
                } else {
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
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/* -------- PROFILE FIELD COMPONENT -------- */
@Composable
private fun ProfileField(
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

