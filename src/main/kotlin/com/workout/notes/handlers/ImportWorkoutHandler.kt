package com.workout.notes.handlers

import com.workout.notes.services.ImportWorkoutService
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class ImportWorkoutHandler(private val importWorkoutService: ImportWorkoutService) {

    fun importWorkout(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.body(BodyExtractors.toMultipartData()).flatMap { parts ->
            val map: Map<String, Part> = parts.toSingleValueMap()
            val filePart: FilePart = map["file"]!! as FilePart
            println("Import file information: ${filePart.filename()}, size: ${serverRequest.headers().firstHeader("Content-Length")}")
            importWorkoutService.importWorkFromFile(filePart,
                serverRequest.headers().firstHeader("userId") ?: "").collectList().flatMap {
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it.size))
            }
        }
    }
}
