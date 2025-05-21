package com.exercisetracker.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.exercisetracker.data.entities.CompletedWorkout
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.entities.Workout
import com.exercisetracker.ui.latest.LatestDestination
import com.exercisetracker.ui.latest.LatestEditScreen
import com.exercisetracker.ui.latest.LatestScreen
import com.exercisetracker.ui.stats.StatsDestination
import com.exercisetracker.ui.stats.StatsScreen
import com.exercisetracker.ui.workouts.ExerciseEditDestination
import com.exercisetracker.ui.workouts.ExerciseEditScreen
import com.exercisetracker.ui.workouts.WorkoutDetailsDestination
import com.exercisetracker.ui.workouts.WorkoutDetailsScreen
import com.exercisetracker.ui.workouts.WorkoutScreen
import com.exercisetracker.ui.workouts.WorkoutsDestination
import java.time.LocalDate

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
            LatestScreen(
                toCompletedWorkoutDetails = { navController.navigate(LatestEditScreen.route) }
            )
        }
        composable(route = WorkoutsDestination.route) {
            WorkoutScreen(
                toWorkoutDetails = { navController.navigate(WorkoutDetailsDestination.route) }
            )
        }
        composable(route = StatsDestination.route) {
            StatsScreen()
        }
        composable(route = ExerciseEditDestination.route) {
            ExerciseEditScreen(Exercise(1, "Упражнение"), modifier) { navController.popBackStack() }
        }
        composable(
            route = WorkoutDetailsDestination.route,
            arguments = listOf(navArgument(WorkoutDetailsDestination.workoutIdArg) {
                type = NavType.LongType
            })
        ) {
            WorkoutDetailsScreen({ navController.popBackStack() })
        }
        composable(
            route = LatestEditScreen.route,
            arguments = listOf(navArgument(LatestEditScreen.completedWorkoutIdArg) {
                type = NavType.LongType
            })
        ) {
            LatestEditScreen(
                navigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}