package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val workoutId: Long = 0,

    val name: String = "Unnamed",
    val type: String = "Undefined",
    val date: String = "1980-01-01",
)

data class WorkoutWithExercisesWithSets(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workout_id",
        entityColumn = "workout_id"
    )
    var exercisesWithSets: List<ExerciseWithSetsView>
)