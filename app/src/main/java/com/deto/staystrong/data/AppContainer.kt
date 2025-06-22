package com.deto.staystrong.data

import android.content.Context
import com.deto.staystrong.data.remote.ApiClient
import com.deto.staystrong.data.remote.services.AuthService
import com.deto.staystrong.data.remote.services.ExerciseService
import com.deto.staystrong.data.remote.services.RoutineService

interface AppContainer {
    val authApiService: AuthService
    val routineApiService: RoutineService
    val exerciseApiService: ExerciseService
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val authApiService: AuthService by lazy {
        ApiClient.create(context).create(AuthService::class.java)
    }

    override val routineApiService: RoutineService by lazy {
        ApiClient.create(context).create(RoutineService::class.java)
    }

    override val exerciseApiService: ExerciseService by lazy {
        ApiClient.create(context).create(ExerciseService::class.java)
    }

}