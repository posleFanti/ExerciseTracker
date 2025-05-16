package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.CompletedWorkoutDao
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.entities.CompletedWorkoutWithSets
import kotlinx.coroutines.flow.Flow

class OfflineCompletedWorkoutRepository(private val completedWorkoutDao: CompletedWorkoutDao) : CompletedWorkoutRepository {
    override fun getAllCompletedWorkoutsStream(): Flow<List<CompletedWorkout>> =
        completedWorkoutDao.getAllCompletedWorkouts()

    override fun getCompletedWorkoutStream(id: Long): Flow<CompletedWorkout?> =
        completedWorkoutDao.getCompletedWorkout(id)

    override fun getCompletedWorkoutWithSetsStream(id: Long): Flow<CompletedWorkoutWithSets> =
        completedWorkoutDao.getCompletedWorkoutWithSets(id)

    override suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.insert(completedWorkout)

    override suspend fun deleteCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.delete(completedWorkout)

    override suspend fun updateCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.update(completedWorkout)
}