package com.deto.staystrong.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomFloatingActionButton(onClick: () -> Unit) {

    FloatingActionButton(
        onClick = onClick,
        containerColor = Color.Gray,
        contentColor = Color.White,
        shape = RoundedCornerShape(30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "navigate",
            modifier = Modifier.size(32.dp)
        )
    }
}