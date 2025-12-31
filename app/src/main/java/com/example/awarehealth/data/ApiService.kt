package com.example.awarehealth.data

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/doctor-login")
    suspend fun doctorLogin(@Body request: DoctorLoginRequest): Response<AuthResponse>
    
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ApiResponse>
    
    @POST("auth/verify-otp")
    suspend fun verifyOTP(@Body request: VerifyOTPRequest): Response<ApiResponse>
    
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>
    
    // OTP Login System (new otp_verification table)
    @POST("send_otp")
    suspend fun sendOTP(@Body request: SendOTPRequest): Response<OTPResponse>
    
    @POST("verify_otp")
    suspend fun verifyOTPLogin(@Body request: VerifyOTPLoginRequest): Response<OTPResponse>
    
    @POST("resend_otp")
    suspend fun resendOTP(@Body request: SendOTPRequest): Response<OTPResponse>
    
    // Appointment Booking System (new endpoints)
    @GET("get_doctors")
    suspend fun getDoctorsList(): Response<GetDoctorsResponse>
    
    @POST("book_appointment")
    suspend fun bookAppointment(@Body request: BookAppointmentRequest): Response<BookAppointmentResponse>
    
    @GET("get_my_appointments")
    suspend fun getMyAppointments(@Query("email") email: String): Response<GetMyAppointmentsResponse>
    
    @POST("auth/google-signin")
    suspend fun googleSignIn(@Body request: GoogleSignInRequest): Response<AuthResponse>
    
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
    @GET("health/diseases")
    suspend fun getDiseases(@Query("category") category: String? = null, @Query("search") search: String? = null): Response<DiseasesResponse>
    
    @GET("health/diseases/{id}")
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

data class DoctorLoginRequest(
    val doctorId: String,
    val password: String
)

data class ForgotPasswordRequest(val email: String)

data class VerifyOTPRequest(val email: String, val otp: String)

data class ResetPasswordRequest(val email: String, val otp: String, val newPassword: String)

data class GoogleSignInRequest(val idToken: String, val userType: String)

// OTP Login System Requests
data class SendOTPRequest(val email: String)

data class VerifyOTPLoginRequest(val email: String, val otp: String)

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

// OTP Response
data class OTPResponse(
    val success: Boolean,
    val message: String?,
    val otp: String? = null, // For testing
    val expires_at: String? = null,
    val inserted_id: Int? = null
)

// Appointment Booking Requests
data class BookAppointmentRequest(
    val user_email: String,
    val doctor_id: Int,
    val appointment_date: String,
    val appointment_time: String
)

// Appointment Booking Responses
data class GetDoctorsResponse(
    val success: Boolean,
    val message: String?,
    val doctors: List<DoctorBookingData>,
    val count: Int?,
    val hospital: String?
)

data class DoctorBookingData(
    val id: Int,
    val name: String,
    val specialization: String,
    val hospital: String,
    val experience: String,
    val available_days: String,
    val available_time: String
)

data class BookAppointmentResponse(
    val success: Boolean,
    val message: String?,
    val appointment: AppointmentBookingData?
)

data class AppointmentBookingData(
    val id: Int,
    val user_email: String,
    val doctor_id: Int,
    val doctor_name: String?,
    val doctor_specialization: String?,
    val appointment_date: String,
    val appointment_time: String,
    val status: String,
    val created_at: String?
)

data class GetMyAppointmentsResponse(
    val success: Boolean,
    val message: String?,
    val appointments: List<AppointmentBookingData>,
    val count: Int?,
    val user_email: String?
)

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

data class DiseaseData(
    val id: String,
    val name: String,
    val category: String? = null,
    val severity: String? = null,
    val emoji: String? = null,
    val description: String,
    val symptoms: List<String>? = null,
    val causes: List<String>? = null,
    val prevention: List<String>? = null,
    val treatment: List<String>? = null,
    val affectedPopulation: String? = null,
    val duration: String? = null
)



