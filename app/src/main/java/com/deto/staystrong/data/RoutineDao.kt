package com.deto.staystrong.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine): Long

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Query("SELECT * FROM routines WHERE id = :routineId")
    fun getRoutineById(routineId: Int): Flow<Routine?>

    @Query("SELECT * FROM routines WHERE userId = :userId")
    fun getRoutinesForUser(userId: Int): Flow<List<Routine>>
}