package com.workout.notes.configurations

import com.workout.notes.handlers.ExerciseHandler
import com.workout.notes.handlers.ImportWorkoutHandler
import com.workout.notes.handlers.WorkoutHandler
import com.workout.notes.repositories.ExerciseRepository
import com.workout.notes.repositories.WorkoutRepository
import com.workout.notes.services.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun getExerciseHandler(exerciseService: ExerciseService): ExerciseHandler = ExerciseHandler(exerciseService )

    @Bean
    fun getWorkoutHandler(workoutService: WorkoutService): WorkoutHandler = WorkoutHandler(workoutService)

    @Bean
    fun getImportWorkoutHandler(importWorkoutService: ImportWorkoutService) = ImportWorkoutHandler(importWorkoutService)

    @Bean
    fun getExerciseService(exerciseRepository: ExerciseRepository): ExerciseService = ExerciseServiceImpl(exerciseRepository)

    @Bean
    fun getWorkoutService(workoutRepository: WorkoutRepository): WorkoutService = WorkoutServiceImpl(workoutRepository)

    @Bean
    fun getImportWorkoutService(workoutService: WorkoutService): ImportWorkoutService = ImportWorkoutServiceImpl(workoutService)
}