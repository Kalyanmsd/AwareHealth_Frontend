package com.example.awarehealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarehealth.data.AppRepository
import com.example.awarehealth.data.AppointmentData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DoctorAppointmentsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val appointments: List<AppointmentData> = emptyList(),
    val pendingCount: Int = 0,
    val acceptedCount: Int = 0
)

class DoctorAppointmentsViewModel(private val repository: AppRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DoctorAppointmentsUiState())
    val uiState: StateFlow<DoctorAppointmentsUiState> = _uiState.asStateFlow()
    
    /**
     * Fetch appointments for a specific doctor
     */
    fun fetchDoctorAppointments(doctorId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val response = repository.getDoctorAppointments(doctorId)
                
                if (response?.isSuccessful == true && response.body() != null) {
                    val appointmentsResponse = response.body()!!
                    
                    if (appointmentsResponse.success && appointmentsResponse.appointments != null) {
                        val appointments = appointmentsResponse.appointments
                        val pending = appointments.filter { 
                            it.status?.lowercase() == "pending" 
                        }
                        val accepted = appointments.filter { 
                            it.status?.lowercase() == "accepted" 
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            appointments = appointments,
                            pendingCount = pending.size,
                            acceptedCount = accepted.size,
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = appointmentsResponse.message ?: "Failed to fetch appointments"
                        )
                    }
                } else {
                    val errorMessage = try {
                        response?.errorBody()?.string() ?: "Failed to fetch appointments"
                    } catch (e: Exception) {
                        "Failed to fetch appointments"
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to fetch appointments"
                )
            }
        }
    }
    
    /**
     * Refresh appointments
     */
    fun refreshAppointments(doctorId: String) {
        fetchDoctorAppointments(doctorId)
    }
}

