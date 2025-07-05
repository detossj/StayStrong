package com.deto.staystrong.ui.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.model.MonthlyVolume
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.FloatEntry





@Composable
fun ProgressScreen(navController: NavController, viewModel: ProgressViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val uiState = viewModel.monthlyVolumesUiState



    LaunchedEffect(Unit) {
        viewModel.refreshMonthlyVolumes(1)
    }

    when (uiState) {
        is ProgressUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularProgressIndicator("graficos")
            }
        }
        is ProgressUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.message}")
            }
        }
        is ProgressUiState.Success -> {
            VolumeBarChart(uiState.monthlyVolumes)
        }

        else -> {}
    }


}

@Composable
fun VolumeBarChart(volumes: List<MonthlyVolume>) {
    val entries = volumes.mapIndexed { index, volume ->
        FloatEntry(index.toFloat(), volume.volume.toFloat())
    }

    val labels = volumes.map { "${it.month}/${it.year}" }

    Chart(
        chart = columnChart(),
        model = entryModelOf(entries),
        startAxis = startAxis(),
        bottomAxis = bottomAxis(valueFormatter = { value, _ ->
            labels.getOrNull(value.toInt()) ?: ""
        })
    )
}
