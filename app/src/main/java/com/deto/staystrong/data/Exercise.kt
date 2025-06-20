package com.deto.staystrong.data.remo
import androidx.room.PrimaryKey


data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String
)
