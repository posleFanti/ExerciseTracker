package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.exercisetracker.data.entities.CompletedWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedWorkoutDao {
    @Insert
    suspend fun insert(completedWorkout: CompletedWorkout): Long

    @Query("SELECT * FROM completed_workouts ORDER BY workout_date DESC")
    fun getAllCompletedWorkouts(): Flow<List<CompletedWorkout>>
}