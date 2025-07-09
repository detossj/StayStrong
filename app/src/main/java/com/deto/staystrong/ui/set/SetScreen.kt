package com.deto.staystrong.ui.set

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.deto.staystrong.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.navigation.NavController
import com.deto.staystrong.ui.components.CustomBottomAppBar


@Composable
fun SetScreen(navController: NavController, idRoutine: Int, idRoutineExercise: Int , nameExercise: String, viewModel: SetViewModel = viewModel(factory = AppViewModelProvider.Factory)) {


    LaunchedEffect(Unit) {
        viewModel.refreshSets(idRoutine, idRoutineExercise)
    }

    val uiState = viewModel.setUiState

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(navController)
        },
        containerColor = Color.Black
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (uiState) {
                is SetUiState.Loading -> {
                    CustomCircularProgressIndicator("sets")
                }

                is SetUiState.Error -> {
                    Text(text = "Error: ${uiState.message}")
                }

                is SetUiState.Success -> {
                    val sets = uiState.sets

                    Text(
                        text = nameExercise,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 70.dp, bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        items(sets) { set ->

                            val originalReps = set.reps.toString()
                            val originalWeight = set.weight.toString()
                            var reps by remember { mutableStateOf(originalReps) }
                            var weight by remember { mutableStateOf(originalWeight) }

                            val hasChanged = reps != originalReps || weight != originalWeight


                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF1E1E1E),
                                    contentColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = reps,
                                            onValueChange = { reps = it },
                                            label = { Text("Reps") },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(R.drawable.exercise_24px),
                                                    contentDescription = null
                                                )
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            modifier = Modifier.weight(1f),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = Color.Gray,
                                                focusedBorderColor = Color.White,
                                                cursorColor = Color.White,
                                                focusedLabelColor = Color.White,
                                                unfocusedLabelColor = Color.Gray,
                                                focusedTextColor = Color.White
                                            )
                                        )

                                        OutlinedTextField(
                                            value = weight,
                                            onValueChange = { weight = it },
                                            label = { Text("Peso (kg)") },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(R.drawable.weight_24px),
                                                    contentDescription = null
                                                )
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            modifier = Modifier.weight(1f),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = Color.Gray,
                                                focusedBorderColor = Color.White,
                                                cursorColor = Color.White,
                                                focusedLabelColor = Color.White,
                                                unfocusedLabelColor = Color.Gray,
                                                focusedTextColor = Color.White
                                            )
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            onClick = {
                                                val updatedSet = set.copy(
                                                    reps = reps.toIntOrNull() ?: set.reps,
                                                    weight = weight.toFloatOrNull() ?: set.weight
                                                )
                                                viewModel.updateSet(updatedSet, idRoutine, idRoutineExercise)
                                            },
                                            enabled = hasChanged
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.save_24px),
                                                contentDescription = "Guardar",
                                                tint = if (hasChanged) Color.White else Color.Gray
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                viewModel.deleteSetById(set.id, idRoutine, idRoutineExercise)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }

                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Button(
                                onClick = { viewModel.addSet(idRoutine, idRoutineExercise)  },
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(
                                    text = "Añadir Set",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }



                        }

                    }


                }

                else -> {}

            }

        }

    }
}




