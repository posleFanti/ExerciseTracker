package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.entities.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkoutsStream(): Flow<List<Workout>>
    fun getWorkoutStream(id: Long): Flow<Workout?>
    fun getSetsForWorkout(id: Long): Flow<List<Set>>
    suspend fun insertWorkout(workout: Workout)
    suspend fun deleteWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
}