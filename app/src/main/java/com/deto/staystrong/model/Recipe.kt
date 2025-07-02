package com.deto.staystrong.model


data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: String,
    val steps: String,
    val calories: String,
    val image_path: String
)