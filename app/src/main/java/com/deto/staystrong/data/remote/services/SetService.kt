package com.deto.staystrong.data.remote.services

import com.deto.staystrong.data.Set
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SetService {

    @POST("api/routine-exercises/{routineExerciseId}/sets")
    suspend fun addSet(@Path("routineExerciseId") routineId: Int, @Body set: Set): SetService

    @PUT("api/sets/{setId}")
    suspend fun updateSetById(@Path("setId") setId: Int)

    @DELETE("api/sets/{setId}")
    suspend fun deleteSetById(@Path("setId") setId: Int)

    @GET("api/routines/{routine}/exercises/{routineExerciseId}/sets")
    suspend fun getSets(@Path("routine") routineId: Int, @Path("routineExerciseId") routineExerciseId: Int) : List<Set>
}