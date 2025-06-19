package com.deto.staystrong.ui.theme.exercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importante para MutableState y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.UUID

// =========================================================
data class SetEntry(
    val id: String = UUID.randomUUID().toString(),
    var kg: String = "",
    var reps: String = "",
    var isCompleted: Boolean = false,
    var restTime: Int = 210,
    var isResting: Boolean = false
)

data class ExerciseData(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val sets: MutableList<SetEntry> = mutableStateListOf(),
    val isExpanded: MutableState<Boolean> = mutableStateOf(false) // <--- ¡CAMBIO AQUÍ!
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ExerciseScreen(modifier: Modifier = Modifier) {
    val exercises = remember { mutableStateListOf<ExerciseData>() }
    var newExerciseNameInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newExerciseNameInput,
                    onValueChange = { newExerciseNameInput = it },
                    label = { Text("Añadir nuevo ejercicio...", color = Color.White) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        if (newExerciseNameInput.isNotBlank()) {
                            exercises.add(
                                ExerciseData(
                                    name = newExerciseNameInput.trim(),
                                    isExpanded = mutableStateOf(true), // <--- ¡CAMBIO AQUÍ!
                                    sets = mutableStateListOf(SetEntry(), SetEntry(), SetEntry())
                                )
                            )
                            newExerciseNameInput = ""
                        }
                    }),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00FFAA),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray
                    )
                )
                Button(
                    onClick = {
                        keyboardController?.hide()
                        if (newExerciseNameInput.isNotBlank()) {
                            exercises.add(
                                ExerciseData(
                                    name = newExerciseNameInput.trim(),
                                    isExpanded = mutableStateOf(true), // <--- ¡CAMBIO AQUÍ!
                                    sets = mutableStateListOf(SetEntry(), SetEntry(), SetEntry())
                                )
                            )
                            newExerciseNameInput = ""
                        }
                    },
                    enabled = newExerciseNameInput.isNotBlank()
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir Ejercicio")
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
                                    .clickable { exercise.isExpanded.value = !exercise.isExpanded.value } // <--- ¡CAMBIO AQUÍ!
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
                                    IconButton(onClick = { exercises.remove(exercise) }) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Eliminar Ejercicio",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = exercise.isExpanded.value, // <--- ¡CAMBIO AQUÍ!
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                ExerciseDetailsContent(
                                    exercise = exercise,
                                    onAddSet = { exercise.sets.add(SetEntry()) },
                                    onUpdateSet = { updatedSet ->
                                        val idx = exercise.sets.indexOfFirst { it.id == updatedSet.id }
                                        if (idx != -1) exercise.sets[idx] = updatedSet
                                    },
                                    onDeleteSet = { id -> exercise.sets.removeIf { it.id == id } }
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


// =========================================================
// ExerciseDetailsContent Composable (antes ExerciseBlock)
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsContent(
    exercise: ExerciseData,
    onAddSet: () -> Unit,
    onUpdateSet: (SetEntry) -> Unit,
    onDeleteSet: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Notas...",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Temporizador de descanso: 3min 30s",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF00FFAA),
            modifier = Modifier.padding(vertical = 8.dp)
        )

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetRow(
    setNumber: Int,
    setEntry: SetEntry,
    onUpdate: (SetEntry) -> Unit,
    onDelete: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
            text = "70kg x 8",
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
                onUpdate(setEntry.copy(isCompleted = isChecked))
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
}