package com.deto.staystrong.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.deto.staystrong.StayStrong
import com.deto.staystrong.ui.auth.AuthViewModel
import com.deto.staystrong.ui.exercise.ExerciseViewModel
import com.deto.staystrong.ui.home.RoutineVideoViewModel
import com.deto.staystrong.ui.progress.ProgressViewModel
import com.deto.staystrong.ui.recipe.RecipeViewModel
import com.deto.staystrong.ui.recipe.RecipesViewModel
import com.deto.staystrong.ui.routine.RoutinesViewModel
import com.deto.staystrong.ui.routineExercise.RoutineExerciseViewModel
import com.deto.staystrong.ui.set.SetViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            AuthViewModel(App().container.authApiService, App().baseContext)
        }
        initializer {
            RoutinesViewModel(App().container.routineApiService)
        }
        initializer {
            ExerciseViewModel(App().container.exerciseApiService)
        }
        initializer {
            RoutineExerciseViewModel(App().container.routineExerciseApiService)
        }
        initializer {
            SetViewModel(App().container.setApiService)
        }
        initializer {
            RoutineVideoViewModel(App().container.routineVideoService)
        }
        initializer {
            RecipesViewModel(App().container.recipeService)
        }
        initializer {
            RecipeViewModel(App().container.recipeService)
        }
        initializer {
            ProgressViewModel(App().container.monthlyVolumeService)
        }
    }
}

fun CreationExtras.App(): StayStrong =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as StayStrong)