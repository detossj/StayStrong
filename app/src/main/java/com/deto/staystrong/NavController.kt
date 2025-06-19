package com.deto.staystrong

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deto.staystrong.ui.auth.AuthManager
import com.deto.staystrong.ui.auth.LoginScreen
import com.deto.staystrong.ui.auth.RegisterScreen
import kotlinx.serialization.Serializable


@Serializable
object AuthManager

@Serializable
object Login

@Serializable
object Register

@Serializable
object Exercise



@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AuthManager) {

        composable<AuthManager> {
            AuthManager(navController = navController)
        }
        composable<Login> {
            LoginScreen(navController = navController)
        }
        composable<Register> {
            RegisterScreen(navController = navController)
        }
        composable<Exercise> {

        }
    }
}