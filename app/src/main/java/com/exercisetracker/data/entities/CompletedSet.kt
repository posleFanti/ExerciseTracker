package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "completed_sets",
    foreignKeys = [
        ForeignKey(
            entity = CompletedWorkout::class,
            parentColumns = ["completed_workout_id"],
            childColumns = ["completed_workout_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Set::class,
            parentColumns = ["set_id"],
            childColumns = ["set_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("completed_workout_id"), Index("set_id")]
)
data class CompletedSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "completed_set_id")
    val completedSetId: Long = 0,

    @ColumnInfo(name = "completed_workout_id")
    val completedWorkoutId: Long,

    @ColumnInfo(name = "set_id")
    val setId: Long,

    @ColumnInfo(name = "weight")
    val weight: Float,

    @ColumnInfo(name = "actual_reps")
    val actualReps: Int
)
)
