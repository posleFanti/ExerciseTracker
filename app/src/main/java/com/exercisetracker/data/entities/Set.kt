package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exercise_id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workout_id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Set(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "set_id")
    val setId: Long = 0,

    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long,

    @ColumnInfo(name = "workout_id")
    val workoutId: Long,

    @ColumnInfo(name = "set_number")
    val setNumber: Int,

    val weight: Double,
    val reps: Int,
)