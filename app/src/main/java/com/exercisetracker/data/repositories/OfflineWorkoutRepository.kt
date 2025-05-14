package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.SetDao
import com.exercisetracker.data.dao.WorkoutDao
import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.entities.Workout
import kotlinx.coroutines.flow.Flow

class OfflineWorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val setDao: SetDao
) : WorkoutRepository {
    override fun getAllWorkoutsStream(): Flow<List<Workout>> =
        workoutDao.getAllWorkouts()

    override fun getWorkoutStream(id: Long): Flow<Workout?> =
        workoutDao.getWorkout(id)

    override fun getSetsForWorkout(id: Long): Flow<List<Set>> =
        setDao.getSetsForWorkout(id)

    override suspend fun insertWorkout(workout: Workout) =
        workoutDao.insert(workout)

    override suspend fun deleteWorkout(workout: Workout) =
        workoutDao.delete(workout)

    override suspend fun updateWorkout(workout: Workout) =
        workoutDao.update(workout)
}