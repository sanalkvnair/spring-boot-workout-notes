package com.workout.notes.routers

import com.workout.notes.handlers.ExerciseHandler
import com.workout.notes.handlers.ImportWorkoutHandler
import com.workout.notes.handlers.WorkoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration(proxyBeanMethods = false)
class ApplicationRouters {

    @Bean
    fun routes(exerciseHandler: ExerciseHandler, workoutHandler: WorkoutHandler, importWorkoutHandler: ImportWorkoutHandler): RouterFunction<ServerResponse> {
        return route(GET("/exercise"), exerciseHandler::getExercises)
            .andRoute(GET("/exercise/{id}"), exerciseHandler::getExercise)
            .andRoute(POST("/exercise"), exerciseHandler::createExercise)
            .andRoute(PUT("/exercise/{id}"), exerciseHandler::updateExercise)
            .andRoute(DELETE("/exercise/{id}"), exerciseHandler::deleteExercise)
            .andRoute(GET("/exercise-types"), exerciseHandler::getExerciseTypes)
            .andRoute(GET("/exercise-categories"), exerciseHandler::getExerciseCategories)
            .andRoute(GET("/workout/{date}"), workoutHandler::getWorkout)
            .andRoute(POST("/workout"), workoutHandler::createAndUpdateWorkout)
            .andRoute(POST("/import-workout").and(accept(MediaType.MULTIPART_FORM_DATA)), importWorkoutHandler::importWorkout)
    }
}