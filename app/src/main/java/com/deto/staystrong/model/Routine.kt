package com.deto.staystrong.model


data class Routine(
    val id: Int,
    val user_id: Int,
    val date: String,
    val type: String,
    val muscle_group: String?
)