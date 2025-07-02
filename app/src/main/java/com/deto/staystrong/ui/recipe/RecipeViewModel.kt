package com.deto.staystrong.ui.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.remote.services.RecipeService
import com.deto.staystrong.model.Recipe
import kotlinx.coroutines.launch

sealed class RecipeUiState {
    data class Success(val recipe: Recipe) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
    object Loading : RecipeUiState()
    object Idle : RecipeUiState()
}

class RecipeViewModel(private val recipeService: RecipeService) : ViewModel(){
    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)
        private set



    fun getRecipeById(idRecipe: Int){
        viewModelScope.launch {
            recipeUiState = RecipeUiState.Loading
            try {
                val recipe = recipeService.getRecipesById(idRecipe)
                recipeUiState = RecipeUiState.Success(recipe)
            }catch (e: Exception) {
                recipeUiState = RecipeUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }



}