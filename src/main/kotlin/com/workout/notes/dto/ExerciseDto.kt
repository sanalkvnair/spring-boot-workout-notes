package com.workout.notes.dto

data class ExerciseDto(
    val id: String? = null,
    val exerciseName: String,
    val exerciseCategory: ExerciseCategory,
    val exerciseType: ExerciseType,
    val exerciseNotes: String? = null
)