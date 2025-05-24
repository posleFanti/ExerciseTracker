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
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.entities.WorkoutWithExercisesWithSets
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: Workout)

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workouts ORDER BY type ASC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE workout_id = :id ORDER BY type ASC")
    fun getWorkout(id: Long): Flow<Workout>

    @Transaction
    @Query("SELECT * FROM workouts WHERE workout_id = :workoutId")
    suspend fun getWorkoutWithExercisesAndSets(workoutId: Long): WorkoutWithExercisesWithSets

    @Transaction
    @Query("SELECT * FROM ExerciseWithSetsView WHERE exercise_id = :exerciseId AND workout_id = :workoutId ORDER BY set_number ASC")
    suspend fun getExerciseWithSetsView(workoutId: Long, exerciseId: Long): List<ExerciseWithSetsView>


    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }
}