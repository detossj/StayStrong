package com.deto.staystrong.ui.routineExercise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.ExerciseList
import com.deto.staystrong.Set
import com.deto.staystrong.model.RoutineExercise
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.deto.staystrong.ui.components.CustomFloatingActionButton
import com.deto.staystrong.ui.components.CustomTopAppBar
import com.deto.staystrong.ui.exercise.rememberExerciseImagePainter


@Composable
fun RoutineScreen( navController: NavController, idRoutine: Int , formattedDate: String, viewModel: RoutineExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    LaunchedEffect(Unit) {
        viewModel.refreshRoutineExercise(idRoutine)
    }

    val uiState = viewModel.routineExerciseUiState

    Scaffold(
        topBar = {
            CustomTopAppBar(navController = navController)
        },
        floatingActionButton = {
            CustomFloatingActionButton({
                navController.navigate(ExerciseList(idRoutine))
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )   {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Rutina de $formattedDate",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is RoutineExerciseUiState.Loading -> {
                    CustomCircularProgressIndicator("rutina")
                }

                is RoutineExerciseUiState.Error -> {
                    Text(text = "Error: ${uiState.message}")
                }

                is RoutineExerciseUiState.Success -> {
                    val routine = uiState.routine
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    ) {
                        items(routine) { exercise ->
                            ExerciseItem(navController, idRoutine,exercise)
                        }
                    }

                }

                else -> {}
            }



        }
    }
}

@Composable
fun ExerciseItem( navController: NavController, idRoutine: Int, routineExercise: RoutineExercise, viewModel: RoutineExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { navController.navigate(Set(idRoutine, routineExercise.id, routineExercise.exercise?.name ?: "")) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                val painter = rememberExerciseImagePainter(routineExercise.exercise?.image_path ?: "")

                Image(
                    painter = painter,
                    contentDescription = routineExercise.exercise?.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(15.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))


                Column(
                    modifier = Modifier.widthIn(max = 180.dp)
                ) {
                    Text(
                        text = routineExercise.exercise?.name ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = routineExercise.exercise?.description ?: "",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            viewModel.deleteRoutineExerciseById(idRoutine, routineExercise.id)
                        }
                    )
                }
            }

        }
    }
}



