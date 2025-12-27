package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */

data class DiseaseDetails(
    val name: String,
    val emoji: String,
    val category: String,
    val severity: String,
    val description: String,
    val symptoms: List<String>,
    val causes: List<String>,
    val prevention: List<String>,
    val treatment: List<String>,
    val affectedPopulation: String,
    val duration: String
)

/* ---------- SCREEN ---------- */

@Composable
fun DiseaseDetailsScreen(
    diseaseId: String,
    onBack: () -> Unit,
    onAskAI: () -> Unit,
    onViewPreventionTips: () -> Unit = {}
) {

    /* ---------- DATABASE ---------- */
    val disease = when (diseaseId) {

        "cold" -> DiseaseDetails(
            name = "Common Cold",
            emoji = "🤧",
            category = "Respiratory",
            severity = "Low",
            description = "The common cold is a viral infection of your nose and throat (upper respiratory tract). It's usually harmless, although it might not feel that way. Many types of viruses can cause a common cold.",
            symptoms = listOf(
                "Runny or stuffy nose",
                "Sore throat",
                "Cough",
                "Congestion",
                "Slight body aches",
                "Sneezing",
                "Low-grade fever"
            ),
            causes = listOf(
                "Rhinoviruses",
                "Air droplets",
                "Direct contact",
                "Touching contaminated surfaces"
            ),
            prevention = listOf(
                "Wash hands frequently",
                "Avoid close contact with sick people",
                "Don't touch face with unwashed hands",
                "Disinfect frequently touched surfaces",
                "Maintain healthy lifestyle"
            ),
            treatment = listOf(
                "Get plenty of rest",
                "Stay hydrated",
                "Use over-the-counter medications",
                "Gargle with salt water",
                "Use humidifier"
            ),
            affectedPopulation = "2-3 colds per year for adults, more for children",
            duration = "7-10 days"
        )

        "flu" -> DiseaseDetails(
            name = "Influenza (Flu)",
            emoji = "🤒",
            category = "Infectious",
            severity = "Moderate",
            description = "Contagious respiratory illness caused by influenza viruses.",
            symptoms = listOf(
                "High fever",
                "Body aches",
                "Fatigue",
                "Dry cough",
                "Sore throat",
                "Headache"
            ),
            causes = listOf(
                "Influenza viruses",
                "Air droplets",
                "Seasonal outbreaks",
                "Direct contact"
            ),
            prevention = listOf(
                "Flu vaccination",
                "Hand hygiene",
                "Mask usage",
                "Avoid close contact",
                "Stay home when sick"
            ),
            treatment = listOf(
                "Get plenty of rest",
                "Stay hydrated",
                "Use antiviral medications",
                "Over-the-counter pain relievers",
                "Use humidifier"
            ),
            affectedPopulation = "5–20% of population annually",
            duration = "1–2 weeks"
        )

        "diabetes" -> DiseaseDetails(
            name = "Diabetes",
            emoji = "🩸",
            category = "Chronic",
            severity = "High",
            description = "Chronic condition affecting blood sugar regulation.",
            symptoms = listOf(
                "Increased thirst",
                "Frequent urination",
                "Fatigue",
                "Blurred vision",
                "Slow healing",
                "Weight loss"
            ),
            causes = listOf(
                "Insulin deficiency",
                "Genetics",
                "Obesity",
                "Inactive lifestyle",
                "Poor diet"
            ),
            prevention = listOf(
                "Healthy diet",
                "Regular exercise",
                "Weight control",
                "Regular checkups",
                "Monitor blood sugar"
            ),
            treatment = listOf(
                "Blood sugar monitoring",
                "Insulin or medication",
                "Diet control",
                "Regular exercise",
                "Lifestyle changes"
            ),
            affectedPopulation = "422 million worldwide",
            duration = "Lifelong"
        )

        "asthma" -> DiseaseDetails(
            name = "Asthma",
            emoji = "😮‍💨",
            category = "Respiratory",
            severity = "Moderate",
            description = "Chronic condition affecting the airways in lungs.",
            symptoms = listOf(
                "Shortness of breath",
                "Wheezing",
                "Coughing",
                "Chest tightness",
                "Difficulty breathing"
            ),
            causes = listOf(
                "Allergens",
                "Air pollution",
                "Respiratory infections",
                "Genetics",
                "Environmental factors"
            ),
            prevention = listOf(
                "Avoid triggers",
                "Use air purifier",
                "Regular medication",
                "Monitor symptoms",
                "Maintain healthy lifestyle"
            ),
            treatment = listOf(
                "Inhalers",
                "Bronchodilators",
                "Corticosteroids",
                "Avoid triggers",
                "Breathing exercises"
            ),
            affectedPopulation = "Over 300 million worldwide",
            duration = "Chronic condition"
        )

        "migraine" -> DiseaseDetails(
            name = "Migraine",
            emoji = "🤕",
            category = "Chronic",
            severity = "Moderate",
            description = "Severe headache with throbbing pain.",
            symptoms = listOf(
                "Headache",
                "Nausea",
                "Sensitivity to light",
                "Sensitivity to sound",
                "Aura",
                "Dizziness"
            ),
            causes = listOf(
                "Hormonal changes",
                "Stress",
                "Food triggers",
                "Sleep patterns",
                "Environmental factors"
            ),
            prevention = listOf(
                "Identify triggers",
                "Regular sleep schedule",
                "Stress management",
                "Stay hydrated",
                "Regular meals"
            ),
            treatment = listOf(
                "Pain relievers",
                "Triptans",
                "Rest in dark room",
                "Cold compress",
                "Preventive medications"
            ),
            affectedPopulation = "1 billion people worldwide",
            duration = "4-72 hours per episode"
        )

        "arthritis" -> DiseaseDetails(
            name = "Arthritis",
            emoji = "🦴",
            category = "Chronic",
            severity = "Moderate",
            description = "Inflammation of joints causing pain and stiffness.",
            symptoms = listOf(
                "Joint pain",
                "Stiffness",
                "Swelling",
                "Redness",
                "Reduced range of motion"
            ),
            causes = listOf(
                "Age",
                "Genetics",
                "Injury",
                "Obesity",
                "Autoimmune disorders"
            ),
            prevention = listOf(
                "Maintain healthy weight",
                "Regular exercise",
                "Protect joints",
                "Healthy diet",
                "Avoid repetitive stress"
            ),
            treatment = listOf(
                "Pain relievers",
                "Anti-inflammatory drugs",
                "Physical therapy",
                "Exercise",
                "Joint protection"
            ),
            affectedPopulation = "Over 350 million worldwide",
            duration = "Chronic condition"
        )

        "depression" -> DiseaseDetails(
            name = "Depression",
            emoji = "😔",
            category = "Chronic",
            severity = "High",
            description = "Mental health condition causing persistent sadness.",
            symptoms = listOf(
                "Sadness",
                "Loss of interest",
                "Fatigue",
                "Sleep problems",
                "Difficulty concentrating",
                "Feelings of worthlessness"
            ),
            causes = listOf(
                "Genetics",
                "Brain chemistry",
                "Life events",
                "Medical conditions",
                "Substance abuse"
            ),
            prevention = listOf(
                "Regular exercise",
                "Healthy diet",
                "Adequate sleep",
                "Social support",
                "Stress management"
            ),
            treatment = listOf(
                "Therapy",
                "Medication",
                "Lifestyle changes",
                "Support groups",
                "Self-care"
            ),
            affectedPopulation = "280 million people worldwide",
            duration = "Varies, can be chronic"
        )

        else -> DiseaseDetails(
            name = "Unknown Disease",
            emoji = "❓",
            category = "General",
            severity = "Low",
            description = "Information not available.",
            symptoms = emptyList(),
            causes = emptyList(),
            prevention = emptyList(),
            treatment = emptyList(),
            affectedPopulation = "Unknown",
            duration = "Unknown"
        )
    }

    /* ---------- UI ---------- */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header is handled in NavGraph

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- MAIN OVERVIEW CARD ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Large emoji
                    Text(
                        text = disease.emoji,
                        fontSize = 64.sp,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                    // Content
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = disease.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tags
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(0xFFE2E8F0),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = disease.category,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF2D3748)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(0xFFAEE4C1),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${disease.severity} Severity",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF2D3748)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Description
                        Text(
                            text = disease.description,
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- INFO CARDS (SIDE BY SIDE) ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoStatCard(
                    title = "Affected Population",
                    value = disease.affectedPopulation,
                    backgroundColor = Color(0xFFFFEAD6),
                    modifier = Modifier.weight(1f)
                )
                InfoStatCard(
                    title = "Typical Duration",
                    value = disease.duration,
                    backgroundColor = Color(0xFFAEE4C1),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- SYMPTOMS SECTION ---------- */
            SectionCard(
                title = "Symptoms",
                items = disease.symptoms,
                backgroundColor = Color(0xFFE9FFF4),
                icon = Icons.Default.Info
            )

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- CAUSES SECTION ---------- */
            SectionCard(
                title = "Causes",
                items = disease.causes,
                backgroundColor = Color(0xFFFFEAD6),
                icon = Icons.Default.Info
            )

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- PREVENTION SECTION ---------- */
            SectionCard(
                title = "Prevention",
                items = disease.prevention,
                backgroundColor = Color(0xFFE9FFF4),
                icon = Icons.Default.Shield
            )

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- TREATMENT SECTION ---------- */
            SectionCard(
                title = "Treatment",
                items = disease.treatment,
                backgroundColor = Color.White,
                icon = Icons.Default.Medication
            )

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- WHEN TO SEE A DOCTOR ---------- */
            WhenToSeeDoctorCard()

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- ACTION BUTTONS ---------- */
            Button(
                onClick = onAskAI,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color(0xFFAEE4C1).copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAEE4C1),
                    contentColor = Color(0xFF2D3748)
                )
            ) {
                Text(
                    text = "Ask AI About This Condition",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onViewPreventionTips,
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
                    containerColor = Color(0xFFE9FFF4)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.5.dp,
                    color = Color(0xFFAEE4C1)
                )
            ) {
                Text(
                    text = "View Prevention Tips",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/* ---------- COMPONENTS ---------- */

@Composable
private fun InfoStatCard(
    title: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(22.dp)
    ) {
        Column {
            Icon(
                imageVector = if (title.contains("Population")) Icons.Default.People else Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF2D3748)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                color = Color(0xFF4A5568),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    items: List<String>,
    backgroundColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(26.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    tint = Color(0xFF2D3748)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 26.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Color(0xFFAEE4C1), CircleShape)
                            .padding(top = 9.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = item,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WhenToSeeDoctorCard() {
    val warningSigns = listOf(
        "Symptoms lasting more than 10 days",
        "High fever (above 101.3°F or 38.5°C)",
        "Difficulty breathing or chest pain",
        "Severe or worsening symptoms"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(26.dp)
    ) {
        Column {
            Text(
                text = "When to See a Doctor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            warningSigns.forEach { sign ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = sign,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
