package com.exercisetracker.data

data class Workout(
    val id: Int = 0,
    val type: String,
    val exerciseList: List<Exercise> = listOf(),
)
