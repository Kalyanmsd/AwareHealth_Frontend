package com.example.awarehealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.example.awarehealth.navigation.AwareHealthNavGraph
import com.example.awarehealth.ui.theme.AwareHealthTheme
import com.example.awarehealth.data.RetrofitClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize API service
        val apiService = RetrofitClient.apiService

        setContent {
            AwareHealthTheme {
                Scaffold {
                    AwareHealthNavGraph(apiService = apiService)
                }
            }
        }
    }
}
