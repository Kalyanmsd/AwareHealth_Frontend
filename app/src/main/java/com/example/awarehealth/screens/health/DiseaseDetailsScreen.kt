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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.viewmodel.DiseaseDetailsViewModel

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
    repository: AppRepository,
    onBack: () -> Unit,
    onAskAI: () -> Unit,
    onViewPreventionTips: () -> Unit = {}
) {
    // Create ViewModel with key based on diseaseId to ensure it reloads for different diseases
    val viewModel: DiseaseDetailsViewModel = viewModel(key = diseaseId) { 
        DiseaseDetailsViewModel(repository, diseaseId)
    }
    val uiState by viewModel.uiState.collectAsState()
    
    // Reload if diseaseId changes
    LaunchedEffect(diseaseId) {
        if (diseaseId.isNotEmpty()) {
            viewModel.retry()
        }
    }

    // Convert DiseaseData to DiseaseDetails format
    val diseaseData = uiState.disease
    val disease = if (diseaseData != null) {
        DiseaseDetails(
            name = diseaseData.name,
            emoji = diseaseData.emoji ?: "🦠",
            category = diseaseData.category ?: "General",
            severity = diseaseData.severity ?: "Unknown",
            description = diseaseData.description ?: "No description available",
            symptoms = diseaseData.symptoms ?: emptyList(),
            causes = diseaseData.causes ?: emptyList(),
            prevention = diseaseData.prevention ?: emptyList(),
            treatment = diseaseData.treatment ?: emptyList(),
            affectedPopulation = diseaseData.affectedPopulation ?: "Unknown",
            duration = diseaseData.duration ?: "Unknown"
        )
    } else {
        // Fallback for loading/error state
        DiseaseDetails(
            name = "Loading...",
            emoji = "⏳",
            category = "General",
            severity = "Unknown",
            description = "Loading disease information...",
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

        // Loading state
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color(0xFFAEE4C1)
                    )
                    Text(
                        text = "Loading disease information...",
                        fontSize = 16.sp,
                        color = Color(0xFF4A5568)
                    )
                }
            }
        }
        // Error state
        else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "⚠️",
                        fontSize = 64.sp
                    )
                    Text(
                        text = "Error loading disease",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Text(
                        text = uiState.error ?: "Unknown error",
                        fontSize = 14.sp,
                        color = Color(0xFF4A5568)
                    )
                    Button(
                        onClick = { viewModel.retry() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFAEE4C1),
                            contentColor = Color(0xFF2D3748)
                        )
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
        // Success state - show disease details
        else if (diseaseData != null) {
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
}

/* ---------- COMPONENTS ---------- */

@Composable
fun InfoStatCard(
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
fun SectionCard(
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

            if (items.isEmpty()) {
                Text(
                    text = "No information available",
                    fontSize = 14.sp,
                    color = Color(0xFF718096),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
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
}

@Composable
fun WhenToSeeDoctorCard() {
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
