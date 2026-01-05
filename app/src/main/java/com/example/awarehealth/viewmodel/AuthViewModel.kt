package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.LoginRequest
import com.example.awarehealth.data.RegisterRequest
import com.example.awarehealth.data.AuthResponse
import com.example.awarehealth.data.ForgotPasswordRequest
import com.example.awarehealth.data.VerifyOTPRequest
import com.example.awarehealth.data.ResetPasswordRequest
import com.example.awarehealth.data.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val user: com.example.awarehealth.data.UserData? = null,
    val token: String? = null
)

class AuthViewModel(private val repository: AppRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    /**
     * Login user with email and password
     */
    fun login(
        email: String,
        password: String,
        userType: String,
        onSuccess: (com.example.awarehealth.data.UserData) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                isSuccess = false
            )
            
            try {
                val request = LoginRequest(
                    email = email.trim(),
                    password = password,
                    userType = userType
                )
                
                val response = repository.login(request)
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val authResponse = response.body()!!
                    
                    if (authResponse.success && authResponse.user != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            user = authResponse.user,
                            token = authResponse.token,
                            error = null
                        )
                        onSuccess(authResponse.user)
                    } else {
                        val errorMsg = authResponse.message ?: "Login failed"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg,
                            isSuccess = false
                        )
                        onError(errorMsg)
                    }
                } else {
                    // Handle error response - parse JSON error body
                    val errorMessage = try {
                        val errorBody = response?.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty()) {
                            try {
                                val gson = Gson()
                                val errorJson = gson.fromJson(errorBody, Map::class.java)
                                (errorJson["message"] as? String) ?: "Invalid username and password"
                            } catch (e: Exception) {
                                "Invalid username and password"
                            }
                        } else {
                            "Invalid username and password"
                        }
                    } catch (e: Exception) {
                        "Invalid username and password"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage,
                        isSuccess = false
                    )
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                // Extract IP from BASE_URL for error messages
                val baseUrl = RetrofitClient.BASE_URL_PUBLIC
                val testUrl = baseUrl.replace("/api/", "/api/test_connection.php")
                
                val errorMsg = when {
                    e.message?.contains("Failed to connect", ignoreCase = true) == true ||
                    e.message?.contains("Connection refused", ignoreCase = true) == true ||
                    e.message?.contains("Connection timed out", ignoreCase = true) == true ||
                    e.message?.contains("timeout", ignoreCase = true) == true ||
                    e.message?.contains("Unable to connect", ignoreCase = true) == true ||
                    e is java.net.ConnectException ||
                    e is java.net.SocketTimeoutException ->
                        "Cannot connect to server.\n\nPlease check:\n1. XAMPP Apache is running\n2. Phone and computer on same Wi-Fi\n3. Firewall allows port 80\n4. Test: $testUrl in phone browser"
                    e.message?.contains("Unable to resolve host", ignoreCase = true) == true ||
                    e.message?.contains("No address associated with hostname", ignoreCase = true) == true ->
                        "Cannot find server.\n\nCheck:\n1. IP address in BASE_URL: $baseUrl\n2. Phone and computer on same Wi-Fi network\n3. XAMPP Apache is running"
                    else -> {
                        val baseMsg = e.message ?: "Unable to connect to server"
                        "Connection error.\n\n$baseMsg\n\nTroubleshooting:\n1. Check XAMPP Apache is running (green)\n2. Same Wi-Fi network\n3. Test server in browser: $testUrl"
                    }
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMsg,
                    isSuccess = false
                )
                onError(errorMsg)
            }
        }
    }
    
    /**
     * Register new user
     */
    fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        userType: String,
        onSuccess: (com.example.awarehealth.data.UserData) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                isSuccess = false
            )
            
            try {
                val request = RegisterRequest(
                    name = name.trim(),
                    email = email.trim(),
                    password = password,
                    phone = phone.trim(),
                    userType = userType
                )
                
                val response = repository.register(request)
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val authResponse = response.body()!!
                    
                    if (authResponse.success && authResponse.user != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            user = authResponse.user,
                            token = authResponse.token,
                            error = null
                        )
                        onSuccess(authResponse.user)
                    } else {
                        val errorMsg = authResponse.message ?: "Registration failed"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg,
                            isSuccess = false
                        )
                        onError(errorMsg)
                    }
                } else {
                    // Handle error response - parse JSON error body
                    val errorMessage = try {
                        val errorBody = response?.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty()) {
                            try {
                                val gson = Gson()
                                val errorJson = gson.fromJson(errorBody, Map::class.java)
                                val message = errorJson["message"] as? String ?: ""
                                
                                // Check if it's a duplicate email error
                                if (message.contains("already", ignoreCase = true) || 
                                    message.contains("exists", ignoreCase = true) ||
                                    message.contains("registered", ignoreCase = true)) {
                                    "User already exists"
                                } else {
                                    message.ifEmpty { "Registration failed. Please try again." }
                                }
                            } catch (e: Exception) {
                                "Registration failed. Please try again."
                            }
                        } else {
                            "Registration failed. Please try again."
                        }
                    } catch (e: Exception) {
                        "Registration failed. Please try again."
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage,
                        isSuccess = false
                    )
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                // Extract IP from BASE_URL for error messages
                val baseUrl = RetrofitClient.BASE_URL_PUBLIC
                val testUrl = baseUrl.replace("/api/", "/api/test_connection.php")
                
                val errorMsg = when {
                    e.message?.contains("Failed to connect", ignoreCase = true) == true ||
                    e.message?.contains("Connection refused", ignoreCase = true) == true ||
                    e.message?.contains("Connection timed out", ignoreCase = true) == true ||
                    e.message?.contains("timeout", ignoreCase = true) == true ||
                    e.message?.contains("Unable to connect", ignoreCase = true) == true ||
                    e is java.net.ConnectException ||
                    e is java.net.SocketTimeoutException ->
                        "Cannot connect to server.\n\nPlease check:\n1. XAMPP Apache is running\n2. Phone and computer on same Wi-Fi\n3. Firewall allows port 80\n4. Test: $testUrl in phone browser"
                    e.message?.contains("Unable to resolve host", ignoreCase = true) == true ||
                    e.message?.contains("No address associated with hostname", ignoreCase = true) == true ->
                        "Cannot find server.\n\nCheck:\n1. IP address in BASE_URL: $baseUrl\n2. Phone and computer on same Wi-Fi network\n3. XAMPP Apache is running"
                    else -> {
                        val baseMsg = e.message ?: "Unable to connect to server"
                        "Connection error.\n\n$baseMsg\n\nTroubleshooting:\n1. Check XAMPP Apache is running\n2. Same Wi-Fi network\n3. Test: $testUrl"
                    }
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMsg,
                    isSuccess = false
                )
                onError(errorMsg)
            }
        }
    }
    
    /**
     * Forgot password - send OTP email
     */
    fun forgotPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                // Normalize email to lowercase for consistent storage
                val normalizedEmail = email.trim().lowercase()
                val request = ForgotPasswordRequest(normalizedEmail)
                val response = repository.forgotPassword(request)
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                        onSuccess()
                    } else {
                        val errorMsg = apiResponse.message ?: "Failed to send OTP email"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg
                        )
                        onError(errorMsg)
                    }
                } else {
                    val errorBody = response?.errorBody()?.string()
                    val errorMsg = errorBody ?: "Failed to send OTP email"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                // Extract IP from BASE_URL for error messages
                val baseUrl = RetrofitClient.BASE_URL_PUBLIC
                val testUrl = baseUrl.replace("/api/", "/api/test_connection.php")
                
                val errorMsg = when {
                    e.message?.contains("Failed to connect", ignoreCase = true) == true || 
                    e.message?.contains("Connection refused", ignoreCase = true) == true ||
                    e is java.net.ConnectException -> 
                        "Cannot connect to server.\n\nCheck:\n1. XAMPP Apache is running\n2. Same Wi-Fi network\n3. Test: $testUrl"
                    e.message?.contains("timeout", ignoreCase = true) == true || 
                    e is java.net.SocketTimeoutException -> 
                        "Connection timeout.\n\nCheck:\n1. XAMPP Apache is running (GREEN)\n2. Firewall allows Apache\n3. Test in browser: $testUrl"
                    e.message?.contains("Unable to resolve host", ignoreCase = true) == true ||
                    e.message?.contains("No address associated with hostname", ignoreCase = true) == true -> 
                        "Cannot find server.\n\nCheck:\n1. IP address: $baseUrl\n2. Phone and computer on same Wi-Fi?"
                    else -> "Network error: ${e.message ?: "Unable to connect"}\n\nCheck XAMPP Apache is running\nTest: $testUrl"
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
                onError(errorMsg)
            }
        }
    }
    
    /**
     * Verify OTP
     */
    fun verifyOTP(
        email: String,
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
        viewModelScope.launch {
            try {
                // Normalize email to lowercase for consistent comparison
                val normalizedEmail = email.trim().lowercase()
                val normalizedOTP = otp.trim()
                val request = VerifyOTPRequest(normalizedEmail, normalizedOTP)
                val response = repository.verifyOTP(request)

                if (response?.isSuccessful == true && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null
                        )
                        onSuccess()
                    } else {
                        val errorMsg = apiResponse.message ?: "Invalid or expired OTP"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg,
                            isSuccess = false
                        )
                        onError(errorMsg)
                    }
                } else {
                    // Parse error response
                    val errorMessage = try {
                        val errorBody = response?.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty()) {
                            try {
                                val gson = Gson()
                                val errorJson = gson.fromJson(errorBody, Map::class.java)
                                (errorJson["message"] as? String) ?: "Invalid or expired OTP"
                            } catch (e: Exception) {
                                "Invalid or expired OTP"
                            }
                        } else {
                            "Invalid or expired OTP"
                        }
                    } catch (e: Exception) {
                        "Invalid or expired OTP"
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage,
                        isSuccess = false
                    )
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMsg = "Network error: ${e.message ?: "Unable to connect to server"}"
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMsg,
                    isSuccess = false
                )
                onError(errorMsg)
            }
        }
    }
    
    /**
     * Reset password with OTP
     */
    fun resetPassword(
        email: String,
        otp: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val request = ResetPasswordRequest(email.trim(), otp.trim(), newPassword)
                val response = repository.resetPassword(request)
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                        onSuccess()
                    } else {
                        val errorMsg = apiResponse.message ?: "Failed to reset password"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg
                        )
                        onError(errorMsg)
                    }
                } else {
                    val errorBody = response?.errorBody()?.string()
                    val errorMsg = errorBody ?: "Failed to reset password"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Network error: ${e.message ?: "Unable to connect"}"
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
                onError(errorMsg)
            }
        }
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Reset state
     */
    fun resetState() {
        _uiState.value = AuthUiState()
    }
}

