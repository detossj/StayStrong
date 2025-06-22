package com.deto.staystrong.ui.routineExercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.ExerciseList
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomFloatingActionButton
import com.deto.staystrong.ui.components.CustomTopAppBar
import com.deto.staystrong.ui.routine.RoutinesViewModel

@Composable
fun RoutineScreen( navController: NavController, idRoutine: Int , viewModel: RoutinesViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    var exercises by remember { mutableStateOf(listOf<Exercise>()) }

    Scaffold(
        topBar = {
            CustomTopAppBar(navController = navController)
        },
        floatingActionButton = {
            CustomFloatingActionButton({
                navController.navigate(ExerciseList)
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
                    text = "Rutina de Hoy",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                items(exercises) { exercise ->
                    ExerciseItem(exercise)
                }
            }

        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nombre: ${exercise.name}", fontWeight = FontWeight.Bold)
            Text("Series: ${exercise.sets}")
            Text("Repeticiones: ${exercise.reps}")
        }
    }
}

// Modelo de datos
data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int
)
