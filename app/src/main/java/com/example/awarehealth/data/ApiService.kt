package com.example.awarehealth.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * PHP API Service Interface
 * Connects to PHP backend API running on XAMPP
 */
interface ApiService {
    
    // ========== Authentication Endpoints ==========
    
    @POST("auth.php/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth.php/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth.php/doctor-login")
    suspend fun doctorLogin(@Body request: DoctorLoginRequest): Response<AuthResponse>
    
    @POST("auth.php/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ApiResponse>
    
    @POST("auth.php/verify-otp")
    suspend fun verifyOTP(@Body request: VerifyOTPRequest): Response<ApiResponse>
    
    @POST("auth.php/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>
    
    @POST("auth.php/google-signin")
    suspend fun googleSignIn(@Body request: GoogleSignInRequest): Response<AuthResponse>
    
    @POST("auth.php/update-profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<AuthResponse>
    
    // ========== OTP Login Endpoints ==========
    
    @POST("send_otp.php")
    suspend fun sendOTP(@Body request: SendOTPRequest): Response<ApiResponse>
    
    @POST("verify_otp.php")
    suspend fun verifyOTPLogin(@Body request: VerifyOTPLoginRequest): Response<AuthResponse>
    
    @POST("resend_otp.php")
    suspend fun resendOTP(@Body request: SendOTPRequest): Response<ApiResponse>
    
    // ========== Doctor Endpoints ==========
    
    @GET("doctors.php")
    suspend fun getDoctors(): Response<DoctorsResponse>
    
    @GET("get_doctors.php")
    suspend fun getDoctorsList(): Response<DoctorsResponse>
    
    // ========== Appointment Endpoints ==========
    
    @POST("appointments.php")
    suspend fun createAppointment(@Body request: CreateAppointmentRequest): Response<ApiResponse>
    
    @GET("appointments.php")
    suspend fun getAppointments(@Query("userId") userId: String): Response<AppointmentsResponse>
    
    @POST("book_appointment.php")
    suspend fun bookAppointment(@Body request: BookAppointmentRequest): Response<ApiResponse>
    
    @GET("get_my_appointments.php")
    suspend fun getMyAppointments(@Query("email") email: String): Response<AppointmentsResponse>
    
    @GET("get_doctor_appointments.php")
    suspend fun getDoctorAppointments(@Query("doctor_id") doctorId: String): Response<AppointmentsResponse>
    
    // ========== Chat Endpoint ==========
    
    @POST("chatbot.php")
    suspend fun sendChatMessage(@Body request: ChatMessageRequest): Response<ChatMessageResponse>
    
    // ========== Disease Endpoints ==========
    
    @GET("simple_diseases.php")
    suspend fun getDiseases(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null
    ): Response<DiseasesResponse>
    
    @GET("simple_diseases.php")
    suspend fun getDisease(@Query("id") id: String): Response<DiseaseResponse>
}

// ========== Request Models ==========

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val userType: String,
    val phone: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String,
    val userType: String
)

data class DoctorLoginRequest(
    val doctorId: String,
    val password: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class VerifyOTPRequest(
    val email: String,
    val otp: String
)

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)

data class GoogleSignInRequest(
    val idToken: String,
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null
)

data class UpdateProfileRequest(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String
)

data class SendOTPRequest(
    val email: String
)

data class VerifyOTPLoginRequest(
    val email: String,
    val otp: String
)

data class CreateAppointmentRequest(
    val userId: String,
    val doctorId: String,
    val date: String,
    val time: String,
    val reason: String? = null
)

data class BookAppointmentRequest(
    val patientEmail: String,
    val doctorId: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val reason: String? = null,
    val patientName: String? = null,
    val patientPhone: String? = null
)

data class ChatMessageRequest(
    val message: String,
    val conversationId: String? = null
)

// ========== Response Models ==========

data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val user: UserData? = null,
    val token: String? = null
)

data class UserData(
    val id: String,
    val name: String,
    val email: String,
    val userType: String,
    val phone: String? = null,
    val photoUrl: String? = null,
    val doctorId: String? = null  // For doctors - stores their doctor_id from doctors table
)

data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val appointment: BookedAppointmentData? = null
)

data class BookedAppointmentData(
    val id: Int,
    @com.google.gson.annotations.SerializedName("user_email")
    val user_email: String? = null,
    @com.google.gson.annotations.SerializedName("doctor_id")
    val doctor_id: Int,
    @com.google.gson.annotations.SerializedName("doctor_name")
    val doctor_name: String? = null,
    @com.google.gson.annotations.SerializedName("doctor_specialization")
    val doctor_specialization: String? = null,
    val location: String? = null,
    @com.google.gson.annotations.SerializedName("appointment_date")
    val appointment_date: String,
    @com.google.gson.annotations.SerializedName("appointment_time")
    val appointment_time: String,
    @com.google.gson.annotations.SerializedName("appointment_time_display")
    val appointment_time_display: String? = null,
    val status: String? = null,
    @com.google.gson.annotations.SerializedName("created_at")
    val created_at: String? = null
)

data class DoctorsResponse(
    val success: Boolean,
    val message: String? = null,
    val doctors: List<DoctorData>? = null
)

data class DoctorData(
    val id: String,
    val doctorId: String? = null,
    val name: String,
    val specialty: String? = null,
    val experience: String? = null,
    val rating: Double? = null,
    val availability: String? = null,
    val location: String? = null,
    val status: String? = null,
    val email: String? = null,
    val phone: String? = null
)

data class AppointmentsResponse(
    val success: Boolean,
    val message: String? = null,
    val appointments: List<AppointmentData>? = null
)

data class AppointmentData(
    val id: String,
    val patientId: String? = null,
    val patientEmail: String? = null,
    val patientName: String? = null,
    val doctorId: String,
    val doctorName: String? = null,
    val date: String,
    val time: String,
    val reason: String? = null,
    val status: String? = null
)

data class ChatMessageResponse(
    val success: Boolean,
    val response: String,
    val conversationId: String = ""
)

data class DiseasesResponse(
    val success: Boolean,
    val message: String? = null,
    val diseases: List<DiseaseData>? = null
)

data class DiseaseResponse(
    val success: Boolean,
    val message: String? = null,
    val disease: DiseaseData? = null
)

data class DiseaseData(
    val id: String,
    val name: String,
    val category: String? = null,
    val description: String? = null,
    val symptoms: List<String>? = null,
    val prevention: List<String>? = null,
    val treatment: List<String>? = null,
    val imageUrl: String? = null,
    val emoji: String? = null,
    val severity: String? = null,
    val causes: List<String>? = null,
    @com.google.gson.annotations.SerializedName("affectedPopulation")
    val affectedPopulation: String? = null,
    val duration: String? = null
)

