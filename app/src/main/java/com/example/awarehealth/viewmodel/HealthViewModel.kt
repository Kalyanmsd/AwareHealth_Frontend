package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.DiseaseData
import com.example.awarehealth.data.DiseasesResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class HealthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val diseases: List<DiseaseData> = emptyList()
)

class HealthViewModel(private val repository: AppRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()
    
    private var isInitialLoad = true
    
    init {
        // Initial load will be triggered by LaunchedEffect in the screen
    }
    
    fun initialLoad() {
        if (isInitialLoad) {
            isInitialLoad = false
            loadDiseases()
        }
    }
    
    /**
     * Load diseases from API
     */
    fun loadDiseases(category: String? = null, search: String? = null) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                Log.d("HealthViewModel", "Loading diseases - category: $category, search: $search")
                
                val response = repository.getDiseases(
                    category = if (category == "All") null else category,
                    search = if (search.isNullOrBlank()) null else search
                )
                
                Log.d("HealthViewModel", "Response received: ${response?.isSuccessful}, Code: ${response?.code()}")
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val diseasesResponse = response.body()!!
                    Log.d("HealthViewModel", "Success: ${diseasesResponse.success}, Diseases count: ${diseasesResponse.diseases?.size ?: 0}")
                    
                    if (diseasesResponse.success) {
                        val diseases = diseasesResponse.diseases ?: emptyList()
                        Log.d("HealthViewModel", "Loaded ${diseases.size} diseases")
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            diseases = diseases,
                            error = null
                        )
                    } else {
                        Log.e("HealthViewModel", "API returned success=false")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to load diseases"
                        )
                    }
                } else {
                    val errorMessage = try {
                        val errorBody = response?.errorBody()?.string()
                        Log.e("HealthViewModel", "API Error - Code: ${response?.code()}, Body: $errorBody")
                        errorBody ?: "Failed to load diseases (HTTP ${response?.code()})"
                    } catch (e: Exception) {
                        Log.e("HealthViewModel", "Error parsing error body: ${e.message}")
                        "Network error: Unable to connect to server"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            } catch (e: java.net.UnknownHostException) {
                Log.e("HealthViewModel", "Unknown host exception: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Cannot connect to server. Check your network and BASE_URL in RetrofitClient.kt"
                )
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("HealthViewModel", "Timeout exception: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Connection timeout. Check if XAMPP Apache is running and server is accessible."
                )
            } catch (e: java.net.ConnectException) {
                Log.e("HealthViewModel", "Connection exception: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Cannot connect to server. Check if XAMPP Apache is running and IP address is correct."
                )
            } catch (e: Exception) {
                Log.e("HealthViewModel", "Exception loading diseases: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message ?: "Unable to connect to server. Check Logcat for details."}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

