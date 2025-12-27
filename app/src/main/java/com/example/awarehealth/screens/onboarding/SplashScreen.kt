package com.example.awarehealth.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .background(Color(0xFFE9FFF4)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Large circular icon with shadow
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    )
                    .background(Color(0xFFAEE4C1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                SplashLogo(size = 80.dp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App name
            Text(
                text = "AwareHealth",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = "AI-Powered Healthcare System",
                fontSize = 18.sp,
                color = Color(0xFF4A5568)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Three dots indicator
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Dot()
                Dot()
                Dot()
            }
        }
    }
}

@Composable
fun SplashLogo(size: androidx.compose.ui.unit.Dp = 80.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val canvasSize = size.toPx()
        val center = Offset(
            x = canvasSize / 2,
            y = canvasSize / 2
        )

        // Heart icon
        val heartPath = Path().apply {
            moveTo(center.x, center.y + canvasSize * 0.25f)

            cubicTo(
                center.x - canvasSize * 0.25f, center.y + canvasSize * 0.1f,
                center.x - canvasSize * 0.35f, center.y - canvasSize * 0.05f,
                center.x - canvasSize * 0.25f, center.y - canvasSize * 0.15f
            )

            cubicTo(
                center.x - canvasSize * 0.15f, center.y - canvasSize * 0.28f,
                center.x - canvasSize * 0.05f, center.y - canvasSize * 0.25f,
                center.x, center.y - canvasSize * 0.12f
            )

            cubicTo(
                center.x + canvasSize * 0.05f, center.y - canvasSize * 0.25f,
                center.x + canvasSize * 0.15f, center.y - canvasSize * 0.28f,
                center.x + canvasSize * 0.25f, center.y - canvasSize * 0.15f
            )

            cubicTo(
                center.x + canvasSize * 0.35f, center.y - canvasSize * 0.05f,
                center.x + canvasSize * 0.25f, center.y + canvasSize * 0.1f,
                center.x, center.y + canvasSize * 0.25f
            )

            close()
        }

        // Draw heart in dark teal (centered)
        drawPath(
            path = heartPath,
            color = Color(0xFF2D3748)
        )

        // Small orange circle at bottom right with ECG waveform inside
        val smallCircleCenter = Offset(
            x = center.x + canvasSize * 0.28f,
            y = center.y + canvasSize * 0.28f
        )

        // Draw orange circle
        drawCircle(
            color = Color(0xFFFFDAB9),
            radius = canvasSize * 0.12f,
            center = smallCircleCenter
        )

        // ECG waveform line inside orange circle (dark teal)
        val ecgStart = Offset(smallCircleCenter.x - canvasSize * 0.08f, smallCircleCenter.y)
        val ecgPeak = Offset(smallCircleCenter.x, smallCircleCenter.y - canvasSize * 0.08f)
        val ecgEnd = Offset(smallCircleCenter.x + canvasSize * 0.08f, smallCircleCenter.y)
        
        drawLine(
            color = Color(0xFF2D3748),
            start = ecgStart,
            end = ecgPeak,
            strokeWidth = canvasSize * 0.025f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF2D3748),
            start = ecgPeak,
            end = ecgEnd,
            strokeWidth = canvasSize * 0.025f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun Dot() {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(Color(0xFFAEE4C1), CircleShape)
    )
}
