package com.exercisetracker

import androidx.compose.ui.graphics.vector.ImageVector
import com.exercisetracker.ui.navigation.NavigationDestination

data class BottomNavItem(
    var title: String,
    var route: String,
    var selectedIcon: ImageVector,
    var unselectedIcon: ImageVector,
)
