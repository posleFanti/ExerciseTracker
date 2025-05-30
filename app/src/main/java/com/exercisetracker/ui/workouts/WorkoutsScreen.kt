@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.workouts

import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object WorkoutsDestination : NavigationDestination {
    override val route = "workouts"
    override val titleRes = R.string.workouts_title
}

@Composable
fun WorkoutScreen(
    toWorkoutDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutsViewModel = viewModel(factory = WorkoutsViewModel.Factory)
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var workoutToEdit by remember { mutableStateOf<Workout?>(null) }
    val workoutsUiState by viewModel.workoutsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.workouts_title),
                canNavigateBack = false,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { innerPadding ->
        Body(
            workoutsUiState.workoutList,
            toWorkoutDetails = toWorkoutDetails,
            onWorkoutLongClick = { workout ->
                workoutToEdit = workout
                showEditDialog = true
            },
            modifier = modifier.padding(innerPadding)
        )
    }

    if (showAddDialog) {
        WorkoutAddDialog(
            onAcceptRequest = { name, type, date ->
                coroutineScope.launch {
                    viewModel.addWorkout(Workout(name = name, type = type, date = date))
                    showAddDialog = false
                }
            },
            onDismissRequest = { showAddDialog = false },
            modifier = modifier
        )
    }

    if (showEditDialog && workoutToEdit != null) {
        WorkoutEditDialog(
            onAcceptRequest = { name, type, date ->
                coroutineScope.launch {
                    viewModel.updateWorkout(
                        Workout(
                            workoutId = workoutToEdit!!.workoutId,
                            name = name,
                            type = type,
                            date = date
                        )
                    )
                    showEditDialog = false
                }
            },
            onDismissRequest = { showEditDialog = false },
            onDeleteWorkout = {
                coroutineScope.launch {
                    viewModel.deleteWorkout(it)
                    showEditDialog = false
                    workoutToEdit = null
                }
            },
            workout = workoutToEdit!!,
            modifier = modifier
        )
    }
}

@Composable
private fun Body(
    workoutList: List<Workout>,
    toWorkoutDetails: (Long) -> Unit,
    onWorkoutLongClick: (Workout) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        if (workoutList.isEmpty()) {
            Text(
                text = "There is no workouts yet! Add one by using floating button",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            WorkoutList(
                workoutList,
                toWorkoutDetails,
                onWorkoutLongClick,
                modifier
            )
        }
    }
}

@Composable
private fun WorkoutList(
    workoutList: List<Workout>,
    toWorkoutDetails: (Long) -> Unit,
    onWorkoutLongClick: (Workout) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = workoutList, key = { it.workoutId }) { item ->
            WorkoutItem(
                workout = item,
                toWorkoutDetails = toWorkoutDetails,
                onWorkoutLongClick = onWorkoutLongClick,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WorkoutItem(
    workout: Workout,
    toWorkoutDetails: (Long) -> Unit,
    onWorkoutLongClick: (Workout) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { toWorkoutDetails(workout.workoutId) },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onWorkoutLongClick(workout)
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
        Row(
            modifier = modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = workout.type,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = workout.date,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            /*Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.cardio_icon),
                contentDescription = "cardio_icon"
            )*/
        }
    }
}

@Composable
private fun WorkoutAddDialog(
    onAcceptRequest: (String, String, String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var workoutType by remember { mutableStateOf("") }
    var workoutName by remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(285.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(
                text = "Добавить тренировку",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
            OutlinedTextField(
                value = workoutName,
                onValueChange = { workoutName = it },
                label = { Text("Название") },
                maxLines = 1,
                modifier = modifier.padding(10.dp),
            )
            OutlinedTextField(
                value = workoutType,
                onValueChange = { workoutType = it },
                label = { Text("Тип") },
                maxLines = 1,
                modifier = modifier.padding(10.dp),
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 10.dp)
            ) {
                TextButton(
                    onClick = {
                        if (workoutName.isNotBlank() && workoutType.isNotBlank())
                            onAcceptRequest(
                                workoutName,
                                workoutType,
                                LocalDate.now().format(formatter)
                            )
                    },
                ) { Text("Добавить") }
                Spacer(modifier.weight(1f))
                TextButton(
                    onClick = { onDismissRequest() },
                ) { Text("Отмена") }
            }
        }
    }
}

@Composable
private fun WorkoutEditDialog(
    onAcceptRequest: (String, String, String) -> Unit,
    onDismissRequest: () -> Unit,
    onDeleteWorkout: (Workout) -> Unit,
    workout: Workout,
    modifier: Modifier = Modifier,
) {
    var workoutType by remember { mutableStateOf(workout.type) }
    var workoutName by remember { mutableStateOf(workout.name) }
    var workoutDate by remember { mutableStateOf(workout.date) }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(365.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 7.dp)
            ) {
                Text(
                    text = "Изменить тренировку",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .padding(start = 14.dp)
                )
                Spacer(modifier.weight(1f))
                IconButton(
                    onClick = { onDeleteWorkout(workout) },
                    modifier = Modifier.padding(end = 5.dp)
                ) {
                    Icon(Icons.Default.Delete, "Delete")
                }
            }
            OutlinedTextField(
                value = workoutName,
                onValueChange = { workoutName = it },
                label = { Text("Название") },
                maxLines = 1,
                modifier = modifier.padding(10.dp),
            )
            OutlinedTextField(
                value = workoutType,
                onValueChange = { workoutType = it },
                label = { Text("Тип") },
                maxLines = 1,
                modifier = modifier.padding(10.dp),
            )
            OutlinedTextField(
                value = workoutDate,
                onValueChange = { workoutDate = it },
                label = { Text("Дата") },
                maxLines = 1,
                modifier = modifier.padding(10.dp),
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 10.dp)
            ) {
                TextButton(
                    onClick = {
                        onAcceptRequest(workoutName, workoutType, workoutDate)
                    },
                ) { Text("Сохранить") }
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
fun WorkoutListPreview() {
    val list = listOf(
        Workout(workoutId = 0, name = "Название", type = "Силовая", date = "2025-05-05"),
        Workout(workoutId = 1, name = "Название", type = "Кардио", date = "2025-05-10"),
        Workout(workoutId = 2, name = "Название", type = "Силовая", date = "2025-05-15"),
    )
    ExerciseTrackerTheme {
        WorkoutList(list, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutItemPreview() {
    ExerciseTrackerTheme {
        WorkoutItem(Workout(name = "Название", type = "Силовая", date = "2025-05-05"), {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutAddDialogPreview() {
    ExerciseTrackerTheme {
        WorkoutAddDialog(onAcceptRequest = { _, _, _ -> }, onDismissRequest = {})
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutEditDialogPreview() {
    ExerciseTrackerTheme {
        WorkoutEditDialog(
            onAcceptRequest = { _, _, _ -> },
            onDismissRequest = {},
            { _ -> },
            workout = Workout(0, "Название", "Кардио", "2025-05-10")
        )
    }
}