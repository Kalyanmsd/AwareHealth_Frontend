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
                
                // Use the new get_doctors endpoint that filters by status = 'Available'
                val response = repository.getDoctorsList()
                
                Log.d("DoctorsViewModel", "Response received: ${response?.isSuccessful}, Code: ${response?.code()}")
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val doctorsResponse = response.body()!!
                    Log.d("DoctorsViewModel", "Success: ${doctorsResponse.success}, Doctors count: ${doctorsResponse.doctors?.size ?: 0}")
                    
                    if (doctorsResponse.success) {
                        val doctorsBookingData = doctorsResponse.doctors ?: emptyList()
                        Log.d("DoctorsViewModel", "Loaded ${doctorsBookingData.size} available doctors")
                        
                        // Convert DoctorBookingData to DoctorData for UI
                        val doctors = doctorsBookingData.map { bookingData ->
                            DoctorData(
                                id = bookingData.id.toString(),
                                name = bookingData.name,
                                specialty = bookingData.specialization,
                                experience = bookingData.experience,
                                rating = 4.5, // Default rating
                                availability = "${bookingData.available_days} - ${bookingData.available_time}",
                                location = bookingData.hospital
                            )
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            doctors = doctors,
                            error = null
                        )
                    } else {
                        Log.e("DoctorsViewModel", "API returned success=false: ${doctorsResponse.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = doctorsResponse.message ?: "Failed to load doctors"
                        )
                    }
                } else {
                    val errorBody = response?.errorBody()?.string()
                    val statusCode = response?.code()
                    Log.e("DoctorsViewModel", "API call failed. Code: $statusCode, Error: $errorBody")
                    
                    // Try to parse error message from JSON
                    val errorMessage = try {
                        if (!errorBody.isNullOrEmpty()) {
                            val gson = com.google.gson.Gson()
                            val errorJson = gson.fromJson(errorBody, Map::class.java)
                            val message = errorJson["message"] as? String
                            val note = errorJson["note"] as? String
                            if (!note.isNullOrEmpty()) {
                                "$message\n\n$note"
                            } else {
                                message ?: "Failed to load doctors"
                            }
                        } else {
                            when (statusCode) {
                                404 -> "API endpoint not found. Check server configuration."
                                500 -> "Server error. Check database connection and doctors table."
                                else -> "Failed to load doctors. Server returned code: $statusCode"
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("DoctorsViewModel", "Error parsing error response", e)
                        "Failed to load doctors. Please check:\n1. XAMPP Apache is running\n2. Doctors table exists\n3. Doctors have status='Available'"
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

