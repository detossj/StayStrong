package com.deto.staystrong.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val date: String
)