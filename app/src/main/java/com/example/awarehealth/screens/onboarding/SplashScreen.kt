package com.example.awarehealth.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinish: () -> Unit
) {
    // navigation trigger
    LaunchedEffect(Unit) {
        delay(2000)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F9F5)), // Light pastel green background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with overlapping circles
            SplashLogo()

            Spacer(modifier = Modifier.height(32.dp))

            // App name - large, bold, dark blue-gray
            Text(
                text = "AwareHealth",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748) // Dark blue-gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline - smaller, lighter gray
            Text(
                text = "AI-Powered Healthcare System",
                fontSize = 16.sp,
                color = Color(0xFF718096) // Lighter gray
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Three dots indicator - first one darker, others lighter
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Dot(isActive = true)
                Dot(isActive = false)
                Dot(isActive = false)
            }
        }
    }
}

@Composable
fun SplashLogo() {
    Box(
        modifier = Modifier.size(160.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = size.minDimension
            val center = Offset(
                x = size.width / 2,
                y = size.height / 2
            )

            // Large soft mint green circle (flat, no shadows)
            val largeCircleRadius = canvasSize * 0.40f
            val largeCircleCenter = center

            // Draw large soft mint green circle (flat design)
            drawCircle(
                color = Color(0xFFB5E6D3), // Soft mint green
                radius = largeCircleRadius,
                center = largeCircleCenter
            )

            // Heart icon path (centered in large circle, simple flat design)
            val heartSize = largeCircleRadius * 1.3f
            val heartPath = Path().apply {
                moveTo(largeCircleCenter.x, largeCircleCenter.y + heartSize * 0.2f)

                cubicTo(
                    largeCircleCenter.x - heartSize * 0.2f, largeCircleCenter.y + heartSize * 0.08f,
                    largeCircleCenter.x - heartSize * 0.28f, largeCircleCenter.y - heartSize * 0.04f,
                    largeCircleCenter.x - heartSize * 0.2f, largeCircleCenter.y - heartSize * 0.12f
                )

                cubicTo(
                    largeCircleCenter.x - heartSize * 0.12f, largeCircleCenter.y - heartSize * 0.22f,
                    largeCircleCenter.x - heartSize * 0.04f, largeCircleCenter.y - heartSize * 0.2f,
                    largeCircleCenter.x, largeCircleCenter.y - heartSize * 0.1f
                )

                cubicTo(
                    largeCircleCenter.x + heartSize * 0.04f, largeCircleCenter.y - heartSize * 0.2f,
                    largeCircleCenter.x + heartSize * 0.12f, largeCircleCenter.y - heartSize * 0.22f,
                    largeCircleCenter.x + heartSize * 0.2f, largeCircleCenter.y - heartSize * 0.12f
                )

                cubicTo(
                    largeCircleCenter.x + heartSize * 0.28f, largeCircleCenter.y - heartSize * 0.04f,
                    largeCircleCenter.x + heartSize * 0.2f, largeCircleCenter.y + heartSize * 0.08f,
                    largeCircleCenter.x, largeCircleCenter.y + heartSize * 0.2f
                )

                close()
            }

            // Draw heart in dark navy/charcoal (flat, no gradients)
            drawPath(
                path = heartPath,
                color = Color(0xFF1B2631) // Dark navy/charcoal
            )

            // Small soft peach circle badge (on bottom-right edge of large circle)
            val smallCircleRadius = canvasSize * 0.18f
            // Position on the edge: calculate angle for bottom-right edge
            val angle = 45f * (Math.PI / 180f).toFloat() // 45 degrees for bottom-right
            val edgeDistance = largeCircleRadius - smallCircleRadius * 0.3f // Overlap slightly
            val smallCircleCenter = Offset(
                x = largeCircleCenter.x + edgeDistance * cos(angle),
                y = largeCircleCenter.y + edgeDistance * sin(angle)
            )

            // Draw small soft peach circle (flat design, no shadows)
            drawCircle(
                color = Color(0xFFFFD4A3), // Soft peach
                radius = smallCircleRadius,
                center = smallCircleCenter
            )

            // Thin ECG/heartbeat wave line inside small badge (dark navy)
            val pulsePath = Path().apply {
                val pulseWidth = smallCircleRadius * 0.7f
                val pulseHeight = smallCircleRadius * 0.4f
                
                val startX = smallCircleCenter.x - pulseWidth * 0.35f
                val midX = smallCircleCenter.x
                val endX = smallCircleCenter.x + pulseWidth * 0.35f
                val baseY = smallCircleCenter.y + pulseHeight * 0.2f
                val peakY = smallCircleCenter.y - pulseHeight * 0.4f
                val midLowY = smallCircleCenter.y + pulseHeight * 0.3f

                // ECG waveform: start at base, go up, diagonal down, up, then down to base
                moveTo(startX, baseY)
                lineTo(startX, peakY) // Left vertical up
                lineTo(midX, midLowY) // Diagonal down to middle
                lineTo(endX, peakY) // Right vertical up
                lineTo(endX, baseY) // Right vertical down
            }

            // Draw thin ECG/heartbeat wave in dark navy (flat, thin line)
            drawPath(
                path = pulsePath,
                color = Color(0xFF1B2631), // Dark navy (same as heart)
                style = Stroke(
                    width = 2.5f, // Thin line
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

@Composable
fun Dot(isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .background(
                color = if (isActive) {
                    Color(0xFF7FD4A3) // Darker green for active dot
                } else {
                    Color(0xFFD4EDE0) // Lighter green for inactive dots
                },
                shape = CircleShape
            )
    )
}
