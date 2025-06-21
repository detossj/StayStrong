package com.deto.staystrong.data

import kotlinx.coroutines.flow.Flow

class ExerciseRepository (
    private val exerciseDao: ExerciseDao
) {
    suspend fun addExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise)
    }

    fun getExercises(): Flow<List<Exercise>> {
        return exerciseDao.getAllExercises()
    }
}