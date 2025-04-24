@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.latest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.exercisetracker.BottomNavBar
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.DoneWorkout
import com.exercisetracker.data.Workout
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

object LatestDestination : NavigationDestination {
    override val route = "latest"
    override val titleRes = R.string.latest_title
}

@Composable
fun LatestScreen(
    toDoneWorkoutDetails: () -> Unit,
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
        val latestList = listOf(DoneWorkout(0, "10.01.2025", 1), DoneWorkout(1, "18.01.2025", 2))
        Body(latestList, toDoneWorkoutDetails, modifier.padding(innerPadding))
    }
}

@Composable
private fun Body(
    doneList: List<DoneWorkout>,
    toDoneWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        if (doneList.isEmpty()) {
            Text(
                text = "Здесь пока нет тренировок! Добавьте одну, используя кнопку в углу экрана",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        } else {
            DoneList(
                doneList,
                toDoneWorkoutDetails,
                modifier
            )
        }
    }
}

@Composable
private fun DoneList(
    doneList: List<DoneWorkout>,
    toDoneWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = doneList, key = { it.id }) { item ->
            DoneWorkoutItem(
                doneWorkout = item,
                toDoneWorkoutDetails = toDoneWorkoutDetails,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun DoneWorkoutItem(
    doneWorkout: DoneWorkout,
    toDoneWorkoutDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    /* test lines, delete when database implemented */
    val workoutList = listOf(Workout(1, "Кардио", listOf()), Workout(2, "Силовая", listOf()))
    val workoutType = workoutList.get(doneWorkout.workoutId - 1).type

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
                text = doneWorkout.date,
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

@Preview(showBackground = true)
@Composable
fun DoneListPreview() {
    val doneList = listOf<DoneWorkout>(
        DoneWorkout(0, "01.03.2025", 1),
        DoneWorkout(1, "08.03.2025", 2),
        DoneWorkout(2, "14.03.2025", 3)
    )
    ExerciseTrackerTheme {
        DoneList(doneList, {})
    }
}

@Preview(showBackground = true)
@Composable
fun DoneWorkoutItemPreview() {
    ExerciseTrackerTheme {
        DoneWorkoutItem(DoneWorkout(0, "01.03.2025", 1), {})
    }
}

@Preview(showBackground = true)
@Composable
fun DoneWorkoutScreenPreview() {
    ExerciseTrackerTheme {
        LatestScreen({})
    }
}