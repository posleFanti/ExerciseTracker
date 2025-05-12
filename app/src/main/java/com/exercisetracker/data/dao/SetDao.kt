package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.exercisetracker.data.entities.Set

@Dao
interface SetDao {
    @Insert
    suspend fun insert(set: Set): Long

    @Query("SELECT * FROM sets WHERE workout_id = :workoutId ORDER BY set_number ASC")
    fun getSetsForWorkout(workoutId: Long): Flow<List<Set>>
}