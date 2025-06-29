package com.deto.staystrong.data.remote.services


import com.deto.staystrong.model.RoutineVideo
import retrofit2.http.GET

interface RoutineVideoService {

    @GET("api/videos/random")
    suspend fun getRoutineVideos(): List<RoutineVideo>
}