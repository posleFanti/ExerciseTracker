package com.exercisetracker.ui.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.data.entities.Set
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import kotlinx.coroutines.launch

object DoneExerciseEditDestination : NavigationDestination {
    override val route = "done_exercise_edit"
    override val titleRes = R.string.done_exercise_edit
    const val workoutIdArg = "workoutId"
    const val exerciseIdArg = "exerciseId"
    val routeWithArgs = "$route/{$workoutIdArg}/{$exerciseIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoneExerciseEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DoneExerciseEditViewModel = viewModel(factory = DoneExerciseEditViewModel.Factory)
) {
    val uiState = viewModel.doneExerciseEditUiState
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Упражнение " + uiState.doneExerciseDetails.exercise.name,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addSet() }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { innerPadding ->
        Column {
            Body(
                uiState = uiState,
                onRemoveSet = viewModel::removeSet,
                onValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateDoneExercise()
                        navigateBack()
                    }
                },
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun Body(
    uiState: DoneExerciseEditUiState,
    onRemoveSet: (Int) -> Unit,
    onValueChange: (DoneExerciseDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(16.dp)
    ) {
        SetsInputForm(
            uiState = uiState,
            onRemoveSet = { setNumber -> onRemoveSet(setNumber) },
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Сохранить")
        }
    }
}

@Composable
private fun SetsInputForm(
    uiState: DoneExerciseEditUiState,
    onRemoveSet: (Int) -> Unit,
    onValueChange: (DoneExerciseDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = uiState.doneExerciseDetails.sets, key = { it.setNumber }) { item ->
           InputRow(
                setNumber = item.setNumber,
                onRemoveSet = onRemoveSet,
                doneExerciseDetails = uiState.doneExerciseDetails,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
           )
        }
    }
}

@Composable
private fun InputRow(
    setNumber: Int,
    onRemoveSet: (Int) -> Unit,
    doneExerciseDetails: DoneExerciseDetails,
    onValueChange: (DoneExerciseDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(text = setNumber.toString())
        OutlinedTextField(
            value = doneExerciseDetails.sets[setNumber-1].reps.toString(),
            onValueChange = { onValueChange(doneExerciseDetails.copy(sets = changeReps(it, doneExerciseDetails.sets, setNumber))) },
            label = { Text(text="Кол-во повторений") },
            singleLine = true,
            modifier = Modifier.width(150.dp)
        )
        Spacer(Modifier.size(30.dp))
        OutlinedTextField(
            value = doneExerciseDetails.sets[setNumber-1].weight.toString(),
            onValueChange = { onValueChange(doneExerciseDetails.copy(sets = changeWeight(it, doneExerciseDetails.sets, setNumber)))},
            label = { Text(text = "Вес") },
            singleLine = true,
            modifier = Modifier.width(150.dp)
        )
        IconButton(onClick = { onRemoveSet(setNumber) }) {
            Icon(Icons.Default.Delete, "Delete")
        }
    }
}

private fun changeReps(newValue: String, sets: List<Set>, setNumber: Int): List<Set> {
    return sets.map { item ->
        if (item.setNumber == setNumber) item.copy(reps = if (newValue.isBlank()) 0 else newValue.toInt()) else item
    }
}

private fun changeWeight(newValue: String, sets: List<Set>, setNumber: Int): List<Set> {
    return sets.map { item ->
        if (item.setNumber == setNumber) item.copy(weight = if (newValue.isBlank()) 0.0 else newValue.toDouble()) else item
    }
}


@Preview(showBackground = true)
@Composable
fun InputRowPreview() {
    ExerciseTrackerTheme {
        InputRow(
            setNumber = 1,
            onRemoveSet = {},
            doneExerciseDetails = DoneExerciseDetails(
                sets = listOf(Set(0,0,0,1,27.4,10))
            ),
            onValueChange = { _ -> },
        )
    }
}