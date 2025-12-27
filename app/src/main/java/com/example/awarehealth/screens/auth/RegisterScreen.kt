package com.example.awarehealth.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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

@Composable
fun RegisterScreen(
    userType: String,            // "patient" or "doctor"
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    onGoogleSignup: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Handle back button
    BackHandler {
        onBackToLogin()
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

            // Title
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (userType == "patient")
                    "Register as a Patient"
                else
                    "Register as a Doctor",
                fontSize = 15.sp,
                color = Color(0xFF718096),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Full Name
            Label("Full Name")
            InputField(
                value = name,
                placeholder = "Enter your full name",
                icon = Icons.Default.Person,
                onValueChange = { name = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email
            Label("Email Address")
            InputField(
                value = email,
                placeholder = "Enter your email",
                icon = Icons.Default.Email,
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Phone
            Label("Phone Number")
            InputField(
                value = phone,
                placeholder = "Enter your phone number",
                icon = Icons.Default.Phone,
                onValueChange = { phone = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password
            Label("Password")
            PasswordField(
                value = password,
                placeholder = "Create a password",
                showPassword = showPassword,
                onValueChange = { password = it },
                onToggle = { showPassword = !showPassword }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password
            Label("Confirm Password")
            PasswordField(
                value = confirmPassword,
                placeholder = "Confirm your password",
                showPassword = showConfirmPassword,
                onValueChange = { confirmPassword = it },
                onToggle = { showConfirmPassword = !showConfirmPassword }
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

            // Register Button
            val isFormValid = name.isNotBlank() && email.isNotBlank() && 
                             phone.isNotBlank() && password.isNotBlank() && 
                             confirmPassword.isNotBlank()
            
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        error = "Passwords do not match"
                    } else if (password.length < 6) {
                        error = "Password must be at least 6 characters"
                    } else {
                        error = ""
                        onRegisterSuccess()
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
                    text = "Create Account",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp
                )
                Text(
                    "  OR  ",
                    color = Color(0xFF718096),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Signup
            OutlinedButton(
                onClick = onGoogleSignup,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2D3748),
                    containerColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.5.dp,
                    color = Color(0xFFE2E8F0)
                )
            ) {
                Text(
                    "Sign up with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Back to login
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Already have an account? ",
                    color = Color(0xFF718096),
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign In",
                    color = Color(0xFF2D7FF9),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { onBackToLogin() }
                        .padding(horizontal = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/* ---------- Helper Composables ---------- */

@Composable
fun Label(text: String) {
    Text(
        text = text,
        color = Color(0xFF2D3748),
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun InputField(
    value: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                color = Color(0xFFA0AEC0),
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF718096),
                modifier = Modifier.size(20.dp)
            )
        },
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
}

@Composable
fun PasswordField(
    value: String,
    placeholder: String,
    showPassword: Boolean,
    onValueChange: (String) -> Unit,
    onToggle: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
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
                        .clickable { onToggle() }
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
                .clickable { onToggle() }
                .padding(vertical = 4.dp, horizontal = 4.dp)
        )
    }
}
