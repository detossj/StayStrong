package com.deto.staystrong.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = RoutineExercise::class,
            parentColumns = ["id"],
            childColumns = ["routine_exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Set(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val routine_exercise_id: Int,
    val reps: Int,
    val weight: Float
)