package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "workout_exercise_cross_ref",
    primaryKeys = ["workout_id", "exercise_id"],
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workout_id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exercise_id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutExerciseCrossRef(
    @ColumnInfo(name = "workout_id")
    val workoutId: Long,

    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long
)
