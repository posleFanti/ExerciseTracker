package com.exercisetracker.ui.workouts

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithSetsView
import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class DoneExerciseEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
): ViewModel() {
    private val workoutId: Long = checkNotNull(savedStateHandle[DoneExerciseEditDestination.workoutIdArg])
    private val exerciseId: Long = checkNotNull(savedStateHandle[DoneExerciseEditDestination.exerciseIdArg])

    var doneExerciseEditUiState by mutableStateOf(DoneExerciseEditUiState())
        private set

    init {
        viewModelScope.launch {
            doneExerciseEditUiState = workoutRepository.getExerciseWithSetsView(workoutId, exerciseId)
                .toDoneExerciseEditUiState(true)
        }
    }

    private fun validateInput(doneExerciseDetails: DoneExerciseDetails): Boolean {
        return true
    }

    fun updateUiState(doneExerciseDetails: DoneExerciseDetails) {
        doneExerciseEditUiState = DoneExerciseEditUiState(
            doneExerciseDetails = doneExerciseDetails,
            isEntryValid = validateInput(doneExerciseDetails)
        )
    }

    fun addSet() {
        val currentSets = doneExerciseEditUiState.doneExerciseDetails.sets
        val lastSet = currentSets.lastOrNull()

        val newSetNumber = (lastSet?.setNumber ?: 0) + 1

        val newSet = Set(
            setId = 0,
            exerciseId = exerciseId,
            workoutId = workoutId,
            setNumber = newSetNumber,
            weight = 0.0,
            reps = 0
        )

        val updatedDetails = doneExerciseEditUiState.doneExerciseDetails.copy(
            sets = currentSets + newSet
        )

        doneExerciseEditUiState = doneExerciseEditUiState.copy(
            doneExerciseDetails = updatedDetails,
            isEntryValid = validateInput(updatedDetails)
        )
    }

    fun removeSet(setNumber: Int) {
        val currentSets = doneExerciseEditUiState.doneExerciseDetails.sets

        // Удаляем подход с указанным номером
        val filteredSets = currentSets.filterNot { it.setNumber == setNumber }

        // Пересчитываем номера подходов, чтобы они шли последовательно (1, 2, 3...)
        val reorderedSets = filteredSets.mapIndexed { index, set ->
            set.copy(setNumber = index + 1)
        }

        val updatedDetails = doneExerciseEditUiState.doneExerciseDetails.copy(
            sets = reorderedSets
        )

        doneExerciseEditUiState = doneExerciseEditUiState.copy(
            doneExerciseDetails = updatedDetails,
            isEntryValid = validateInput(updatedDetails)
        )
    }

    suspend fun updateDoneExercise() {
        if (validateInput(doneExerciseEditUiState.doneExerciseDetails)) {
            val details = doneExerciseEditUiState.doneExerciseDetails

            // Обновляем упражнение
            workoutRepository.updateExercise(details.exercise)
            Log.d("ViewModel", "exerciseId: $exerciseId, workoutId: $workoutId")

            // Получаем текущие подходы из базы данных
            val existingSets = workoutRepository.getSets(workoutId, exerciseId)
                .filterNotNull()
                .first()

            workoutRepository.runInTransaction {
                // Удаляем все существующие подходы
                existingSets.forEach { set ->
                    workoutRepository.deleteSet(set)
                }

                // Добавляем все новые подходы
                details.sets.forEach { set ->
                    workoutRepository.insertSet(set.copy(setId = 0)) // Сбрасываем ID для создания нового
                }
            }


        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DoneExerciseEditViewModel(
                    this.createSavedStateHandle(),
                    workoutRepository = trackerApplication().container.workoutRepository,
                )
            }
        }
    }
}

data class DoneExerciseEditUiState(
    val doneExerciseDetails: DoneExerciseDetails = DoneExerciseDetails(),
    val isEntryValid: Boolean = true
)

data class DoneExerciseDetails(
    val exercise: Exercise = Exercise(),
    val sets: List<Set> = listOf()
)

fun List<ExerciseWithSetsView>.toDoneExerciseEditUiState(isEntryValid: Boolean = false): DoneExerciseEditUiState {
    val exerciseWithSets = groupExercisesWithSets(this).first()
    return DoneExerciseEditUiState(
        doneExerciseDetails = DoneExerciseDetails(exercise = exerciseWithSets.exercise, sets = exerciseWithSets.sets),
        isEntryValid = isEntryValid
    )
}
