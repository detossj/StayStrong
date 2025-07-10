package com.deto.staystrong.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.Home
import com.deto.staystrong.Login
import com.deto.staystrong.R
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomOutlinedTextFieldLoginAndRegister

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var validationPassword by remember { mutableStateOf("") }

    var errorName by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }
    var errorValidationPassword by remember { mutableStateOf(false) }

    val authState = viewModel.authState

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            navController.navigate(Home) {
                popUpTo(Login) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {


        Image(
            painter = painterResource(id = R.drawable.register),
            contentDescription = "Fondo de registro",
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
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomOutlinedTextFieldLoginAndRegister(
                value = name,
                onValueChange = { name = it },
                icon = Icons.Default.Face,
                placeholder = R.string.register_placeholder_name,
                supportingText = R.string.register_error_name,
                isError = errorName,
                isPassword = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextFieldLoginAndRegister(
                value = email,
                onValueChange = { email = it },
                icon = Icons.Default.Email,
                placeholder = R.string.register_placeholder_email,
                supportingText = R.string.register_error_email,
                isError = errorEmail,
                isPassword = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextFieldLoginAndRegister(
                value = password,
                onValueChange = { password = it },
                icon = Icons.Default.Lock,
                placeholder = R.string.register_placeholder_password,
                supportingText = R.string.register_error_password,
                isError = errorPassword,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextFieldLoginAndRegister(
                value = validationPassword,
                onValueChange = { validationPassword = it },
                icon = Icons.Default.Lock,
                placeholder = R.string.register_placeholder_password,
                supportingText = R.string.register_error_password,
                isError = errorValidationPassword,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    errorName = name.isBlank()
                    errorEmail = email.isBlank()
                    errorPassword = password.isBlank()
                    errorValidationPassword = validationPassword.isBlank()

                    if (!errorName && !errorEmail && !errorPassword && !errorValidationPassword) {
                        viewModel.register(name, email, password, validationPassword)
                    }
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(R.string.register_button_text), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                when (authState) {
                    is AuthUiState.Loading -> {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    is AuthUiState.Success -> {
                        Text(
                            text = stringResource(R.string.register_auth_successful),
                            color = Color.White
                        )
                    }

                    is AuthUiState.Error -> {
                        Text(
                            text = authState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {  }
                }
            }
        }
    }
}
