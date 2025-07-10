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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.Home
import com.deto.staystrong.Login
import com.deto.staystrong.R
import com.deto.staystrong.Register
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomOutlinedTextFieldLoginAndRegister


@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }

    val authState = viewModel.authState

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            navController.navigate(Home) {
                popUpTo(Login) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.1f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

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

            Button(
                onClick = {
                    errorEmail = email.isBlank()
                    errorPassword = password.isBlank()

                    if (!errorEmail && !errorPassword) {
                        viewModel.login(email, password)
                    } },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ){
                Text(stringResource(R.string.login_button_text), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.login_bottom_text), color = Color.White)
                TextButton(onClick = { navController.navigate(Register) }) {
                    Text(stringResource(R.string.login_bottom_textbutton), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (authState) {
                is AuthUiState.Loading -> {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }

                is AuthUiState.Success -> {
                    Text(
                        text = stringResource(R.string.login_auth_successful),
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is AuthUiState.Error -> {
                    Text(
                        text = authState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                else -> {}

            }
        }
    }
}
