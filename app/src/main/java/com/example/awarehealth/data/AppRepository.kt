package com.example.awarehealth.data

class AppRepository(private val apiService: ApiService?) {
    suspend fun register(request: RegisterRequest) = apiService?.register(request)
    suspend fun login(request: LoginRequest) = apiService?.login(request)
    suspend fun doctorLogin(request: DoctorLoginRequest) = apiService?.doctorLogin(request)
    suspend fun getDoctors() = apiService?.getDoctors()
    suspend fun createAppointment(request: CreateAppointmentRequest) = apiService?.createAppointment(request)
    suspend fun getAppointments(userId: String) = apiService?.getAppointments(userId)
    suspend fun sendChatMessage(request: ChatMessageRequest) = apiService?.sendChatMessage(request)
    suspend fun getDiseases(category: String? = null, search: String? = null) = apiService?.getDiseases(category, search)
    suspend fun getDisease(id: String) = apiService?.getDisease(id)
    suspend fun forgotPassword(request: ForgotPasswordRequest) = apiService?.forgotPassword(request)
    suspend fun verifyOTP(request: VerifyOTPRequest) = apiService?.verifyOTP(request)
    suspend fun resetPassword(request: ResetPasswordRequest) = apiService?.resetPassword(request)
    suspend fun googleSignIn(request: GoogleSignInRequest) = apiService?.googleSignIn(request)
}
