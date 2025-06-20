package com.deto.staystrong.ui.routine

import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.deto.staystrong.R
import com.deto.staystrong.ui.components.CustomTopAppBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoutinesScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val showCalendarDialog = remember { mutableStateOf(false) }

    val today = LocalDate.now()
    val formattedDate = when {
        selectedDate == today -> "HOY"
        selectedDate == today.minusDays(1) -> "AYER"
        selectedDate == today.plusDays(1) -> "MAÑANA"
        else -> selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Botón agregar Ejercicio */ },
                containerColor = Color.Gray,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Barra de fecha y flechas como la tenías antes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { selectedDate = selectedDate.minusDays(1) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous day",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = formattedDate,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Row {
                        IconButton(onClick = { showCalendarDialog.value = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Pick date",
                                tint = Color.White
                            )
                        }

                        IconButton(onClick = { selectedDate = selectedDate.plusDays(1) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next day",
                                tint = Color.White
                            )
                        }
                    }
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.White)
                )
            }
        }
    }

    if (showCalendarDialog.value) {
        AlertDialog(
            onDismissRequest = { showCalendarDialog.value = false },
            confirmButton = {
                TextButton(onClick = { showCalendarDialog.value = false }) {
                    Text("OK")
                }
            },
            title = { Text("Selecciona una fecha") },
            text = {
                AndroidView(
                    factory = { ctx ->
                        CalendarView(ctx).apply {
                            date = selectedDate.toEpochDay() * 24 * 60 * 60 * 1000
                            setOnDateChangeListener { _, year, month, day ->
                                selectedDate = LocalDate.of(year, month + 1, day)
                            }
                        }
                    }
                )
            }
        )
    }
}
