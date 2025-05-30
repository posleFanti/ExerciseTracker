@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.Set
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import kotlinx.coroutines.launch

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
    var showAddDialog by remember { mutableStateOf(false) }
    val uiState = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = uiState.workout.name,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { innerPadding ->
        val exerciseList = uiState.exercisesWithSets
        Column {
            DetailsBody(
                navigateToEditWorkout = navigateToEditWorkout,
                doneExerciseDelete = {
                    coroutineScope.launch {
                        viewModel.deleteExercise(it)
                    }
                },
                exerciseList = exerciseList,
                modifier = modifier.padding(innerPadding)
            )
        }
    }

    if (showAddDialog) {
        ExerciseToWorkoutAddDialog(
            onAcceptRequest = {
                coroutineScope.launch {
                    viewModel.addExerciseWithSets(it)
                }
            },
            onDismissRequest = { showAddDialog = false },
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@Composable
private fun DetailsBody(
    navigateToEditWorkout: (Long) -> Unit,
    doneExerciseDelete: (ExerciseWithSets) -> Unit,
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
        ExerciseList(navigateToEditWorkout, doneExerciseDelete, exerciseList)
    }
}

@Composable
private fun ExerciseList(
    navigateToEditWorkout: (Long) -> Unit,
    doneExerciseDelete: (ExerciseWithSets) -> Unit,
    exerciseList: List<ExerciseWithSets>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(exerciseList) { exercise ->
            Exercise(navigateToEditWorkout, doneExerciseDelete, exercise, modifier)
        }
    }
}

@Composable
private fun Exercise(
    navigateToEditWorkout: (Long) -> Unit,
    doneExerciseDelete: (ExerciseWithSets) -> Unit,
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
            IconButton(onClick = { doneExerciseDelete(exerciseWithSets) }) {
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

@Composable
private fun ExerciseToWorkoutAddDialog(
    onAcceptRequest: (Exercise) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: WorkoutDetailsViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery
    val searchResults by viewModel.searchResults

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(500.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Заголовок
                Text(
                    text = "Добавить упражнение",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Поисковая строка
                SearchBar(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    onSearch = {},
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    active = false,
                    onActiveChange = {},
                    modifier = Modifier.fillMaxWidth()
                ) {}

                // Список результатов
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(searchResults) { exercise ->
                        ExerciseSearchItem(
                            exercise = exercise,
                            onExerciseSelected = {
                                onAcceptRequest(exercise)
                                onDismissRequest()
                            }
                        )
                    }

                    if (searchResults.isEmpty()) {
                        item {
                            Text(
                                text = if (searchQuery.isEmpty())
                                    "Введите название упражнения"
                                else
                                    "Ничего не найдено",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseSearchItem(
    exercise: Exercise,
    onExerciseSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onExerciseSelected,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/*@Preview(showBackground = true)
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
}*/