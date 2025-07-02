package com.deto.staystrong.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    var isEditable by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Hombre", "Mujer")
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    var nombre by remember { mutableStateOf("") }
    var nacimiento by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }

    Scaffold {Padding ->
        Column(
            modifier = Modifier.padding(Padding)
                .fillMaxSize()
                .padding(16.dp)

        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE91E63))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Poto",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Correo Electrónico",
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CampoEditable(
                    "Nombre completo",
                    nombre,
                    { nombre = it },
                    isEditable,
                    Modifier.weight(1f),
                    1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nacimiento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de Nacimiento") },
                    colors = customTextFieldColors(),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (isEditable) {
                    IconButton(
                        onClick = {
                            val calendar = Calendar.getInstance()
                            val year = calendar.get(Calendar.YEAR)
                            val month = calendar.get(Calendar.MONTH)
                            val day = calendar.get(Calendar.DAY_OF_MONTH)

                            android.app.DatePickerDialog(
                                context,
                                { _, y, m, d ->
                                    nacimiento = "%02d/%02d/%04d".format(d, m + 1, y)
                                },
                                year, month, day
                            ).show()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE91E63))
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha",
                            tint = Color.White
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded && isEditable,
                        onExpandedChange = {
                            if (isEditable) expanded = !expanded
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedOptionText,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Sexo") },
                            trailingIcon = {
                                if (!isEditable) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Bloqueado",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            colors = customTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded && isEditable,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        selectedOptionText = selectionOption
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                CampoEditable("Peso (kg)", peso, { peso = it }, isEditable, Modifier.weight(1f), 1)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded && isEditable,
                        onExpandedChange = {
                            if (isEditable) expanded = !expanded
                        }
                    ) {

                        CampoEditable("Altura (cm)", altura, { altura = it }, isEditable, maxLine = 1)
                    }

                }
                Box(modifier = Modifier.weight(1f)) {
                    CampoEditable("Edad (años)", edad, { edad = it }, isEditable, maxLine = 1)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Social",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))
            CampoEditable("Bio", bio, { bio = it }, isEditable, maxLine = 5)
            Spacer(modifier = Modifier.height(8.dp))
            CampoEditable("Instagram", instagram, { instagram = it }, isEditable, maxLine = 1)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { isEditable = !isEditable },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (isEditable) "Guardar" else "Modificar perfil")
            }
        }
    }
}



@Composable
fun CampoEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxLine: Int
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = !isEditable,
            label = { Text(label) },
            trailingIcon = {
                if (!isEditable) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.Gray
                    )
                }
            },
            colors = customTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLine
        )
    }
}

@Composable
fun customTextFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.White,
    unfocusedIndicatorColor = Color.Gray,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.Gray
)