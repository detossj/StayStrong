// data/Exercise.kt
package com.deto.staystrong.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val category: String = "Otros" // <-- ¡Nuevo campo aquí! Valor por defecto "Otros"
)