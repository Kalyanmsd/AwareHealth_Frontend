package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */

data class Symptom(
    val id: String,
    val name: String,
    val emoji: String,
    val definition: String,
    val normalRange: String,
    val feverRange: String,
    val severity: Map<String, String>,
    val possibleCauses: List<String>,
    val whatToDo: List<String>,
    val whenToSeekHelp: List<String>,
    val associatedSymptoms: List<String>
)

/* ---------- SCREEN ---------- */

@Composable
fun SymptomExplanationScreen(
    symptomId: String,
    onBack: () -> Unit,
    onAskAI: () -> Unit,
    onViewDiseases: () -> Unit
) {

    /* ---------- MOCK DATA ---------- */
    val symptom = Symptom(
        id = symptomId,
        name = "Fever",
        emoji = "🤒",
        definition = "A temporary increase in body temperature, usually caused by infection. It is a sign that your body is fighting illness.",
        normalRange = "97°F – 99°F (36.1°C – 37.2°C)",
        feverRange = "100.4°F (38°C) or higher",
        severity = mapOf(
            "Mild" to "99°F – 100.3°F",
            "Moderate" to "100.4°F – 102.9°F",
            "High" to "103°F – 105°F",
            "Dangerous" to "Above 105°F"
        ),
        possibleCauses = listOf(
            "Viral infections",
            "Bacterial infections",
            "Heat exhaustion",
            "Inflammation",
            "Vaccinations",
            "Certain medicines"
        ),
        whatToDo = listOf(
            "Drink plenty of fluids",
            "Take rest",
            "Use fever-reducing medicines",
            "Keep room cool",
            "Monitor temperature"
        ),
        whenToSeekHelp = listOf(
            "Fever above 103°F",
            "Fever lasting more than 3 days",
            "Breathing difficulty",
            "Chest pain",
            "Confusion",
            "Persistent vomiting"
        ),
        associatedSymptoms = listOf(
            "Headache", "Chills", "Body aches",
            "Sweating", "Weakness", "Loss of appetite"
        )
    )

    /* ---------- UI ---------- */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        /* ---------- BACK ---------- */
        Text(
            text = "← Back",
            fontSize = 18.sp,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9FFF4), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(symptom.emoji, fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    symptom.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )
                Text(
                    "Understanding the symptom",
                    color = Color(0xFF4A5568)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(symptom.definition)

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- TEMPERATURE RANGES ---------- */
        InfoBox("🌡️ Temperature Ranges") {
            RangeRow("Normal", symptom.normalRange, Color(0xFFAEE4C1))
            RangeRow("Fever", symptom.feverRange, Color(0xFFFFEAD6))
        }

        /* ---------- SEVERITY ---------- */
        InfoBox("⚠️ Severity Levels") {
            symptom.severity.forEach { (level, range) ->
                RangeRow(level, range, Color(0xFFF3F3F3))
            }
        }

        /* ---------- CAUSES ---------- */
        InfoBox("ℹ️ Possible Causes") {
            symptom.possibleCauses.forEach {
                BulletText(it)
            }
        }

        /* ---------- WHAT TO DO ---------- */
        InfoBox("❤️ What You Can Do", Color(0xFFAEE4C1)) {
            symptom.whatToDo.forEach {
                CheckText(it)
            }
        }

        /* ---------- WHEN TO SEEK HELP ---------- */
        InfoBox("🚨 When to Seek Medical Help", Color(0xFFFFE8E8)) {
            symptom.whenToSeekHelp.forEach {
                WarningText(it)
            }
        }

        /* ---------- ASSOCIATED SYMPTOMS ---------- */
        InfoBox("🤕 Associated Symptoms") {
            FlowRow {
                symptom.associatedSymptoms.forEach {
                    Chip(it)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- ACTION BUTTONS ---------- */
        ActionButton("Check Symptoms with AI", Color(0xFFAEE4C1), onAskAI)
        Spacer(modifier = Modifier.height(12.dp))
        ActionButton("View Related Diseases", Color(0xFFF3F3F3), onViewDiseases)
    }
}

/* ---------- REUSABLE UI ---------- */

@Composable
private fun InfoBox(
    title: String,
    bg: Color = Color(0xFFF3F3F3),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(title, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun RangeRow(label: String, value: String, bg: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(12.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontSize = 13.sp)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun BulletText(text: String) {
    Text("• $text", color = Color(0xFF4A5568))
}

@Composable
private fun CheckText(text: String) {
    Text("✓ $text", color = Color(0xFF2D3748))
}

@Composable
private fun WarningText(text: String) {
    Text("⚠️ $text", color = Color(0xFF2D3748))
}

@Composable
private fun Chip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFE9FFF4), RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 13.sp)
    }
}

@Composable
private fun FlowRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
private fun ActionButton(
    text: String,
    bg: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontWeight = FontWeight.Medium)
    }
}
