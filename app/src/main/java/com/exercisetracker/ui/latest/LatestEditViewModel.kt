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
import com.exercisetracker.data.dao.ExerciseDao
import com.exercisetracker.data.entities.CompletedSetWithSet
import com.exercisetracker.data.entities.CompletedWorkoutWithSets
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithCompletedSets
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
            latestEditUiState = completedWorkoutRepository.getAllExercisesWithCompletedSets(completedWorkoutId)
                .filterNotNull()
                .first()
                .toLatestEditUiState(true)
        }
    }

    var latestEditUiState by mutableStateOf(LatestEditUiState())
        private set

    private val completedWorkoutId: Long = checkNotNull(savedStateHandle[LatestEditScreen.completedWorkoutIdArg])

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
    val exerciseList: List<ExerciseWithCompletedSetsDetails> = listOf(),
    val isEntryValid: Boolean = false
)

data class ExerciseWithCompletedSetsDetails(
    val exerciseId: Long = 0,
    val name: String = "",
    val completedSetsWithSetList: List<CompletedSetWithSet> = listOf()
)

fun ExerciseWithCompletedSets.toExerciseWithCompletedSetsDetails(): ExerciseWithCompletedSetsDetails = ExerciseWithCompletedSetsDetails (
    exerciseId = exercise.exerciseId,
    name = exercise.name,
    completedSetsWithSetList = completedSetsWithSetList
)

/*suspend fun CompletedWorkoutWithSets.listExercisesWithCompletedSets(
    completedSets: List<CompletedSetWithSet>
): List<ExerciseWithCompletedSets> {
    return completedSets
        .groupBy { it.originalSet.exerciseId }
        .map { (exerciseId, sets) ->
            val exercise = exerciseDao.getExerciseById(exerciseId) ?: return@map null
            ExerciseWithCompletedSets(
                exerciseId = exerciseId,
                exerciseName = exercise.name,
                completedSetsWithSets = sets
            )
        }
        .filterNotNull()
}*/

fun List<ExerciseWithCompletedSets>.toLatestEditUiState(isEntryValid: Boolean = false): LatestEditUiState = LatestEditUiState(
    exerciseList = map { it ->
        it.toExerciseWithCompletedSetsDetails()
    },
    isEntryValid = isEntryValid
)