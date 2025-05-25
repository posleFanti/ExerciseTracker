package com.exercisetracker.ui.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.repositories.WorkoutRepository
import com.exercisetracker.ui.workouts.trackerApplication
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ExerciseEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    private val exerciseId: Long = checkNotNull(savedStateHandle[ExerciseEditDestination.exerciseIdArg])

    var exerciseUiState by mutableStateOf(ExerciseUiState())
        private set

    init {
        viewModelScope.launch {
            exerciseUiState = workoutRepository.getExercise(exerciseId)
                .filterNotNull()
                .first()
                .toExerciseUiState()
        }
    }

    fun updateUiState(exerciseDetails: ExerciseDetails) {
        exerciseUiState = ExerciseUiState(exerciseDetails)
    }

    suspend fun updateExercise() {
        workoutRepository.updateExercise(exerciseUiState.exerciseDetails.toExercise())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ExerciseEditViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    workoutRepository = trackerApplication().container.workoutRepository
                )
            }
        }
    }
}

data class ExerciseDetails(
    val exerciseId: Long = 0,
    val name: String = ""
)

data class ExerciseUiState(
    val exerciseDetails: ExerciseDetails = ExerciseDetails()
)

fun Exercise.toExerciseUiState(): ExerciseUiState = ExerciseUiState(ExerciseDetails(
    exerciseId = this.exerciseId,
    name = this.name
))

fun ExerciseDetails.toExercise(): Exercise = Exercise(
    exerciseId = this.exerciseId,
    name = this.name
)