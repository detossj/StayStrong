package com.deto.staystrong.ui.home

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.ui.AppViewModelProvider
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.deto.staystrong.Routines
import com.deto.staystrong.model.RoutineVideo
import com.deto.staystrong.ui.auth.AuthViewModel
import com.deto.staystrong.ui.components.CustomBottomAppBar
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator

@Composable
fun HomeScreen(navController: NavController, viewModel: RoutineVideoViewModel = viewModel(factory = AppViewModelProvider.Factory), authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val uiState = viewModel.routineVideoUiState

    val userData = authViewModel.userData
    val userName = userData?.name ?: return
    LaunchedEffect(Unit) {
        viewModel.refreshRoutineVideos()
    }

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(navController)
        },
        containerColor = Color.Black
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(70.dp))

                Text(
                    text = "¿Listo para empezar a levantar, $userName?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Simplemente comienza tu entrenamiento y agrega tus ejercicios favoritos. ¡Tus estadísticas estarán listas cuando termines!",
                    fontSize = 16.sp,
                    color = Color.Gray,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { navController.navigate(Routines) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Empezar el entrenamiento de hoy",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "¿No sabes cómo entrenar?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            when (uiState) {
                is RoutineVideoUiState.Loading -> item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        CustomCircularProgressIndicator("videos")
                    }
                }

                is RoutineVideoUiState.Error -> item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${uiState.message}")
                    }
                }

                is RoutineVideoUiState.Success -> {
                    items(uiState.routineVideos) { video ->
                        RoutineVideoCard(video = video)
                    }
                }

                RoutineVideoUiState.Idle -> item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay videos disponibles")
                    }
                }
            }
        }
    }
}




fun extractYoutubeId(url: String): String {
    return Uri.parse(url).getQueryParameter("v")
        ?: url.substringAfterLast("/") // para enlaces tipo youtu.be/xxxxx
}

fun getYoutubeThumbnailUrl(youtubeUrl: String): String {
    val videoId = extractYoutubeId(youtubeUrl)
    return "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
}

@Composable
fun RoutineVideoCard(video: RoutineVideo) {
    val context = LocalContext.current
    val thumbnailUrl = getYoutubeThumbnailUrl(video.video_url)

    Card(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.video_url))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {

            Image(
                painter = rememberAsyncImagePainter(thumbnailUrl),
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}