package com.deto.staystrong.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: Set): Long

    @Update
    suspend fun updateSet(set: Set)

    @Delete
    suspend fun deleteSet(set: Set)

    @Query("SELECT * FROM sets WHERE routine_exercise_id = :routineExerciseId")
    fun getSetsForRoutineExercise(routineExerciseId: Int): Flow<List<Set>>
}