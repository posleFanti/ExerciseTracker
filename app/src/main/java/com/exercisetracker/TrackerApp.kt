@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker

import android.media.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exercisetracker.ui.exercises.ExercisesDestination
import com.exercisetracker.ui.navigation.TrackerNavHost
import com.exercisetracker.ui.stats.StatsDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import com.exercisetracker.ui.workouts.WorkoutsDestination

@Composable
fun TrackerApp(navController: NavHostController = rememberNavController()) {
    Scaffold (
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute in listOf(WorkoutsDestination.route, ExercisesDestination.route,
                    StatsDestination.route)) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        TrackerNavHost(navController, Modifier.padding(innerPadding))
    }
}

@Composable
fun TopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem(
            title = "Тренировки",
            route = WorkoutsDestination.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.workout_day),
            unselectedIcon = ImageVector.vectorResource(R.drawable.workout_day)
        ),
        BottomNavItem(
            title = "Упражнения",
            route = ExercisesDestination.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.workout_list),
            unselectedIcon = ImageVector.vectorResource(R.drawable.workout_list)
        ),
        BottomNavItem(
            title = "Статистика",
            route = StatsDestination.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.statistics_graphic_selected),
            unselectedIcon = ImageVector.vectorResource(R.drawable.statistics_graph_unselected)
        )
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar {
        items.forEachIndexed { index, item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route) },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        imageVector = if (selected) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    ExerciseTrackerTheme {
        BottomNavBar(rememberNavController())
    }
}