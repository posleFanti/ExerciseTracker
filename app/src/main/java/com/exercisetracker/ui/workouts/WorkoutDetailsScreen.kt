@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.Set
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

object WorkoutDetailsDestination : NavigationDestination {
    override val route = "workout_details"
    override val titleRes = R.string.workout_details
    const val workoutIdArg = "workoutId"
    val routeWithArgs = "$route/{$workoutIdArg}"
}

// TODO:    Add navigation to edit workout

@Composable
fun WorkoutDetailsScreen(
    navigateToEditWorkout: (Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailsViewModel = viewModel(factory = WorkoutDetailsViewModel.Factory)
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Тренировка " + uiState.workout.name,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addExerciseWithSets() }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { innerPadding ->
        val exerciseList = uiState.exercisesWithSets
        Column {
            DetailsBody(
                navigateToEditWorkout = navigateToEditWorkout,
                exerciseList = exerciseList,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun DetailsBody(
    navigateToEditWorkout: (Long) -> Unit,
    exerciseList: List<ExerciseWithSets>,
    modifier: Modifier = Modifier
) {
    if (exerciseList.isEmpty()) {
        Text(
            text = "В этой тренировке пока нет упражнений",
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
    } else {
        Text(
            text = "Упражнения: ",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
        ExerciseList(navigateToEditWorkout,exerciseList)
    }
}

@Composable
private fun ExerciseList(
    navigateToEditWorkout: (Long) -> Unit,
    exerciseList: List<ExerciseWithSets>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(exerciseList) { exercise ->
            Exercise(navigateToEditWorkout, exercise, modifier)
        }
    }
}

@Composable
private fun Exercise(
    navigateToEditWorkout: (Long) -> Unit,
    exerciseWithSets: ExerciseWithSets,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(10.dp),
        onClick = { navigateToEditWorkout(exerciseWithSets.exercise.exerciseId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = exerciseWithSets.exercise.name,
                modifier = modifier.padding(10.dp)
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {  }) {
                Icon(Icons.Default.Close, "Delete")
            }
        }
        HorizontalDivider(modifier = modifier.padding(horizontal = 10.dp))
        Row {
            Text(
                text = "Подходы: ",
                modifier = modifier.padding(start = 20.dp, top = 10.dp)
            )
            Spacer(modifier.weight(1f))
            Text(
                text = "Кол-во повторов: ",
                modifier = modifier.padding(end = 20.dp, top = 10.dp)
            )
            Spacer(modifier.weight(1f))
            Text(
                text = "Вес: ",
                modifier = modifier.padding(end = 20.dp, top = 10.dp)
            )
        }
        AttemptsList(exerciseWithSets.sets, modifier)
    }
}

@Composable
private fun AttemptsList(
    attemptsList: List<Set>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(10.dp),
    ) {
        attemptsList.forEachIndexed { index, item ->
            Row {
                Text(
                    text = (index + 1).toString(),
                    modifier = modifier.padding(horizontal = 40.dp)
                )
                Spacer(modifier.weight(1f))
                Text(
                    text = item.reps.toString(),
                    modifier = modifier.padding(horizontal = 40.dp)
                )
                Spacer(modifier.weight(1f))
                Text(
                    text = item.weight.toString(),
                    modifier = modifier.padding(horizontal = 40.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailsBodyPreview() {
    ExerciseTrackerTheme {
        DetailsBody(
            navigateToEditWorkout = { _ -> },
            exerciseList = listOf(
                ExerciseWithSets(
                    exercise = Exercise(0, "Название упражнения"),
                    sets = listOf(Set(0, 0, 0, 1, 37.4, 10), Set(1, 0, 0, 2, 27.1, 12))
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseListPreview() {
    ExerciseTrackerTheme {
        ExerciseList(
            navigateToEditWorkout = { _ ->  },
            exerciseList = listOf(ExerciseWithSets(
                exercise = Exercise(0, "Название упражнения 1"),
                sets = listOf(Set(0, 0, 0, 1, 37.4, 10), Set(1, 0, 0, 2, 27.1, 12))
            ),
                ExerciseWithSets(
                    exercise = Exercise(1, "Название упражнения 2"),
                    sets = listOf(Set(0, 0, 0, 1, 40.8, 15), Set(1, 0, 0, 2, 40.8, 15))
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoWorkoutDetailsBodyPreview() {
    ExerciseTrackerTheme {
        DetailsBody(navigateToEditWorkout = {_ -> }, exerciseList = listOf())
    }
}