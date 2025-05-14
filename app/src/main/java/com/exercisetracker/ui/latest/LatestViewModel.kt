package com.exercisetracker.ui.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exercisetracker.TrackerApplication
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.repositories.CompletedWorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LatestViewModel(completedWorkoutRepository: CompletedWorkoutRepository) : ViewModel() {
    val latestUiState: StateFlow<LatestUiState> =
        completedWorkoutRepository.getAllCompletedWorkoutsStream().map { LatestUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = LatestUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
           initializer {
               LatestViewModel(
                    completedWorkoutRepository = trackerApplication().container.completedWorkoutRepository
               )
           }
        }
    }
}

data class LatestUiState(val completedWorkoutsList: List<CompletedWorkout> = listOf())

fun CreationExtras.trackerApplication(): TrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TrackerApplication)