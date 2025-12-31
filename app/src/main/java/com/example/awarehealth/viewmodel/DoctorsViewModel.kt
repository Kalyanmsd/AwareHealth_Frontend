package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.DoctorData
import com.example.awarehealth.data.DoctorsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class DoctorsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val doctors: List<DoctorData> = emptyList()
)

class DoctorsViewModel(private val repository: AppRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DoctorsUiState())
    val uiState: StateFlow<DoctorsUiState> = _uiState.asStateFlow()
    
    private var isInitialLoad = true
    
    fun initialLoad() {
        if (isInitialLoad) {
            isInitialLoad = false
            loadDoctors()
        }
    }
    
    /**
     * Load doctors from API (Saveetha Hospital doctors)
     */
    fun loadDoctors() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                Log.d("DoctorsViewModel", "Loading doctors from API...")
                
                val response = repository.getDoctors()
                
                Log.d("DoctorsViewModel", "Response received: ${response?.isSuccessful}, Code: ${response?.code()}")
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val doctorsResponse = response.body()!!
                    Log.d("DoctorsViewModel", "Success: ${doctorsResponse.success}, Doctors count: ${doctorsResponse.doctors?.size ?: 0}")
                    
                    if (doctorsResponse.success) {
                        val doctors = doctorsResponse.doctors ?: emptyList()
                        Log.d("DoctorsViewModel", "Loaded ${doctors.size} doctors from Saveetha Hospital")
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            doctors = doctors,
                            error = null
                        )
                    } else {
                        Log.e("DoctorsViewModel", "API returned success=false")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to load doctors"
                        )
                    }
                } else {
                    val errorBody = response?.errorBody()?.string()
                    Log.e("DoctorsViewModel", "API call failed. Code: ${response?.code()}, Error: $errorBody")
                    
                    // Try to parse error message from JSON
                    val errorMessage = try {
                        if (!errorBody.isNullOrEmpty()) {
                            val gson = com.google.gson.Gson()
                            val errorJson = gson.fromJson(errorBody, Map::class.java)
                            (errorJson["message"] as? String) ?: "Failed to load doctors"
                        } else {
                            "Failed to load doctors. Server returned code: ${response?.code()}"
                        }
                    } catch (e: Exception) {
                        "Failed to load doctors. Please check your connection and ensure doctors are set up."
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            } catch (e: Exception) {
                Log.e("DoctorsViewModel", "Exception loading doctors", e)
                val errorMsg = when {
                    e.message?.contains("Failed to connect") == true -> 
                        "Cannot connect to server. Check:\n1. XAMPP Apache is running\n2. Same Wi-Fi network\n3. IP: 172.20.10.2"
                    e.message?.contains("timeout") == true -> 
                        "Connection timeout. Server may be slow."
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Cannot find server. Check IP: 172.20.10.2"
                    else -> "Network error: ${e.message ?: "Unable to connect to server"}"
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
            }
        }
    }
    
    /**
     * Refresh doctors list
     */
    fun refresh() {
        loadDoctors()
    }
}

