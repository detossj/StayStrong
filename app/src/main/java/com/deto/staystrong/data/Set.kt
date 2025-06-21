package com.deto.staystrong.data
import androidx.room.PrimaryKey

data class Set(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val routine_exercise_id: Int,
    val reps: Int,
    val weight: Float
)