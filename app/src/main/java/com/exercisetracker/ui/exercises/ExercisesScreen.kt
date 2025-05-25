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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.ui.navigation.NavigationDestination
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
        Column(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                onSearch = { /* Можно добавить подтверждение поиска */ },
                active = false,
                onActiveChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start=16.dp, end = 16.dp)
            ) {
                // Дополнительный контент при активации поиска
            }

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
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = exercises) { exercise ->
                            ExerciseCard(
                                navigateToExerciseEdit = navigateToExerciseEdit,
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

@Composable
fun ExerciseCard(
    navigateToExerciseEdit: (Long) -> Unit,
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
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
private fun ExerciseAddDialog(
    onAcceptRequest: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var exerciseName by remember { mutableStateOf("Название упражнения") }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card (
            modifier = modifier
                .fillMaxWidth()
                .height(255.dp)
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
                maxLines = 1,
                modifier = modifier.padding(10.dp),
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 10.dp)
            ){
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