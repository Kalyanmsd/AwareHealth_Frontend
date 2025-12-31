package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.ChatMessageRequest
import com.example.awarehealth.data.ChatMessageResponse
import com.example.awarehealth.data.SymptomResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class ChatbotUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val conversationId: String? = null,
    val lastSymptomResponse: SymptomResponse? = null, // Store last AI symptom check result
    val symptomConversationId: String? = null, // Track symptom conversation state
    val waitingForDays: Boolean = false // Track if waiting for days input
)

class ChatbotViewModel(private val repository: AppRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState: StateFlow<ChatbotUiState> = _uiState.asStateFlow()
    
    /**
     * Send message to chatbot API
     */
    suspend fun sendMessage(
        message: String,
        conversationId: String? = null
    ): ChatMessageResponse? {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        return try {
            val request = ChatMessageRequest(
                message = message,
                conversationId = conversationId
            )
            
            val response = repository.sendChatMessage(request)
            
            Log.d("ChatbotViewModel", "Response received: ${response?.isSuccessful}, Code: ${response?.code()}")
            
            if (response?.isSuccessful == true && response.body() != null) {
                val chatResponse = response.body()!!
                
                Log.d("ChatbotViewModel", "Response body: success=${chatResponse.success}, response=${chatResponse.response.take(50)}...")
                
                // Check if response indicates success
                if (!chatResponse.success) {
                    val errorMsg = chatResponse.response.ifEmpty { "Server returned an error. Please try again." }
                    Log.e("ChatbotViewModel", "Server returned error: $errorMsg")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                    return null
                }
                
                // Update conversation ID if provided
                val newConversationId = chatResponse.conversationId.ifEmpty { null }
                _uiState.value = _uiState.value.copy(
                    conversationId = newConversationId ?: _uiState.value.conversationId,
                    isLoading = false,
                    error = null
                )
                
                chatResponse
            } else {
                // Handle error response
                val errorMessage = try {
                    val errorBody = response?.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val gson = Gson()
                            val errorJson = gson.fromJson(errorBody, Map::class.java)
                            (errorJson["error"] as? String) ?: errorBody
                        } catch (e: Exception) {
                            errorBody
                        }
                    } else {
                        "Server error (${response?.code() ?: "Unknown"}). Please try again."
                    }
                } catch (e: Exception) {
                    "Network error: ${response?.code() ?: "Unable to connect to server"}"
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                null
            }
        } catch (e: Exception) {
            // Log detailed error for debugging
            Log.e("ChatbotViewModel", "Exception occurred", e)
            Log.e("ChatbotViewModel", "Error message: ${e.message}")
            Log.e("ChatbotViewModel", "Error type: ${e.javaClass.simpleName}")
            
            // Handle network or other exceptions
            val errorMsg = when {
                e.message?.contains("Failed to connect", ignoreCase = true) == true || 
                e.message?.contains("Unable to resolve host", ignoreCase = true) == true ||
                e.message?.contains("Connection refused", ignoreCase = true) == true ||
                e.message?.contains("Connection timed out", ignoreCase = true) == true -> 
                    "Cannot connect to server.\n\nPlease check:\n1. Phone and computer on same Wi-Fi\n2. XAMPP Apache is running\n3. Test: http://172.20.10.2/AwareHealth/api/chatbot/message in phone browser"
                e.message?.contains("timeout", ignoreCase = true) == true -> "Request timed out. Please try again."
                e.message?.contains("Network is unreachable", ignoreCase = true) == true -> "Network unreachable. Please check your Wi-Fi connection."
                e.message?.contains("Cleartext", ignoreCase = true) == true -> "Network security error. Please check app configuration."
                else -> {
                    "Connection error: ${e.message ?: "Unable to connect to server"}\n\nCheck Android Studio Logcat for details (filter: ChatbotViewModel)"
                }
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = errorMsg
            )
            null
        }
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Reset conversation (start new conversation)
     */
    fun resetConversation() {
        _uiState.value = _uiState.value.copy(
            conversationId = null,
            lastSymptomResponse = null,
            symptomConversationId = null,
            waitingForDays = false
        )
    }
    
    /**
     * Check symptoms using AI model
     * This is the primary method for symptom checking
     */
    suspend fun checkSymptoms(message: String, conversationId: String? = null): SymptomResponse? {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        return try {
            // Use stored conversation ID or generate new one
            val convId = conversationId ?: _uiState.value.symptomConversationId ?: System.currentTimeMillis().toString()
            
            val response = repository.checkSymptoms(message, convId)
            
            if (response?.isSuccessful == true && response.body() != null) {
                val symptomResponse = response.body()!!
                
                if (symptomResponse.success) {
                    // Update conversation state
                    val waitingForDays = symptomResponse.conversation_state == "asking_days"
                    
                    // Store the symptom response for UI to use
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        lastSymptomResponse = symptomResponse,
                        symptomConversationId = convId,
                        waitingForDays = waitingForDays,
                        error = null
                    )
                    symptomResponse
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = symptomResponse.error ?: "Failed to analyze symptoms"
                    )
                    null
                }
            } else {
                val errorMsg = "Unable to connect to AI symptom checker. Please try again."
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
                null
            }
        } catch (e: Exception) {
            Log.e("ChatbotViewModel", "Error checking symptoms", e)
            val errorMsg = when {
                e.message?.contains("Failed to connect", ignoreCase = true) == true ||
                e.message?.contains("Connection refused", ignoreCase = true) == true ||
                e.message?.contains("Connection timed out", ignoreCase = true) == true ->
                    "Cannot connect to AI service.\n\nPlease check:\n1. Flask API is running (python flask_api.py)\n2. Phone and computer on same Wi-Fi\n3. Test: http://172.20.10.2:5000/health in phone browser"
                else -> "Error analyzing symptoms: ${e.message ?: "Unknown error"}"
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = errorMsg
            )
            null
        }
    }
    
    /**
     * Send message - tries AI symptom checker first, falls back to regular chatbot
     */
    suspend fun sendMessageWithAI(
        message: String,
        conversationId: String? = null
    ): Pair<ChatMessageResponse?, SymptomResponse?> {
        // If waiting for days input, use symptom conversation ID
        val symptomConvId = if (_uiState.value.waitingForDays) {
            _uiState.value.symptomConversationId
        } else {
            null
        }
        
        // Try AI symptom checker
        val symptomResponse = checkSymptoms(message, symptomConvId)
        
        if (symptomResponse != null && symptomResponse.success) {
            // AI responded successfully, return symptom response
            // Don't call regular chatbot
            return Pair(null, symptomResponse)
        } else {
            // AI failed or not available, fall back to regular chatbot
            val chatResponse = sendMessage(message, conversationId)
            return Pair(chatResponse, null)
        }
    }
}

