package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity(
    tableName = "completed_workouts",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workout_id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workout_id")]
)
data class CompletedWorkout(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "completed_workout_id")
    val completedWorkoutId: Long = 0,

    @ColumnInfo(name = "workout_id")
    val workoutId: Long,

    @ColumnInfo(name = "workout_date")
    val date: LocalDate
)

data class CompletedWorkoutWithSets(
    @Embedded
    val completedWorkout: CompletedWorkout,

    @Relation(
        parentColumn = "completed_workout_id",
        entityColumn = "completed_workout_id",
        entity = CompletedSet::class
    )
    val completedSets: List<CompletedSetWithSet>
)
