package com.exercisetracker.ui.workouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercisetracker.BottomNavBar
import com.exercisetracker.R
import com.exercisetracker.data.Workout
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

@Composable
fun WorkoutScreen(
    modifier: Modifier = Modifier
) {
    val workoutList = listOf(Workout(0, "Cardio"), Workout(2, "Power"))
    Scaffold (
        bottomBar = { BottomNavBar() }
    ) { innerPadding ->
        Body(workoutList, modifier = modifier.padding(innerPadding))
    }
}

@Composable
private fun Body(
    workoutList: List<Workout>,
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
                modifier
            )
        }
    }
}

@Composable
private fun WorkoutList(
    workoutList: List<Workout>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = workoutList, key = { it.id }) { item ->
            WorkoutItem(
                workout = item,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun WorkoutItem(
    workout: Workout,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
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

@Preview(showBackground = true)
@Composable
fun WorkoutListPreview() {
    val list = listOf(
        Workout(1, "Cardio"),
        Workout(2, "Power"),
        Workout(3, "Legs")
    )
    ExerciseTrackerTheme {
        WorkoutList(list)
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutItemPreview() {
    ExerciseTrackerTheme {
        WorkoutItem(Workout(1, "Cardio"))
    }
}