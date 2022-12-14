package com.workout.notes.dto

data class WorkoutDto(
    val date: String,
    val userId: String? = null,
    val workoutRoutines: MutableList<WorkoutRoutineDto> = mutableListOf()
)

data class WorkoutRoutineDto(
    val exerciseName: String,
    val exerciseCategory: ExerciseCategory,
    val exerciseType: ExerciseType? = null,
    val workoutTracks: MutableList<WorkoutTrackDto> = mutableListOf()
)

data class WorkoutTrackDto(
    val weight: Double? = null,
    val weightUnit: String? = null,
    val reps: Int? = null,
    val distance: Double? = null,
    val distanceUnit: String? = null,
    val time: Long? = null
)
