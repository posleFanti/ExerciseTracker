@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.latest

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import java.time.LocalDate

object LatestDestination : NavigationDestination {
    override val route = "latest"
    override val titleRes = R.string.latest_title
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LatestScreen(
    toCompletedWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Прошедшие тренировки",
                canNavigateBack = false,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, "Add")
            }
        },
    ) { innerPadding ->
        val latestList = listOf(CompletedWorkout(completedWorkoutId = 1, workoutId = 1, date = LocalDate.now()), CompletedWorkout(completedWorkoutId = 2, workoutId = 2, date = LocalDate.of(2025, 10, 18)))
        Body(latestList, toCompletedWorkoutDetails, modifier.padding(innerPadding))
    }
}

@Composable
private fun Body(
    completedList: List<CompletedWorkout>,
    toCompletedWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        if (completedList.isEmpty()) {
            Text(
                text = "Здесь пока нет тренировок! Добавьте одну, используя кнопку в углу экрана",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        } else {
            CompletedWorkoutsList(
                completedList,
                toCompletedWorkoutDetails,
                modifier
            )
        }
    }
}

@Composable
private fun CompletedWorkoutsList(
    completedList: List<CompletedWorkout>,
    toCompletedWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = completedList, key = { it.completedWorkoutId }) { item ->
            CompletedWorkoutItem(
                completedWorkout = item,
                toDoneWorkoutDetails = toCompletedWorkoutDetails,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun CompletedWorkoutItem(
    completedWorkout: CompletedWorkout,
    toDoneWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    /* test lines, delete when database and viewmodel implemented */
    val workoutList = listOf(Workout(1, "Кардио"), Workout(2, "Силовая"))
    val workoutType = workoutList[completedWorkout.workoutId.toInt() - 1].type

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = toDoneWorkoutDetails
            ),
    ) {
        Column(
            modifier = modifier.padding(20.dp)
        ) {
            Text(
                text = completedWorkout.date.toString(),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = workoutType,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    HorizontalDivider(
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyListTextPreview() {
    ExerciseTrackerTheme {
        Body(listOf(), {})
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CompletedWorkoutListPreview() {
    val completedList = listOf<CompletedWorkout>(
        CompletedWorkout(completedWorkoutId = 1, workoutId = 1, date = LocalDate.of(2025, 5, 2)),
        CompletedWorkout(completedWorkoutId = 2, workoutId = 2, date = LocalDate.of(2025, 5, 5)),
        CompletedWorkout(completedWorkoutId = 3, workoutId = 3, date = LocalDate.of(2025, 5, 12))
    )
    ExerciseTrackerTheme {
        CompletedWorkoutsList(completedList, {})
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DoneWorkoutItemPreview() {
    ExerciseTrackerTheme {
        CompletedWorkoutItem(CompletedWorkout(completedWorkoutId = 1, workoutId = 1, date = LocalDate.of(2025, 5, 1)), {})
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DoneWorkoutScreenPreview() {
    ExerciseTrackerTheme {
        LatestScreen({})
    }
}