package com.example.awarehealth.ui.screens

/* ---------- IMPORTS ---------- */

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- SCREEN ---------- */

@Composable
fun LoginScreen(
    userType: String,
    onLoginSuccess: () -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onGoogleLogin: () -> Unit,
    onBack: () -> Unit = {}
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Handle back button
    BackHandler {
        onBack()
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

        /* ---------- FORGOT PASSWORD ---------- */
        Text(
            text = "Forgot Password?",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPassword() }
                .padding(vertical = 4.dp, horizontal = 4.dp),
            color = Color(0xFF4A5568),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- SIGN IN BUTTON ---------- */
        val isLoginEnabled = email.isNotBlank() && password.isNotBlank()
        
        Button(
            onClick = {
                if (isLoginEnabled) {
                    onLoginSuccess()
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
            Text(
                "Sign In", 
                fontSize = 17.sp, 
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        /* ---------- OR ---------- */
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

        /* ---------- GOOGLE SIGN IN ---------- */
        OutlinedButton(
            onClick = onGoogleLogin,
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
            // Google logo
            GoogleLogo(size = 22.dp)
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                "Sign in with Google", 
                fontSize = 16.sp, 
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            )
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

@Composable
fun GoogleLogo(size: androidx.compose.ui.unit.Dp = 20.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val canvasSize = size.toPx()
        val center = Offset(canvasSize / 2, canvasSize / 2)
        val radius = canvasSize * 0.4f
        val strokeWidth = canvasSize * 0.15f

        // Google "G" logo - simplified version with colored arcs
        // Blue arc (top right)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -45f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        
        // Red arc (top left)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 135f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        
        // Yellow arc (bottom left)
        drawArc(
            color = Color(0xFFFBBC04),
            startAngle = 225f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        
        // Green arc (bottom right)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 315f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        
        // Horizontal line for "G" (extending from center to right)
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(center.x, center.y),
            end = Offset(center.x + radius * 0.6f, center.y),
            strokeWidth = strokeWidth
        )
    }
}
