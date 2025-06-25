package com.deto.staystrong.ui.set

import androidx.lifecycle.ViewModel
import com.deto.staystrong.data.Set
import com.deto.staystrong.data.remote.services.SetService
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


sealed class SetUiState {
    data class Success(val sets: List<Set>) : SetUiState()
    data class Error(val message: String) : SetUiState()
    object Loading : SetUiState()
    object Idle : SetUiState()
}

class SetViewModel(private val setService: SetService) : ViewModel() {
    var setUiState: SetUiState by mutableStateOf(SetUiState.Loading)
        private set


    fun getSets(idRoutine: Int, idRoutineExercise: Int){
        viewModelScope.launch {
            setUiState = SetUiState.Loading
            setUiState = try {
                val listSets = setService.getSets(idRoutine,idRoutineExercise)
                SetUiState.Success(listSets)
            } catch (e: Exception) {
                SetUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun refreshSets(idRoutine: Int, idRoutineExercise: Int) {
        getSets(idRoutine, idRoutineExercise)
    }
}