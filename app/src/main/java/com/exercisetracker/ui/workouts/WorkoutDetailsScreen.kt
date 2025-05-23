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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.ExerciseWithSets
import com.exercisetracker.data.entities.Workout
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
    val uiState = viewModel.workoutDetailsUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Тренировка " + uiState.value.workoutId,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*Add exercise*/ }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { innerPadding ->
        val exerciseList = uiState.value.exerciseList
        Column {
            Body({navigateToEditWorkout},exerciseList, modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun Body(
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
        ExerciseList({navigateToEditWorkout},exerciseList)
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
            Exercise({navigateToEditWorkout}, exercise, modifier)
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
        }
        HorizontalDivider(modifier = modifier.padding(horizontal = 10.dp))
        Row {
            Text(
                text = "Подходы: ",
                modifier = modifier.padding(start = 30.dp, top = 10.dp)
            )
            Spacer(modifier.weight(1f))
            Text(
                text = "Кол-во повторов: ",
                modifier = modifier.padding(end = 30.dp, top = 10.dp)
            )
        }
        AttemptsList(listOf(), modifier)
    }
}

@Composable
private fun AttemptsList(attemptsList: List<Int>, modifier: Modifier = Modifier) {
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
                    text = item.toString(),
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
        Body(
            navigateToEditWorkout = {},
            exerciseList = listOf(
                ExerciseWithSets(
                    exercise = Exercise(0, "Cardio"),
                    sets = listOf(Set(0, 0, 0, 0, 4), Set(1, 0, 0, 1, 5))
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoWorkoutDetailsBodyPreview() {
    ExerciseTrackerTheme {
        Body(navigateToEditWorkout = {}, exerciseList = listOf())
    }
}