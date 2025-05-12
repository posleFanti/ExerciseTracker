package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.CompletedWorkout
import kotlinx.coroutines.flow.Flow

interface CompletedWorkoutRepository {
    fun getAllCompletedWorkoutsStream(): Flow<List<CompletedWorkout>>
    fun getCompletedWorkoutStream(completedWorkout: CompletedWorkout): Flow<CompletedWorkout?>
    suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkout)
    suspend fun deleteCompletedWorkout(completedWorkout: CompletedWorkout)
    suspend fun updateCompletedWorkout(completedWorkout: CompletedWorkout)
}