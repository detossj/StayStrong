package com.deto.staystrong.data.remote.services



import com.deto.staystrong.model.RoutineExercise
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoutineExerciseService {

    @POST("api/routines/{routine}/exercises")
    suspend fun addRoutineExercise(@Path("routine") routineId: Int, @Body routineExercise: RoutineExercise): RoutineExercise

    @GET("api/routines/{routine}/exercises")
    suspend fun getRoutineExerciseById(@Path("routine") routineId: Int): List<RoutineExercise>

    @DELETE("api/routines/{routine}/exercises/{routineExerciseId}")
    suspend fun deleteRoutineExerciseById(@Path("routine") routineId: Int, @Path("routineExerciseId") routineExerciseId: Int ): RoutineExercise
}