package com.exercisetracker.ui.exercises

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.exercisetracker.data.repositories.ExerciseRepository
import com.exercisetracker.data.repositories.WorkoutRepository
import com.exercisetracker.ui.workouts.trackerApplication
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ExerciseEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val exerciseId: Long =
        checkNotNull(savedStateHandle[ExerciseEditDestination.exerciseIdArg])

    var exerciseUiState by mutableStateOf(ExerciseUiState())
        private set

    init {
        viewModelScope.launch {
            exerciseUiState = exerciseRepository.getExercise(exerciseId)
                .filterNotNull()
                .first()
                .toExerciseUiState()
        }
    }

    fun updateUiState(exerciseDetails: ExerciseDetails) {
        exerciseUiState = ExerciseUiState(exerciseDetails)
    }

    fun updateImage(bitmap: Bitmap?) {
        val imageByteArray = bitmap?.let { bitmapToByteArray(it) }
        exerciseUiState = exerciseUiState.copy(
            exerciseDetails = exerciseUiState.exerciseDetails.copy(image = imageByteArray)
        )
    }

    fun clearImage() {
        exerciseUiState = exerciseUiState.copy(
            exerciseDetails = exerciseUiState.exerciseDetails.copy(image = null)
        )
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        return stream.toByteArray()
    }

    suspend fun updateExercise() {
        exerciseRepository.updateExercise(exerciseUiState.exerciseDetails.toExercise())
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ExerciseEditViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    exerciseRepository = trackerApplication().container.exerciseRepository
                )
            }
        }
    }
}

data class ExerciseDetails(
    val exerciseId: Long = 0,
    val name: String = "",
    val image: ByteArray? = null
)

data class ExerciseUiState(
    val exerciseDetails: ExerciseDetails = ExerciseDetails()
)

fun Exercise.toExerciseUiState(): ExerciseUiState = ExerciseUiState(
    ExerciseDetails(
        exerciseId = this.exerciseId,
        name = this.name,
        image = image
    )
)

fun ExerciseDetails.toExercise(): Exercise = Exercise(
    exerciseId = this.exerciseId,
    name = this.name,
    image = image
)

fun ByteArray.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeByteArray(this, 0, this.size)
    } catch (e: Exception) {
        null
    }
}