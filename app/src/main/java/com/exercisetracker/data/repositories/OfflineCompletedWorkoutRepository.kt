package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.CompletedWorkoutDao
import com.exercisetracker.data.dao.ExerciseDao
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.entities.CompletedWorkoutWithSets
import com.exercisetracker.data.entities.ExerciseWithCompletedSets
import kotlinx.coroutines.flow.Flow

class OfflineCompletedWorkoutRepository(
    private val completedWorkoutDao: CompletedWorkoutDao,
    private val exerciseDao: ExerciseDao
    ) : CompletedWorkoutRepository {
    override fun getAllCompletedWorkoutsStream(): Flow<List<CompletedWorkout>> =
        completedWorkoutDao.getAllCompletedWorkouts()

    override fun getCompletedWorkoutStream(id: Long): Flow<CompletedWorkout?> =
        completedWorkoutDao.getCompletedWorkout(id)

    override fun getCompletedWorkoutWithSetsStream(id: Long): Flow<CompletedWorkoutWithSets?> =
        completedWorkoutDao.getCompletedWorkoutWithSets(id)

    override fun getAllExercisesWithCompletedSets(completedWorkoutId: Long): Flow<List<ExerciseWithCompletedSets>> =
        exerciseDao.getAllExercisesWithCompletedSets(completedWorkoutId)

    override fun getExerciseWithCompletedSet(exerciseId: Long): Flow<ExerciseWithCompletedSets?> =
        exerciseDao.getExerciseWithCompletedSets(exerciseId)

    fun getExerciseWithCompletedSetsById(exerciseId: Long): Flow<ExerciseWithCompletedSets?> =
        exerciseDao.getExerciseWithCompletedSets(exerciseId)

    override suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.insert(completedWorkout)

    override suspend fun deleteCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.delete(completedWorkout)

    override suspend fun updateCompletedWorkout(completedWorkout: CompletedWorkout) =
        completedWorkoutDao.update(completedWorkout)
}