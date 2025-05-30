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
    val name: String = "Unnamed",

    val image: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exercise

        if (exerciseId != other.exerciseId) return false
        if (name != other.name) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = exerciseId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}

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

    @ColumnInfo(name = "set_id")
    val setId: Long,

    @ColumnInfo(name = "workout_id")
    val workoutId: Long,

    @ColumnInfo(name = "set_number")
    val setNumber: Int,

    val weight: Double,
    val reps: Int
)

data class DateMaxWeight(
    val date: String,

    @ColumnInfo(name = "max_weight")
    val maxWeight: Double
)