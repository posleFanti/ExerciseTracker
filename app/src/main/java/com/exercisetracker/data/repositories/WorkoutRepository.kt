package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithSetsView
import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.entities.WorkoutWithExercisesWithSets
import com.exercisetracker.ui.workouts.ExerciseWithSets
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkoutsStream(): Flow<List<Workout>>
    //fun getWorkoutWithExercisesStream(id: Long): Flow<WorkoutWithExercises?>
    fun getWorkoutStream(id: Long): Flow<Workout?>
    fun getSets(workoutId: Long, exerciseId: Long): Flow<List<Set>?>
    suspend fun getWorkoutWithExercisesWithSets(workoutId: Long): WorkoutWithExercisesWithSets
    suspend fun getExerciseWithSetsView(workoutId: Long, exerciseId: Long): List<ExerciseWithSetsView>
    suspend fun insertWorkout(workout: Workout)
    suspend fun insertSet(set: Set): Long
    suspend fun deleteWorkout(workout: Workout)
    suspend fun deleteSet(set: Set)
    suspend fun updateWorkout(workout: Workout)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun updateSet(set: Set)
    suspend fun runInTransaction(block: suspend () -> Unit)
}