package com.workout.notes.repositories

import com.workout.notes.models.Exercise
import com.workout.notes.models.Workout
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ExerciseRepository : ReactiveMongoRepository<Exercise, String> {
    fun findByUserId(userid: String): Flux<Exercise>
    fun findByIdAndUserId(id: String, userid: String): Mono<Exercise>
    fun deleteByIdAndUserId(id:String, userid: String): Mono<Void>
}

interface WorkoutRepository: ReactiveMongoRepository<Workout, String> {
    fun findByDateAndUserId(date: String, userid: String): Mono<Workout>
}
