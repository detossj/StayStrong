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

// Importa directamente los objetos @Serializable de tu archivo de navegación.
// Estos objetos son las rutas que usas directamente, sin '.route'.
import com.deto.staystrong.Login
import com.deto.staystrong.Routines
import com.deto.staystrong.AuthManager as AuthManagerRoute // Alias para diferenciar del Composable

import com.deto.staystrong.ui.AppViewModelProvider

@Composable
fun AuthManager(navController: NavHostController, viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val authState = viewModel.authState

    LaunchedEffect(authState) {
        when (authState) {
            // Ahora observa el estado 'AuthUiState.SessionState'
            is AuthUiState.SessionState -> {
                // Accede a la propiedad 'loggedIn' del estado
                if (authState.loggedIn) {
                    // Navega usando directamente el objeto @Serializable 'Routines'
                    navController.navigate(Routines) {
                        // Usa el objeto @Serializable 'AuthManagerRoute' para popUpTo
                        popUpTo(AuthManagerRoute) {
                            inclusive = true // Quita esta pantalla de la pila de navegación
                        }
                    }
                } else { // Si el usuario NO está logueado
                    // Navega usando directamente el objeto @Serializable 'Login'
                    navController.navigate(Login) {
                        // Usa el objeto @Serializable 'AuthManagerRoute' para popUpTo
                        popUpTo(AuthManagerRoute) {
                            inclusive = true // Quita esta pantalla de la pila de navegación
                        }
                    }
                }
            }
            // Si el estado es Loading, Error o Success, no hacemos nada especial aquí,
            // la pantalla de carga se mostrará mientras el ViewModel trabaja.
            else -> {}
        }
    }

    // Muestra un indicador de progreso mientras el AuthViewModel determina el estado de la sesión.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = Color.Black
        )
    }
}