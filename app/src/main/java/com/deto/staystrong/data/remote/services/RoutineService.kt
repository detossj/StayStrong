package com.deto.staystrong.data.remote.services

import com.deto.staystrong.model.Routine
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoutineService {

    @GET("api/routines")
    suspend fun getRoutines(): List<Routine>

    @POST("api/routines")
    suspend fun addRoutine(@Body routine: Routine): Routine

    @POST("api/routines/default")
    suspend fun addDefaultRoutine(@Body routine: Routine)

    @DELETE("api/routines/{id}")
    suspend fun deleteRoutine(@Path("id") routineId: Int)

}