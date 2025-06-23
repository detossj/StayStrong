package com.deto.staystrong

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.deto.staystrong.ui.auth.AuthManager
import com.deto.staystrong.ui.auth.LoginScreen
import com.deto.staystrong.ui.auth.RegisterScreen
import com.deto.staystrong.ui.exercise.ExerciseListScreen
import com.deto.staystrong.ui.routineExercise.RoutineScreen
import com.deto.staystrong.ui.routine.RoutinesScreen
import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
object AuthManager

@Serializable
object Login

@Serializable
object Register

@Serializable
object Routines

@Serializable
data class Routine(val idRoutine: Int, val formattedDate: String)

@Serializable
data class ExerciseList(val idRoutine: Int)



@RequiresApi(Build.VERSION_CODES.O)
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
        composable<Routines> {
            RoutinesScreen(navController = navController)
        }
        composable<Routine> { backStackEntry ->
            val args = backStackEntry.toRoute<Routine>()
            RoutineScreen(navController = navController, idRoutine = args.idRoutine, formattedDate = args.formattedDate)
        }
        composable<ExerciseList> { backStackEntry ->
            val args = backStackEntry.toRoute<ExerciseList>()
            ExerciseListScreen(navController = navController, idRoutine = args.idRoutine)
        }
    }
}