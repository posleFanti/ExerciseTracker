@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

object ExerciseEditDestination : NavigationDestination {
    override val route = "exercise_edit"
    override val titleRes = R.string.exercise_edit_title
    const val exerciseIdArg = "exerciseId"
    val routeWithArgs = "$route/{$exerciseIdArg}"
}

@Composable
fun ExerciseEditScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseEditViewModel = viewModel(factory = ExerciseEditViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = viewModel.exerciseUiState.exerciseDetails.name,
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        ExerciseEditBody(
            exerciseUiState = viewModel.exerciseUiState,
            onExerciseValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateExercise()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ExerciseEditBody(
    exerciseUiState: ExerciseUiState,
    onExerciseValueChange: (ExerciseDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(16.dp)
    ) {
        ExerciseEditForm(
            exerciseDetails = exerciseUiState.exerciseDetails,
            onValueChange = onExerciseValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Сохранить")
        }
    }
}

@Composable
private fun ExerciseEditForm(
    exerciseDetails: ExerciseDetails,
    onValueChange: (ExerciseDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = exerciseDetails.name,
            onValueChange = { onValueChange(exerciseDetails.copy(name = it)) },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}
