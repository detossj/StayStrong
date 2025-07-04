package com.deto.staystrong.data

import android.content.Context
import com.deto.staystrong.data.remote.ApiClient
import com.deto.staystrong.data.remote.services.AuthService
import com.deto.staystrong.data.remote.services.ExerciseService
import com.deto.staystrong.data.remote.services.MonthlyVolumeService
import com.deto.staystrong.data.remote.services.RecipeService
import com.deto.staystrong.data.remote.services.RoutineExerciseService
import com.deto.staystrong.data.remote.services.RoutineService
import com.deto.staystrong.data.remote.services.RoutineVideoService
import com.deto.staystrong.data.remote.services.SetService

interface AppContainer {
    val authApiService: AuthService
    val routineApiService: RoutineService
    val exerciseApiService: ExerciseService
    val routineExerciseApiService: RoutineExerciseService
    val setApiService: SetService
    val routineVideoService: RoutineVideoService
    val recipeService: RecipeService
    val monthlyVolumeService: MonthlyVolumeService
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

    override val routineExerciseApiService: RoutineExerciseService by lazy {
        ApiClient.create(context).create(RoutineExerciseService::class.java)
    }

    override val setApiService: SetService by lazy {
        ApiClient.create(context).create(SetService::class.java)
    }

    override val routineVideoService: RoutineVideoService by lazy {
        ApiClient.create(context).create(RoutineVideoService::class.java)
    }

    override val recipeService: RecipeService by lazy {
        ApiClient.create(context).create(RecipeService::class.java)
    }

    override val monthlyVolumeService: MonthlyVolumeService by lazy {
        ApiClient.create(context).create(MonthlyVolumeService::class.java)
    }



}