package com.exercisetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.exercisetracker.data.Exercise
import com.exercisetracker.data.Workout
import com.exercisetracker.ui.latest.LatestDestination
import com.exercisetracker.ui.latest.LatestScreen
import com.exercisetracker.ui.stats.StatsDestination
import com.exercisetracker.ui.stats.StatsScreen
import com.exercisetracker.ui.workouts.ExerciseEditDestination
import com.exercisetracker.ui.workouts.ExerciseEditScreen
import com.exercisetracker.ui.workouts.WorkoutDetailsDestination
import com.exercisetracker.ui.workouts.WorkoutDetailsScreen
import com.exercisetracker.ui.workouts.WorkoutScreen
import com.exercisetracker.ui.workouts.WorkoutsDestination

@Composable
fun TrackerNavHost (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LatestDestination.route,
        modifier = modifier
    ) {
        composable(route = LatestDestination.route) {
            LatestScreen(navController)
        }
        composable(route = WorkoutsDestination.route) {
            WorkoutScreen(navController)
        }
        composable(route = StatsDestination.route) {
            StatsScreen(navController)
        }
        composable(route = ExerciseEditDestination.route) {
            ExerciseEditScreen(Exercise("Упражнение", listOf()), modifier) { navController.popBackStack() }
        }
        composable(route = WorkoutDetailsDestination.route) {
            WorkoutDetailsScreen(Workout(1, "Кардио", listOf()), { navController.popBackStack() })
        }
    }
}