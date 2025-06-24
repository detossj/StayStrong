package com.deto.staystrong.ui.set

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deto.staystrong.data.Set

@Composable
fun SetScreen(modifier: Modifier = Modifier) {

    var rep by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf(false) }


    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Nombre ejercicio",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 70.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = rep,
                    onValueChange = { newValue ->
                        rep = newValue
                        errorMessage = false
                    },
                    label = { Text("Repeticiones") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { newValue ->
                        weight = newValue
                        errorMessage = false
                    },
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth().padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Añadir Set",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }

    }
}



@Composable
fun SetItem(set: Set, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Set ${set.id}: ${set.reps} repes @ ${set.weight} kg",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar Set")
            }
        }
    }
}

