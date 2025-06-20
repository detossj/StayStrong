package com.deto.staystrong.data
import androidx.room.PrimaryKey

data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val date: String
)
