package com.deto.staystrong.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.remote.services.RoutineVideoService
import com.deto.staystrong.model.RoutineVideo
import kotlinx.coroutines.launch


sealed class RoutineVideoUiState {
    data class Success(val routineVideos: List<RoutineVideo>) : RoutineVideoUiState()
    data class Error(val message: String) : RoutineVideoUiState()
    object Loading : RoutineVideoUiState()
    object Idle : RoutineVideoUiState()
}

class RoutineVideoViewModel(private val routineVideoService: RoutineVideoService) : ViewModel(){
    var routineVideoUiState: RoutineVideoUiState by mutableStateOf(RoutineVideoUiState.Loading)
        private set

    private fun getRoutineVideos() {
        viewModelScope.launch {
            routineVideoUiState = RoutineVideoUiState.Loading
            try {
                val listRoutineVideos = routineVideoService.getRoutineVideos()
                routineVideoUiState = RoutineVideoUiState.Success(listRoutineVideos)
            } catch (e: Exception) {
                routineVideoUiState = RoutineVideoUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun refreshRoutineVideos() {
        getRoutineVideos()
    }

}