package com.example.awarehealth.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- MODEL ---------- */
data class Pharmacy(
    val id: String,
    val name: String,
    val distance: String,
    val address: String,
    val phone: String,
    val openNow: Boolean,
    val type: String // "24/7", "Regular", etc.
)

/* ---------- SCREEN ---------- */
@Composable
fun NearbyPharmacyScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    var selectedPharmacyId by remember { mutableStateOf<String?>(null) }

    val pharmacies = listOf(
        Pharmacy("1", "City Pharmacy", "0.8 km", "123 Main Street", "5551234567", true, "24/7"),
        Pharmacy("2", "MediCare Pharmacy", "1.5 km", "456 Oak Avenue", "5552345678", true, "Regular"),
        Pharmacy("3", "HealthPlus Pharmacy", "2.2 km", "789 Park Road", "5553456789", true, "24/7"),
        Pharmacy("4", "QuickCare Pharmacy", "3.0 km", "321 Hill Street", "5554567890", false, "Regular")
    )

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
                    Column {
                        Text(
                            text = "Nearby Pharmacies",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pharmacy stores near you",
                            fontSize = 15.sp,
                            color = Color(0xFF4A5568),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            /* ---------- PHARMACY LIST ---------- */
            items(pharmacies) { pharmacy ->
                PharmacyCard(
                    pharmacy = pharmacy,
                    isSelected = selectedPharmacyId == pharmacy.id,
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${pharmacy.phone}")
                        context.startActivity(intent)
                    },
                    onDirections = {
                        selectedPharmacyId = if (selectedPharmacyId == pharmacy.id) null else pharmacy.id
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun PharmacyCard(
    pharmacy: Pharmacy,
    isSelected: Boolean,
    onCall: () -> Unit,
    onDirections: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            // Pharmacy Name
            Text(
                text = pharmacy.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            
            // Type
            Text(
                text = pharmacy.type,
                fontSize = 15.sp,
                color = Color(0xFF4A5568),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            // Address
            Text(
                text = pharmacy.address,
                fontSize = 14.sp,
                color = Color(0xFF718096),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Distance and Status Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pharmacy.distance,
                        fontSize = 15.sp,
                        color = Color(0xFF2D3748),
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = if (pharmacy.openNow) "Open Now" else "Closed",
                    fontSize = 15.sp,
                    color = if (pharmacy.openNow) Color(0xFF2D7A46) else Color(0xFFE53935),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onCall,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFAEE4C1),
                        contentColor = Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Call",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onDirections,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFEAD6),
                        contentColor = Color(0xFF2D3748)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Directions",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

