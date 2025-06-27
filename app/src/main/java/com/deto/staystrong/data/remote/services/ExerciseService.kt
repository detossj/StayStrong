package com.deto.staystrong.data.remote.services


import com.deto.staystrong.model.Exercise
import retrofit2.http.GET

interface ExerciseService {

    @GET("api/exercises")
    suspend fun getExercises(): List<Exercise>

}