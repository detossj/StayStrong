package com.deto.staystrong.data.remote.services
import com.deto.staystrong.model.MonthlyVolume
import retrofit2.http.GET
import retrofit2.http.Query

interface MonthlyVolumeService {

    @GET("api/monthly-volume")
    suspend fun getMonthlyVolumes(@Query("user_id") userId: Int): List<MonthlyVolume>

}