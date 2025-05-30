package com.exercisetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.ui.stats.StatsDestination
import com.exercisetracker.ui.stats.StatsScreen
import com.exercisetracker.ui.exercises.ExerciseEditDestination
import com.exercisetracker.ui.exercises.ExerciseEditScreen
import com.exercisetracker.ui.exercises.ExercisesDestination
import com.exercisetracker.ui.exercises.ExercisesScreen
import com.exercisetracker.ui.workouts.DoneExerciseEditDestination
import com.exercisetracker.ui.workouts.DoneExerciseEditScreen
import com.exercisetracker.ui.workouts.WorkoutDetailsDestination
import com.exercisetracker.ui.workouts.WorkoutDetailsScreen
import com.exercisetracker.ui.workouts.WorkoutScreen
import com.exercisetracker.ui.workouts.WorkoutsDestination

@Composable
fun TrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = WorkoutsDestination.route,
        modifier = modifier
    ) {
        composable(route = WorkoutsDestination.route) {
            WorkoutScreen(
                toWorkoutDetails = { navController.navigate("${WorkoutDetailsDestination.route}/$it") },
            )
        }
        composable(route = StatsDestination.route) {
            StatsScreen()
        }
        composable(route = ExercisesDestination.route) {
            ExercisesScreen(
                navigateToExerciseEdit = { navController.navigate("${ExerciseEditDestination.route}/$it") }
            )
        }
        composable(
            route = ExerciseEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ExerciseEditDestination.exerciseIdArg) {
                type = NavType.LongType
            })
        ) {
            ExerciseEditScreen(
                navigateBack = { navController.popBackStack() },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = WorkoutDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(WorkoutDetailsDestination.workoutIdArg) {
                type = NavType.LongType
            })
        ) {
            val workoutId = checkNotNull(it.arguments?.getLong("workoutId"))
            WorkoutDetailsScreen(
                navigateToEditWorkout = { exerciseId -> navController.navigate("${DoneExerciseEditDestination.route}/$workoutId/$exerciseId") },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = DoneExerciseEditDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DoneExerciseEditDestination.workoutIdArg) { type = NavType.LongType },
                navArgument(DoneExerciseEditDestination.exerciseIdArg) { type = NavType.LongType }
            )
        ) {
            DoneExerciseEditScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}