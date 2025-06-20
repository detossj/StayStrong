package com.deto.staystrong.data
import androidx.room.PrimaryKey


data class RoutineExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val routine_id: Int = 0,
    val exercise_id: Int = 0,
    val order: Int = 0,

)
