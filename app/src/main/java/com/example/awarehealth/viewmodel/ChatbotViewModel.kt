package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.ChatMessageRequest
import com.example.awarehealth.data.ChatMessageResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class ChatbotUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val conversationId: String? = null
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
        _uiState.value = _uiState.value.copy(conversationId = null)
    }
}

