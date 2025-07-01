package com.deto.staystrong.ui.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.remote.services.RecipeService
import com.deto.staystrong.model.Recipe

import kotlinx.coroutines.launch

sealed class RecipesUiState {
    data class Success(val recipes: List<Recipe>) : RecipesUiState()
    data class Error(val message: String) : RecipesUiState()
    object Loading : RecipesUiState()
    object Idle : RecipesUiState()
}

class RecipesViewModel(private val recipesService: RecipeService) : ViewModel(){
    var recipesUiState: RecipesUiState by mutableStateOf(RecipesUiState.Loading)
        private set

    private fun getRecipes() {
        viewModelScope.launch {
            recipesUiState = RecipesUiState.Loading
            try {
                val listRecipes = recipesService.getRecipes()
                recipesUiState = RecipesUiState.Success(listRecipes)
            } catch (e: Exception) {
                recipesUiState = RecipesUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun refreshRecipes() {
        getRecipes()
    }

}