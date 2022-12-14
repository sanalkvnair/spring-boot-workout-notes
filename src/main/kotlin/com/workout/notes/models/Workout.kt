package com.workout.notes.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(value = "workouts")
data class Workout(
    @Id val date: String,
    val userId: String,
    val workoutRoutines: MutableList<WorkoutRoutine> = mutableListOf()
)

data class WorkoutRoutine(
    val exerciseName: String,
    val exerciseCategory: String,
    val exerciseType: String? = null,
    val workoutTracks: MutableList<WorkoutTrack> = mutableListOf()
)

data class WorkoutTrack(
    val weight: Double? = null,
    val weightUnit: String? = null,
    val reps: Int? = null,
    val distance: Double? = null,
    val distanceUnit: String? = null,
    val time: Long? = null
)
