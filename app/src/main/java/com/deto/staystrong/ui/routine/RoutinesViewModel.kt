package com.deto.staystrong.ui.routine
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.model.Routine
import com.deto.staystrong.data.remote.services.RoutineService
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class RoutinesUiState {
    data class Success(val routines: List<Routine>) : RoutinesUiState()
    data class Error(val message: String) : RoutinesUiState()
    object Loading : RoutinesUiState()
    object Idle : RoutinesUiState()
}

class RoutinesViewModel(private val routineService: RoutineService) : ViewModel(){
    var routinesUiState: RoutinesUiState by mutableStateOf(RoutinesUiState.Loading)
        private set

    private fun getRoutines(){
        viewModelScope.launch {
            routinesUiState = RoutinesUiState.Loading
            routinesUiState = try {
                val listRoutines = routineService.getRoutines()
                RoutinesUiState.Success(listRoutines)
            } catch (e: Exception) {
                RoutinesUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun addRoutine(date: LocalDate) {
        routinesUiState = RoutinesUiState.Loading
        viewModelScope.launch {
            try {
                routineService.addRoutine(Routine(0,0,date.toString(),"fullbody",null))
                val updatedList = routineService.getRoutines()
                routinesUiState = RoutinesUiState.Success(updatedList)
            }
            catch (e: Exception) {
                routinesUiState = RoutinesUiState.Error(e.message ?: "error")
            }
        }
    }

    fun refreshRoutines() {
        getRoutines()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addDefaultRoutine(type: String, muscleGroup: String? = null, date: LocalDate = LocalDate.now()) {
        routinesUiState = RoutinesUiState.Loading
        viewModelScope.launch {
            try {
                // Envía al endpoint /routines/default
                routineService.addDefaultRoutine(
                    Routine(0,0,date.toString(),type,muscleGroup)
                )

                val updatedList = routineService.getRoutines()
                routinesUiState = RoutinesUiState.Success(updatedList)
            } catch (e: Exception) {
                routinesUiState = RoutinesUiState.Error(e.message ?: "Error al crear rutina por defecto")
            }
        }
    }
}