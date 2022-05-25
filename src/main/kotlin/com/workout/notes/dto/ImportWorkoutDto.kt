package com.workout.notes.dto

data class ImportWorkoutDto(
    val date: String,
    val exercise: String,
    val category: String,
    val weight: Double,
    val reps: Int
)
