@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme

object ExerciseEditDestination : NavigationDestination {
    override val route = "exercise_edit"
    override val titleRes = R.string.exercise_edit_title
}

@Composable
fun ExerciseEditScreen(
    exercise: Exercise,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(
            title = exercise.name,
            canNavigateBack = true,
            navigateUp = navigateBack
        ) }
    ) { innerPadding ->
        Column {
            Text(
                text = "Подходы: ",
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            )
            TextFieldsList(modifier, exercise.attemptsList)
        }
    }
}

@Composable
private fun TextFieldsList(
    modifier: Modifier = Modifier,
    attemptsList: List<Int>
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        attemptsList.forEachIndexed { index, item ->
            Row (
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = index.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = modifier.align(Alignment.CenterVertically)
                )
                OutlinedTextField(
                    value = "Кол-во повторов",
                    onValueChange = { /*...*/ },
                    modifier = modifier.padding(10.dp)
                )
                FilledIconButton(
                    onClick = {},
                    modifier = modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.Clear, "Clear")
                }
            }
        }
        Row(
            modifier = modifier.align(Alignment.End)
        ) {
            Button (onClick = {}, modifier = modifier.padding(10.dp)) {
                Text("Добавить подход")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseEditPreview() {
    ExerciseTrackerTheme {
        ExerciseEditScreen(
            exercise = Exercise("Упражнение 1", listOf(15, 12, 10, 10, 10, 10)),
            navigateBack = {}
        )
    }
}