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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.deto.staystrong.model.RoutineVideo
import com.deto.staystrong.ui.components.CustomBottomAppBar
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator

@Composable
fun HomeScreen(navController: NavController, viewModel: RoutineVideoViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val uiState = viewModel.routineVideoUiState

    LaunchedEffect(Unit) {
        viewModel.refreshRoutineVideos()
    }

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )   {
                Text(
                    text = "¿No sabes como entrenar?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 70.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                when (uiState) {
                    is RoutineVideoUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CustomCircularProgressIndicator("videos")
                        }
                    }
                    is RoutineVideoUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ${uiState.message}")
                        }
                    }
                    is RoutineVideoUiState.Success -> {
                        val routineVideos = uiState.routineVideos
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(routineVideos) { video ->
                                RoutineVideoCard(video = video)
                            }
                        }
                    }
                    RoutineVideoUiState.Idle -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay videos disponibles")
                        }
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
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.video_url))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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