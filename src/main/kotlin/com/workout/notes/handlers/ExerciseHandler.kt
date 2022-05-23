package com.workout.notes.handlers

import com.workout.notes.dto.ExerciseDto
import com.workout.notes.services.ExerciseService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import java.net.URI


class ExerciseHandler(private val exerciseService: ExerciseService) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ExerciseHandler::class.java)
    }

    fun getExercises(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(exerciseService.getWorkouts()).also {
                logger.info("Exercise list fetched.")
            }
    }

    fun getExercise(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(exerciseService.getWorkout(serverRequest.pathVariable("id"))).also {
                logger.info("Exercise ${serverRequest.pathVariable("id")} fetched.")
            }
    }

    fun createExercise(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(ExerciseDto::class.java)
            .flatMap { exerciseDto -> exerciseService.createWorkout(exerciseDto) }
            .flatMap {
                ServerResponse.created(URI.create("/exercise/${it.id}")).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
            }.also {
                logger.info("Exercise saved.")
            }
    }

    fun updateExercise(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(ExerciseDto::class.java)
            .flatMap { exerciseDto -> exerciseService.updateWorkout(serverRequest.pathVariable("id"), exerciseDto) }
            .flatMap {
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
            }.also {
                logger.info("Exercise ${serverRequest.pathVariable("id")} updated.")
            }
    }

    fun deleteExercise(serverRequest: ServerRequest): Mono<ServerResponse> {
        return exerciseService.deleteWorkout(serverRequest.pathVariable("id")).then(ServerResponse.noContent().build()).also {
            logger.info("Exercise ${serverRequest.pathVariable("id")} deleted.")
        }
    }

    fun getExerciseTypes(serverRequest: ServerRequest): Mono<ServerResponse> = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(exerciseService.getExerciseTypes())

    fun getExerciseCategories(serverRequest: ServerRequest): Mono<ServerResponse> = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(exerciseService.getExerciseCategories())

}