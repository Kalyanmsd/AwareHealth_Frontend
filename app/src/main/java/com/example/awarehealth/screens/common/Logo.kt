package com.example.awarehealth.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Logo(
    size: Dp = 32.dp
) {
    Canvas(modifier = Modifier.size(size)) {

        /* ---------- CANVAS SIZE ---------- */
        val canvasSize = size.toPx()
        val center = Offset(
            x = canvasSize / 2,
            y = canvasSize / 2
        )

        /* ---------- BACKGROUND CIRCLE ---------- */
        drawCircle(
            color = Color(0xFFAEE4C1),
            radius = canvasSize / 2,
            center = center
        )

        /* ---------- HEART ---------- */
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

        drawPath(
            path = heartPath,
            color = Color(0xFF2D3748)
        )

        /* ---------- PULSE LINE ---------- */
        fun pulse(start: Offset, end: Offset) {
            drawLine(
                color = Color.White,
                start = start,
                end = end,
                strokeWidth = canvasSize * 0.03f,
                cap = StrokeCap.Round
            )
        }

        pulse(
            Offset(center.x - canvasSize * 0.25f, center.y),
            Offset(center.x - canvasSize * 0.12f, center.y)
        )
        pulse(
            Offset(center.x - canvasSize * 0.12f, center.y),
            Offset(center.x - canvasSize * 0.06f, center.y - canvasSize * 0.12f)
        )
        pulse(
            Offset(center.x - canvasSize * 0.06f, center.y - canvasSize * 0.12f),
            Offset(center.x, center.y + canvasSize * 0.12f)
        )
        pulse(
            Offset(center.x, center.y + canvasSize * 0.12f),
            Offset(center.x + canvasSize * 0.06f, center.y)
        )
        pulse(
            Offset(center.x + canvasSize * 0.06f, center.y),
            Offset(center.x + canvasSize * 0.25f, center.y)
        )

        /* ---------- SMALL ORANGE CIRCLE WITH ECG LINE ---------- */
        val smallCircleCenter = Offset(
            x = center.x + canvasSize * 0.28f,
            y = center.y - canvasSize * 0.28f
        )

        // Orange circle
        drawCircle(
            color = Color(0xFFFFDAB9),
            radius = canvasSize * 0.12f,
            center = smallCircleCenter
        )

        // ECG/heartbeat waveform inside orange circle
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
