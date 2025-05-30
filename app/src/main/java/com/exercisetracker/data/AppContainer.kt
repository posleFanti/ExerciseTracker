package com.exercisetracker.data

import android.content.Context
import com.exercisetracker.data.repositories.ExerciseRepository
import com.exercisetracker.data.repositories.OfflineExerciseRepository
import com.exercisetracker.data.repositories.OfflineSetRepository
import com.exercisetracker.data.repositories.OfflineWorkoutRepository
import com.exercisetracker.data.repositories.SetRepository
import com.exercisetracker.data.repositories.WorkoutRepository

interface AppContainer {
    val workoutRepository: WorkoutRepository
    val exerciseRepository: ExerciseRepository
    val setRepository: SetRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val workoutRepository: WorkoutRepository by lazy {
        OfflineWorkoutRepository(
            AppDatabase.getDatabase(context).workoutDao(),
        )
    }

    override val exerciseRepository: ExerciseRepository by lazy {
        OfflineExerciseRepository(
            AppDatabase.getDatabase(context).exerciseDao(),
            AppDatabase.getDatabase(context).setDao()
        )
    }

    override val setRepository: SetRepository by lazy {
        OfflineSetRepository(
            AppDatabase.getDatabase(context).setDao()
        )
    }
}