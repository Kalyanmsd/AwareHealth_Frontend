package com.example.awarehealth.data

class AppRepository(private val apiService: ApiService?) {
    suspend fun register(request: RegisterRequest) = apiService?.register(request)
    suspend fun login(request: LoginRequest) = apiService?.login(request)
    suspend fun getDoctors() = apiService?.getDoctors()
    suspend fun createAppointment(request: CreateAppointmentRequest) = apiService?.createAppointment(request)
    suspend fun getAppointments(userId: String) = apiService?.getAppointments(userId)
    suspend fun sendChatMessage(request: ChatMessageRequest) = apiService?.sendChatMessage(request)
    suspend fun getDiseases() = apiService?.getDiseases()
}
