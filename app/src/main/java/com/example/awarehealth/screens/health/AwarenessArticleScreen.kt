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

data class Article(
    val id: String,
    val title: String,
    val category: String,
    val readTime: String,
    val excerpt: String,
    val trending: Boolean,
    var bookmarked: Boolean,
    val emoji: String
)

/* ---------- SCREEN ---------- */

@Composable
fun AwarenessArticlesScreen(
    onBack: () -> Unit
) {

    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All", "Allergies", "Cardiology", "Mental Health",
        "Diabetes", "Nutrition", "Sleep", "Fitness"
    )

    val articles = remember {
        mutableStateListOf(
            Article("1", "Understanding Seasonal Allergies", "Allergies",
                "5 min read",
                "Learn about allergens, symptoms, and treatments.",
                true, false, "🌸"),

            Article("2", "10 Steps to a Healthy Heart", "Cardiology",
                "7 min read",
                "Lifestyle habits for better heart health.",
                true, true, "❤️"),

            Article("3", "Managing Stress Effectively", "Mental Health",
                "6 min read",
                "Techniques to improve mental wellness.",
                false, false, "🧠"),

            Article("4", "Diabetes Management Guide", "Diabetes",
                "10 min read",
                "Diet, exercise, and medication tips.",
                true, false, "💉"),

            Article("5", "Nutrition Basics", "Nutrition",
                "4 min read",
                "Build a balanced and healthy plate.",
                false, true, "🥗"),

            Article("6", "Better Sleep Hygiene", "Sleep",
                "5 min read",
                "Improve sleep quality naturally.",
                false, false, "😴"),

            Article("7", "Exercise for Beginners", "Fitness",
                "8 min read",
                "Start your fitness journey safely.",
                true, false, "🏃"),

            Article("8", "Cold & Flu Prevention", "Allergies",
                "5 min read",
                "Boost immunity during seasonal changes.",
                false, false, "🤧")
        )
    }

    val filteredArticles =
        if (selectedCategory == "All")
            articles
        else
            articles.filter { it.category == selectedCategory }

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
                Text("📘", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Health Articles",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Educational content for awareness",
                    color = Color(0xFF4A5568)
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
                    Text(category)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- TRENDING INFO ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFEAD6), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Text(
                "🔥 ${articles.count { it.trending }} Trending Articles This Week",
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* ---------- ARTICLE LIST ---------- */
        filteredArticles.forEach { article ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F3F3), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {

                Row {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFE9FFF4), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(article.emoji, fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                article.title,
                                fontWeight = FontWeight.Medium
                            )

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        if (article.bookmarked)
                                            Color(0xFFAEE4C1)
                                        else
                                            Color.White,
                                        CircleShape
                                    )
                                    .clickable {
                                        article.bookmarked = !article.bookmarked
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🔖")
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            article.excerpt,
                            fontSize = 13.sp,
                            color = Color(0xFF4A5568)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Tag(article.category)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                article.readTime,
                                fontSize = 12.sp,
                                color = Color(0xFF718096)
                            )
                            if (article.trending) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("🔥 Trending", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFAEE4C1), RoundedCornerShape(14.dp))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Read Article")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (filteredArticles.isEmpty()) {
            Text(
                "No articles found",
                modifier = Modifier.padding(20.dp),
                color = Color(0xFF718096)
            )
        }
    }
}

/* ---------- TAG ---------- */

@Composable
private fun Tag(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFE9FFF4), RoundedCornerShape(50.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, fontSize = 12.sp)
    }
}
