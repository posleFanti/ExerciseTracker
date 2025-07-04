package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithSetsView
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Update
    suspend fun update(exercise: Exercise)

    @Query("SELECT * FROM exercises WHERE exercise_id = :id")
    fun getExerciseById(id: Long): Flow<Exercise>

    @Query("SELECT * FROM exercises ORDER BY exercise_name ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE exercise_name LIKE :query")
    fun searchExercisesFlow(query: String): Flow<List<Exercise>>

    @Transaction
    @Query("SELECT * FROM ExerciseWithSetsView WHERE exercise_id = :exerciseId AND workout_id = :workoutId ORDER BY set_number ASC")
    suspend fun getExerciseWithSetsView(
        workoutId: Long,
        exerciseId: Long
    ): List<ExerciseWithSetsView>

    /*@Transaction
    @Query("SELECT * FROM exercises WHERE exercise_id = :id")
    fun getExerciseWithCompletedSets(id: Long): Flow<ExerciseWithCompletedSets?>

    @Transaction
    @Query("SELECT * FROM exercises")
    fun getAllExercisesWithCompletedSets(completedWorkoutId: Long): Flow<List<ExerciseWithCompletedSets>>*/
}