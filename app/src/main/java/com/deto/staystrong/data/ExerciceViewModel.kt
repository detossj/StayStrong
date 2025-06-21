// ExerciseViewModel.kt
package com.deto.staystrong

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.AppDatabase
import com.deto.staystrong.data.Exercise
import com.deto.staystrong.data.ExerciseDao
import com.deto.staystrong.data.Routine
import com.deto.staystrong.data.RoutineDao
import com.deto.staystrong.data.RoutineExercise
import com.deto.staystrong.data.RoutineExerciseDao
import com.deto.staystrong.data.Set
import com.deto.staystrong.data.SetDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data classes para el estado de la UI (no son entidades de Room)
data class SetEntryUI(
    val id: String = java.util.UUID.randomUUID().toString(),
    var kg: String = "",
    var reps: String = "",
    var isCompleted: Boolean = false,
    var restTime: Int = 210, // Default rest time in seconds (3 minutes 30 seconds)
    var isResting: Boolean = false,
    var currentRestTime: Int = 0, // Current countdown for rest
    var showRestTimePicker: Boolean = false, // To show/hide the time picker dialog
    var setId: Int = 0 // ID de la base de datos para el Set
)

data class ExerciseDataUI(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val sets: MutableList<SetEntryUI> = mutableListOf(),
    var isExpanded: Boolean = false,
    val exerciseId: Int = 0, // ID de la base de datos para el Exercise
    val routineExerciseId: Int = 0, // ID de la base de datos para RoutineExercise
    val category: String = "Otros" // <-- ¡Añadido a la UI Data Class!
)

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val exerciseDao: ExerciseDao
    private val routineDao: RoutineDao
    private val routineExerciseDao: RoutineExerciseDao
    private val setDao: SetDao

    // Estados para el filtrado
    private val _allExercisesFromDb = MutableStateFlow<List<Exercise>>(emptyList()) // Todos los ejercicios de la BD
    private val _selectedCategoryFilter = MutableStateFlow<String>("Todos") // Categoría seleccionada para filtrar

    private val _exercises = MutableStateFlow<List<ExerciseDataUI>>(emptyList())
    val exercises: StateFlow<List<ExerciseDataUI>> = _exercises.asStateFlow()

    // Estado para las categorías disponibles (para la UI del filtro)
    private val _availableCategories = MutableStateFlow<List<String>>(emptyList())
    val availableCategories: StateFlow<List<String>> = _availableCategories.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        exerciseDao = database.exerciseDao()
        routineDao = database.routineDao()
        routineExerciseDao = database.routineExerciseDao()
        setDao = database.setDao()

        // Cargar ejercicios predefinidos en la base de datos si no existen
        viewModelScope.launch {
            val existingExercises = exerciseDao.getAllExercises().first()
            if (existingExercises.isEmpty()) {
                insertInitialExercises()
            }
        }

        // Combinar los ejercicios de la BD y el filtro seleccionado para actualizar _exercises
        // También actualizar las categorías disponibles
        viewModelScope.launch {
            exerciseDao.getAllExercises().collect { exercisesDb ->
                _allExercisesFromDb.value = exercisesDb
                // Actualizar las categorías disponibles
                val categories = mutableSetOf("Todos") // Añadir "Todos" como opción
                categories.addAll(exercisesDb.map { it.category }.distinct().filter { it.isNotBlank() })
                _availableCategories.value = categories.toList()
                loadCurrentRoutineData() // Recargar la rutina después de obtener los ejercicios
            }
        }
    }

    private suspend fun insertInitialExercises() {
        val predefined = listOf(
            // Pecho
            Exercise(name = "Press Banca con Barra", category = "Pecho"),
            Exercise(name = "Press Banca con Mancuernas", category = "Pecho"),
            Exercise(name = "Aperturas con Mancuernas", category = "Pecho"),
            Exercise(name = "Press Inclinado con Barra", category = "Pecho"),
            Exercise(name = "Fondos en Paralelas", category = "Pecho"),

            // Espalda
            Exercise(name = "Peso Muerto", category = "Espalda"),
            Exercise(name = "Remo con Barra", category = "Espalda"),
            Exercise(name = "Dominadas", category = "Espalda"),
            Exercise(name = "Jalón al Pecho", category = "Espalda"),
            Exercise(name = "Remo con Mancuerna", category = "Espalda"),

            // Pierna
            Exercise(name = "Sentadilla con Barra", category = "Pierna"),
            Exercise(name = "Prensa de Piernas", category = "Pierna"),
            Exercise(name = "Extensiones de Cuádriceps", category = "Pierna"),
            Exercise(name = "Femoral Tumbado", category = "Pierna"),
            Exercise(name = "Peso Muerto Rumano", category = "Pierna"),
            Exercise(name = "Gemelos de Pie", category = "Pierna"),

            // Hombro
            Exercise(name = "Press Militar con Barra", category = "Hombro"),
            Exercise(name = "Press de Hombros con Mancuernas", category = "Hombro"),
            Exercise(name = "Elevaciones Laterales", category = "Hombro"),
            Exercise(name = "Pájaros con Mancuernas", category = "Hombro"),

            // Brazo (Bíceps y Tríceps)
            Exercise(name = "Curl de Bíceps con Barra", category = "Brazo"),
            Exercise(name = "Curl de Bíceps con Mancuernas", category = "Brazo"),
            Exercise(name = "Extensión de Tríceps con Barra Z", category = "Brazo"),
            Exercise(name = "Press Francés", category = "Brazo"),
            Exercise(name = "Fondos para Tríceps", category = "Brazo"),

            // Abdominales
            Exercise(name = "Crunch Abdominal", category = "Abdominales"),
            Exercise(name = "Elevación de Piernas Colgado", category = "Abdominales"),
            Exercise(name = "Plancha", category = "Abdominales"),

            // Otros / Cuerpo Completo
            Exercise(name = "Burpees", category = "Otros"),
            Exercise(name = "Saltar la Cuerda", category = "Otros"),
            Exercise(name = "Zancadas", category = "Pierna"), // Reclasificado a Pierna
            Exercise(name = "Paseo del Granjero", category = "Otros")
        )
        predefined.forEach { exerciseDao.insertExercise(it) }
    }

    private suspend fun loadCurrentRoutineData() {
        val userId = 1
        var currentRoutine: Routine? = routineDao.getRoutinesForUser(userId).first().firstOrNull()

        if (currentRoutine == null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            val newRoutine = Routine(userId = userId, date = currentDate)
            val routineId = routineDao.insertRoutine(newRoutine).toInt()
            currentRoutine = newRoutine.copy(id = routineId)
        }

        currentRoutine?.let { routine ->
            routineExerciseDao.getRoutineExercisesForRoutine(routine.id).collect { routineExercises ->
                val exerciseDataUIs = mutableListOf<ExerciseDataUI>()
                routineExercises.forEach { re ->
                    val exercise = exerciseDao.getExerciseById(re.exercise_id).first()
                    exercise?.let { ex ->
                        val sets = setDao.getSetsForRoutineExercise(re.id).first().map { dbSet ->
                            SetEntryUI(
                                id = java.util.UUID.randomUUID().toString(),
                                kg = dbSet.weight.toString(),
                                reps = dbSet.reps.toString(),
                                isCompleted = false,
                                restTime = 210,
                                isResting = false,
                                currentRestTime = 0,
                                showRestTimePicker = false,
                                setId = dbSet.id
                            )
                        }.toMutableList()

                        exerciseDataUIs.add(
                            ExerciseDataUI(
                                id = java.util.UUID.randomUUID().toString(),
                                name = ex.name,
                                sets = sets,
                                isExpanded = false,
                                exerciseId = ex.id,
                                routineExerciseId = re.id,
                                category = ex.category // <-- Asignar la categoría aquí
                            )
                        )
                    }
                }
                _exercises.value = exerciseDataUIs
            }
        }
    }

    fun addExerciseToRoutine(exerciseName: String, category: String) { // <-- ¡Añadir categoría!
        viewModelScope.launch {
            var exercise = exerciseDao.getAllExercises().first().find { it.name == exerciseName }
            if (exercise == null) {
                // Si el ejercicio no existe, lo insertamos con la categoría proporcionada
                val newExercise = Exercise(name = exerciseName, category = category) // <-- Usar la categoría
                val newExerciseId = exerciseDao.insertExercise(newExercise).toInt()
                exercise = newExercise.copy(id = newExerciseId)
            }

            val userId = 1
            var currentRoutine: Routine? = routineDao.getRoutinesForUser(userId).first().firstOrNull()

            if (currentRoutine == null) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())
                val newRoutine = Routine(userId = userId, date = currentDate)
                val routineId = routineDao.insertRoutine(newRoutine).toInt()
                currentRoutine = newRoutine.copy(id = routineId)
            }

            currentRoutine?.let { routine ->
                val existingRoutineExercise = routineExerciseDao.getRoutineExercisesForRoutine(routine.id)
                    .first()
                    .find { it.exercise_id == exercise.id }

                if (existingRoutineExercise == null) {
                    val newRoutineExercise = RoutineExercise(
                        routine_id = routine.id,
                        exercise_id = exercise.id,
                        order = _exercises.value.size + 1
                    )
                    val routineExerciseId = routineExerciseDao.insertRoutineExercise(newRoutineExercise).toInt()

                    val newExerciseDataUI = ExerciseDataUI(
                        name = exercise.name,
                        sets = mutableListOf(SetEntryUI(), SetEntryUI(), SetEntryUI()), // 3 sets por defecto
                        isExpanded = true,
                        exerciseId = exercise.id,
                        routineExerciseId = routineExerciseId,
                        category = exercise.category // <-- Asignar la categoría aquí
                    )

                    // Insertar los sets iniciales en la base de datos
                    newExerciseDataUI.sets.forEach { setEntryUI ->
                        val dbSet = Set(
                            routine_exercise_id = routineExerciseId,
                            reps = setEntryUI.reps.toIntOrNull() ?: 0,
                            weight = setEntryUI.kg.toFloatOrNull() ?: 0f
                        )
                        val setId = setDao.insertSet(dbSet).toInt()
                        setEntryUI.setId = setId // Actualizar el ID de la UI con el de la BD
                    }

                    _exercises.value = _exercises.value + newExerciseDataUI
                }
            }
        }
    }

    fun removeExercise(exerciseDataUI: ExerciseDataUI) {
        viewModelScope.launch {
            val routineExerciseToDelete = RoutineExercise(
                id = exerciseDataUI.routineExerciseId,
                routine_id = 0,
                exercise_id = 0,
                order = 0
            )
            routineExerciseDao.deleteRoutineExercise(routineExerciseToDelete)
            _exercises.value = _exercises.value.filter { it.id != exerciseDataUI.id }
        }
    }


    fun updateSet(exerciseDataUI: ExerciseDataUI, updatedSetEntryUI: SetEntryUI) {
        viewModelScope.launch {
            val currentExerciseIndex = _exercises.value.indexOfFirst { it.id == exerciseDataUI.id }
            if (currentExerciseIndex != -1) {
                val updatedExercise = _exercises.value[currentExerciseIndex]
                val currentSetIndex = updatedExercise.sets.indexOfFirst { it.id == updatedSetEntryUI.id }
                if (currentSetIndex != -1) {
                    val newSets = updatedExercise.sets.toMutableList()
                    newSets[currentSetIndex] = updatedSetEntryUI.copy()
                    _exercises.value = _exercises.value.toMutableList().also {
                        it[currentExerciseIndex] = updatedExercise.copy(sets = newSets)
                    }

                    val dbSet = Set(
                        id = updatedSetEntryUI.setId,
                        routine_exercise_id = exerciseDataUI.routineExerciseId,
                        reps = updatedSetEntryUI.reps.toIntOrNull() ?: 0,
                        weight = updatedSetEntryUI.kg.toFloatOrNull() ?: 0f
                    )
                    setDao.updateSet(dbSet)
                }
            }
        }
    }

    fun addSet(exerciseDataUI: ExerciseDataUI) {
        viewModelScope.launch {
            val currentExerciseIndex = _exercises.value.indexOfFirst { it.id == exerciseDataUI.id }
            if (currentExerciseIndex != -1) {
                val updatedExercise = _exercises.value[currentExerciseIndex]
                val newSetEntryUI = SetEntryUI()

                val dbSet = Set(
                    routine_exercise_id = exerciseDataUI.routineExerciseId,
                    reps = newSetEntryUI.reps.toIntOrNull() ?: 0,
                    weight = newSetEntryUI.kg.toFloatOrNull() ?: 0f
                )
                val setId = setDao.insertSet(dbSet).toInt()
                newSetEntryUI.setId = setId

                val newSets = updatedExercise.sets.toMutableList()
                newSets.add(newSetEntryUI)
                _exercises.value = _exercises.value.toMutableList().also {
                    it[currentExerciseIndex] = updatedExercise.copy(sets = newSets)
                }
            }
        }
    }

    fun deleteSet(exerciseDataUI: ExerciseDataUI, setEntryId: String) {
        viewModelScope.launch {
            val currentExerciseIndex = _exercises.value.indexOfFirst { it.id == exerciseDataUI.id }
            if (currentExerciseIndex != -1) {
                val updatedExercise = _exercises.value[currentExerciseIndex]
                val setToDelete = updatedExercise.sets.find { it.id == setEntryId }

                setToDelete?.let {
                    val dbSet = Set(
                        id = it.setId,
                        routine_exercise_id = exerciseDataUI.routineExerciseId,
                        reps = 0, weight = 0f
                    )
                    setDao.deleteSet(dbSet)

                    val newSets = updatedExercise.sets.toMutableList()
                    newSets.remove(it)
                    _exercises.value = _exercises.value.toMutableList().also { list ->
                        list[currentExerciseIndex] = updatedExercise.copy(sets = newSets)
                    }
                }
            }
        }
    }

    fun toggleExerciseExpansion(exerciseDataUI: ExerciseDataUI) {
        val currentExerciseIndex = _exercises.value.indexOfFirst { it.id == exerciseDataUI.id }
        if (currentExerciseIndex != -1) {
            val updatedExercise = _exercises.value[currentExerciseIndex].copy(isExpanded = !exerciseDataUI.isExpanded)
            _exercises.value = _exercises.value.toMutableList().also { it[currentExerciseIndex] = updatedExercise }
        }
    }

    // Nuevo: Función para actualizar el filtro de categoría
    fun setSelectedCategoryFilter(category: String) {
        _selectedCategoryFilter.value = category
    }

    // Nuevo: Obtener los ejercicios predefinidos filtrados
    val predefinedExercisesFiltered: StateFlow<List<PredefinedExercise>> = combine(
        _allExercisesFromDb, // Observa todos los ejercicios en la BD
        _selectedCategoryFilter // Observa la categoría de filtro seleccionada
    ) { allExercises, selectedCategory ->
        allExercises
            .filter { exercise ->
                // Filtra por categoría si no es "Todos"
                if (selectedCategory == "Todos") true else exercise.category == selectedCategory
            }
            .map { exercise ->
                // Mapea a PredefinedExercise, manteniendo el estado de selección
                PredefinedExercise(name = exercise.name, category = exercise.category, isSelected = MutableStateFlow(false))
            }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExerciseViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

// Mover PredefinedExercise al ViewModel o a un archivo compartido
// para que pueda ser utilizado por predefinedExercisesFiltered
data class PredefinedExercise(
    val name: String,
    val category: String, // Añadir categoría aquí también
    var isSelected: MutableStateFlow<Boolean> = MutableStateFlow(false) // Usar MutableStateFlow para que sea observable
)