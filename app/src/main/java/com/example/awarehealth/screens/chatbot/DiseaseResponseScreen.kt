package com.example.awarehealth.ui.screens

/* ---------- IMPORTS ---------- */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODELS ---------- */

data class Diagnosis(
    val name: String,
    val severity: String,
    val description: String,
    val matchPercentage: Int
)

data class SymptomMatch(
    val name: String,
    val match: Boolean
)

/* ---------- SCREEN ---------- */

@Composable
fun DiseaseResponseScreen(
    onBack: () -> Unit,
    onPrecautions: () -> Unit,
    onMedicines: () -> Unit,
    onHospitals: () -> Unit
) {

    val diagnosis = Diagnosis(
        name = "Common Viral Fever",
        severity = "Moderate",
        description = "Based on your symptoms and duration, this appears to be a common viral fever. It usually resolves within 5–7 days with rest and proper care.",
        matchPercentage = 85
    )

    val symptoms = listOf(
        SymptomMatch("Fever", true),
        SymptomMatch("Headache", true),
        SymptomMatch("Body Aches", true),
        SymptomMatch("Fatigue", false)
    )

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
            /* ---------- DIAGNOSIS CARD ---------- */
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
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                diagnosis.name,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748),
                                lineHeight = 32.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFFAEE4C1),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "AI Analysis Complete",
                                    fontSize = 14.sp,
                                    color = Color(0xFF4A5568),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(
                                    when (diagnosis.severity.lowercase()) {
                                        "low" -> Color(0xFFAEE4C1)
                                        "moderate" -> Color(0xFFFFEAD6)
                                        "high" -> Color(0xFFFFE8E8)
                                        else -> Color(0xFFFFEAD6)
                                    },
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                diagnosis.severity,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Match Accuracy: ${diagnosis.matchPercentage}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(Color(0xFFFFFDF7), RoundedCornerShape(50.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(diagnosis.matchPercentage / 100f)
                                .height(10.dp)
                                .background(Color(0xFFAEE4C1), RoundedCornerShape(50.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        diagnosis.description,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- SYMPTOM ANALYSIS ---------- */
            Text(
                "Symptom Analysis",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(16.dp))

            symptoms.forEach { symptom ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(18.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        )
                        .background(
                            if (symptom.match)
                                Color(0xFFE9FFF4)
                            else
                                Color(0xFFF3F3F3),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (symptom.match) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFFAEE4C1),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                            Text(
                                symptom.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2D3748)
                            )
                        }
                        Text(
                            if (symptom.match) "Match" else "Not Reported",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (symptom.match) Color(0xFF2D3748) else Color(0xFF718096)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- ACTION BUTTONS ---------- */
            ActionButton("View Precautions & Care", Color(0xFFAEE4C1), onPrecautions)
            ActionButton("Suggested Medicines", Color(0xFFE9FFF4), onMedicines)
            ActionButton("Find Nearby Hospitals", Color(0xFFF3F3F3), onHospitals)
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/* ---------- BUTTON ---------- */

@Composable
private fun ActionButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(color, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2D3748)
        )
    }

    Spacer(modifier = Modifier.height(14.dp))
}
