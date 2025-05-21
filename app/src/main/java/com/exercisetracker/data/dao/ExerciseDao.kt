package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithCompletedSets
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: Exercise): Long

    @Query("SELECT * FROM exercises WHERE exercise_id = :id")
    suspend fun getExerciseById(id: Long): Exercise?

    @Query("SELECT * FROM exercises ORDER BY exercise_name ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Transaction
    @Query("SELECT * FROM exercises WHERE exercise_id = :id")
    fun getExerciseWithCompletedSets(id: Long): Flow<ExerciseWithCompletedSets?>

    @Transaction
    @Query("SELECT * FROM exercises")
    fun getAllExercisesWithCompletedSets(completedWorkoutId: Long): Flow<List<ExerciseWithCompletedSets>>
}