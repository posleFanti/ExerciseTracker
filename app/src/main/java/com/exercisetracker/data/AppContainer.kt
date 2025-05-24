package com.exercisetracker.data

import android.content.Context
import com.exercisetracker.data.repositories.OfflineWorkoutRepository
import com.exercisetracker.data.repositories.WorkoutRepository

interface AppContainer {
    val workoutRepository: WorkoutRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val workoutRepository: WorkoutRepository by lazy {
        OfflineWorkoutRepository(
            AppDatabase.getDatabase(context).workoutDao(),
            AppDatabase.getDatabase(context).exerciseDao(),
            AppDatabase.getDatabase(context).setDao()
        )
    }
}