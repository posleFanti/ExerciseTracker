package com.exercisetracker.data

data class DoneWorkout(
    val id: Int = 0,
    val type: String,
    val date: String,
    val workoutId: Int,
)
