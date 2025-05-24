package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long = 0,

    @ColumnInfo(name = "exercise_name")
    val name: String = "Unnamed"
)

@DatabaseView(
    """
    SELECT 
        exercises.*,
        sets.set_id AS set_id,
        sets.workout_id AS workout_id,
        sets.set_number AS set_number,
        sets.weight, 
        sets.reps
    FROM exercises
    INNER JOIN sets ON exercises.exercise_id = sets.exercise_id
    """
)
data class ExerciseWithSetsView(
    @Embedded
    val exercise: Exercise,

    @ColumnInfo(name="set_id")
    val setId: Long,

    @ColumnInfo(name="workout_id")
    val workoutId: Long,

    @ColumnInfo(name="set_number")
    val setNumber: Int,

    val weight: Double,
    val reps: Int
)