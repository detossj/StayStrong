package com.deto.staystrong.ui.set

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
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


@Composable
fun SetScreen(idRoutine: Int, idRoutineExercise: Int , nameExercise: String, viewModel: SetViewModel = viewModel(factory = AppViewModelProvider.Factory)) {


    LaunchedEffect(Unit) {
        viewModel.refreshSets(idRoutine, idRoutineExercise)
    }

    val uiState = viewModel.setUiState

    Scaffold(
        modifier = Modifier.fillMaxSize()
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

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = set.reps.toString(),
                                    onValueChange = {},
                                    label = { Text("Repeticiones") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = set.weight.toString(),
                                    onValueChange = {},
                                    label = { Text("Peso (kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.save_24px),
                                        contentDescription = "Guardar",
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {

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

                else -> {}

            }


            Button(
                onClick = { viewModel.addSet(idRoutine, idRoutineExercise)  },
                modifier = Modifier
                    .fillMaxWidth().padding(top = 10.dp),
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




