package com.workout.notes.repositories

import com.workout.notes.models.Exercise
import com.workout.notes.models.Workout
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ExerciseRepository : ReactiveMongoRepository<Exercise, String>

interface WorkoutRepository: ReactiveMongoRepository<Workout, String>
