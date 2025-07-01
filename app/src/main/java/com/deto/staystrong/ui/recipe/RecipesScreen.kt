package com.deto.staystrong.ui.recipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.ui.AppViewModelProvider

@Composable
fun RecipesScreen(navController : NavController, viewModel: RecipesViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    val uiState = viewModel.recipesUiState

    LaunchedEffect(Unit) {
        viewModel.refreshRecipes()
    }
}