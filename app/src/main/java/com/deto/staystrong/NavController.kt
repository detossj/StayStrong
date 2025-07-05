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
import com.deto.staystrong.ui.home.HomeScreen
import com.deto.staystrong.ui.profile.ProfileScreen
import com.deto.staystrong.ui.progress.ProgressScreen
import com.deto.staystrong.ui.recipe.RecipeScreen
import com.deto.staystrong.ui.recipe.RecipesScreen
import com.deto.staystrong.ui.routineExercise.RoutineScreen
import com.deto.staystrong.ui.routine.RoutinesScreen
import com.deto.staystrong.ui.set.SetScreen
import kotlinx.serialization.Serializable


@Serializable
object AuthManager

@Serializable
object Login

@Serializable
object Register

@Serializable
object Home

@Serializable
object Recipes

@Serializable
data class Recipe(val idRecipe: Int)

@Serializable
object Routines

@Serializable
object Progress

@Serializable
object Profile

@Serializable
data class Routine(val idRoutine: Int, val formattedDate: String)

@Serializable
data class ExerciseList(val idRoutine: Int)

@Serializable
data class Set(val idRoutine: Int,val idRoutineExercise: Int, val nameExercise: String)


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
        composable<Home> {
            HomeScreen(navController = navController)
        }
        composable<Recipes> {
            RecipesScreen(navController = navController)
        }
        composable<Recipe> { backStackEntry ->
            val args = backStackEntry.toRoute<Recipe>()
            RecipeScreen(navController = navController, idRecipe = args.idRecipe)
        }
        composable<Routines> {
            RoutinesScreen(navController = navController)
        }
        composable<Progress> {
            ProgressScreen(navController = navController)
        }
        composable<Profile> {
            ProfileScreen(navController = navController)
        }
        composable<Routine> { backStackEntry ->
            val args = backStackEntry.toRoute<Routine>()
            RoutineScreen(navController = navController, idRoutine = args.idRoutine, formattedDate = args.formattedDate)
        }
        composable<ExerciseList> { backStackEntry ->
            val args = backStackEntry.toRoute<ExerciseList>()
            ExerciseListScreen(navController = navController, idRoutine = args.idRoutine)
        }
        composable<Set> { backStackEntry ->
            val args = backStackEntry.toRoute<Set>()
            SetScreen(idRoutine = args.idRoutine, idRoutineExercise = args.idRoutineExercise, nameExercise = args.nameExercise)
        }


    }
}