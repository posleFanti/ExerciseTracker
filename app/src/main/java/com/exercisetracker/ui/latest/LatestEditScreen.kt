package com.exercisetracker.ui.latest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import java.time.LocalDate
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import com.exercisetracker.data.entities.CompletedSet
import com.exercisetracker.data.entities.CompletedSetWithSet
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithCompletedSets
import com.exercisetracker.data.entities.Set

object LatestEditScreen : NavigationDestination {
    override val route = "latest_edit"
    override val titleRes = R.string.latest_edit_title
    const val completedWorkoutIdArg = "itemId"
    val routeWithArgs = "$route/{$completedWorkoutIdArg}"
}

// TODO:    Add updating ui state
//          Add output of ui state correctly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LatestEditViewModel = viewModel(factory = LatestEditViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Тренировка: дата",
                canNavigateBack = true,
                navigateUp = navigateBack
            ) },
        modifier = modifier
    ) { innerPadding ->
        LatestEditScreenBody(viewModel.latestEditUiState, modifier.padding(innerPadding))
    }
}

@Composable
private fun LatestEditScreenBody(
    latestEditUiState: LatestEditUiState,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Выполненные упражнения: ",
            modifier = Modifier.padding(16.dp)
        )
        DoneExercisesList(latestEditUiState, modifier)
    }
}

@Composable
private fun DoneExercisesList(
    latestEditUiState: LatestEditUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = latestEditUiState.exerciseList, key = { it.exerciseId }) { item ->
            DoneExercise(item)
        }
    }
}

@Composable
private fun DoneExercise(
    exerciseWithCompletedSetsDetails: ExerciseWithCompletedSetsDetails,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Text(
            text = exerciseWithCompletedSetsDetails.name,
            style = MaterialTheme.typography.headlineSmall,
        )
        LazyColumn {
            items(items = exerciseWithCompletedSetsDetails.completedSetsWithSetList, key = {it.completedSet.setId}) { item ->
                Row {
                    Text(text = item.completedSet.actualReps.toString())
                    Text(text = item.completedSet.weight.toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BodyPreview() {
    ExerciseTrackerTheme {
        LatestEditScreenBody(LatestEditUiState(
            exerciseList = listOf(ExerciseWithCompletedSetsDetails(
                exerciseId = 0,
                name = "Упражнение",
                completedSetsWithSetList = listOf(CompletedSetWithSet(
                    completedSet = CompletedSet(0, 1, 2, 37f, 4),
                    originalSet = Set(0, 0, 1, 0, 4),
                    exercise = Exercise(0, "Упражнение")
                ))
            ))
        ))
    }
}