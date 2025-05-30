package com.exercisetracker.data.repositories

import com.exercisetracker.data.dao.ExerciseDao
import com.exercisetracker.data.dao.SetDao
import com.exercisetracker.data.entities.DateMaxWeight
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithSetsView
import kotlinx.coroutines.flow.Flow

class OfflineExerciseRepository(
    private val exerciseDao: ExerciseDao,
    private val setDao: SetDao
) : ExerciseRepository {
    override fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAllExercises()

    override fun getExercise(exerciseId: Long): Flow<Exercise> =
        exerciseDao.getExerciseById(exerciseId)

    override fun getExerciseStats(exerciseId: Long): Flow<List<DateMaxWeight>> =
        setDao.getExerciseStats(exerciseId)

    override suspend fun getExerciseWithSetsView(
        workoutId: Long,
        exerciseId: Long
    ): List<ExerciseWithSetsView> =
        exerciseDao.getExerciseWithSetsView(workoutId, exerciseId)

    override suspend fun insertExercise(exercise: Exercise) =
        exerciseDao.insert(exercise)

    override suspend fun deleteExercise(exercise: Exercise) =
        exerciseDao.delete(exercise)

    override suspend fun updateExercise(exercise: Exercise) =
        exerciseDao.update(exercise)

    override suspend fun searchExercisesFlow(query: String): Flow<List<Exercise>> =
        exerciseDao.searchExercisesFlow(query)
}