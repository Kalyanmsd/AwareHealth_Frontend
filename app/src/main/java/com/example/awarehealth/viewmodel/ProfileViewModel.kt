package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.UpdateProfileRequest
import com.example.awarehealth.data.UserData
import com.example.awarehealth.data.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val updatedUser: UserData? = null
)

class ProfileViewModel(private val repository: AppRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    /**
     * Update user profile
     */
    fun updateProfile(
        userId: String,
        name: String,
        email: String,
        phone: String,
        onSuccess: (UserData) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                isSuccess = false
            )
            
            try {
                val request = UpdateProfileRequest(
                    userId = userId,
                    name = name.trim(),
                    email = email.trim(),
                    phone = phone.trim()
                )
                
                val response = repository.updateProfile(request)
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val authResponse = response.body()!!
                    
                    if (authResponse.success && authResponse.user != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            updatedUser = authResponse.user,
                            error = null
                        )
                        onSuccess(authResponse.user)
                    } else {
                        val errorMsg = authResponse.message ?: "Failed to update profile"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg,
                            isSuccess = false
                        )
                        onError(errorMsg)
                    }
                } else {
                    // Handle error response
                    val errorMessage = try {
                        val errorBody = response?.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty()) {
                            try {
                                val gson = Gson()
                                val errorJson = gson.fromJson(errorBody, Map::class.java)
                                (errorJson["message"] as? String) ?: "Failed to update profile"
                            } catch (e: Exception) {
                                "Failed to update profile"
                            }
                        } else {
                            "Failed to update profile"
                        }
                    } catch (e: Exception) {
                        "Failed to update profile"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage,
                        isSuccess = false
                    )
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating profile", e)
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
                        "Cannot connect to server.\n\nPlease check:\n1. XAMPP Apache is running\n2. Phone and computer on same Wi-Fi\n3. Test: $testUrl in phone browser"
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
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Reset state
     */
    fun resetState() {
        _uiState.value = ProfileUiState()
    }
}

