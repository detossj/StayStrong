package com.deto.staystrong.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.Login
import com.deto.staystrong.R

import com.deto.staystrong.Routines
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(navController: NavController, viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    TopAppBar(
        title = {},
        actions = {
            IconButton(onClick = {
                viewModel.logout()
                navController.navigate(Login) {
                    popUpTo(Routines) {
                        inclusive = true
                    } // Quitar la pantalla de la pila de navegación
                } }) {
                Icon(
                    painter = painterResource(R.drawable.logout_24px),
                    contentDescription = "Logout",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}