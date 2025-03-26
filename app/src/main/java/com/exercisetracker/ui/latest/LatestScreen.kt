package com.exercisetracker.ui.latest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercisetracker.BottomNavBar
import com.exercisetracker.data.DoneWorkout
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

@Composable
fun LatestScreen(
    modifier: Modifier = Modifier
) {
    Scaffold (
        bottomBar = { BottomNavBar() }
    ) { innerPadding ->
        Body(listOf(), modifier.padding(innerPadding))
    }
}

@Composable
private fun Body(
    doneList: List<DoneWorkout>,
    modifier: Modifier = Modifier
) {
    Column {
        if (doneList.isEmpty()) {
            Text(
                text = "There is no workouts yet! Add one by using floating button",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(10.dp)
            )
        } else {
            DoneList(
                doneList,
                modifier
            )
        }
    }
}

@Composable
private fun DoneList(
    doneList: List<DoneWorkout>,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier
    ) {
        items(items = doneList, key = {it.id}) { item ->
            DoneWorkoutItem(
                doneWorkout = item,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DoneWorkoutItem(
    doneWorkout: DoneWorkout,
    modifier: Modifier = Modifier
) {
   Row(
       modifier = modifier.fillMaxWidth()
   ) {
       Column(
           modifier = modifier.padding(20.dp)
       ) {
           Text(
               text = doneWorkout.date,
               style = MaterialTheme.typography.labelMedium
           )
           Text(
               text = doneWorkout.type,
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
        Body(listOf())
    }
}

@Preview(showBackground = true)
@Composable
fun DoneListPreview() {
    val doneList = listOf<DoneWorkout>(
        DoneWorkout(0, "Cardio", "01.03.2025", 1),
        DoneWorkout(1, "Power", "08.03.2025", 2),
        DoneWorkout(2, "Legs", "14.03.2025", 3)
    )
    ExerciseTrackerTheme {
        DoneList(doneList)
    }
}

@Preview(showBackground = true)
@Composable
fun DoneWorkoutItemPreview() {
    ExerciseTrackerTheme {
        DoneWorkoutItem(DoneWorkout(0, "Cardio", "01.03.2025", 1))
    }
}