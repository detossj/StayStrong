package com.deto.staystrong.ui.exercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.Exercise
import com.deto.staystrong.data.remote.services.ExerciseService

import kotlinx.coroutines.launch

sealed class ExerciseUiState {
    data class Success(val exercises: List<Exercise>) : ExerciseUiState()
    data class Error(val message: String) : ExerciseUiState()
    object Loading : ExerciseUiState()
    object Idle : ExerciseUiState()
}

class ExerciseViewModel(private val exerciseService: ExerciseService) : ViewModel(){
    var exercisesUiState: ExerciseUiState by mutableStateOf(ExerciseUiState.Loading)
        private set

    private fun getExercises(){
        viewModelScope.launch {
            exercisesUiState = ExerciseUiState.Loading
            exercisesUiState = try {
                val listExercises = exerciseService.getExercises()
                ExerciseUiState.Success(listExercises)
            } catch (e: Exception) {
                ExerciseUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun refreshExercises() {
        getExercises()
    }
}