package com.example.awarehealth.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ResetPasswordScreen(
    onBackToLogin: () -> Unit
) {

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Handle back button
    BackHandler {
        onBackToLogin()
    }

    // Auto redirect after success
    LaunchedEffect(success) {
        if (success) {
            delay(2000)
            onBackToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable { onBackToLogin() }
                    .padding(4.dp),
                color = Color(0xFF2D3748),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "AwareHealth",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            if (!success) {

                // Icon
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        )
                        .background(Color(0xFFE9FFF4), CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFFFF9800)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Reset Password",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Create a new secure password",
                    fontSize = 15.sp,
                    color = Color(0xFF718096),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // New password
                Text(
                    text = "New Password",
                    color = Color(0xFF2D3748),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Enter new password",
                            color = Color(0xFFA0AEC0),
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF718096),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showPassword)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                            tint = Color(0xFF718096),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { showPassword = !showPassword }
                        )
                    },
                    visualTransformation =
                        if (showPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAEE4C1),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color(0xFF2D3748),
                        unfocusedTextColor = Color(0xFF2D3748)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 15.sp,
                        color = Color(0xFF2D3748)
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (showPassword) "Hide Password" else "Show Password",
                    color = Color(0xFF4A5568),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { showPassword = !showPassword }
                        .padding(vertical = 4.dp, horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm password
                Text(
                    text = "Confirm Password",
                    color = Color(0xFF2D3748),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = {
                        Text(
                            "Confirm new password",
                            color = Color(0xFFA0AEC0),
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF718096),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showConfirmPassword)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (showConfirmPassword) "Hide password" else "Show password",
                            tint = Color(0xFF718096),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { showConfirmPassword = !showConfirmPassword }
                        )
                    },
                    visualTransformation =
                        if (showConfirmPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAEE4C1),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color(0xFF2D3748),
                        unfocusedTextColor = Color(0xFF2D3748)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 15.sp,
                        color = Color(0xFF2D3748)
                    )
                )

                if (error.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        error,
                        color = Color(0xFFE53E3E),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                val isFormValid = password.isNotBlank() && confirmPassword.isNotBlank()
                
                Button(
                    onClick = {
                        if (password.length < 6) {
                            error = "Password must be at least 6 characters"
                        } else if (password != confirmPassword) {
                            error = "Passwords do not match"
                        } else {
                            error = ""
                            success = true
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = if (isFormValid) 4.dp else 0.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = Color(0xFFAEE4C1).copy(alpha = 0.3f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFAEE4C1),
                        contentColor = Color(0xFF2D3748),
                        disabledContainerColor = Color(0xFFE2E8F0),
                        disabledContentColor = Color(0xFFA0AEC0)
                    )
                ) {
                    Text(
                        text = "Reset Password",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }

            } else {

                // Success state
                Spacer(modifier = Modifier.height(60.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        )
                        .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF34A853)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Password Reset Successful!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Redirecting to login...",
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


