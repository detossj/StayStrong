package com.deto.staystrong.ui.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.deto.staystrong.data.remote.ApiClient.BASE_URL
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomBottomAppBar
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.deto.staystrong.ui.exercise.rememberExerciseImagePainter


@Composable
fun RecipeScreen(navController: NavController, idRecipe: Int, viewModel: RecipeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.recipeUiState

    LaunchedEffect(Unit) {
        viewModel.getRecipeById(idRecipe)
    }

    Scaffold(
        bottomBar = { CustomBottomAppBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {

            when (uiState) {
                is RecipeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CustomCircularProgressIndicator("receta")
                    }
                }

                is RecipeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${uiState.message}")
                    }
                }

                is RecipeUiState.Success -> {
                    val recipe = uiState.recipe
                    val ingredientsList = recipe.ingredients.split("\n").filter { it.isNotBlank() }
                    val stepsList = recipe.steps.split("\n").filter { it.isNotBlank() }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item {

                            val painter = rememberExerciseImagePainter(recipe.image_path)

                            Image(
                                painter = painter,
                                contentDescription = recipe.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Spacer(modifier = Modifier.padding(8.dp))


                            Text(
                                text = recipe.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.padding(4.dp))


                            Text(
                                text = recipe.description,
                                fontSize = 16.sp,
                                color = Color.LightGray
                            )

                            Spacer(modifier = Modifier.padding(8.dp))


                            Text(
                                text = "Calorías: ${recipe.calories}",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.padding(8.dp))


                            Text(
                                text = "Ingredientes",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.padding(4.dp))
                        }


                        items(ingredientsList.size) { index ->
                            Text(
                                text = "• ${ingredientsList[index]}",
                                color = Color.LightGray,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.padding(12.dp))

                            Text(
                                text = "Pasos",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.padding(4.dp))
                        }

                        items(stepsList.size) { index ->
                            Text(
                                text = "${index + 1}. ${stepsList[index]}",
                                color = Color.LightGray,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
