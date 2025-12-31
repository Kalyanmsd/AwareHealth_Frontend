package com.example.awarehealth.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.viewmodel.AuthViewModel
import java.util.regex.Pattern

@Composable
fun RegisterScreen(
    repository: AppRepository,
    userType: String,            // "patient" or "doctor"
    onRegisterSuccess: (com.example.awarehealth.data.UserData) -> Unit,
    onBackToLogin: () -> Unit
) {
    // Initialize ViewModel
    val viewModel: AuthViewModel = remember { AuthViewModel(repository) }
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    
    // Observe UI state changes
    LaunchedEffect(uiState) {
        isLoading = uiState.isLoading
        uiState.error?.let {
            error = it
            viewModel.clearError()
        }
        if (uiState.isSuccess && uiState.user != null) {
            onRegisterSuccess(uiState.user!!)
        }
    }

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

            // Icon Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = Color(0xFFAEE4C1).copy(alpha = 0.3f)
                    )
                    .background(
                        color = Color(0xFFE9FFF4),
                        shape = CircleShape
                    )
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF34A853)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (userType == "patient")
                    "Register as a Patient"
                else
                    "Register as a Doctor",
                fontSize = 16.sp,
                color = Color(0xFF718096),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Medium
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

            // Error Message with better styling
            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFE53E3E),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            val isFormValid = name.isNotBlank() && email.isNotBlank() && 
                             phone.isNotBlank() && password.isNotBlank() && 
                             confirmPassword.isNotBlank() && !isLoading
            
            val buttonElevation by animateFloatAsState(
                targetValue = if (isFormValid) 8f else 0f,
                animationSpec = tween(300),
                label = "button_elevation"
            )
            
            val buttonAlpha by animateFloatAsState(
                targetValue = if (isFormValid) 1f else 0.6f,
                animationSpec = tween(300),
                label = "button_alpha"
            )
            
            Button(
                onClick = {
                    // Validation
                    if (password != confirmPassword) {
                        error = "Passwords do not match"
                    } else if (password.length < 6) {
                        error = "Password must be at least 6 characters"
                    } else if (!isValidEmail(email)) {
                        error = "Please enter a valid email address"
                    } else {
                        error = ""
                        viewModel.register(
                            name = name,
                            email = email,
                            password = password,
                            phone = phone,
                            userType = userType,
                            onSuccess = { user ->
                                // Navigation handled in LaunchedEffect
                            },
                            onError = { errorMsg ->
                                error = errorMsg
                            }
                        )
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .alpha(buttonAlpha)
                    .shadow(
                        elevation = buttonElevation.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color(0xFFAEE4C1).copy(alpha = 0.4f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    contentColor = Color(0xFF2D3748),
                    disabledContainerColor = Color(0xFFE2E8F0),
                    disabledContentColor = Color(0xFFA0AEC0)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 4.dp,
                    disabledElevation = 0.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF2D3748),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Back to login
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Already have an account? ",
                    color = Color(0xFF718096),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Sign In",
                    color = Color(0xFF34A853),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onBackToLogin() }
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/* ---------- Helper Functions ---------- */

private fun isValidEmail(email: String): Boolean {
    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    )
    return emailPattern.matcher(email).matches()
}

/* ---------- Helper Composables ---------- */

@Composable
fun Label(text: String) {
    Text(
        text = text,
        color = Color(0xFF2D3748),
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp
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

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (showPassword) "Hide Password" else "Show Password",
            color = Color(0xFF4A5568),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onToggle() }
                .padding(vertical = 6.dp, horizontal = 6.dp)
        )
    }
}
