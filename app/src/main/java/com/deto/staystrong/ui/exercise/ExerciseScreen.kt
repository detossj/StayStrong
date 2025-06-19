package com.deto.staystrong.ui.theme.exercise // Puedes ajustar el paquete según tu estructura

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.* // Usando Material3
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// =========================================================
// Data Classes (Modelos de Datos)
// =========================================================

// Para representar un ejercicio en la biblioteca
data class Exercise(
    val name: String,
    val description: String,
    val muscleGroup: String
)

// Para representar un set de trabajo registrado
data class setsTrabajos(
    val id: Int, // Un ID único para cada set (simple para este boceto)
    val NombreEjercicio: String, // El nombre del ejercicio al que pertenece este set
    var Peso: String,
    var Repes: String
)

// Una lista de ejercicios de ejemplo para tu "biblioteca"
val sampleExercises = listOf(
    Exercise("Press de Banca", "Ejercicio para pecho, hombros y tríceps.", "Pecho"),
    Exercise("Sentadilla", "Ejercicio fundamental para piernas y glúteos.", "Piernas"),
    Exercise("Peso Muerto", "Ejercicio de cuerpo completo para fuerza posterior.", "Espalda/Piernas"),
    Exercise("Dominadas", "Ejercicio de fuerza para espalda y bíceps.", "Espalda/Bíceps"),
    Exercise("Press Militar", "Ejercicio para hombros y tríceps.", "Hombros"),
    Exercise("Remo con Barra", "Ejercicio para espalda y bíceps.", "Espalda"),
    Exercise("Curl de Bíceps", "Ejercicio de aislamiento para bíceps.", "Bíceps"),
    Exercise("Extensión de Tríceps", "Ejercicio de aislamiento para tríceps.", "Tríceps")
)

// =========================================================
// Composable Principal: ExerciseSketchScreen (todo en uno)
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(modifier: Modifier = Modifier) {
    // Estado para saber qué ejercicio está actualmente seleccionado para el detalle
    // Si es null, mostramos la lista de ejercicios. Si tiene un nombre, mostramos el detalle.
    var selectedExerciseName by remember { mutableStateOf<String?>(null) }

    // Estados para el registro de sets (se reinician cuando se selecciona un nuevo ejercicio)
    var currentPeso by remember { mutableStateOf("") }
    var currentRepes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // Mapa para almacenar los sets de cada ejercicio individualmente
    // Clave: Nombre del Ejercicio, Valor: Lista de setsTrabajos para ese ejercicio
    val allSetsByExercise = remember { mutableStateMapOf<String, MutableList<setsTrabajos>>() }
    var setIdCounter by remember { mutableStateOf(0) } // Contador global para IDs de sets

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedExerciseName == null) "Biblioteca de Ejercicios"
                        else "Registrar: ${selectedExerciseName}"
                    )
                },
                navigationIcon = {
                    // Mostrar botón de volver solo si hay un ejercicio seleccionado
                    if (selectedExerciseName != null) {
                        IconButton(onClick = {
                            selectedExerciseName = null // Volver a la lista de ejercicios
                            // Limpiar campos de entrada al volver
                            currentPeso = ""
                            currentRepes = ""
                            errorMessage = null
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver a la Biblioteca")
                        }
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        // Contenido principal de la pantalla basado en si hay un ejercicio seleccionado
        if (selectedExerciseName == null) {
            // =========================================================
            // Vista de la Biblioteca de Ejercicios (lista de "casilleros")
            // =========================================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleExercises) { exercise ->
                    ExerciseCard(exercise = exercise) {
                        selectedExerciseName = exercise.name // Al hacer clic, selecciona el ejercicio
                        // Asegurarse de que exista una lista de sets para este ejercicio
                        if (!allSetsByExercise.containsKey(exercise.name)) {
                            allSetsByExercise[exercise.name] = mutableStateListOf()
                        }
                        // Cargar los sets existentes para este ejercicio en los campos de entrada
                        // (esto no es necesario si solo se añaden sets nuevos al iniciar)
                    }
                }
            }
        } else {
            // =========================================================
            // Vista de Registro de Sets para el Ejercicio Seleccionado
            // =========================================================
            val currentExerciseSets = allSetsByExercise[selectedExerciseName] ?: remember { mutableStateListOf() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sección para añadir un nuevo set
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentRepes,
                        onValueChange = { newValue ->
                            currentRepes = newValue
                            errorMessage = null
                        },
                        label = { Text("Repeticiones") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = currentPeso,
                        onValueChange = { newValue ->
                            currentPeso = newValue
                            errorMessage = null
                        },
                        label = { Text("Peso (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = {
                        // Validaciones
                        if (currentRepes.isBlank() || currentPeso.isBlank()) {
                            errorMessage = "Repeticiones y Peso no pueden estar vacíos."
                            return@Button
                        }
                        val repesNum = currentRepes.toIntOrNull()
                        val pesoNum = currentPeso.toDoubleOrNull()

                        if (repesNum == null || pesoNum == null || repesNum <= 0 || pesoNum < 0) {
                            errorMessage = "Valores numéricos válidos y positivos requeridos."
                            return@Button
                        }

                        setIdCounter++
                        val newSet = setsTrabajos(
                            id = setIdCounter,
                            NombreEjercicio = selectedExerciseName!!, // Sabes que no es null aquí
                            Peso = currentPeso,
                            Repes = currentRepes
                        )
                        // Añade el set a la lista específica de este ejercicio
                        currentExerciseSets.add(newSet)
                        // Asegúrate de que el mapa se actualice si la lista fue inicializada de nuevo
                        allSetsByExercise[selectedExerciseName!!] = currentExerciseSets

                        currentRepes = ""
                        currentPeso = ""
                        errorMessage = null
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Set")
                    }
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de sets registrados para el ejercicio actual
                if (currentExerciseSets.isNotEmpty()) {
                    Text(
                        text = "Sets Registrados para ${selectedExerciseName}:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(currentExerciseSets) { set ->
                            SetItem(set = set) {
                                currentExerciseSets.removeIf { it.id == set.id }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Añade tu primer set para este ejercicio.",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para "Guardar todo" (simulado) para el ejercicio actual
                Button(
                    onClick = {
                        // Aquí, los sets ya están almacenados en 'allSetsByExercise'
                        // Puedes mostrar un Toast o un mensaje de confirmación
                        println("Todos los sets de ${selectedExerciseName} guardados (simulado).")
                        // Opcional: Podrías volver a la biblioteca automáticamente después de guardar
                        // selectedExerciseName = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = currentExerciseSets.isNotEmpty()
                ) {
                    Text("Guardar Cambios para ${selectedExerciseName}")
                }
            }
        }
    }
}

// =========================================================
// Composables Auxiliares (Elementos individuales de UI)
// =========================================================

@Composable
fun ExerciseCard(exercise: Exercise, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Hace que la tarjeta sea clickeable
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = exercise.muscleGroup,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SetItem(set: setsTrabajos, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Set ${set.id}: ${set.Repes} repes @ ${set.Peso} kg",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar Set")
            }
        }
    }
}

// =========================================================
// Preview (para ver en Android Studio)
// =========================================================

@Preview(showBackground = true)
@Composable
fun PreviewExerciseSketchScreen() {
    MaterialTheme { // Asegúrate de usar el tema de tu aplicación
        ExerciseScreen()
    }
}