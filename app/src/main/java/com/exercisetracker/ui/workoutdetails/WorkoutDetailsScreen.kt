package com.exercisetracker.ui.workoutdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.exercisetracker.data.Exercise
import com.exercisetracker.data.Workout
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

@Composable
fun WorkoutDetails(workout: Workout, modifier: Modifier = Modifier) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Edit, "FAB")
            }
        }
    ) { innerPadding ->
        val exerciseList = workout.exerciseList
        Column {
            Text(
                text = "Тренировка " + workout.id,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = modifier.fillMaxWidth().padding(20.dp)
            )
            Body(exerciseList, modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun Body(
    exerciseList: List<Exercise>,
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
        ExerciseList(exerciseList)
    }
}

@Composable
private fun ExerciseList(
    exerciseList: List<Exercise>,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier
    ) {
        items (exerciseList) { exercise ->
            Exercise(exercise, modifier)
        }
    }
}

@Composable
private fun Exercise(exercise: Exercise, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = exercise.name,
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
            Text (
                text = "Кол-во повторов:",
                modifier = modifier.padding(end = 30.dp, top = 10.dp)
            )
        }
        AttemptsList(exercise.attemptsList, modifier)
    }
}

@Composable
private fun AttemptsList(attemptsList: List<Int>, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier.padding(10.dp),
    ) {
        attemptsList.forEachIndexed { index, item ->
            Row {
                Text(
                    text = (index+1).toString(),
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

@Preview(showBackground=true)
@Composable
fun WorkoutDetailsPreview() {
    val exerciseList = listOf<Exercise>(Exercise("Упражнение 1", listOf(15, 12, 10, 10, 10, 10)), Exercise("Упражнение 2", listOf(12, 10, 10, 10)))
    ExerciseTrackerTheme {
        WorkoutDetails(Workout(1, "Кардио", exerciseList))
    }
}

@Preview(showBackground=true)
@Composable
fun NoWorkoutDetailsPreview() {
    val exerciseList = listOf<Exercise>()
    ExerciseTrackerTheme {
        WorkoutDetails(Workout(1, "Кардио", exerciseList))
    }
}