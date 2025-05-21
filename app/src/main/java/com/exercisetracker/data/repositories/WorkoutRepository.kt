package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.entities.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkoutsStream(): Flow<List<Workout>>
    fun getWorkoutWithExercisesStream(id: Long): Flow<WorkoutWithExercises?>
    fun getWorkoutStream(id: Long): Flow<Workout?>
    fun getSetsForWorkout(id: Long): Flow<List<Set>>
    suspend fun insertWorkout(workout: Workout)
    suspend fun deleteWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
}