package com.exercisetracker.ui.stats

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.exercisetracker.BottomNavBar
import com.exercisetracker.R
import com.exercisetracker.ui.navigation.NavigationDestination

object StatsDestination : NavigationDestination {
    override val route = "stats"
    override val titleRes = R.string.stats_title
}

@Composable
fun StatsScreen(
){
    Scaffold (
    ) { innerPadding ->
        Text(text = "Not implemented yet!", modifier = Modifier.padding(innerPadding))
    }
}