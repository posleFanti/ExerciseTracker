package com.exercisetracker.ui.workouts

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import com.exercisetracker.data.repositories.ExerciseRepository
import com.exercisetracker.data.repositories.SetRepository
import com.exercisetracker.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val setRepository: SetRepository
) : ViewModel() {
    private val workoutId: Long =
        checkNotNull(savedStateHandle[WorkoutDetailsDestination.workoutIdArg])

    var uiState by mutableStateOf(WorkoutDetailsUiState())
        private set

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _searchResults = mutableStateOf(emptyList<Exercise>())
    val searchResults: State<List<Exercise>> = _searchResults

    init {
        viewModelScope.launch {
            workoutRepository.getWorkoutWithExercisesWithSets(workoutId)
                .collect { workoutData ->
                    uiState = workoutData.toWorkoutDetailsUiState()
                }
        }

        viewModelScope.launch {
            snapshotFlow { _searchQuery.value }
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    _searchResults.value = if (query.isBlank()) {
                        exerciseRepository.getAllExercises()
                            .filterNotNull()
                            .first()
                    } else {
                        exerciseRepository.searchExercisesFlow("%$query%")
                            .filterNotNull().first()
                    }
                }
        }
    }

    suspend fun addExerciseWithSets(exercise: Exercise) {
        val currentWorkoutId = uiState.workout.workoutId
        val newSet = Set(
            exerciseId = exercise.exerciseId,
            workoutId = currentWorkoutId,
            setNumber = 1,
            weight = 0.0,
            reps = 0
        )

        setRepository.insertSet(newSet)
    }

    suspend fun deleteExercise(exerciseWithSets: ExerciseWithSets) {
        exerciseWithSets.sets.forEach { set ->
            setRepository.deleteSet(set)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WorkoutDetailsViewModel(
                    this.createSavedStateHandle(),
                    workoutRepository = trackerApplication().container.workoutRepository,
                    exerciseRepository = trackerApplication().container.exerciseRepository,
                    setRepository = trackerApplication().container.setRepository
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

fun WorkoutWithExercisesWithSets.toWorkoutDetailsUiState(): WorkoutDetailsUiState =
    WorkoutDetailsUiState(
        workout = workout,
        exercisesWithSets = groupExercisesWithSets(exercisesWithSets)
    )

fun groupExercisesWithSets(views: List<ExerciseWithSetsView>): List<ExerciseWithSets> {
    return views
        .groupBy { it.exercise.exerciseId }
        .map { (_, setsViews) ->
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