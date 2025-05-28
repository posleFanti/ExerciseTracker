package com.exercisetracker.ui.exercises

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.repositories.WorkoutRepository
import com.exercisetracker.ui.workouts.trackerApplication
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch

class ExerciseSearchViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _searchResults = mutableStateOf(emptyList<Exercise>())
    val searchResults: State<List<Exercise>> = _searchResults

    init {
        viewModelScope.launch {
            snapshotFlow { _searchQuery.value }
                .debounce(300) // Задержка для уменьшения запросов
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        workoutRepository.getAllExercises()
                    } else {
                        workoutRepository.searchExercisesFlow("%$query%")
                    }
                }
                .collect { exercises ->
                    _searchResults.value = exercises
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    suspend fun addExercise(exercise: Exercise) {
        workoutRepository.insertExercise(exercise)
        if (_searchQuery.value.isBlank()) {
            _searchResults.value = workoutRepository.getAllExercises().first()
        }
    }

    suspend fun deleteExercise(exercise: Exercise) {
        val sets = workoutRepository.getAllSets(exerciseId = exercise.exerciseId)
            .filterNotNull()
            .first()
        sets.forEach { set ->
            workoutRepository.deleteSet(set)
        }

        workoutRepository.deleteExercise(exercise)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ExerciseSearchViewModel(
                    workoutRepository = trackerApplication().container.workoutRepository
                )
            }
        }
    }
}