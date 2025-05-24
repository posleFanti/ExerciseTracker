package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.ExerciseDao
import com.exercisetracker.data.dao.SetDao
import com.exercisetracker.data.dao.WorkoutDao
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithSetsView
import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.entities.WorkoutWithExercisesWithSets
import com.exercisetracker.ui.workouts.ExerciseWithSets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineWorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val setDao: SetDao
) : WorkoutRepository {
    override fun getAllWorkoutsStream(): Flow<List<Workout>> =
        workoutDao.getAllWorkouts()

    /*override fun getWorkoutWithExercisesStream(id: Long): Flow<WorkoutWithExercises?> =
        workoutDao.getWorkoutWithExercises(id)*/

    override fun getWorkoutStream(id: Long): Flow<Workout?> =
        workoutDao.getWorkout(id)

    override fun getSets(
        workoutId: Long,
        exerciseId: Long
    ): Flow<List<Set>?> = setDao.getSets(workoutId, exerciseId)

    override suspend fun getWorkoutWithExercisesWithSets(workoutId: Long): WorkoutWithExercisesWithSets =
        workoutDao.getWorkoutWithExercisesAndSets(workoutId)

    override suspend fun getExerciseWithSetsView(
        workoutId: Long,
        exerciseId: Long
    ): List<ExerciseWithSetsView> = workoutDao.getExerciseWithSetsView(workoutId, exerciseId)

    override suspend fun insertWorkout(workout: Workout) =
        workoutDao.insert(workout)

    override suspend fun insertSet(set: Set): Long =
        setDao.insert(set)

    override suspend fun deleteWorkout(workout: Workout) =
        workoutDao.delete(workout)

    override suspend fun deleteSet(set: Set) =
        setDao.delete(set)

    override suspend fun updateWorkout(workout: Workout) =
        workoutDao.update(workout)

    override suspend fun updateExercise(exercise: Exercise) =
        exerciseDao.update(exercise)

    override suspend fun updateSet(set: Set) =
        setDao.update(set)

    override suspend fun runInTransaction(block: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            workoutDao.runInTransaction(block)
        }
    }
}