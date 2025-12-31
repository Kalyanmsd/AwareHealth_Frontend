package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.DiseaseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class DiseaseDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val disease: DiseaseData? = null
)

class DiseaseDetailsViewModel(
    private val repository: AppRepository,
    private val diseaseId: String
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DiseaseDetailsUiState())
    val uiState: StateFlow<DiseaseDetailsUiState> = _uiState.asStateFlow()
    
    init {
        loadDisease()
    }
    
    /**
     * Load disease details from API
     */
    private fun loadDisease() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                Log.d("DiseaseDetailsViewModel", "Loading disease with ID: $diseaseId")
                
                val response = repository.getDisease(diseaseId)
                
                Log.d("DiseaseDetailsViewModel", "Response received: ${response?.isSuccessful}, Code: ${response?.code()}")
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val diseaseResponse = response.body()!!
                    Log.d("DiseaseDetailsViewModel", "Success: ${diseaseResponse.success}")
                    
                    if (diseaseResponse.success && diseaseResponse.disease != null) {
                        Log.d("DiseaseDetailsViewModel", "Loaded disease: ${diseaseResponse.disease.name}")
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            disease = diseaseResponse.disease,
                            error = null
                        )
                    } else {
                        Log.e("DiseaseDetailsViewModel", "API returned success=false or disease is null")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Disease not found"
                        )
                    }
                } else {
                    val errorMessage = try {
                        val errorBody = response?.errorBody()?.string()
                        Log.e("DiseaseDetailsViewModel", "API Error - Code: ${response?.code()}, Body: $errorBody")
                        errorBody ?: "Failed to load disease (HTTP ${response?.code()})"
                    } catch (e: Exception) {
                        Log.e("DiseaseDetailsViewModel", "Error parsing error body: ${e.message}")
                        "Network error: Unable to connect to server"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            } catch (e: java.net.UnknownHostException) {
                Log.e("DiseaseDetailsViewModel", "Unknown host exception: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Cannot connect to server. Check your network and BASE_URL in RetrofitClient.kt"
                )
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("DiseaseDetailsViewModel", "Timeout exception: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Connection timeout. Check if XAMPP Apache is running and server is accessible."
                )
            } catch (e: java.net.ConnectException) {
                Log.e("DiseaseDetailsViewModel", "Connection exception: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Cannot connect to server. Check if XAMPP Apache is running and IP address is correct."
                )
            } catch (e: Exception) {
                Log.e("DiseaseDetailsViewModel", "Exception loading disease: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message ?: "Unable to connect to server. Check Logcat for details."}"
                )
            }
        }
    }
    
    fun retry() {
        loadDisease()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

