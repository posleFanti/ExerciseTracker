package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.WorkoutDao
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.entities.WorkoutWithExercisesWithSets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineWorkoutRepository(
    private val workoutDao: WorkoutDao,
) : WorkoutRepository {
    override fun getAllWorkoutsStream(): Flow<List<Workout>> =
        workoutDao.getAllWorkouts()

    override fun getWorkoutStream(id: Long): Flow<Workout?> =
        workoutDao.getWorkout(id)

    override suspend fun getWorkoutWithExercisesWithSets(workoutId: Long): Flow<WorkoutWithExercisesWithSets> =
        workoutDao.getWorkoutWithExercisesAndSets(workoutId)

    override suspend fun insertWorkout(workout: Workout) =
        workoutDao.insert(workout)

    override suspend fun deleteWorkout(workout: Workout) =
        workoutDao.delete(workout)

    override suspend fun updateWorkout(workout: Workout) =
        workoutDao.update(workout)

    override suspend fun runInTransaction(block: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            workoutDao.runInTransaction(block)
        }
    }
}