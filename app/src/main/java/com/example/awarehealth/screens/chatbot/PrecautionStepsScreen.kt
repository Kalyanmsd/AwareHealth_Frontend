package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrecautionStepsScreen(
    diagnosisName: String = "your condition",
    onBack: () -> Unit,
    onBookDoctor: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header is handled in NavGraph

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            /* ---------- HEADER CARD ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    )
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text("🩺", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Precautions & Care",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 34.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Follow these steps for faster recovery from $diagnosisName",
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

        CareCard(
            "Stay Hydrated",
            "Drink 8–10 glasses of water daily.",
            Color(0xFFE9FFF4)
        )

        CareCard(
            "Get Adequate Rest",
            "Sleep 7–8 hours and avoid heavy activity.",
            Color(0xFFAEE4C1)
        )

        CareCard(
            "Eat Nutritious Food",
            "Include fruits, vegetables, and proteins.",
            Color(0xFFFFEAD6)
        )

        CareCard(
            "Monitor Temperature",
            "Consult a doctor if fever exceeds 102°F.",
            Color(0xFFF3F3F3)
        )

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- DO'S AND DON'TS SECTION ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DosDontsCard(
                    title = "Do's",
                    items = listOf(
                        "Wash hands",
                        "Use separate utensils",
                        "Keep warm",
                        "Steam inhalation"
                    ),
                    bgColor = Color(0xFFAEE4C1),
                    modifier = Modifier.weight(1f)
                )

                DosDontsCard(
                    title = "Don'ts",
                    items = listOf(
                        "Avoid cold drinks",
                        "Don't skip meals",
                        "Avoid crowds",
                        "No self-medication"
                    ),
                    bgColor = Color(0xFFFFEAD6),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- BOOK DOCTOR BUTTON ---------- */
            Button(
                onClick = onBookDoctor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    contentColor = Color(0xFF2D3748)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Book Doctor Appointment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CareCard(
    title: String,
    description: String,
    bgColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF4A5568),
                lineHeight = 20.sp
            )
        }
    }
    Spacer(modifier = Modifier.height(14.dp))
}

@Composable
private fun DosDontsCard(
    title: String,
    items: List<String>,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            items.forEach { item ->
                Text(
                    text = "• $item",
                    fontSize = 14.sp,
                    color = Color(0xFF2D3748),
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}
