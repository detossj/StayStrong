package com.deto.staystrong.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.deto.staystrong.AuthManager
import com.deto.staystrong.Home
import com.deto.staystrong.Login
import com.deto.staystrong.ui.AppViewModelProvider

@Composable
fun AuthManager(navController: NavHostController, viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val authState = viewModel.authState

    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.loggedIn -> {
                if (authState.logged) {
                    navController.navigate(Home) {
                        popUpTo(AuthManager) {
                            inclusive = true
                        }
                    }
                }
                if (!authState.logged) {
                    navController.navigate(Login) {
                        popUpTo(AuthManager) {
                            inclusive = true
                        }
                    }
                }
            }
            else -> {}
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = Color.Black
        )
    }
}