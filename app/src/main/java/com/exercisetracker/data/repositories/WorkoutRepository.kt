package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.entities.WorkoutWithExercisesWithSets
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkoutsStream(): Flow<List<Workout>>
    fun getWorkoutStream(id: Long): Flow<Workout?>
    suspend fun getWorkoutWithExercisesWithSets(workoutId: Long): Flow<WorkoutWithExercisesWithSets>
    suspend fun insertWorkout(workout: Workout)
    suspend fun deleteWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun runInTransaction(block: suspend () -> Unit)
}