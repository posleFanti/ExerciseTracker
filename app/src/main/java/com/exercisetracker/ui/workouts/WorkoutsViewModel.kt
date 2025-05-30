package com.exercisetracker.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exercisetracker.TrackerApplication
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.repositories.SetRepository
import com.exercisetracker.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WorkoutsViewModel(
    private val workoutRepository: WorkoutRepository,
    private val setRepository: SetRepository
) : ViewModel() {
    val workoutsUiState: StateFlow<WorkoutsUiState> =
        workoutRepository.getAllWorkoutsStream().map { WorkoutsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkoutsUiState()
            )

    suspend fun addWorkout(workout: Workout) {
        workoutRepository.insertWorkout(workout)
    }

    suspend fun updateWorkout(workout: Workout) {
        workoutRepository.updateWorkout(workout)
    }

    suspend fun deleteWorkout(workout: Workout) {
        setRepository.runInTransaction {
            setRepository.getSetsByWorkoutId(workout.workoutId).filterNotNull().first()
                .forEach { set ->
                    setRepository.deleteSet(set)
                }
            workoutRepository.deleteWorkout(workout)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WorkoutsViewModel(
                    workoutRepository = trackerApplication().container.workoutRepository,
                    setRepository = trackerApplication().container.setRepository
                )
            }
        }
    }
}

data class WorkoutsUiState(val workoutList: List<Workout> = listOf())

fun CreationExtras.trackerApplication(): TrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TrackerApplication)