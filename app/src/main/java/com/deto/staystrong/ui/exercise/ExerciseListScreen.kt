package com.deto.staystrong.ui.exercise

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.request.CachePolicy
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.data.Exercise
import coil.imageLoader
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.deto.staystrong.ui.routineExercise.RoutineExerciseViewModel


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExerciseListScreen(navController: NavController, idRoutine: Int) {



    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

    AnimatedContent(
        targetState = selectedExercise,
        transitionSpec = {
            fadeIn(tween(300)) + scaleIn(tween(300)) with
                    fadeOut(tween(300)) + scaleOut(tween(300))
        },
    ) { exercise ->
        if (exercise == null) {
            ExerciseGridScreen(onExerciseClick = { selectedExercise = it })
        } else {
            ExpandedMuscleView(
                navController = navController,
                idRoutine = idRoutine,
                exercise = exercise,
                onBack = { selectedExercise = null }
            )
        }
    }
}

@Composable
fun ExerciseGridScreen(onExerciseClick: (Exercise) -> Unit, viewModel: ExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    LaunchedEffect(Unit) {
        viewModel.refreshExercises()
    }


    val uiState = viewModel.exercisesUiState

    val context = LocalContext.current

    LaunchedEffect(uiState) {
        if (uiState is ExerciseUiState.Success) {
            uiState.exercises.forEach { exercise ->
                val request = ImageRequest.Builder(context)
                    .data("http://192.168.1.91:8000/${exercise.image_path}")
                    .build()
                //context.imageLoader.enqueue(request) // precarga

            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿Qué quieres entrenar hoy?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 70.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            when (uiState) {
                is ExerciseUiState.Loading -> {
                    CustomCircularProgressIndicator("ejercicios")
                }

                is ExerciseUiState.Error -> {
                    Text(text = "Error: ${uiState.message}")
                }

                is ExerciseUiState.Success -> {
                    val exercises = uiState.exercises

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(exercises) { exercise ->
                            ExerciseCard(
                                exercise = exercise,
                                onClick = { onExerciseClick(exercise) }
                            )
                        }
                    }
                }

                else-> {}
            }



        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise, onClick: () -> Unit) {

    val painter = rememberExerciseImagePainter(exercise.image_path)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray)
            .clickable { onClick() }
            .padding(16.dp)
            .width(160.dp)
            .height(180.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painter,
            contentDescription = exercise.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = exercise.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center

        )
        Text(
            text = exercise.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ExpandedMuscleView( navController: NavController, idRoutine: Int, exercise: Exercise, onBack: () -> Unit, viewModel: RoutineExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    BackHandler {
        onBack()
    }
    val painter = rememberExerciseImagePainter(exercise.image_path)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painter,
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))

            )


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = exercise.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tipo de Ejercicio:",
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )

            Text(
                text = exercise.description,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.addRoutineExercise(idRoutine,exercise.id)
                    navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Añadir Ejercicio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
fun rememberExerciseImagePainter(imagePath: String): Painter {
    val context = LocalContext.current
    return rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data("http://192.168.1.91:8000/$imagePath")
            .crossfade(true)
            .decoderFactory(
                if (Build.VERSION.SDK_INT >= 28)
                    ImageDecoderDecoder.Factory()
                else
                    GifDecoder.Factory()
            )

            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
}




