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
import com.google.gson.Gson
import com.google.gson.JsonObject

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
        if (diseaseId.isNotEmpty()) {
            loadDisease()
        } else {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Invalid disease ID"
            )
        }
    }
    
    /**
     * Load disease details from API
     */
    private fun loadDisease() {
        if (diseaseId.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Invalid disease ID"
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                Log.d("DiseaseDetailsViewModel", "=== Loading disease ===")
                Log.d("DiseaseDetailsViewModel", "Disease ID: $diseaseId")
                Log.d("DiseaseDetailsViewModel", "Repository available: ${repository != null}")
                
                val response = repository.getDisease(diseaseId)
                
                Log.d("DiseaseDetailsViewModel", "=== API Response ===")
                Log.d("DiseaseDetailsViewModel", "Response received: ${response != null}")
                if (response != null) {
                    Log.d("DiseaseDetailsViewModel", "Response successful: ${response.isSuccessful}")
                    Log.d("DiseaseDetailsViewModel", "Response code: ${response.code()}")
                    Log.d("DiseaseDetailsViewModel", "Response body exists: ${response.body() != null}")
                    if (response.body() != null) {
                        Log.d("DiseaseDetailsViewModel", "Response success flag: ${response.body()!!.success}")
                        Log.d("DiseaseDetailsViewModel", "Disease in response: ${response.body()!!.disease != null}")
                    }
                }
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val diseaseResponse = response.body()!!
                    Log.d("DiseaseDetailsViewModel", "Success: ${diseaseResponse.success}")
                    
                    if (diseaseResponse.success && diseaseResponse.disease != null) {
                        val disease = diseaseResponse.disease
                        Log.d("DiseaseDetailsViewModel", "Loaded disease: ${disease.name}")
                        Log.d("DiseaseDetailsViewModel", "Disease ID: ${disease.id}")
                        Log.d("DiseaseDetailsViewModel", "Symptoms count: ${disease.symptoms?.size ?: 0}")
                        Log.d("DiseaseDetailsViewModel", "Prevention count: ${disease.prevention?.size ?: 0}")
                        Log.d("DiseaseDetailsViewModel", "Causes count: ${disease.causes?.size ?: 0}")
                        Log.d("DiseaseDetailsViewModel", "Treatment count: ${disease.treatment?.size ?: 0}")
                        Log.d("DiseaseDetailsViewModel", "Symptoms: ${disease.symptoms}")
                        Log.d("DiseaseDetailsViewModel", "Prevention: ${disease.prevention}")
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            disease = disease,
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
                        val errorBodyString = response?.errorBody()?.string()
                        Log.e("DiseaseDetailsViewModel", "API Error - Code: ${response?.code()}, Body: $errorBodyString")
                        
                        // Try to parse JSON error response
                        if (errorBodyString != null && errorBodyString.startsWith("{")) {
                            try {
                                val errorJson = Gson().fromJson(
                                    errorBodyString,
                                    JsonObject::class.java
                                )
                                val message = errorJson.get("message")?.asString
                                val debug = errorJson.get("debug")
                                
                                if (debug != null && debug.isJsonObject) {
                                    val debugObj = debug.asJsonObject
                                    val searchedId = debugObj.get("searched_id")?.asString
                                    val totalDiseases = debugObj.get("total_diseases")?.asInt
                                    val sampleIds = debugObj.get("sample_ids")
                                    
                                    Log.e("DiseaseDetailsViewModel", "Debug Info - Searched ID: $searchedId, Total Diseases: $totalDiseases")
                                    if (sampleIds != null && sampleIds.isJsonArray) {
                                        Log.e("DiseaseDetailsViewModel", "Sample IDs in DB: ${sampleIds.toString()}")
                                    }
                                }
                                
                                message ?: "Disease not found. Check Logcat for details."
                            } catch (e: Exception) {
                                Log.e("DiseaseDetailsViewModel", "Error parsing JSON error: ${e.message}")
                                errorBodyString
                            }
                        } else {
                            errorBodyString ?: "Failed to load disease (HTTP ${response?.code()})"
                        }
                    } catch (e: Exception) {
                        Log.e("DiseaseDetailsViewModel", "Error parsing error body: ${e.message}")
                        "Failed to load disease. HTTP ${response?.code()}. Check Logcat for details."
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
        if (diseaseId.isNotEmpty()) {
            loadDisease()
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

