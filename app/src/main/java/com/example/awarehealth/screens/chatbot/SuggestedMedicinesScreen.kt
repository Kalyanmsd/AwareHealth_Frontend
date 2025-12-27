package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

/* ---------- MODEL ---------- */

data class Medicine(
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val purpose: String,
    val color: Color
)

/* ---------- SCREEN ---------- */

@Composable
fun SuggestedMedicinesScreen(
    diagnosisName: String = "your condition",
    onBack: () -> Unit,
    onConsultDoctor: () -> Unit,
    onFindPharmacy: () -> Unit
) {

    val medicines = listOf(
        Medicine(
            name = "Paracetamol",
            dosage = "500mg",
            frequency = "3 times daily",
            duration = "5 days",
            purpose = "Fever and pain relief",
            color = Color(0xFFFFEAD6) // Light pink/orange
        ),
        Medicine(
            name = "Ibuprofen",
            dosage = "400mg",
            frequency = "2 times daily",
            duration = "3-5 days",
            purpose = "Anti-inflammatory",
            color = Color(0xFFE9FFF4) // Light green
        ),
        Medicine(
            name = "Vitamin C",
            dosage = "100mg",
            frequency = "Once daily",
            duration = "7 days",
            purpose = "Immunity boost",
            color = Color(0xFFFFEAD6) // Light pink/orange
        )
    )

    val guidelines = listOf(
        "Take medicines after meals",
        "Complete the full course",
        "Do not exceed dosage",
        "Store in cool, dry place",
        "Keep away from children"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7)),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Header is handled in NavGraph

        /* ---------- HEADER CARD ---------- */
        item {
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
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text("💊", fontSize = 56.sp)
                    Spacer(modifier = Modifier.width(18.dp))
                    Column {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Suggested Medicines",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "For your condition",
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        /* ---------- WARNING CARD ---------- */
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(18.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(Color(0xFFFFEAD6), RoundedCornerShape(18.dp))
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text("⚠️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "These are general suggestions. Consult a doctor before taking medicines.",
                        fontSize = 14.sp,
                        color = Color(0xFF2D3748),
                        lineHeight = 22.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        /* ---------- MEDICINES ---------- */
        items(medicines) { med ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(med.color, RoundedCornerShape(20.dp))
                    .padding(22.dp)
            ) {
                Column {
                    Text(
                        text = med.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 30.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = med.purpose,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Dosage: ${med.dosage}",
                        fontSize = 14.sp,
                        color = Color(0xFF2D3748),
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Frequency: ${med.frequency}",
                        fontSize = 14.sp,
                        color = Color(0xFF2D3748),
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Duration: ${med.duration}",
                        fontSize = 14.sp,
                        color = Color(0xFF2D3748),
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        /* ---------- GUIDELINES ---------- */
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(Color(0xFFF3F3F3), RoundedCornerShape(20.dp))
                    .padding(22.dp)
            ) {
                Column {
                    Text(
                        text = "General Guidelines",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    guidelines.forEachIndexed { index, guideline ->
                        Text(
                            text = "• $guideline",
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 22.sp
                        )
                        if (index < guidelines.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        /* ---------- ACTION BUTTONS ---------- */
        item {
            Button(
                onClick = onConsultDoctor,
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
                    text = "Consult a Doctor",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item {
            Button(
                onClick = onFindPharmacy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F3F3),
                    contentColor = Color(0xFF2D3748)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Find Nearby Pharmacies",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
