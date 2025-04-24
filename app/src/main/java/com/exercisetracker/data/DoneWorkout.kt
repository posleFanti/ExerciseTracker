package com.exercisetracker.data

data class DoneWorkout(
    val id: Int = 0,
    val date: String,
    val workoutId: Int,
    val weightsList: List<Int> = listOf()
)
