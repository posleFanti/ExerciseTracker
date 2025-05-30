package com.exercisetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.exercisetracker.data.entities.DateMaxWeight
import kotlinx.coroutines.flow.Flow
import com.exercisetracker.data.entities.Set

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set: Set): Long

    @Update
    suspend fun update(set: Set)

    @Delete
    suspend fun delete(set: Set)

    @Query("SELECT * FROM sets WHERE workout_id = :workoutId AND exercise_id = :exerciseId ORDER BY exercise_id ASC, set_number ASC")
    fun getSets(workoutId: Long, exerciseId: Long): Flow<List<Set>?>

    @Query(
        """
        SELECT workouts.date as date, MAX(sets.weight) as max_weight 
        FROM sets 
        INNER JOIN workouts ON sets.workout_id = workouts.workout_id 
        WHERE sets.exercise_id = :exerciseId 
        GROUP BY workouts.date
        ORDER BY workouts.date
    """
    )
    fun getExerciseStats(exerciseId: Long): Flow<List<DateMaxWeight>>

    @Query("SELECT * FROM sets WHERE exercise_id = :exerciseId")
    fun getSetsByExerciseId(exerciseId: Long): Flow<List<Set>>

    @Query("SELECT * FROM sets WHERE workout_id = :workoutId")
    fun getSetsByWorkoutId(workoutId: Long): Flow<List<Set>>

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }
}