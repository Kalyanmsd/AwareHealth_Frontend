package com.example.awarehealth.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Flask AI API Service for Symptom Checker
 * Connects to Python Flask API running on port 5000
 */
interface FlaskApiService {
    /**
     * Health check endpoint
     */
    @GET("health")
    suspend fun checkHealth(): Response<FlaskHealthResponse>
    
    /**
     * Chat endpoint for symptom checking
     * Analyzes symptoms and returns risk assessment
     */
    @POST("chat")
    suspend fun checkSymptoms(@Body request: SymptomRequest): Response<SymptomResponse>
}

/**
 * Request model for symptom checking
 */
data class SymptomRequest(
    val message: String,
    val conversation_id: String? = null // Optional conversation ID for state tracking
)

/**
 * Response model from disease information chatbot
 */
data class SymptomResponse(
    val success: Boolean,
    val conversation_state: String? = null, // "waiting_for_disease", "asking_prevention", "asking_days", "completed"
    val disease_name: String? = null, // Name of the disease
    val message: String? = null, // AI response message
    val symptoms: List<String>? = null, // List of disease symptoms
    val prevention_tips: List<String>? = null, // Prevention tips
    val food_recommendations: List<String>? = null, // Food recommendations
    val days_suffering: Int? = null, // Number of days user has been suffering
    val suggest_appointment: Boolean? = null, // Whether to suggest appointment
    val available_diseases: List<String>? = null, // Available diseases if disease not found
    val error: String? = null // Error message if any
)

/**
 * Hospital information for appointment suggestion
 */
data class HospitalInfo(
    val name: String? = null,
    val department: String? = null,
    val timeframe: String? = null,
    val message: String? = null
)

/**
 * Health check response
 */
data class FlaskHealthResponse(
    val status: String,
    val model_status: String? = null,
    val message: String? = null
)

