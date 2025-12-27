@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */

data class Disease(
    val id: String,
    val name: String,
    val category: String,
    val severity: String,
    val description: String,
    val symptoms: List<String>,
    val emoji: String
)

/* ---------- SCREEN ---------- */

@Composable
fun DiseaseListScreen(
    onBack: () -> Unit,
    onMenu: () -> Unit,
    onDiseaseClick: (Disease) -> Unit
) {

    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All", "Infectious", "Chronic", "Respiratory", "Digestive", "Seasonal"
    )

    val diseases = listOf(
        Disease(
            "cold",
            "Common Cold",
            "Respiratory",
            "Low",
            "Viral infection of the upper respiratory tract",
            listOf("Runny nose", "Cough", "Sneezing"),
            "🤧"
        ),
        Disease(
            "flu",
            "Influenza (Flu)",
            "Infectious",
            "Moderate",
            "Contagious respiratory illness caused by influenza viruses",
            listOf("Fever", "Body pain", "Fatigue"),
            "🤒"
        ),
        Disease(
            "diabetes",
            "Diabetes",
            "Chronic",
            "High",
            "Chronic condition affecting blood sugar regulation",
            listOf("Thirst", "Fatigue", "Frequent urination"),
            "🩸"
        ),
        Disease(
            "asthma",
            "Asthma",
            "Respiratory",
            "Moderate",
            "Chronic condition affecting the airways in lungs",
            listOf("Shortness of breath", "Wheezing", "Coughing"),
            "😮‍💨"
        ),
        Disease(
            "migraine",
            "Migraine",
            "Chronic",
            "Moderate",
            "Severe headache with throbbing pain",
            listOf("Headache", "Nausea", "Sensitivity to light"),
            "🤕"
        ),
        Disease(
            "arthritis",
            "Arthritis",
            "Chronic",
            "Moderate",
            "Inflammation of joints causing pain and stiffness",
            listOf("Joint pain", "Stiffness", "Swelling"),
            "🦴"
        ),
        Disease(
            "depression",
            "Depression",
            "Chronic",
            "High",
            "Mental health condition causing persistent sadness",
            listOf("Sadness", "Loss of interest", "Fatigue"),
            "😔"
        )
    )

    val filteredDiseases = diseases.filter {
        it.name.contains(search, true) &&
                (selectedCategory == "All" || it.category == selectedCategory)
    }

    fun severityColor(level: String) = when (level) {
        "Low" -> Color(0xFFAEE4C1)
        "Moderate" -> Color(0xFFFFEAD6)
        "High" -> Color(0xFFFFE8E8)
        else -> Color(0xFFF3F3F3)
    }

    fun severityTagColor(level: String) = when (level) {
        "Low" -> Color(0xFFE2E8F0)
        "Moderate" -> Color(0xFFFFEAD6)
        "High" -> Color(0xFFFFE8E8)
        else -> Color(0xFFE2E8F0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header is handled in NavGraph

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            /* ---------- TITLE CARD ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(20.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Disease Database",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 34.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Learn about common diseases and their symptoms",
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- SEARCH ---------- */
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = {
                    Text(
                        "Search diseases...",
                        color = Color(0xFFA0AEC0),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
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

            /* ---------- CATEGORY FILTER ---------- */
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                categories.forEach { category ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (category == selectedCategory)
                                    Color(0xFFAEE4C1)
                                else
                                    Color(0xFFF3F3F3),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = category,
                            fontSize = 14.sp,
                            fontWeight = if (category == selectedCategory) FontWeight.SemiBold else FontWeight.Medium,
                            color = Color(0xFF2D3748)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------- LIST ---------- */
            if (filteredDiseases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No diseases found",
                        fontSize = 16.sp,
                        color = Color(0xFF718096)
                    )
                }
            } else {
                filteredDiseases.forEach { disease ->
                    DiseaseCard(
                        disease = disease,
                        severityColor = severityColor(disease.severity),
                        severityTagColor = severityTagColor(disease.severity),
                        onClick = { onDiseaseClick(disease) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- MEDICAL DISCLAIMER ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .background(Color(0xFFFFEAD6), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF2D3748)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Medical Disclaimer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "This information is for educational purposes only. Always consult with a healthcare professional for proper diagnosis and treatment.",
                            fontSize = 13.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DiseaseCard(
    disease: Disease,
    severityColor: Color,
    severityTagColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(severityColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Emoji/Icon
            Text(
                text = disease.emoji,
                fontSize = 40.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = disease.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Severity tag
                    Box(
                        modifier = Modifier
                            .background(
                                severityTagColor,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = disease.severity,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (disease.severity == "High") Color(0xFFE53E3E) else Color(0xFF2D3748),
                            lineHeight = 14.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = disease.description,
                    fontSize = 14.sp,
                    color = Color(0xFF4A5568),
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Category and symptoms
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFE2E8F0),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = disease.category,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2D3748),
                            lineHeight = 14.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Box(
                        modifier = Modifier.size(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color(0xFFAEE4C1), CircleShape)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${disease.symptoms.size} symptoms",
                        fontSize = 12.sp,
                        color = Color(0xFF4A5568),
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}
