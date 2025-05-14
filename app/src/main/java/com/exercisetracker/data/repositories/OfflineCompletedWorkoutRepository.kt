package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.CompletedWorkoutDao
import com.exercisetracker.data.entities.CompletedWorkout
import kotlinx.coroutines.flow.Flow

class OfflineCompletedWorkoutRepository(private val completedWorkoutDao: CompletedWorkoutDao) : CompletedWorkoutRepository {
    override fun getAllCompletedWorkoutsStream(): Flow<List<CompletedWorkout>> =
        completedWorkoutDao.getAllCompletedWorkouts()

    override fun getCompletedWorkoutStream(completedWorkout: CompletedWorkout): Flow<CompletedWorkout?> =
        completedWorkoutDao.getCompletedWorkout(completedWorkout.completedWorkoutId)

    override suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.insert(completedWorkout)

    override suspend fun deleteCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.delete(completedWorkout)

    override suspend fun updateCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.update(completedWorkout)
}