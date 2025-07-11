package com.deto.staystrong.ui.progress


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.components.CustomBottomAppBar
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import com.deto.staystrong.model.MonthlyVolume
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.deto.staystrong.Calculator
import com.deto.staystrong.model.SimpleBarData
import com.deto.staystrong.ui.auth.AuthViewModel
import com.deto.staystrong.R


@Composable
fun ProgressScreen(navController: NavController, viewModel: ProgressViewModel = viewModel(factory = AppViewModelProvider.Factory), authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val uiState = viewModel.monthlyVolumesUiState

    val userData = authViewModel.userData
    val userId = userData?.id ?: return


    LaunchedEffect(userId) {
        viewModel.getMonthlyVolumes(userId)
    }


    Scaffold(
        bottomBar = {
            CustomBottomAppBar(navController)
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.progress_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 50.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

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
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF1E1E1E))
                                    .padding(16.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                    if (uiState.monthlyVolumes.isEmpty()) {
                                        Text(
                                            text = stringResource(R.string.progress_grafico_text_condition),
                                            color = Color.LightGray,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp)
                                        )
                                    } else {
                                        AnimatedBarChart(volumes = uiState.monthlyVolumes, modifier = Modifier.fillMaxWidth())
                                    }


                                    Text(
                                        text = stringResource(R.string.progress_grafico_text),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(top = 12.dp)
                                    )
                                }
                            }


                            Spacer(Modifier.padding(10.dp))


                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.progress_subtitle),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .padding(bottom = 10.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = { navController.navigate(Calculator) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text(
                                        text = stringResource(R.string.progress_text_button),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
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



@Composable
fun AnimatedBarChart(
    volumes: List<MonthlyVolume>,
    modifier: Modifier = Modifier,
    lightBarColor: Color = Color(0xFF4CAF50),
    darkBarColor: Color = Color(0xFF81C784)
) {
    val isDarkTheme = isSystemInDarkTheme()
    val barColor = if (isDarkTheme) darkBarColor else lightBarColor


    val monthNames = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
    val data = volumes.map {
        val label = if (it.month in 1..12) monthNames[it.month - 1] else "Mes${it.month}"
        SimpleBarData(label = label, value = it.volume.toFloat())
    }

    val maxValue = data.maxOfOrNull { it.value } ?: 0f

    val spaceBetweenBars = 20.dp
    val barWidth = 40.dp
    val chartHeight = 200.dp

    val animatedValues = remember(data) {
        data.map { Animatable(0f) }
    }

    LaunchedEffect(data) {
        animatedValues.forEachIndexed { index, animatable ->
            animatable.animateTo(
                targetValue = data[index].value,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
    }

    Column(modifier = modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        val barWidthPx = with(LocalDensity.current) { barWidth.toPx() }
        val spacePx = with(LocalDensity.current) { spaceBetweenBars.toPx() }

        Box {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Líneas guía horizontales
                val linesCount = 5
                val lineSpacing = canvasHeight / linesCount
                for (i in 0..linesCount) {
                    val y = i * lineSpacing
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1f
                    )
                }

                val totalBars = data.size
                val totalWidthBars = totalBars * barWidthPx + (totalBars - 1) * spacePx
                val startX = (canvasWidth - totalWidthBars) / 2f

                data.forEachIndexed { index, item ->
                    val animatedHeight = if (maxValue > 0) (animatedValues[index].value / maxValue) * canvasHeight else 0f
                    val left = startX + index * (barWidthPx + spacePx)
                    val top = canvasHeight - animatedHeight

                    val gradientBrush = Brush.verticalGradient(
                        colors = listOf(barColor, barColor.copy(alpha = 0.7f)),
                        startY = top,
                        endY = canvasHeight
                    )

                    drawRect(
                        brush = gradientBrush,
                        topLeft = Offset(left, top),
                        size = Size(barWidthPx, animatedHeight)
                    )

                    drawContext.canvas.nativeCanvas.apply {
                        val text = animatedValues[index].value.toInt().toString()
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 30f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                            typeface = android.graphics.Typeface.create("", android.graphics.Typeface.BOLD)
                        }
                        val x = left + barWidthPx / 2
                        val y = top - 10
                        drawText(text, x, y, paint)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spaceBetweenBars, Alignment.CenterHorizontally)
        ) {
            data.forEach { item ->
                Text(
                    text = item.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                    modifier = Modifier.width(barWidth),
                    maxLines = 1
                )
            }
        }
    }
}
