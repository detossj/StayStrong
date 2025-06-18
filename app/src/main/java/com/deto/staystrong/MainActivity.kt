package com.deto.staystrong // Tu paquete principal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge // Generalmente útil para el diseño de pantalla completa
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme // Importa MaterialTheme para el color de fondo
import androidx.compose.material3.Surface // Importa Surface para el fondo de la pantalla
import androidx.compose.ui.Modifier
import com.deto.staystrong.ui.theme.StayStrongTheme // Tu tema de aplicación
import com.deto.staystrong.ui.theme.exercise.ExerciseScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el diseño de borde a borde para un aspecto moderno
        setContent {
            StayStrongTheme { // Aplica el tema de tu aplicación
                // Un Surface para el color de fondo de la pantalla, buena práctica
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Usa el color de fondo de tu tema
                ) {
                    // Aquí es donde llamas a tu pantalla de boceto de ejercicios todo-en-uno
                    ExerciseScreen(
                        modifier = Modifier.fillMaxSize() // Asegura que ocupe todo el espacio disponible
                    )
                }
            }
        }
    }
}

// Puedes eliminar la función Greeting y GreetingPreview si ya no las necesitas,
// ya que el foco ahora está en ExerciseSketchScreen.
// Si las necesitas para otros propósitos, déjalas, pero no son llamadas aquí.