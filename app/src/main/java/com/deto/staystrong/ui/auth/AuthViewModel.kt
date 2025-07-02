package com.deto.staystrong.ui.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deto.staystrong.data.remote.TokenManager
import kotlinx.coroutines.launch
import com.deto.staystrong.data.remote.services.AuthService
import com.deto.staystrong.model.LoginRequest
import com.deto.staystrong.model.RegisterRequest

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class loggedIn(var logged: Boolean) : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()

}

class AuthViewModel(
    private val authService: AuthService,
    private val context: Context
) : ViewModel() {

    var authState: AuthUiState by mutableStateOf(AuthUiState.Idle)
        private set

    init {
        isLoggedIn()
    }
    fun login(email: String, password: String) {

        viewModelScope.launch {

            authState = AuthUiState.Loading

            try {

                val response = authService.login(LoginRequest(email, password))
                TokenManager.saveToken(context, response.token)
                authState = AuthUiState.Success(response.token)

            } catch (e: Exception) {

                authState = AuthUiState.Error(e.message ?: "Login failed")

            }
        }
    }

    fun register(name: String, email: String, password: String, validationPassword: String) {

        viewModelScope.launch {

            authState = AuthUiState.Loading

            try {

                val response = authService.register(RegisterRequest( name, email, password, validationPassword ))
                TokenManager.saveToken(context, response.token)
                authState = AuthUiState.Success(response.token)


            } catch (e: Exception) {

                authState = AuthUiState.Error(e.message ?: "Login failed")

            }
        }
    }

    fun logout() {

        viewModelScope.launch {
            try {
                val token = TokenManager.getToken(context)
                if (token != null) {
                    authService.logout("Bearer $token")
                }
                TokenManager.clearToken(context)
                authState = AuthUiState.Idle
            } catch (e: Exception) {
                authState = AuthUiState.Error("")
            }

        }
    }

    private fun isLoggedIn() {
        viewModelScope.launch {
            try {
//                val token = TokenManager.getToken(context)
//                if (token != null) {
//                    // Opción rápida: Confiar en el token local ( funciona para login persistente )
//                    authState = AuthUiState.loggedIn(true)
//
//
//
//                } else {
//                    authState = AuthUiState.loggedIn(false)
//                }

                val response = authService.getUser();
                authState = AuthUiState.loggedIn(true)
            } catch (e: Exception) {
                authState = AuthUiState.loggedIn(false)
            }
        }
    }


}