@file:OptIn(ExperimentalMaterial3Api::class)
package com.deto.staystrong.ui.calculator




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.deto.staystrong.ui.components.CustomBottomAppBar
import kotlin.math.roundToInt


@Composable
fun CalculatorScreen(navController: NavController) {
    var altura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }

    var error1 by remember { mutableStateOf(false) }
    var error2 by remember { mutableStateOf(false) }
    var error3 by remember { mutableStateOf(false) }

    val sexos = listOf("Hombre", "Mujer")
    var expanded1 by remember { mutableStateOf(false) }
    var seleccionSexos by remember { mutableStateOf(sexos[0]) }

    val actividad = listOf(
        "Baja (rara vez o nunca)",
        "Ligera (1-3 veces por semana)",
        "Moderada (3-5 veces por semana)",
        "Alta (6 veces por semana)",
        "Muy alta (Deportista Profesional)"
    )
    var expanded2 by remember { mutableStateOf(false) }
    var seleccionActividad by remember { mutableStateOf(actividad[1]) }

    var resultadoCalorias by remember { mutableStateOf<Int?>(null) }


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
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Calculadora de Calorías",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 70.dp)
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, top = 16.dp)
                    ) {
                        CustomOutlinedTextField(
                            value = altura,
                            onValueChange = {
                                altura = it
                                if (error1 && it.isNotBlank()) error1 = false
                            },
                            label = "Altura (cm)",
                            placeholder = "Ej: 175",
                            error = error1,
                            supportingText = "Ingresa tu altura "
                        )

                        CustomOutlinedTextField(
                            value = edad,
                            onValueChange = {
                                edad = it
                                if (error2 && it.isNotBlank()) error2 = false
                            },
                            label = "Edad",
                            placeholder = "Ej: 25",
                            error = error2,
                            supportingText = "Ingresa tu edad"
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, top = 16.dp)
                    ) {
                        CustomOutlinedTextField(
                            value = peso,
                            onValueChange = {
                                peso = it
                                if (error3 && it.isNotBlank()) error3 = false
                            },
                            label = "Peso (kg)",
                            placeholder = "Ej: 70",
                            error = error3,
                            supportingText = "Ingresa tu peso "
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded1,
                            onExpandedChange = { expanded1 = !expanded1 }
                        ) {
                            OutlinedTextField(
                                value = seleccionSexos,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Sexo") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded1) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp)
                                    .menuAnchor(),
                                colors = ExposedDropdownMenuDefaults.textFieldColors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color(0xFFAAAAAA),
                                    focusedContainerColor = Color(0xFF1E1E1E),
                                    unfocusedContainerColor = Color(0xFF1E1E1E),
                                    focusedIndicatorColor = Color(0xFFCCCCCC),
                                    unfocusedIndicatorColor = Color(0xFFAAAAAA),
                                    disabledIndicatorColor = Color(0xFFAAAAAA),
                                    focusedLabelColor = Color(0xFFCCCCCC),
                                    unfocusedLabelColor = Color(0xFFAAAAAA)
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded1,
                                onDismissRequest = { expanded1 = false }
                            ) {
                                sexos.forEach { opcion ->
                                    DropdownMenuItem(
                                        text = { Text(opcion) },
                                        onClick = {
                                            seleccionSexos = opcion
                                            expanded1 = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expanded2,
                    onExpandedChange = { expanded2 = !expanded2 },
                    modifier = Modifier
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                ) {
                    OutlinedTextField(
                        value = seleccionActividad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nivel de actividad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded2) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color(0xFFAAAAAA),
                            focusedContainerColor = Color(0xFF1E1E1E),
                            unfocusedContainerColor = Color(0xFF1E1E1E),
                            focusedIndicatorColor = Color(0xFFCCCCCC),
                            unfocusedIndicatorColor = Color(0xFFAAAAAA),
                            disabledIndicatorColor = Color(0xFFAAAAAA),
                            focusedLabelColor = Color(0xFFCCCCCC),
                            unfocusedLabelColor = Color(0xFFAAAAAA)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded2,
                        onDismissRequest = { expanded2 = false }
                    ) {
                        actividad.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    seleccionActividad = opcion
                                    expanded2 = false
                                }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 30.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            error1 = altura.isEmpty()
                            error2 = edad.isEmpty()
                            error3 = peso.isEmpty()

                            if (!error1 && !error2 && !error3) {
                                resultadoCalorias = CaloriasTotales(
                                    seleccionActividad,
                                    peso.toDouble(),
                                    altura.toDouble(),
                                    edad.toInt(),
                                    seleccionSexos
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Calcular Calorías",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (resultadoCalorias != null) {
                        Text(
                            text = "Calorías estimadas: ${resultadoCalorias} kcal/día",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    error: Boolean,
    supportingText: String
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color(0xFFAAAAAA),
            focusedContainerColor = Color(0xFF1E1E1E),
            unfocusedContainerColor = Color(0xFF1E1E1E),
            focusedBorderColor = Color(0xFFCCCCCC),
            unfocusedBorderColor = Color(0xFFAAAAAA),
            focusedLabelColor = Color(0xFFCCCCCC),
            unfocusedLabelColor = Color(0xFFAAAAAA),
            errorTextColor = Color(0xFFFF5252),
            errorSupportingTextColor = Color(0xFFFF5252),
            errorLabelColor = Color(0xFFFF5252),
            errorBorderColor = Color(0xFFFF5252),
            errorContainerColor = Color(0xFF1E1E1E)
        ),
        isError = error,
        supportingText = { if (error) Text(supportingText) }
    )
}

fun TasaMetabolismoBasal(peso: Double, altura: Double, edad: Int, sexo: String): Double {
    return if (sexo == "Hombre") {
        (10 * peso) + (6.25 * altura) - (5 * edad) + 5
    } else {
        (10 * peso) + (6.25 * altura) - (5 * edad) - 161
    }
}

fun CaloriasTotales(
    actividad: String,
    peso: Double,
    altura: Double,
    edad: Int,
    sexo: String
): Int {
    val factor = when (actividad) {
        "Baja (rara vez o nunca)" -> 1.2
        "Ligera (1-3 veces por semana)" -> 1.375
        "Moderada (3-5 veces por semana)" -> 1.55
        "Alta (6 veces por semana)" -> 1.725
        "Muy alta (Deportista Profesional)" -> 1.9
        else -> 1.2
    }
    return (TasaMetabolismoBasal(peso, altura, edad, sexo) * factor).roundToInt()
}
