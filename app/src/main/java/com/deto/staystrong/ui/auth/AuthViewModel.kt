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
    // Renombrado para ser más explícito sobre el estado de la sesión.
    data class SessionState(val loggedIn: Boolean) : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val authService: AuthService,
    private val context: Context // Asegúrate de que el contexto sea ApplicationContext en la inyección de dependencia
) : ViewModel() {

    var authState: AuthUiState by mutableStateOf(AuthUiState.Idle)
        private set

    init {
        // Al inicializar el ViewModel, verifica inmediatamente el estado de login
        // usando el token guardado localmente.
        checkLocalLoginStatusOnAppStartup()
    }

    // Esta función se encarga de determinar el estado de la sesión al inicio de la app.
    private fun checkLocalLoginStatusOnAppStartup() {
        viewModelScope.launch {
            val localToken = TokenManager.getToken(context)
            if (localToken != null) {
                // Si existe un token guardado localmente, asumimos que el usuario está logueado.
                // Esta es la clave de la persistencia: no necesitas internet para saber si hay sesión.
                authState = AuthUiState.SessionState(true)

                // OPCIONAL: Si deseas una validación más robusta (ej. token expirado en el servidor)
                // puedes descomentar este bloque. Esto hará una llamada a la API.
                /*
                try {
                    // Intenta hacer una llamada a una ruta protegida (ej. obtener perfil de usuario)
                    // Si falla, significa que el token no es válido en el servidor.
                    authService.getUser() // Asumiendo que esta llamada es protegida
                    authState = AuthUiState.SessionState(true) // Token es válido
                } catch (e: Exception) {
                    // Si la llamada falla (ej. 401 Unauthorized), el token local es inválido.
                    TokenManager.clearToken(context) // Limpia el token inválido
                    authState = AuthUiState.SessionState(false)
                }
                */

            } else {
                // Si no hay token guardado localmente, el usuario no ha iniciado sesión.
                authState = AuthUiState.SessionState(false)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthUiState.Loading
            try {
                val response = authService.login(LoginRequest(email, password))
                // Guarda el token exitosamente obtenido de la API
                TokenManager.saveToken(context, response.token)
                // Actualiza el estado a éxito, que tu LoginScreen observará para navegar.
                authState = AuthUiState.Success(response.token)
                // Opcional: También podrías actualizar el estado de sesión directamente aquí
                // si AuthUiState.Success no te parece suficiente para el comportamiento futuro.
                // authState = AuthUiState.SessionState(true)

            } catch (e: Exception) {
                // En caso de error, limpia el token si hubiera alguno (por si acaso)
                TokenManager.clearToken(context)
                authState = AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(name: String, email: String, password: String, validationPassword: String) {
        viewModelScope.launch {
            authState = AuthUiState.Loading
            try {
                val response = authService.register(RegisterRequest( name, email, password, validationPassword ))
                // Guarda el token exitosamente obtenido de la API
                TokenManager.saveToken(context, response.token)
                // Actualiza el estado a éxito
                authState = AuthUiState.Success(response.token)
                // Opcional: authState = AuthUiState.SessionState(true)

            } catch (e: Exception) {
                // En caso de error, limpia el token si hubiera alguno
                TokenManager.clearToken(context)
                authState = AuthUiState.Error(e.message ?: "Registration failed") // Mensaje más descriptivo
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken(context)
                if (token != null) {
                    // Solo intentar logout en la API si hay un token
                    authService.logout("Bearer $token")
                }
                // Siempre limpia el token local, independientemente del éxito de la API de logout
                TokenManager.clearToken(context)
                authState = AuthUiState.SessionState(false) // Establece el estado a no logueado
            } catch (e: Exception) {
                // Aunque la llamada a la API de logout falle, el token local se ha limpiado.
                // Podrías mostrar un error al usuario si es crítico, pero la sesión local ya no existe.
                TokenManager.clearToken(context) // Asegurarse de limpiar de nuevo por si acaso
                authState = AuthUiState.SessionState(false) // Establece el estado a no logueado
                // Si quieres mostrar un mensaje de error de logout:
                // authState = AuthUiState.Error(e.message ?: "Logout failed but local session cleared")
            }
        }
    }

    // Si tuvieras una función `getUser()` que solo obtiene el perfil, no la uses para la persistencia inicial.
    // Solo úsala cuando realmente necesites los datos del usuario después de confirmar que está logueado.
    /*
    fun getUserProfile() {
        viewModelScope.launch {
            // Esto solo se llamaría si ya sabes que el usuario está (o debería estar) logueado
            try {
                val user = authService.getUser()
                // Aquí podrías actualizar otro LiveData para los datos del usuario.
                // Por ejemplo: _userProfile.value = user
            } catch (e: Exception) {
                // Manejar error al obtener el perfil, quizás el token expiró.
                // En este caso, podrías forzar un logout local:
                // TokenManager.clearToken(context)
                // authState = AuthUiState.SessionState(false)
            }
        }
    }
    */
}