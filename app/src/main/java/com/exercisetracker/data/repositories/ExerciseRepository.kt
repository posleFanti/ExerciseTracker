package com.exercisetracker.data.repositories

import com.exercisetracker.data.entities.DateMaxWeight
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithSetsView
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getAllExercises(): Flow<List<Exercise>>
    fun getExercise(exerciseId: Long): Flow<Exercise>
    fun getExerciseStats(exerciseId: Long): Flow<List<DateMaxWeight>>
    suspend fun getExerciseWithSetsView(
        workoutId: Long,
        exerciseId: Long
    ): List<ExerciseWithSetsView>

    suspend fun insertExercise(exercise: Exercise)
    suspend fun deleteExercise(exercise: Exercise)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun searchExercisesFlow(query: String): Flow<List<Exercise>>
}