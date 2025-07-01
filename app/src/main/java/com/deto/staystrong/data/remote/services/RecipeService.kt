package com.deto.staystrong.data.remote.services

import com.deto.staystrong.model.Recipe
import retrofit2.http.GET

interface RecipeService {

    @GET("api/recipes")
    suspend fun getRecipes(): List<Recipe>

    @GET("api/recipes/{id}")
    suspend fun getRecipesById(id: Int): Recipe
}