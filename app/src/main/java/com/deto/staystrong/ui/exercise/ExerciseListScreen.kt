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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.request.CachePolicy
import androidx.compose.runtime.*
import com.deto.staystrong.R
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.model.Exercise
import coil.imageLoader
import com.deto.staystrong.data.remote.ApiClient.BASE_URL
import com.deto.staystrong.ui.components.CustomBottomAppBar
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.deto.staystrong.ui.routineExercise.RoutineExerciseViewModel
import java.text.Normalizer


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExerciseListScreen(navController: NavController, idRoutine: Int) {



    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
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
        }
    }


}

@Composable
fun ExerciseGridScreen(onExerciseClick: (Exercise) -> Unit, viewModel: ExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    var exerciseFilter by remember { mutableStateOf("") }

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
        modifier = Modifier
            .fillMaxSize()
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
                    var exercisesListFilter = exercises.filter {
                        it.name.normalize().contains(exerciseFilter.normalize()) || it.description.normalize().contains(exerciseFilter.normalize())
                    }


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            OutlinedTextField(
                                value = exerciseFilter,
                                onValueChange = { exerciseFilter = it },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Buscar ejercicios"
                                    )
                                },
                                placeholder = { Text(text = stringResource(R.string.SearchFilter)) },
                                shape = RoundedCornerShape(30.dp)
                            )
                        }

                        items(exercisesListFilter) { exercise ->
                            ExerciseCard(
                                exercise = exercise,
                                exercisesListFilter = exerciseFilter,
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
fun ExerciseCard(exercise: Exercise, exercisesListFilter: String, onClick: () -> Unit) {

    val painter = rememberExerciseImagePainter(exercise.image_path)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray)
            .clickable { onClick() }
            .padding(16.dp)
            .width(160.dp)
            .height(230.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
            text = highlightMatch(exercise.name,exercisesListFilter,Color(0xFFFF9800)),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = highlightMatch(exercise.description,exercisesListFilter,Color(0xFFFF9800)),
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
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.DarkGray, Color.Black)
                )
            )

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
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Grupos musculares trabajados:",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )

            Text(
                text = exercise.description,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
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
            .data(BASE_URL + "/"+ "$imagePath")
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

@Composable
fun highlightMatch(text: String, filter: String, highlightColor: Color): AnnotatedString {
    return buildAnnotatedString {
        if (filter.isEmpty()) {
            append(text)
            return@buildAnnotatedString
        }

        val normalizedFilter = filter.normalize()
        var i = 0

        while (i < text.length) {

            val remainingText = text.substring(i)
            val normalizedRemaining = remainingText.normalize()

            val matchIndex = normalizedRemaining.indexOf(normalizedFilter)
            if (matchIndex == -1) {
                append(text.substring(i))
                break
            } else {
                val realMatchStart = i + matchIndex
                val realMatchEnd = realMatchStart + text.substring(realMatchStart).take(filter.length).length

                append(text.substring(i, realMatchStart))

                withStyle(style = SpanStyle(color = highlightColor, fontWeight = FontWeight.Bold)) {
                    append(text.substring(realMatchStart, realMatchEnd))
                }

                i = realMatchEnd
            }
        }
    }
}



fun String.normalize(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()
}





