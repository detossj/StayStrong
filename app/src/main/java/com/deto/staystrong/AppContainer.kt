package com.deto.staystrong

import android.app.Application
import com.deto.staystrong.data.remote.AppContainer
import com.deto.staystrong.data.remote.AppDataContainer

class StayStrong : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}