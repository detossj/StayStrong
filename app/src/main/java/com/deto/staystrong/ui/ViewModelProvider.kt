package com.deto.staystrong.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.deto.staystrong.StayStrong
import com.deto.staystrong.ui.auth.AuthViewModel
import com.deto.staystrong.ui.routine.RoutinesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            AuthViewModel(App().container.authApiService, App().baseContext)
        }
        initializer {
            RoutinesViewModel(App().container.routineApiService)
        }
    }
}

fun CreationExtras.App(): StayStrong =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as StayStrong)