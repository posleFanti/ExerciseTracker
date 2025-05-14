package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.exercisetracker.data.entities.CompletedWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedWorkoutDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completedWorkout: CompletedWorkout)

    @Delete
    suspend fun delete(completedWorkout: CompletedWorkout)

    @Update
    suspend fun update(completedWorkout: CompletedWorkout)

    @Query("SELECT * FROM completed_workouts ORDER BY workout_date DESC")
    fun getAllCompletedWorkouts(): Flow<List<CompletedWorkout>>

    @Query("SELECT * FROM completed_workouts WHERE completed_workout_id = :id ORDER BY workout_date DESC")
    fun getCompletedWorkout(id: Long): Flow<CompletedWorkout?>
}