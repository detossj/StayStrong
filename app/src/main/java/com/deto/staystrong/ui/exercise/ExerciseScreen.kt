// ui/theme/exercise/ExerciseScreen.kt
package com.deto.staystrong.ui.theme.exercise

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deto.staystrong.ExerciseDataUI
import com.deto.staystrong.ExerciseViewModel
import com.deto.staystrong.PredefinedExercise // Importar el PredefinedExercise del ViewModel
import com.deto.staystrong.SetEntryUI
import kotlinx.coroutines.delay
// No necesitamos java.util.UUID aquí si ya lo manejamos en los data classes de UI
// import java.util.UUID // <-- Eliminar esta línea si no se usa directamente

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ExerciseScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current.applicationContext
    val viewModel: ExerciseViewModel = viewModel(factory = ExerciseViewModel.Factory(context as Application))

    val exercises by viewModel.exercises.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Observar las categorías disponibles desde el ViewModel
    val availableCategories by viewModel.availableCategories.collectAsState()
    // Observar la lista filtrada de ejercicios predefinidos
    val predefinedExercisesFiltered by viewModel.predefinedExercisesFiltered.collectAsState()

    var showPredefinedExerciseList by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("Todos") } // Estado local para el filtro seleccionado en la UI
    var expandedCategoryFilter by remember { mutableStateOf(false) } // Para el DropdownMenu

    var isRunning by remember { mutableStateOf(true) }
    val startTime = remember { System.currentTimeMillis() }
    val elapsedTime by produceState(initialValue = "00:00:00", key1 = isRunning) {
        while (isRunning) {
            val elapsedMillis = System.currentTimeMillis() - startTime
            val seconds = (elapsedMillis / 1000) % 60
            val minutes = (elapsedMillis / (1000 * 60)) % 60
            val hours = (elapsedMillis / (1000 * 60 * 60)) % 24
            value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            delay(1000)
        }
    }

    val totalSets = exercises.sumOf { it.sets.size }
    val totalVolume = exercises.sumOf { exercise ->
        exercise.sets.sumOf { set ->
            if (set.isCompleted) {
                set.kg.toDoubleOrNull()?.times(set.reps.toIntOrNull() ?: 0) ?: 0.0
            } else 0.0
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Mi Sesión de Ejercicios", color = Color.White) },
                actions = {
                    Button(onClick = { isRunning = false }) {
                        Text("Terminar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray)
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryBox("Duración", elapsedTime, Color.White)
                SummaryBox("Volumen", "${totalVolume.toInt()} kg", Color.LightGray)
                SummaryBox("Sets", "$totalSets", Color.LightGray)
            }

            // Botón para mostrar/ocultar la lista de ejercicios predefinidos
            Button(
                onClick = { showPredefinedExerciseList = !showPredefinedExerciseList },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Ejercicio")
                Spacer(Modifier.width(8.dp))
                Text("Añadir Ejercicio")
            }

            // Lista de ejercicios predefinidos y botón de añadir, ahora dentro de AnimatedVisibility
            AnimatedVisibility(
                visible = showPredefinedExerciseList,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                        .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp)) // Fondo para la lista
                        .padding(12.dp)
                ) {
                    Text(
                        "Selecciona ejercicios:",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Filtro de Categorías (DropdownMenu)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(Color.DarkGray, RoundedCornerShape(8.dp))
                            .clickable { expandedCategoryFilter = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Filtrar por: $selectedCategoryFilter", color = Color.White)
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Filtrar categoría", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = expandedCategoryFilter,
                            onDismissRequest = { expandedCategoryFilter = false },
                            modifier = Modifier.fillMaxWidth(0.9f) // Ajusta el ancho
                                .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp))
                        ) {
                            availableCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category, color = Color.White) },
                                    onClick = {
                                        selectedCategoryFilter = category
                                        viewModel.setSelectedCategoryFilter(category) // Actualizar el filtro en ViewModel
                                        expandedCategoryFilter = false
                                    }
                                )
                            }
                        }
                    }


                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        predefinedExercisesFiltered.forEach { predefinedExercise -> // Usar la lista filtrada
                            // Observar el estado de selección de cada PredefinedExercise
                            val isSelected by predefinedExercise.isSelected.collectAsState()
                            val backgroundColor = if (isSelected) Color(0xFF00FFAA) else Color(0xFF333335)
                            val textColor = if (isSelected) Color.Black else Color.White
                            Text(
                                text = predefinedExercise.name,
                                color = textColor,
                                modifier = Modifier
                                    .background(backgroundColor, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF00FFAA), RoundedCornerShape(8.dp))
                                    .clickable { predefinedExercise.isSelected.value = !predefinedExercise.isSelected.value } // Toggle isSelected
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val selectedExercisesToAdd = predefinedExercisesFiltered.filter { it.isSelected.value }
                            selectedExercisesToAdd.forEach { selected ->
                                viewModel.addExerciseToRoutine(selected.name, selected.category) // Pasar la categoría
                                selected.isSelected.value = false // Deseleccionar después de añadir
                            }
                            showPredefinedExerciseList = false // Ocultar la lista después de añadir
                        },
                        enabled = predefinedExercisesFiltered.any { it.isSelected.value }, // Habilitar si hay alguno seleccionado
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Seleccionados")
                        Spacer(Modifier.width(8.dp))
                        Text("Añadir Seleccionados")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(exercises, key = { _, exercise -> exercise.id }) { _, exercise ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleExerciseExpansion(exercise) }
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = exercise.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = { viewModel.removeExercise(exercise) }) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Eliminar Ejercicio",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = exercise.isExpanded,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                ExerciseDetailsContent(
                                    exercise = exercise,
                                    onAddSet = { viewModel.addSet(exercise) },
                                    onUpdateSet = { updatedSet -> viewModel.updateSet(exercise, updatedSet) },
                                    onDeleteSet = { setId -> viewModel.deleteSet(exercise, setId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryBox(title: String, value: String, valueColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .padding(4.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = valueColor)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsContent(
    exercise: ExerciseDataUI,
    onAddSet: () -> Unit,
    onUpdateSet: (SetEntryUI) -> Unit,
    onDeleteSet: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notas...",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )

            if (exercise.sets.isNotEmpty()) {
                Text(
                    text = "Tiempo de descanso predefinido: ${exercise.sets[0].restTime / 60}min ${exercise.sets[0].restTime % 60}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF00FFAA),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            Text("ANTERIOR", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.weight(0.2f))
            Text("KG", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.weight(0.25f), textAlign = TextAlign.Center)
            Text("REPS", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.weight(0.25f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.weight(0.1f))
        }
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.DarkGray)


        Column {
            exercise.sets.forEachIndexed { index, set ->
                SetRow(
                    setNumber = index + 1,
                    setEntry = set,
                    onUpdate = onUpdateSet,
                    onDelete = onDeleteSet
                )
            }
        }


        Button(
            onClick = onAddSet,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333335))
        ) {
            Text("+ Agregar conjunto", color = Color.White)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SetRow(
    setNumber: Int,
    setEntry: SetEntryUI,
    onUpdate: (SetEntryUI) -> Unit,
    onDelete: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(setEntry.isResting) {
        if (setEntry.isResting) {
            var countdown = setEntry.restTime
            onUpdate(setEntry.copy(currentRestTime = countdown))
            while (countdown > 0) {
                delay(1000L)
                countdown--
                onUpdate(setEntry.copy(currentRestTime = countdown))
            }
            onUpdate(setEntry.copy(isResting = false))
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = setNumber.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(0.1f)
            )
            Text(
                text = "70kg x 8", // Placeholder for previous
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.weight(0.2f)
            )
            OutlinedTextField(
                value = setEntry.kg,
                onValueChange = { newValue: String ->
                    onUpdate(setEntry.copy(kg = newValue))
                },
                modifier = Modifier
                    .weight(0.25f)
                    .height(48.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                ),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00FFAA),
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    disabledTextColor = Color.Gray,
                    errorTextColor = Color.Red,
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = setEntry.reps,
                onValueChange = { newValue: String ->
                    onUpdate(setEntry.copy(reps = newValue))
                },
                modifier = Modifier
                    .weight(0.25f)
                    .height(48.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                ),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00FFAA),
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    disabledTextColor = Color.Gray,
                    errorTextColor = Color.Red,
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Checkbox(
                checked = setEntry.isCompleted,
                onCheckedChange = { isChecked ->
                    onUpdate(setEntry.copy(isCompleted = isChecked, isResting = isChecked))
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF00FFAA),
                    uncheckedColor = Color.DarkGray,
                    checkmarkColor = Color.Black
                ),
                modifier = Modifier
                    .weight(0.1f)
                    .size(24.dp)
            )

            IconButton(
                onClick = { onDelete(setEntry.id) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar set", tint = Color.Red)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val minutes = setEntry.currentRestTime / 60
            val seconds = setEntry.currentRestTime % 60
            val restTimeDisplay = String.format("%02d:%02d", minutes, seconds)

            Text(
                text = if (setEntry.isResting) "Descanso: $restTimeDisplay" else "Descanso predefinido: ${setEntry.restTime / 60}min ${setEntry.restTime % 60}s",
                style = MaterialTheme.typography.bodySmall,
                color = if (setEntry.isResting) Color(0xFF00FFAA) else Color.Gray,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    onUpdate(setEntry.copy(isResting = !setEntry.isResting))
                },
                modifier = Modifier.wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (setEntry.isResting) Color.Red else Color(0xFF00FFAA)
                )
            ) {
                Text(if (setEntry.isResting) "Detener" else "Iniciar Descanso", color = Color.Black)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    onUpdate(setEntry.copy(showRestTimePicker = true))
                },
                modifier = Modifier.wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Establecer", color = Color.White)
            }
        }

        if (setEntry.showRestTimePicker) {
            RestTimePickerDialog(
                initialRestTime = setEntry.restTime,
                onDismiss = {
                    onUpdate(setEntry.copy(showRestTimePicker = false))
                },
                onConfirm = { newRestTimeInSeconds ->
                    onUpdate(setEntry.copy(restTime = newRestTimeInSeconds, showRestTimePicker = false))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestTimePickerDialog(
    initialRestTime: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val initialMinutes = initialRestTime / 60
    val initialSeconds = initialRestTime % 60

    val timePickerState = rememberTimePickerState(
        initialHour = initialMinutes,
        initialMinute = initialSeconds
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Seleccionar tiempo de descanso", color = Color.White)
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Minutos y Segundos:",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TimeInput(state = timePickerState)
            }
        },
        confirmButton = {
            Button(onClick = {
                val totalSeconds = (timePickerState.hour * 60) + timePickerState.minute
                onConfirm(totalSeconds)
            }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = Color(0xFF1C1C1E),
        titleContentColor = Color.White,
        textContentColor = Color.LightGray
    )
}

