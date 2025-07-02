package com.deto.staystrong.data.remote.services

import com.deto.staystrong.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {

    @GET("api/recipes")
    suspend fun getRecipes(): List<Recipe>

    @GET("api/recipes/{id}")
    suspend fun getRecipesById(@Path("id") id: Int): Recipe
}