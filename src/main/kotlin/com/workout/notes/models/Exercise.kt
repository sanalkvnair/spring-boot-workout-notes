package com.workout.notes.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(value="exercises")
data class Exercise(
    @Id val id: String,
    val exerciseName: String,
    val exerciseCategory: String,
    val exerciseType: String,
    val exerciseNotes: String? = null
)