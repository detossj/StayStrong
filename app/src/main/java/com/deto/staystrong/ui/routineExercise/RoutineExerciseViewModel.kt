package com.deto.staystrong.ui.routineExercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.RoutineExercise
import com.deto.staystrong.data.remote.services.RoutineExerciseService
import kotlinx.coroutines.launch

sealed class RoutineExerciseUiState {
    data class Success(val routine: List<RoutineExercise>) : RoutineExerciseUiState()
    data class Error(val message: String) : RoutineExerciseUiState()
    object Loading : RoutineExerciseUiState()
    object Idle : RoutineExerciseUiState()
}
class RoutineExerciseViewModel(private val routineExerciseService: RoutineExerciseService) : ViewModel(){
    var routineExerciseUiState: RoutineExerciseUiState by mutableStateOf(RoutineExerciseUiState.Loading)
        private set

    private fun getRoutineExercise(id: Int){
        viewModelScope.launch {
            routineExerciseUiState = RoutineExerciseUiState.Loading
            routineExerciseUiState = try {
                val listRoutineExercise = routineExerciseService.getRoutineExerciseById(id)
                RoutineExerciseUiState.Success(listRoutineExercise)
            } catch (e: Exception) {
                RoutineExerciseUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }


    fun refreshRoutineExercise(id: Int) {
        getRoutineExercise(id)
    }

}