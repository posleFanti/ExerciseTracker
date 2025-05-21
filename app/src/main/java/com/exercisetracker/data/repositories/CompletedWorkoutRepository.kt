package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.entities.CompletedWorkoutWithSets
import com.exercisetracker.data.entities.ExerciseWithCompletedSets
import kotlinx.coroutines.flow.Flow

interface CompletedWorkoutRepository {
    fun getAllCompletedWorkoutsStream(): Flow<List<CompletedWorkout>>
    fun getCompletedWorkoutStream(id: Long): Flow<CompletedWorkout?>
    fun getCompletedWorkoutWithSetsStream(id: Long): Flow<CompletedWorkoutWithSets?>
    fun getAllExercisesWithCompletedSets(completedWorkoutId: Long): Flow<List<ExerciseWithCompletedSets>>
    fun getExerciseWithCompletedSet(exerciseId: Long): Flow<ExerciseWithCompletedSets?>
    suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkout)
    suspend fun deleteCompletedWorkout(completedWorkout: CompletedWorkout)
    suspend fun updateCompletedWorkout(completedWorkout: CompletedWorkout)
}