package com.deto.staystrong.ui.routine

import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomTopAppBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.deto.staystrong.R
import com.deto.staystrong.Routine
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.deto.staystrong.ui.components.CustomFloatingActionButton


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoutinesScreen(navController: NavController, viewModel: RoutinesViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    LaunchedEffect(Unit) {
        viewModel.refreshRoutines()
    }

    val uiState = viewModel.routinesUiState

    var selected by remember { mutableIntStateOf(0) }
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
            CustomFloatingActionButton({ viewModel.addRoutine(selectedDate)})
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.routine),
//                contentDescription = "Fondo",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF2C2C2C),
                                    Color(0xFF4F4F4F)
                                )
                            )
                        )
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
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

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    when (uiState) {
                        is RoutinesUiState.Loading -> {
                            CustomCircularProgressIndicator("rutinas")
                        }

                        is RoutinesUiState.Error -> {
                            Text(text = "Error: ${uiState.message}")
                        }

                        is RoutinesUiState.Success -> {
                            val routines = uiState.routines

                            // Filtrar solo para la fecha seleccionada
                            val filteredRoutines = routines.filter { routine ->
                                routine.date.startsWith(selectedDate.toString())
                            }

                            if (filteredRoutines.isEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(32.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("No hay rutinas para esta fecha.", style = MaterialTheme.typography.bodyLarge)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Toca el botón '+' para crear una nueva.", style = MaterialTheme.typography.bodySmall)
                                }
                            }


                            LazyVerticalGrid(
                                columns = GridCells.Fixed(1),
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(filteredRoutines.size) { index ->

                                    val routine = filteredRoutines[index]

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 10.dp)
                                            .clickable {
                                                selected = routine.id
                                                navController.navigate(Routine(selected, formattedDate))
                                            },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFF2F2F2),
                                            contentColor = Color.Black
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp)
                                        ) {
                                            Text(
                                                text = "Rutina ${index + 1}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        else -> {}

                    }
                }
            }
        }
    }

    if (showCalendarDialog.value) {
        AlertDialog(
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
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
