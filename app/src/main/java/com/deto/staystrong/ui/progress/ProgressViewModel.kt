package com.deto.staystrong.ui.progress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.remote.services.MonthlyVolumeService
import com.deto.staystrong.model.MonthlyVolume
import kotlinx.coroutines.launch

sealed class ProgressUiState {
    data class Success(val monthlyVolumes: List<MonthlyVolume>) : ProgressUiState()
    data class Error(val message: String) : ProgressUiState()
    object Loading : ProgressUiState()
    object Idle : ProgressUiState()
}

class ProgressViewModel(private val monthlyVolumeService: MonthlyVolumeService) : ViewModel(){
    var monthlyVolumesUiState: ProgressUiState by mutableStateOf(ProgressUiState.Loading)
        private set

    private fun getMonthlyVolumes(userId: Int ){
        viewModelScope.launch {
            monthlyVolumesUiState = ProgressUiState.Loading
            monthlyVolumesUiState = try {
                val listExercises = monthlyVolumeService.getMonthlyVolumes(userId)
                ProgressUiState.Success(listExercises)
            } catch (e: Exception) {
                ProgressUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun refreshMonthlyVolumes(userId: Int) {
        getMonthlyVolumes(userId)
    }
}