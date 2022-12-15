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
            .body(workoutService.getWorkout(serverRequest.pathVariable("date"),
                serverRequest.headers().firstHeader("userId") ?: ""))
    }

    fun createAndUpdateWorkout(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(WorkoutDto::class.java)
            .flatMap { workoutService.createAndUpdateWorkout(it, serverRequest.headers().firstHeader("userId") ?: "") }
            .flatMap {
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
            }
    }

    fun deleteWorkout(serverRequest: ServerRequest): Mono<ServerResponse> {
        return workoutService.deleteWorkout(
            serverRequest.pathVariable("date"),
            serverRequest.headers().firstHeader("userId") ?: ""
        ).then(ServerResponse.noContent().build())
    }

}