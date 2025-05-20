package com.exercisetracker.ui.latest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exercisetracker.data.entities.CompletedSetWithSet
import com.exercisetracker.data.entities.CompletedWorkoutWithSets
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.repositories.CompletedWorkoutRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class LatestEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val completedWorkoutRepository: CompletedWorkoutRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            latestEditUiState = completedWorkoutRepository.getCompletedWorkoutWithSetsStream(completedWorkoutId)
                .filterNotNull()
                .first()
                .toLatestEditUiState(true)
        }
    }

    var latestEditUiState by mutableStateOf(LatestEditUiState())
        private set

    private val completedWorkoutId: Long = checkNotNull(savedStateHandle[LatestEditScreen.completedWorkoutIdArg])

    private fun getExercisesWithCompletedSets() {}

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LatestEditViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    completedWorkoutRepository = trackerApplication().container.completedWorkoutRepository
                )
            }
        }
    }
}

data class LatestEditUiState(
    val completedWorkoutDetails: CompletedWorkoutWithSetsDetails = CompletedWorkoutWithSetsDetails(),
    val isEntryValid: Boolean = false
)

data class CompletedWorkoutWithSetsDetails(
    val completedWorkoutId: Long = 0,
    val workoutId: Long = 0,
    val date: LocalDate = LocalDate.of(2025, 5, 1),
    val completedSetsWithSets: List<CompletedSetWithSet> = listOf()
)

data class ExerciseWithCompletedSets(
    val exerciseId: Long = 0,
    val exerciseName: String = "",
    val completedSetsWithSets: CompletedWorkoutWithSets
)

fun CompletedWorkoutWithSets.toCompletedWorkoutWithSetsDetails(): CompletedWorkoutWithSetsDetails = CompletedWorkoutWithSetsDetails(
    completedWorkoutId = completedWorkout.completedWorkoutId,
    workoutId = completedWorkout.completedWorkoutId,
    date = completedWorkout.date,
    completedSetsWithSets = completedSets
)

fun CompletedWorkoutWithSets.toLatestEditUiState(isEntryValid: Boolean = false): LatestEditUiState = LatestEditUiState(
    completedWorkoutDetails = this.toCompletedWorkoutWithSetsDetails(),
    isEntryValid = isEntryValid
)