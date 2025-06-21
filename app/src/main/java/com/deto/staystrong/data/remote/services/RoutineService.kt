package com.deto.staystrong.data.remote.services

import com.deto.staystrong.data.Routine
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoutineService {

    @GET("api/routines")
    suspend fun getRoutines(): List<Routine>

    @POST("api/routines")
    suspend fun addRoutine(@Body routine: Routine): Routine

    @GET("api/routines/{id}")
    suspend fun getRoutineById(@Path("id") id: Int): Routine


}