package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.exercisetracker.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: Exercise): Long

    @Query("SELECT * FROM exercises WHERE exercise_id = :id")
    suspend fun getExerciseById(id: Long): Exercise?

    @Query("SELECT * FROM exercises ORDER BY exercise_name ASC")
    fun getAllExercises(): Flow<List<Exercise>>
}