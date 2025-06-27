package com.deto.staystrong.ui.routineExercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.model.RoutineExercise
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

    fun addRoutineExercise(id: Int,exercise_id: Int) {
        routineExerciseUiState = RoutineExerciseUiState.Loading
        viewModelScope.launch {
            try {
                routineExerciseService.addRoutineExercise(id, RoutineExercise(0,id,exercise_id,null))
                val updatedList = routineExerciseService.getRoutineExerciseById(id)
                RoutineExerciseUiState.Success(updatedList)
            }
            catch (e: Exception) {
                routineExerciseUiState = RoutineExerciseUiState.Error(e.message ?: "error")
            }
        }
    }

    fun deleteRoutineExerciseById(routineId: Int, routineExerciseId: Int) {
        viewModelScope.launch {
            routineExerciseUiState = RoutineExerciseUiState.Loading
            try {
                routineExerciseService.deleteRoutineExerciseById(routineExerciseId,routineId)
                val updatedList = routineExerciseService.getRoutineExerciseById(routineId)
                routineExerciseUiState = RoutineExerciseUiState.Success(updatedList)
            }
            catch (e: Exception) {
                routineExerciseUiState = RoutineExerciseUiState.Error(e.message ?: "error")
            }
        }
    }

    fun refreshRoutineExercise(id: Int) {
        getRoutineExercise(id)
    }

}