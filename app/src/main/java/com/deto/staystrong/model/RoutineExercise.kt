package com.deto.staystrong.model


data class RoutineExercise(
    val id: Int,
    val routine_id: Int,
    val exercise_id: Int,
    val exercise:  Exercise? = null

)