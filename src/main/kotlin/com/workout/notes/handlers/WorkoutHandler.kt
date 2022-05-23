package com.workout.notes.handlers

import com.workout.notes.dto.WorkoutDto
import com.workout.notes.services.WorkoutService
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

class WorkoutHandler(private val workoutService: WorkoutService) {

    fun getWorkout(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(workoutService.getWorkout(serverRequest.pathVariable("date")))
    }

    fun createAndUpdateWorkout(sererRequest: ServerRequest): Mono<ServerResponse> {
        return sererRequest.bodyToMono(WorkoutDto::class.java)
            .flatMap { workoutService.createAndUpdateWorkout(it) }
            .flatMap {
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
            }
    }

}