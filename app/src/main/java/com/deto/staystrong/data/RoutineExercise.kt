package com.deto.staystrong.data
import androidx.room.PrimaryKey


data class RoutineExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val routine_id: Int,
    val exercise_id: Int,
    val order: Int,
    val exercise: Exercise

)
