package com.deto.staystrong.data.remote

import android.content.Context
import com.deto.staystrong.data.remote.services.AuthService

interface AppContainer {
    val authUiState: AuthService
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val authUiState: AuthService by lazy {
        ApiClient.create(context).create(AuthService::class.java)
    }

}