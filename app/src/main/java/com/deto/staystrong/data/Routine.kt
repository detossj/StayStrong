package com.deto.staystrong.data
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val user_id: Int,
    val date: String
)
