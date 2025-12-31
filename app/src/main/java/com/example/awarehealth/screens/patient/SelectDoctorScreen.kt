package com.example.awarehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.DoctorData
import com.example.awarehealth.viewmodel.DoctorsViewModel
import com.example.awarehealth.viewmodel.DoctorsViewModelFactory
import com.example.awarehealth.viewmodel.DoctorsUiState

/* ---------- MODEL ---------- */

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val experience: String,
    val rating: Double,
    val availability: String,
    val location: String,
    val color: Color
)

/* ---------- SCREEN ---------- */

@Composable
fun SelectDoctorScreen(
    onBack: () -> Unit,
    onDoctorSelected: (Doctor) -> Unit,
    repository: AppRepository? = null
) {
    // Create ViewModel - only if repository is available
    val viewModel: DoctorsViewModel? = if (repository != null) {
        viewModel(factory = DoctorsViewModelFactory(repository))
    } else {
        null
    }
    
    val uiState: com.example.awarehealth.viewmodel.DoctorsUiState = if (viewModel != null) {
        viewModel.uiState.collectAsState().value
    } else {
        com.example.awarehealth.viewmodel.DoctorsUiState(
            isLoading = false,
            error = "Repository not available. Please restart the app.",
            doctors = emptyList()
        )
    }
    
    // Load doctors when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel?.initialLoad()
    }
    
    // Convert DoctorData to Doctor with alternating colors
    val doctors: List<Doctor> = uiState.doctors.mapIndexed { index, doctorData ->
        Doctor(
            id = doctorData.id,
            name = doctorData.name,
            specialty = doctorData.specialty,
            experience = doctorData.experience,
            rating = doctorData.rating,
            availability = doctorData.availability,
            location = doctorData.location,
            // Alternate colors for visual variety
            color = if (index % 2 == 0) Color(0xFFFFEAD6) else Color(0xFFE9FFF4)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
    ) {
        // Header is handled in NavGraph

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            /* ---------- TITLE SECTION ---------- */
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Select Doctor",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        lineHeight = 38.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Choose a specialist for your appointment",
                        fontSize = 15.sp,
                        color = Color(0xFF718096),
                        lineHeight = 22.sp
                        )
                    }
                    
                    // Refresh button
                    Text(
                        text = "🔄",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clickable { viewModel?.refresh() }
                            .padding(8.dp),
                        color = Color(0xFF4A5568)
                    )
                }
            }

            /* ---------- LOADING STATE ---------- */
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFAEE4C1))
                    }
                }
            }
            
            /* ---------- ERROR STATE ---------- */
            if (uiState.error != null && !uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "⚠️ ${uiState.error}",
                                color = Color(0xFFEA4335),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel?.refresh() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFAEE4C1)
                                )
                            ) {
                            Text(
                                    text = "Tap to Refresh",
                                    color = Color(0xFF2D3748),
                                fontSize = 14.sp
                            )
                            }
                        }
                    }
                }
            }
            
            /* ---------- DOCTOR LIST ---------- */
            if (!uiState.isLoading && uiState.error == null) {
                if (doctors.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No doctors available at Saveetha Hospital",
                                color = Color(0xFF718096),
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    items(doctors) { doctor ->
                        DoctorCard(
                            doctor = doctor,
                            onClick = { onDoctorSelected(doctor) }
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/* ---------- COMPONENT ---------- */

@Composable
fun DoctorCard(
    doctor: Doctor,
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
            .background(doctor.color, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(22.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* Avatar with light green background */
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFE9FFF4), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🩺", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Doctor Name
                Text(
                    text = doctor.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Specialty and Experience
                Text(
                    text = "${doctor.specialty} • ${doctor.experience}",
                    fontSize = 15.sp,
                    color = Color(0xFF4A5568),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Rating and Location Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = doctor.rating.toString(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.width(18.dp))

                    Text("📍", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = doctor.location,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Availability Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⏰", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = doctor.availability,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568)
                    )
                }
            }
        }
    }
}


