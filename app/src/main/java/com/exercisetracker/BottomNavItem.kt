package com.exercisetracker

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    var title: String,
    var selectedIcon: ImageVector,
    var unselectedIcon: ImageVector,
)
