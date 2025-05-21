package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val workoutId: Long = 0,

    @ColumnInfo(name = "workout_type")
    val type: String
)

data class WorkoutWithExercises(
    @Embedded
    val workout: Workout,

    @Relation(
        parentColumn = "workout_id",
        entityColumn = "workout_id",
        entity = Exercise::class
    )
    val exerciseList: List<ExerciseWithSets>
)
