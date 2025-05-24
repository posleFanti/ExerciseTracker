package com.exercisetracker.ui.exercises

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.exercisetracker.R
import com.exercisetracker.ui.navigation.NavigationDestination

object ExercisesDestination : NavigationDestination {
    override val route = "exercises"
    override val titleRes = R.string.exercises
}

@Composable
fun ExercisesScreen(
    modifier: Modifier = Modifier
) {

}