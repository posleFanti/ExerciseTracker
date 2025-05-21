package com.exercisetracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long = 0,

    @ColumnInfo(name = "exercise_name")
    val name: String
)

data class ExerciseWithCompletedSets(
    @Embedded
    val exercise: Exercise,

    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "exercise_id",
        entity = CompletedSetWithSet::class
    )
    val completedSetsWithSetList: List<CompletedSetWithSet>
)

data class ExerciseWithSets(
    @Embedded
    val exercise: Exercise,

    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "exercise_id",
        entity = Set::class
    )
    val sets: List<Set>
)
