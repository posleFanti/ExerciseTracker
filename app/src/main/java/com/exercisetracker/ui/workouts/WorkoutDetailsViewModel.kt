package com.exercisetracker.ui.workouts

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
import com.exercisetracker.data.entities.ExerciseWithSetsView
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.data.entities.WorkoutWithExercisesWithSets
import com.exercisetracker.data.entities.Set
import com.exercisetracker.data.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    private val workoutId: Long = checkNotNull(savedStateHandle[WorkoutDetailsDestination.workoutIdArg])
    var uiState by mutableStateOf(WorkoutDetailsUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = workoutRepository.getWorkoutWithExercisesWithSets(workoutId)
                    .toWorkoutDetailsUiState()
        }
    }

    fun addExerciseWithSets() {

    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WorkoutDetailsViewModel(
                    this.createSavedStateHandle(),
                    workoutRepository = trackerApplication().container.workoutRepository
                )
            }
        }
    }
}

data class WorkoutDetailsUiState(
    val workout: Workout = Workout(),
    val exercisesWithSets: List<ExerciseWithSets> = listOf()
)

data class ExerciseWithSets(
    val exercise: Exercise,
    val sets: List<Set>
)

fun WorkoutWithExercisesWithSets.toWorkoutDetailsUiState(): WorkoutDetailsUiState = WorkoutDetailsUiState(
    workout = workout,
    exercisesWithSets = groupExercisesWithSets(exercisesWithSets)
)

fun groupExercisesWithSets(views: List<ExerciseWithSetsView>): List<ExerciseWithSets> {
    return views
        .groupBy { it.exercise.exerciseId }
        .map { (exerciseId, setsViews) ->
            ExerciseWithSets(
                exercise = setsViews.first().exercise,
                sets = setsViews.map {
                    Set(
                        setId = it.setId,
                        exerciseId = it.exercise.exerciseId,
                        workoutId = it.workoutId,
                        weight = it.weight,
                        setNumber = it.setNumber,
                        reps = it.reps
                    )
                }
            )
        }
}