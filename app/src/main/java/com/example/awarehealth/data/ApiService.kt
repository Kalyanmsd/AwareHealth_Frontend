package com.example.awarehealth.data

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ApiResponse>
    
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>
    
    // Doctors
    @GET("doctors")
    suspend fun getDoctors(): Response<DoctorsResponse>
    
    @GET("doctors/{id}")
    suspend fun getDoctor(@Path("id") id: String): Response<DoctorResponse>
    
    // Appointments
    @GET("appointments")
    suspend fun getAppointments(@Query("userId") userId: String): Response<AppointmentsResponse>
    
    @POST("appointments")
    suspend fun createAppointment(@Body request: CreateAppointmentRequest): Response<AppointmentResponse>
    
    @PUT("appointments/{id}")
    suspend fun updateAppointment(@Path("id") id: String, @Body request: UpdateAppointmentRequest): Response<AppointmentResponse>
    
    // Chatbot
    @POST("chatbot/message")
    suspend fun sendChatMessage(@Body request: ChatMessageRequest): Response<ChatMessageResponse>
    
    // Health Info
    @GET("diseases")
    suspend fun getDiseases(): Response<DiseasesResponse>
    
    @GET("diseases/{id}")
    suspend fun getDisease(@Path("id") id: String): Response<DiseaseResponse>
}

// Request Models
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val userType: String
)

data class LoginRequest(
    val email: String,
    val password: String,
    val userType: String
)

data class ForgotPasswordRequest(val email: String)

data class ResetPasswordRequest(val email: String, val newPassword: String)

data class CreateAppointmentRequest(
    val patientId: String,
    val doctorId: String,
    val date: String,
    val time: String,
    val symptoms: String
)

data class UpdateAppointmentRequest(val status: String)

data class ChatMessageRequest(val message: String, val conversationId: String?)

// Response Models
data class AuthResponse(
    val success: Boolean,
    val token: String?,
    val user: UserData?,
    val message: String?
)

data class ApiResponse(val success: Boolean, val message: String?, val error: String?)

data class DoctorsResponse(val success: Boolean, val doctors: List<DoctorData>)

data class DoctorResponse(val success: Boolean, val doctor: DoctorData)

data class AppointmentsResponse(val success: Boolean, val appointments: List<AppointmentData>)

data class AppointmentResponse(val success: Boolean, val appointment: AppointmentData)

data class ChatMessageResponse(
    val success: Boolean,
    val response: String,
    val conversationId: String
)

data class DiseasesResponse(val success: Boolean, val diseases: List<DiseaseData>)

data class DiseaseResponse(val success: Boolean, val disease: DiseaseData)

// Data Models
data class UserData(val id: String, val name: String, val email: String, val userType: String)

data class DoctorData(
    val id: String,
    val name: String,
    val specialty: String,
    val experience: String,
    val rating: Double,
    val availability: String,
    val location: String
)

data class AppointmentData(
    val id: String,
    val patientId: String,
    val doctorId: String,
    val date: String,
    val time: String,
    val symptoms: String,
    val status: String
)

data class DiseaseData(val id: String, val name: String, val description: String)



