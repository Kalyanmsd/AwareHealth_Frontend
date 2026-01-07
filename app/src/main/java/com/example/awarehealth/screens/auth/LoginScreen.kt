package com.example.awarehealth.ui.screens

/* ---------- IMPORTS ---------- */

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip   // ✅ FIXED
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.SharedPreferencesHelper
import com.example.awarehealth.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

/* ---------- SCREEN ---------- */

@Composable
fun LoginScreen(
    repository: AppRepository,
    userType: String,
    onLoginSuccess: (com.example.awarehealth.data.UserData) -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onBack: () -> Unit = {}
) {
    // Get context for SharedPreferences
    val context = LocalContext.current
    val prefsHelper = remember { SharedPreferencesHelper(context) }
    
    // Initialize ViewModel
    val viewModel: AuthViewModel = remember { AuthViewModel(repository) }
    val uiState by viewModel.uiState.collectAsState()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var autoLoginAttempted by remember { mutableStateOf(false) }

    // Handle back button
    BackHandler {
        onBack()
    }
    
    // Auto-fill credentials and auto-login on first load
    LaunchedEffect(userType) {
        if (!autoLoginAttempted) {
            autoLoginAttempted = true
            
            // Check if credentials are saved for this user type
            val savedEmail = prefsHelper.getSavedEmail(userType)
            val savedPassword = prefsHelper.getSavedPassword(userType)
            
            if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                // Auto-fill credentials
                email = savedEmail
                password = savedPassword
                rememberMe = prefsHelper.isRememberMeEnabled()
                
                // Auto-login after a short delay
                delay(500)
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(
                        email = email,
                        password = password,
                        userType = userType,
                        onSuccess = { user ->
                            // Save doctorId if user is a doctor
                            if (userType == "doctor" && user.doctorId != null) {
                                prefsHelper.saveDoctorId(user.doctorId)
                            }
                            
                            // Save credentials if Remember Me is enabled
                            if (rememberMe) {
                                prefsHelper.saveCredentials(email, password, userType, true)
                                prefsHelper.saveUserSession(user)
                            }
                            // Navigation handled in LaunchedEffect
                        },
                        onError = { error ->
                            errorMessage = error
                        }
                    )
                }
            }
        }
    }
    
    // Observe UI state changes
    LaunchedEffect(uiState) {
        isLoading = uiState.isLoading
        uiState.error?.let {
            errorMessage = it
            viewModel.clearError()
        }
        if (uiState.isSuccess && uiState.user != null) {
            val loggedInUser = uiState.user!!
            
            // Save doctorId if user is a doctor
            if (userType == "doctor" && loggedInUser.doctorId != null) {
                prefsHelper.saveDoctorId(loggedInUser.doctorId)
            }
            
            // Save credentials if Remember Me is checked
            if (rememberMe) {
                prefsHelper.saveCredentials(email, password, userType, true)
                prefsHelper.saveUserSession(loggedInUser)
            } else {
                // Clear saved credentials if Remember Me is unchecked
                prefsHelper.clearCredentials(userType)
            }
            onLoginSuccess(loggedInUser)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        /* ---------- TOP BAR ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable { onBack() }
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

        Spacer(modifier = Modifier.height(40.dp))

        /* ---------- TITLE ---------- */
        Text(
            text = if (userType == "doctor") "Doctor Login" else "Patient Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in to access your account",
            fontSize = 15.sp,
            color = Color(0xFF718096),
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(40.dp))

        /* ---------- EMAIL ---------- */
        Text(
            text = "Email Address",
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF2D3748),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { 
                Text(
                    "Enter your email",
                    color = Color(0xFFA0AEC0),
                    fontSize = 15.sp
                ) 
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Email, 
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

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- PASSWORD ---------- */
        Text(
            text = "Password",
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF2D3748),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { 
                Text(
                    "Enter your password",
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
                    imageVector = if (passwordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = Color(0xFF718096),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            passwordVisible = !passwordVisible
                        }
                )
            },
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
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

        Spacer(modifier = Modifier.height(14.dp))

        /* ---------- REMEMBER ME & FORGOT PASSWORD ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Remember Me checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    rememberMe = !rememberMe
                }
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFAEE4C1),
                        uncheckedColor = Color(0xFFE2E8F0),
                        checkmarkColor = Color(0xFF2D3748)
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Remember Me",
                    fontSize = 14.sp,
                    color = Color(0xFF4A5568),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        rememberMe = !rememberMe
                    }
                )
            }
            
            // Forgot Password
            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .clickable { onForgotPassword() }
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                color = Color(0xFF4A5568),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        
        /* ---------- ERROR MESSAGE ---------- */
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (error.contains("\n")) error.split("\n").firstOrNull() ?: error else error,
                        color = Color(0xFFE53E3E),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (error.contains("\n")) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error.substringAfter("\n").trim(),
                            color = Color(0xFFC62828),
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        /* ---------- SIGN IN BUTTON ---------- */
        val isLoginEnabled = email.isNotBlank() && password.isNotBlank() && !isLoading
        
        Button(
            onClick = {
                if (isLoginEnabled) {
                    errorMessage = null
                    viewModel.login(
                        email = email,
                        password = password,
                        userType = userType,
                        onSuccess = { user ->
                            // Save credentials if Remember Me is checked
                            if (rememberMe) {
                                prefsHelper.saveCredentials(email, password, userType, true)
                                prefsHelper.saveUserSession(user)
                            } else {
                                // Clear saved credentials if Remember Me is unchecked
                                prefsHelper.clearCredentials(userType)
                            }
                            // Navigation handled in LaunchedEffect
                        },
                        onError = { error ->
                            errorMessage = error
                        }
                    )
                }
            },
            enabled = isLoginEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = if (isLoginEnabled) 4.dp else 0.dp,
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
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFF2D3748),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "Sign In", 
                    fontSize = 17.sp, 
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        /* ---------- REGISTER ---------- */
        Row(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                "Don't have an account? ",
                fontSize = 14.sp,
                color = Color(0xFF718096)
            )
            Text(
                text = "Register Now",
                color = Color(0xFF2D7FF9),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onRegister() }
                    .padding(horizontal = 2.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

