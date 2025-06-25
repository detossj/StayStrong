package com.deto.staystrong.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.Login // Importa el objeto @Serializable Login
import com.deto.staystrong.Register // Importa el objeto @Serializable Register
import com.deto.staystrong.Routines // Importa el objeto @Serializable Routines
import com.deto.staystrong.R // Asegúrate de que R esté correctamente importado para tus recursos
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomButtonLoginAndRegister
import com.deto.staystrong.ui.components.CustomOutlinedTextFieldLoginAndRegister


@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorEmail by remember { mutableStateOf(false) }
    // CORRECCIÓN: 'mutableToStateOf' debe ser 'mutableStateOf'
    var errorPassword by remember { mutableStateOf(false) }

    val authState = viewModel.authState
    val scope = rememberCoroutineScope() // Se mantiene por si necesitas ejecutar coroutines aquí

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            // Navega a la pantalla de Rutinas una vez que el login es exitoso
            navController.navigate(Routines) {
                // popUpTo remueve la pantalla de Login de la pila de navegación
                popUpTo(Login) { inclusive = true }
            }
            // Opcional: Si quieres resetear el estado del ViewModel después de una navegación exitosa,
            // descomenta lo siguiente. Esto puede ser útil para evitar re-trigger si la pantalla
            // se recompone o si se vuelve a ella por alguna razón inusual.
            // scope.launch {
            //     viewModel.authState = AuthUiState.Idle
            // }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // IMAGEN DE FONDO
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // CAPA SEMI-TRANSPARENTE SOBRE LA IMAGEN
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.1f)) // Ajusta la opacidad si es necesario
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // LOGO DE LA APLICACIÓN
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo Aplicacion",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape) // Borde blanco alrededor del logo
            )

            Spacer(modifier = Modifier.height(24.dp))

            // TÍTULO DE INICIO DE SESIÓN
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CAMPO DE TEXTO PARA EMAIL
            CustomOutlinedTextFieldLoginAndRegister(
                value = email,
                onValueChange = { email = it },
                icon = Icons.Default.Email,
                placeholder = R.string.login_placeholder_email,
                supportingText = R.string.login_error_email,
                isError = errorEmail,
                isPassword = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            // CAMPO DE TEXTO PARA CONTRASEÑA
            CustomOutlinedTextFieldLoginAndRegister(
                value = password,
                onValueChange = { password = it },
                icon = Icons.Default.Lock,
                placeholder = R.string.login_placeholder_password,
                supportingText = R.string.login_error_password,
                isError = errorPassword,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // BOTÓN DE INICIAR SESIÓN
            CustomButtonLoginAndRegister(
                onClick = {
                    // Reinicia los estados de error antes de validar
                    errorEmail = false
                    errorPassword = false

                    // Valida si los campos están vacíos
                    if (email.isBlank()) {
                        errorEmail = true
                    }
                    if (password.isBlank()) {
                        errorPassword = true
                    }

                    // Si no hay errores, procede con el login
                    if (!errorEmail && !errorPassword) {
                        viewModel.login(email, password)
                    }
                },
                text = R.string.login_button_text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TEXTO Y BOTÓN PARA REGISTRO
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.login_bottom_text), color = Color.White)
                // Navega a la pantalla de Registro usando el objeto @Serializable Register
                TextButton(onClick = { navController.navigate(Register) }) {
                    Text(stringResource(R.string.login_bottom_textbutton), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // INDICADORES DE ESTADO (Loading, Success, Error)
            when (authState) {
                is AuthUiState.Loading -> {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
                is AuthUiState.Success -> {
                    // El LaunchedEffect ya maneja la navegación.
                    // Este mensaje es solo informativo y se mostrará brevemente antes de la navegación.
                    Text(
                        text = stringResource(R.string.login_auth_successful),
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is AuthUiState.Error -> {
                    // Muestra el mensaje de error específico del ViewModel
                    Text(
                        text = authState.message, // Accede al mensaje de error
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                // Los estados Idle y SessionState no necesitan una representación visual específica
                // en esta pantalla de login, ya que su lógica de navegación se maneja en AuthManager (Composable).
                AuthUiState.Idle, is AuthUiState.SessionState -> {
                    // No hace nada, la UI permanece normal o sin indicadores extra.
                }
            }
        }
    }
}