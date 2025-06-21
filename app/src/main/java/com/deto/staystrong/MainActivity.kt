package com.deto.staystrong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.deto.staystrong.ui.theme.StayStrongTheme
import com.deto.staystrong.ui.theme.exercise.ExerciseScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StayStrongTheme {
                ExerciseScreen()
            }
        }
    }
}

