package com.workout.notes.dto

import com.fasterxml.jackson.annotation.JsonProperty

enum class ExerciseType {
    @JsonProperty("Weight and Reps") WEIGHT_AND_REPS,
    @JsonProperty("Weight and Distance") WEIGHT_AND_DISTANCE,
    @JsonProperty("Weight and Time") WEIGHT_AND_TIME,
    @JsonProperty("Reps and Distance") REPS_AND_DISTANCE,
    @JsonProperty("Distance and Time") DISTANCE_AND_TIME,
    @JsonProperty("Weight") WEIGHT,
    @JsonProperty("Reps") REPS,
    @JsonProperty("Distance") DISTANCE,
    @JsonProperty("Time") TIME;
}

enum class ExerciseCategory {
    @JsonProperty("Abs") ABS,
    @JsonProperty("Back") BACK,
    @JsonProperty("Biceps") BICEPS,
    @JsonProperty("Cardio") CARDIO,
    @JsonProperty("Chest") CHEST,
    @JsonProperty("Glutes") GLUTES,
    @JsonProperty("Legs") LEGS,
    @JsonProperty("Shoulders") SHOULDERS,
    @JsonProperty("Triceps") TRICEPS;
}