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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.viewmodel.AuthViewModel
import com.example.awarehealth.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onOTPVerified: (String, String) -> Unit, // Pass email and OTP to reset password screen
    repository: AppRepository
) {

    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))
    val uiState by authViewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otpVerified by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    // Handle back button
    BackHandler {
        if (otpSent) {
            otpSent = false
            otp = ""
        } else {
            onBackToLogin()
        }
    }

    // Observe errors
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            errorMessage = it
            authViewModel.clearError()
        }
    }

    // Auto navigate after OTP verified
    LaunchedEffect(otpVerified) {
        if (otpVerified) {
            delay(1000)
            onOTPVerified(email, otp)
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
                    .clickable { 
                        if (otpSent) {
                            otpSent = false
                            otp = ""
                        } else {
                            onBackToLogin()
                        }
                    }
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

            if (!otpSent) {
                // Step 1: Enter Email and Send OTP
                
                // Icon
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        )
                        .background(Color(0xFFFFEAD6), CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFFFF9800)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Forgot Password?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter your email to receive OTP",
                    fontSize = 15.sp,
                    color = Color(0xFF718096),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Email Address",
                    color = Color(0xFF2D3748),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
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

                Spacer(modifier = Modifier.height(32.dp))

                val isEmailValid = email.isNotBlank() && email.contains("@") && !uiState.isLoading
                
                // Error message display
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
                
                Button(
                    onClick = {
                        errorMessage = null
                        authViewModel.forgotPassword(
                            email = email,
                            onSuccess = {
                                otpSent = true
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    },
                    enabled = isEmailValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = if (isEmailValid) 4.dp else 0.dp,
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
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color(0xFF2D3748),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Send OTP",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Back to Login",
                    color = Color(0xFF4A5568),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onBackToLogin() }
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                )

            } else if (!otpVerified) {
                // Step 2: Enter OTP and Verify
                
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
                        imageVector = Icons.Default.Pin,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF34A853)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Enter OTP",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We sent a 6-digit OTP to\n$email",
                    fontSize = 15.sp,
                    color = Color(0xFF718096),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "One-Time Password (OTP)",
                    color = Color(0xFF2D3748),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = otp,
                    onValueChange = { if (it.length <= 6) otp = it },
                    placeholder = {
                        Text(
                            "Enter 6-digit OTP",
                            color = Color(0xFFA0AEC0),
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Pin,
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

                Spacer(modifier = Modifier.height(32.dp))

                val isOTPValid = otp.length == 6 && !uiState.isLoading
                
                // Error message display
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
                
                Button(
                    onClick = {
                        errorMessage = null
                        authViewModel.verifyOTP(
                            email = email,
                            otp = otp,
                            onSuccess = {
                                otpVerified = true
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    },
                    enabled = isOTPValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = if (isOTPValid) 4.dp else 0.dp,
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
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color(0xFF2D3748),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Verify OTP",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Didn't receive OTP?",
                    color = Color(0xFF4A5568),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Resend OTP",
                    color = Color(0xFFAEE4C1),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            otp = ""
                            errorMessage = null
                            authViewModel.forgotPassword(
                                email = email,
                                onSuccess = {
                                    // OTP resent
                                },
                                onError = { error ->
                                    errorMessage = error
                                }
                            )
                        }
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                )

            } else {
                // Step 3: OTP Verified Successfully
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF34A853)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "OTP Verified!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Redirecting to reset password...",
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
