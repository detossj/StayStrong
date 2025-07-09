package com.deto.staystrong.ui.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.deto.staystrong.data.remote.ApiClient.BASE_URL
import com.deto.staystrong.model.Recipe
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomBottomAppBar
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.deto.staystrong.ui.exercise.rememberExerciseImagePainter
import com.deto.staystrong.ui.home.RoutineVideoUiState

@Composable
fun RecipesScreen(navController : NavController, viewModel: RecipesViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    val uiState = viewModel.recipesUiState

    LaunchedEffect(Unit) {
        viewModel.refreshRecipes()
    }

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(navController)
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Text(
                    text = "Recetas Fit",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 50.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                when (uiState) {
                    is RecipesUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CustomCircularProgressIndicator("recetas")
                        }
                    }
                    is RecipesUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ${uiState.message}")
                        }
                    }
                    is RecipesUiState.Success -> {
                        val recipes = uiState.recipes
                        RecipesGrid(navController = navController ,recipes = recipes)
                    }

                    else -> {}
                }

            }

        }

    }

}

@Composable
fun RecipesGrid(navController : NavController,recipes: List<Recipe>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recipes) { recipe ->
            RecipeCard(navController, recipe)
        }
    }

}

@Composable
fun RecipeCard(navController : NavController, recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable {
                navController.navigate(com.deto.staystrong.Recipe(recipe.id))
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter = rememberExerciseImagePainter(recipe.image_path)

            Image(
                painter = painter,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}