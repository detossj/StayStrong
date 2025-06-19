package com.deto.staystrong.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import com.deto.staystrong.StayStrong

object ViewModelProvider {
    val Factory = viewModelFactory {

    }
}

fun CreationExtras.App(): StayStrong =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as StayStrong)