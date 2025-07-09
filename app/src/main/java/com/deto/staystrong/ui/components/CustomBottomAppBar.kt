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
import androidx.compose.material3.Divider
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.deto.staystrong.Calculator
import com.deto.staystrong.Home
import com.deto.staystrong.Profile
import com.deto.staystrong.Progress
import com.deto.staystrong.Recipes
import com.deto.staystrong.R
import com.deto.staystrong.Routines
import com.deto.staystrong.Recipe
import com.deto.staystrong.Routine
import com.deto.staystrong.ExerciseList
import com.deto.staystrong.Set


@Composable
fun CustomBottomAppBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val currentDestinationRoute = navBackStackEntry.value?.destination?.route

    Column {
        Divider(
            color = Color.Gray.copy(alpha = 0.5f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Black
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val items = listOf(
                    Triple(R.drawable.home_24px, "Inicio", Home::class.qualifiedName to listOf(Home::class.qualifiedName)),
                    Triple(R.drawable.nutrition_24px, "Recetas", Recipes::class.qualifiedName to listOf(Recipes::class.qualifiedName, Recipe::class.qualifiedName)),
                    Triple(R.drawable.add_circle_24px, "Entrenar", Routines::class.qualifiedName to listOf(Routines::class.qualifiedName, Routine::class.qualifiedName, ExerciseList::class.qualifiedName, Set::class.qualifiedName)),
                    Triple(R.drawable.show_chart_24px, "Progreso", Progress::class.qualifiedName to listOf(Progress::class.qualifiedName,Calculator::class.qualifiedName)),
                    Triple(R.drawable.account_circle_24px, "Tú", Profile::class.qualifiedName to listOf(Profile::class.qualifiedName))
                )

                items.forEach { (iconRes, label, routeInfo) ->
                    val (primaryRouteQualifiedName, associatedRoutesQualifiedNames) = routeInfo

                    val isSelected = currentDestinationRoute != null && associatedRoutesQualifiedNames.any {
                        currentDestinationRoute.startsWith(it.toString())
                    }


                    val selectedColor = Color.White
                    val unselectedColor = Color.Gray

                    val itemColor = if (isSelected) selectedColor else unselectedColor

                    Button(
                        onClick = {
                            navController.navigate(primaryRouteQualifiedName.toString()) {

                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
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
                                tint = itemColor
                            )
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                color = itemColor
                            )
                        }
                    }
                }
            }
        }
    }

}