package com.exercisetracker.data

import android.content.Context
import com.exercisetracker.data.repositories.CompletedWorkoutRepository
import com.exercisetracker.data.repositories.OfflineCompletedWorkoutRepository
import com.exercisetracker.data.repositories.OfflineWorkoutRepository
import com.exercisetracker.data.repositories.WorkoutRepository

interface AppContainer {
    val workoutRepository: WorkoutRepository
    val completedWorkoutRepository: CompletedWorkoutRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val workoutRepository: WorkoutRepository by lazy {
        OfflineWorkoutRepository(
            AppDatabase.getDatabase(context).workoutDao(),
            AppDatabase.getDatabase(context).setDao()
        )
    }
    override val completedWorkoutRepository: CompletedWorkoutRepository by lazy {
        OfflineCompletedWorkoutRepository(
            AppDatabase.getDatabase(context).completedWorkoutDao()
        )
    }
}