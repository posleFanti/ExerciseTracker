package com.exercisetracker.ui.latest

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import java.time.LocalDate

object LatestEditScreen : NavigationDestination {
    override val route = "latest_edit"
    override val titleRes = R.string.latest_edit_title
    const val completedWorkoutIdArg = "itemId"
    val routeWithArgs = "$route/{$completedWorkoutIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestEditScreen(
    completedWorkout: CompletedWorkout,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Тренировка: дата",
                canNavigateBack = true,
                navigateUp = navigateBack
            ) },
        modifier = modifier
    ) { innerPadding ->
        Body(completedWorkout, modifier.padding(innerPadding))
    }
}

@Composable
private fun Body(
    completedWorkout: CompletedWorkout,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Выполненные упражнения: ",
            modifier = Modifier.padding(16.dp)
        )
        DoneExercisesList(completedWorkout, modifier)
    }
}

@Composable
private fun DoneExercisesList(
    completedWorkout: CompletedWorkout,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        //TODO: Decide how to keep data about done exercises
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun LatestEditScreenPreview() {
    ExerciseTrackerTheme {
        LatestEditScreen(CompletedWorkout(completedWorkoutId = 1, workoutId = 1, date = LocalDate.now()), {})
    }
}