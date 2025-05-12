package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.exercisetracker.data.entities.CompletedSet

@Dao
interface CompletedSetDao {
    @Insert
    suspend fun insert(completedSet: CompletedSet): Long

    @Query("SELECT * FROM completed_sets WHERE completed_workout_id = :completedWorkoutId")
    suspend fun getCompletedSetsForWorkout(completedWorkoutId: Long): List<CompletedSet>
}