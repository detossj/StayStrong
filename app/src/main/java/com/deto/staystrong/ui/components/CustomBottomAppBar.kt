package com.deto.staystrong.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.deto.staystrong.Home
import com.deto.staystrong.Profile
import com.deto.staystrong.Progress
import com.deto.staystrong.R
import com.deto.staystrong.Recipes
import com.deto.staystrong.Routines


@Composable
fun CustomBottomAppBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Black
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                Triple(R.drawable.home_24px, "Inicio", Home),
                Triple(R.drawable.nutrition_24px, "Recetas", Recipes),
                Triple(R.drawable.add_circle_24px, "Entrenar", Routines),
                Triple(R.drawable.show_chart_24px, "Progreso", Progress),
                Triple(R.drawable.account_circle_24px, "Tú", Profile )
            )


            items.forEach { (iconRes, label, route) ->
                Button(
                    onClick = {
                        navController.navigate(route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = label,
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

