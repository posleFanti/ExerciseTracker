package com.exercisetracker.ui.latest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.DoneWorkout
import com.exercisetracker.data.Workout
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

object LatestEditScreen : NavigationDestination {
    override val route = "latest_edit"
    override val titleRes = R.string.latest_edit_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestEditScreen(
    doneWorkout: DoneWorkout,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Тренировка: дата",
                canNavigateBack = true,
                navigateUp = navigateBack
            ) },
        modifier = modifier
    ) { innerPadding ->
        Body(doneWorkout, modifier.padding(innerPadding))
    }
}

@Composable
private fun Body(
    doneWorkout: DoneWorkout,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Выполненные упражнения: ",
            modifier = Modifier.padding(16.dp)
        )
        DoneExercisesList(doneWorkout, modifier)
    }
}

@Composable
private fun DoneExercisesList(
    doneWorkout: DoneWorkout,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        //TODO: Decide how to keep data about done exercises
    }
}

@Preview(showBackground = true)
@Composable
fun LatestEditScreenPreview() {
    ExerciseTrackerTheme {
        LatestEditScreen(DoneWorkout(id = 1, date = "10.01.2025", workout = Workout(1, "Кардио")), {})
    }
}