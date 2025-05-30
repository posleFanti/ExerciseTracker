package com.exercisetracker.ui.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import kotlinx.coroutines.launch

object ExercisesDestination : NavigationDestination {
    override val route = "exercises"
    override val titleRes = R.string.exercises
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    navigateToExerciseEdit: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseSearchViewModel = viewModel(factory = ExerciseSearchViewModel.Factory)
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery
    val exercises by viewModel.searchResults
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.exercises_screen_name),
                canNavigateBack = false,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { innerPadding ->
        ExerciseScreenBody(
            searchQuery = searchQuery,
            exercises = exercises,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            navigateToExerciseEdit = navigateToExerciseEdit,
            onDeleteExercise = {
                coroutineScope.launch {
                    viewModel.deleteExercise(it)
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }

    if (showAddDialog) {
        ExerciseAddDialog(
            onAcceptRequest = { name ->
                coroutineScope.launch {
                    viewModel.addExercise(Exercise(name = name))
                    showAddDialog = false
                }
            },
            onDismissRequest = { showAddDialog = false },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreenBody(
    searchQuery: String,
    exercises: List<Exercise>,
    onSearchQueryChanged: (String) -> Unit,
    navigateToExerciseEdit: (Long) -> Unit,
    onDeleteExercise: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            onSearch = {},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            active = false,
            onActiveChange = {},
            placeholder = { Text("Поиск упражнений...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {}

        when {
            exercises.isEmpty() -> {
                Text(
                    text = "Ничего не найдено",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {
                    items(items = exercises) { exercise ->
                        ExerciseCard(
                            navigateToExerciseEdit = navigateToExerciseEdit,
                            onDeleteExercise = onDeleteExercise,
                            exercise = exercise,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    navigateToExerciseEdit: (Long) -> Unit,
    onDeleteExercise: (Exercise) -> Unit,
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { navigateToExerciseEdit(exercise.exerciseId) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier.weight(1f))
                IconButton(onClick = { onDeleteExercise(exercise) }) {
                    Icon(Icons.Default.Close, "Delete")
                }
            }
        }
    }
}

@Composable
private fun ExerciseAddDialog(
    onAcceptRequest: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var exerciseName by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(195.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(
                text = "Добавить упражнение",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
            OutlinedTextField(
                value = exerciseName,
                onValueChange = { exerciseName = it },
                label = { Text("Название") },
                singleLine = true,
                modifier = modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 10.dp)
            ) {
                TextButton(
                    onClick = { onAcceptRequest(exerciseName) },
                ) { Text("Добавить") }
                Spacer(modifier.weight(1f))
                TextButton(
                    onClick = { onDismissRequest() },
                ) { Text("Отмена") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenBodyPreview() {
    ExerciseTrackerTheme {
        ExerciseScreenBody(
            searchQuery = "",
            exercises = listOf(),
            onSearchQueryChanged = {},
            navigateToExerciseEdit = {},
            onDeleteExercise = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseAddDialogPreview() {
    ExerciseTrackerTheme {
        ExerciseAddDialog(
            onAcceptRequest = {},
            onDismissRequest = {}
        )
    }
}