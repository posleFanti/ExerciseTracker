package com.exercisetracker.ui.workouts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exercisetracker.data.entities.ExerciseWithSets
import com.exercisetracker.data.repositories.WorkoutRepository
import com.exercisetracker.ui.latest.trackerApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WorkoutDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    private val workoutId: Long = checkNotNull(savedStateHandle[WorkoutDetailsDestination.workoutIdArg])
    val workoutDetailsUiState: StateFlow<WorkoutDetailsUiState> =
        workoutRepository.getWorkoutWithExercisesStream(workoutId)
            .filterNotNull()
            .map { WorkoutDetailsUiState(
                workoutId = it.workout.workoutId,
                type = it.workout.type,
                exerciseList = it.exerciseList
            ) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkoutDetailsUiState()
            )



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WorkoutDetailsViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    workoutRepository = trackerApplication().container.workoutRepository
                )
            }
        }
    }
}

data class WorkoutDetailsUiState(
    val workoutId: Long = 0,
    val type: String = "",
    val exerciseList: List<ExerciseWithSets> = listOf(),
)