@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.workouts

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

object WorkoutsDestination : NavigationDestination {
    override val route = "workouts"
    override val titleRes = R.string.workouts_title
}

@Composable
fun WorkoutScreen(
    toWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val workoutList = listOf(Workout(0, "Cardio"), Workout(2, "Power"))
    Scaffold (
        topBar = { TopAppBar(
            title = stringResource(R.string.workouts_title),
            canNavigateBack = false,
        ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { innerPadding ->
        Body(workoutList, toWorkoutDetails = toWorkoutDetails, modifier = modifier.padding(innerPadding))
    }

    if (showAddDialog)
        WorkoutAddDialog(onDismissRequest = {}, modifier = modifier)
}

@Composable
private fun Body(
    workoutList: List<Workout>,
    toWorkoutDetails: () -> Unit,
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
                modifier
            )
        }
    }
}

@Composable
private fun WorkoutList(
    workoutList: List<Workout>,
    toWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = workoutList, key = { it.id }) { item ->
            WorkoutItem(
                workout = item,
                toWorkoutDetails = toWorkoutDetails,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun WorkoutItem(
    workout: Workout,
    toWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = toWorkoutDetails
    ) {
        Row(
            modifier = modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Тренировка " + workout.id,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = workout.type,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.cardio_icon),
                contentDescription = "cardio_icon"
            )
        }
    }
}

// TODO: Implement state changing
@Composable
private fun WorkoutAddDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card (
            modifier = modifier
                .fillMaxWidth()
                .height(275.dp)
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
                value = "Название тренировки",
                onValueChange = {},
                label = { Text("Название") },
                maxLines = 1,
                modifier = modifier.padding(horizontal = 16.dp),
            )
            OutlinedTextField(
                value = "Тип тренировки",
                onValueChange = {},
                label = { Text("Тип") },
                maxLines = 1,
                modifier = modifier.padding(16.dp),
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 10.dp)
            ){
                TextButton(
                    onClick = { onDismissRequest() },
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
fun WorkoutListPreview() {
    val list = listOf(
        Workout(1, "Cardio"),
        Workout(2, "Power"),
        Workout(3, "Legs")
    )
    ExerciseTrackerTheme {
        WorkoutList(list, {})
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutItemPreview() {
    ExerciseTrackerTheme {
        WorkoutItem(Workout(1, "Cardio"), {})
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutAddDialogPreview() {
    ExerciseTrackerTheme {
        WorkoutAddDialog({})
    }
}