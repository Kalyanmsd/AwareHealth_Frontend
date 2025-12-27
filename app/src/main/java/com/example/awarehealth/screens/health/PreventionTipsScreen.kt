package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */

data class PreventionTip(
    val id: String,
    val category: String,
    val icon: String,
    val title: String,
    val description: String,
    val impact: String,
    val color: Color
)

/* ---------- SCREEN ---------- */

@Composable
fun PreventionTipsScreen(
    diseaseId: String,
    onBack: () -> Unit
) {

    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All", "Hygiene", "Nutrition", "Exercise", "Mental Health"
    )

    // Get disease-specific prevention tips
    val diseasePreventionTips = when (diseaseId) {
        "cold" -> listOf(
            PreventionTip(
                "1", "Hygiene", "🧼",
                "Wash Hands Frequently",
                "Wash hands with soap for at least 20 seconds, especially after coughing or sneezing.",
                "Prevents 80% of cold infections",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Hygiene", "🛡️",
                "Avoid Close Contact",
                "Stay away from people who are sick and avoid touching your face.",
                "Reduces transmission risk",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Hygiene", "🧴",
                "Disinfect Surfaces",
                "Clean frequently touched surfaces like doorknobs and phones regularly.",
                "Kills cold viruses",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Nutrition", "🥗",
                "Eat Healthy Foods",
                "Include fruits and vegetables rich in vitamin C to boost immunity.",
                "Strengthens immune system",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "5", "Nutrition", "💧",
                "Stay Hydrated",
                "Drink plenty of water, herbal teas, and warm liquids.",
                "Helps flush out toxins",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "6", "Exercise", "🌙",
                "Get Adequate Rest",
                "Sleep 7-9 hours per night to help your body fight infections.",
                "Boosts immune function",
                Color(0xFFFFEAD6)
            )
        )
        "flu" -> listOf(
            PreventionTip(
                "1", "Hygiene", "💉",
                "Get Flu Vaccination",
                "Annual flu vaccine is the best way to prevent influenza.",
                "Reduces flu risk by 40-60%",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Hygiene", "🧼",
                "Wash Hands Regularly",
                "Use soap and water or alcohol-based hand sanitizer frequently.",
                "Prevents virus spread",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Hygiene", "😷",
                "Wear Masks",
                "Wear masks in crowded places during flu season.",
                "Reduces airborne transmission",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Hygiene", "🏠",
                "Stay Home When Sick",
                "Avoid going to work or school if you have flu symptoms.",
                "Prevents spreading to others",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "5", "Nutrition", "🥗",
                "Boost Immunity",
                "Eat a balanced diet with plenty of fruits and vegetables.",
                "Strengthens body defenses",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "6", "Exercise", "🌙",
                "Get Enough Sleep",
                "Adequate rest helps your immune system fight the flu virus.",
                "Improves recovery time",
                Color(0xFFFFEAD6)
            )
        )
        "diabetes" -> listOf(
            PreventionTip(
                "1", "Nutrition", "🥗",
                "Healthy Diet",
                "Eat balanced meals with whole grains, lean proteins, and vegetables.",
                "Helps control blood sugar",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Exercise", "🏃",
                "Regular Exercise",
                "At least 150 minutes of moderate exercise per week.",
                "Improves insulin sensitivity",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Nutrition", "⚖️",
                "Maintain Healthy Weight",
                "Losing even 5-7% of body weight can prevent or delay diabetes.",
                "Reduces diabetes risk",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Nutrition", "🍎",
                "Limit Sugar Intake",
                "Reduce consumption of sugary drinks and processed foods.",
                "Prevents blood sugar spikes",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "5", "Exercise", "🌙",
                "Adequate Sleep",
                "Sleep 7-9 hours per night to help regulate blood sugar.",
                "Improves glucose control",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "6", "Mental Health", "😊",
                "Manage Stress",
                "Practice relaxation techniques to reduce stress hormones.",
                "Helps blood sugar control",
                Color(0xFFFFEAD6)
            )
        )
        "asthma" -> listOf(
            PreventionTip(
                "1", "Hygiene", "🌬️",
                "Avoid Triggers",
                "Identify and avoid allergens, smoke, and air pollution.",
                "Reduces asthma attacks",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Hygiene", "🏠",
                "Use Air Purifier",
                "Keep indoor air clean with HEPA filters and air purifiers.",
                "Removes allergens",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Hygiene", "💊",
                "Take Medications",
                "Use preventive medications as prescribed by your doctor.",
                "Prevents symptoms",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Exercise", "🏃",
                "Regular Exercise",
                "Stay active but warm up properly and avoid cold air.",
                "Improves lung function",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "5", "Mental Health", "😊",
                "Manage Stress",
                "Stress can trigger asthma, practice relaxation techniques.",
                "Reduces flare-ups",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "6", "Nutrition", "🥗",
                "Healthy Diet",
                "Eat anti-inflammatory foods like fruits and vegetables.",
                "Reduces inflammation",
                Color(0xFFFFEAD6)
            )
        )
        "migraine" -> listOf(
            PreventionTip(
                "1", "Nutrition", "📝",
                "Identify Triggers",
                "Keep a diary to identify food, stress, or environmental triggers.",
                "Helps avoid attacks",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Exercise", "🌙",
                "Regular Sleep Schedule",
                "Go to bed and wake up at the same time every day.",
                "Reduces migraine frequency",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Mental Health", "😊",
                "Stress Management",
                "Practice meditation, yoga, or deep breathing exercises.",
                "Prevents stress-triggered migraines",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Nutrition", "💧",
                "Stay Hydrated",
                "Drink plenty of water throughout the day.",
                "Prevents dehydration headaches",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "5", "Nutrition", "🍽️",
                "Regular Meals",
                "Don't skip meals, maintain consistent eating schedule.",
                "Prevents hunger-triggered migraines",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "6", "Exercise", "🏃",
                "Moderate Exercise",
                "Regular, moderate exercise can reduce migraine frequency.",
                "Improves overall health",
                Color(0xFFFFEAD6)
            )
        )
        "arthritis" -> listOf(
            PreventionTip(
                "1", "Exercise", "⚖️",
                "Maintain Healthy Weight",
                "Extra weight puts stress on joints, especially knees and hips.",
                "Reduces joint pressure",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Exercise", "🏃",
                "Regular Exercise",
                "Low-impact activities like swimming and walking strengthen joints.",
                "Improves joint function",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Hygiene", "🛡️",
                "Protect Joints",
                "Use proper techniques when lifting and avoid repetitive stress.",
                "Prevents joint injury",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Nutrition", "🥗",
                "Anti-Inflammatory Diet",
                "Eat foods rich in omega-3s, fruits, and vegetables.",
                "Reduces inflammation",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "5", "Exercise", "🌙",
                "Adequate Rest",
                "Balance activity with rest to avoid overexertion.",
                "Prevents joint fatigue",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "6", "Hygiene", "🧘",
                "Joint Protection",
                "Use assistive devices and ergonomic tools when needed.",
                "Reduces strain",
                Color(0xFFFFEAD6)
            )
        )
        "depression" -> listOf(
            PreventionTip(
                "1", "Exercise", "🏃",
                "Regular Exercise",
                "Physical activity releases endorphins and improves mood.",
                "Boosts mental health",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Nutrition", "🥗",
                "Healthy Diet",
                "Eat balanced meals with omega-3s, whole grains, and vegetables.",
                "Supports brain health",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Exercise", "🌙",
                "Adequate Sleep",
                "Maintain a regular sleep schedule of 7-9 hours per night.",
                "Improves mood regulation",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Mental Health", "❤️",
                "Social Support",
                "Stay connected with friends, family, and support groups.",
                "Reduces isolation",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "5", "Mental Health", "😊",
                "Stress Management",
                "Practice mindfulness, meditation, or relaxation techniques.",
                "Reduces anxiety",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "6", "Mental Health", "🎯",
                "Set Realistic Goals",
                "Break tasks into small, achievable steps to build confidence.",
                "Improves self-esteem",
                Color(0xFFFFEAD6)
            )
        )
        else -> listOf(
            PreventionTip(
                "1", "Hygiene", "🧼",
                "Wash Hands Regularly",
                "Wash hands with soap for at least 20 seconds.",
                "Prevents 80% of infections",
                Color(0xFFE9FFF4)
            ),
            PreventionTip(
                "2", "Nutrition", "🥗",
                "Balanced Diet",
                "Eat fruits, vegetables, whole grains and proteins.",
                "Boosts immunity",
                Color(0xFFAEE4C1)
            ),
            PreventionTip(
                "3", "Exercise", "🏃",
                "Regular Exercise",
                "At least 30 minutes of activity daily.",
                "Reduces chronic diseases",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "4", "Mental Health", "😊",
                "Stress Management",
                "Meditation, breathing and hobbies help reduce stress.",
                "Improves mental health",
                Color(0xFFF3F3F3)
            ),
            PreventionTip(
                "5", "Exercise", "🌙",
                "Adequate Sleep",
                "Sleep 7–9 hours every night.",
                "Strengthens immunity",
                Color(0xFFFFEAD6)
            ),
            PreventionTip(
                "6", "Nutrition", "💧",
                "Stay Hydrated",
                "Drink enough water daily.",
                "Supports body functions",
                Color(0xFFAEE4C1)
            )
        )
    }

    val tips = diseasePreventionTips

    val filteredTips =
        if (selectedCategory == "All") tips
        else tips.filter { it.category == selectedCategory }

    // Get disease name for header
    val diseaseName = when (diseaseId) {
        "cold" -> "Common Cold"
        "flu" -> "Influenza (Flu)"
        "diabetes" -> "Diabetes"
        "asthma" -> "Asthma"
        "migraine" -> "Migraine"
        "arthritis" -> "Arthritis"
        "depression" -> "Depression"
        else -> "General Health"
    }

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
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            /* ---------- HEADER CARD ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text("🛡️", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Prevention Tips",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Text(
                        "Prevention tips for $diseaseName",
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- CATEGORY FILTER ---------- */
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                categories.forEach { category ->
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                if (category == selectedCategory)
                                    Color(0xFFAEE4C1)
                                else
                                    Color(0xFFF3F3F3),
                                RoundedCornerShape(50.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(category, color = Color(0xFF2D3748))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- TIPS LIST ---------- */
            filteredTips.forEach { tip ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(tip.color, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tip.icon, fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                tip.title,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2D3748)
                            )
                            Text(
                                tip.description,
                                fontSize = 13.sp,
                                color = Color(0xFF4A5568)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "✓ ${tip.impact}",
                                fontSize = 12.sp,
                                color = Color(0xFF22C55E)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
