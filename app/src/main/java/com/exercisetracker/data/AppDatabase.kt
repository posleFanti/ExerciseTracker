package com.exercisetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.exercisetracker.data.dao.CompletedSetDao
import com.exercisetracker.data.dao.CompletedWorkoutDao
import com.exercisetracker.data.dao.ExerciseDao
import com.exercisetracker.data.dao.SetDao
import com.exercisetracker.data.dao.WorkoutDao
import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.entities.CompletedSet
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.Workout

@Database(
    entities = [
        Workout::class,
        Exercise::class,
        Set::class,
        CompletedWorkout::class,
        CompletedSet::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun setDao(): SetDao
    abstract fun completedWorkoutDao(): CompletedWorkoutDao
    abstract fun completedSetDao(): CompletedSetDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "workout_tracker.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}