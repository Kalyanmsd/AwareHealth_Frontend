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

data class Vaccine(
    val id: String,
    val name: String,
    val ageGroup: String,
    val status: String, // completed | upcoming | overdue
    val dueDate: String? = null,
    val description: String
)

/* ---------- SCREEN ---------- */

@Composable
fun VaccinationReminderScreen(
    onBack: () -> Unit,
    onBookAppointment: () -> Unit
) {

    var selectedAgeGroup by remember { mutableStateOf("All Ages") }

    val ageGroups = listOf(
        "All Ages", "Infants", "Children", "Teens", "Adults", "Seniors"
    )

    val vaccines = listOf(
        Vaccine("1", "COVID-19 Booster", "Adults", "upcoming", "Jan 15, 2025",
            "Booster dose for continued protection"),
        Vaccine("2", "Influenza (Flu)", "All Ages", "overdue", "Dec 1, 2024",
            "Annual flu vaccine"),
        Vaccine("3", "Tetanus Booster", "Adults", "completed",
            description = "Protection against tetanus"),
        Vaccine("4", "Hepatitis B", "Infants", "completed",
            description = "Protects against hepatitis B"),
        Vaccine("5", "MMR Vaccine", "Children", "completed",
            description = "Measles, Mumps, Rubella"),
        Vaccine("6", "HPV Vaccine", "Teens", "upcoming", "Feb 20, 2025",
            "Prevents HPV related diseases"),
        Vaccine("7", "Pneumococcal", "Seniors", "upcoming", "Mar 10, 2025",
            "Prevents pneumococcal infections")
    )

    val filteredVaccines =
        if (selectedAgeGroup == "All Ages")
            vaccines
        else
            vaccines.filter { it.ageGroup == selectedAgeGroup || it.ageGroup == "All Ages" }

    fun statusColor(status: String) = when (status) {
        "completed" -> Color(0xFFAEE4C1)
        "upcoming" -> Color(0xFFFFEAD6)
        "overdue" -> Color(0xFFFFE8E8)
        else -> Color(0xFFF3F3F3)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        /* ---------- BACK ---------- */
        Text(
            "← Back",
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
                Text("💉", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Vaccination Reminder",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Stay up-to-date with immunizations",
                    color = Color(0xFF4A5568)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- AGE FILTER ---------- */
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            ageGroups.forEach { group ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(
                            if (group == selectedAgeGroup)
                                Color(0xFFAEE4C1)
                            else
                                Color(0xFFF3F3F3),
                            RoundedCornerShape(50.dp)
                        )
                        .clickable { selectedAgeGroup = group }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(group)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- VACCINE LIST ---------- */
        filteredVaccines.forEach { vaccine ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(statusColor(vaccine.status), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                when (vaccine.status) {
                                    "completed" -> "✅"
                                    "upcoming" -> "📅"
                                    "overdue" -> "⚠️"
                                    else -> "💉"
                                }
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(vaccine.name, fontWeight = FontWeight.Medium)
                            Text(
                                vaccine.ageGroup,
                                fontSize = 12.sp,
                                color = Color(0xFF718096)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        vaccine.description,
                        fontSize = 13.sp,
                        color = Color(0xFF4A5568)
                    )

                    vaccine.dueDate?.let {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Due: $it", fontSize = 12.sp)
                    }

                    if (vaccine.status != "completed") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(14.dp))
                                .clickable { }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔔 Set Reminder")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        /* ---------- BOOK BUTTON ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFAEE4C1), RoundedCornerShape(20.dp))
                .clickable { onBookAppointment() }
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Book Appointment for Vaccination",
                fontWeight = FontWeight.Medium
            )
        }
    }
}
