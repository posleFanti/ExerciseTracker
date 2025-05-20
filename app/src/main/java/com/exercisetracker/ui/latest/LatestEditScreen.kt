package com.exercisetracker.ui.latest

import androidx.compose.foundation.layout.Column
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
import com.exercisetracker.data.entities.CompletedSetWithSet

object LatestEditScreen : NavigationDestination {
    override val route = "latest_edit"
    override val titleRes = R.string.latest_edit_title
    const val completedWorkoutIdArg = "itemId"
    val routeWithArgs = "$route/{$completedWorkoutIdArg}"
}

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
        items(items = latestEditUiState.completedWorkoutDetails.completedSetsWithSets, key = {it.originalSet}) { item ->
            DoneExercise()
        }
    }
}

@Composable
private fun DoneExercise(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun BodyPreview() {
    ExerciseTrackerTheme {
        LatestEditScreenBody(LatestEditUiState(
            CompletedWorkoutWithSetsDetails(
                completedWorkoutId = 0,
                workoutId = 0,
                date = LocalDate.of(2025, 1, 1),
                completedSetsWithSets = listOf()
            )
        ))
    }
}