package com.deto.staystrong.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExercise(routineExercise: RoutineExercise): Long

    @Update
    suspend fun updateRoutineExercise(routineExercise: RoutineExercise)

    @Delete
    suspend fun deleteRoutineExercise(routineExercise: RoutineExercise)

    @Query("SELECT * FROM routine_exercises WHERE routine_id = :routineId ORDER BY `order` ASC")
    fun getRoutineExercisesForRoutine(routineId: Int): Flow<List<RoutineExercise>>
}