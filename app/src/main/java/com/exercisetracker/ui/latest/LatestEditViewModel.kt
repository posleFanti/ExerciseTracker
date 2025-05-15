package com.exercisetracker.ui.latest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.repositories.CompletedWorkoutRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LatestEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val completedWorkoutRepository: CompletedWorkoutRepository
) : ViewModel() {

    var latestEditUiState by mutableStateOf(LatestUiState())
        private set

    private val completedWorkoutId: Long = checkNotNull(savedStateHandle[LatestEditScreen.completedWorkoutIdArg])
}
