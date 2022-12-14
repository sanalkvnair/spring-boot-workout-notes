package com.workout.notes.services

import com.workout.notes.dto.ExerciseCategory
import com.workout.notes.dto.ExerciseDto
import com.workout.notes.dto.ExerciseType
import com.workout.notes.models.Exercise
import com.workout.notes.repositories.ExerciseRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface ExerciseService {
    fun getWorkouts(userId: String): Flux<ExerciseDto>
    fun getWorkout(id: String, userId: String): Mono<ExerciseDto>
    fun createWorkout(exerciseDto: ExerciseDto, userId: String): Mono<ExerciseDto>
    fun updateWorkout(id: String, exerciseDto: ExerciseDto, userId: String): Mono<ExerciseDto>
    fun deleteWorkout(id: String, userId: String): Mono<Void>

    fun getExerciseTypes(): Flux<ExerciseType>
    fun getExerciseCategories(): Flux<ExerciseCategory>
}

class ExerciseServiceImpl(private val exerciseRepository: ExerciseRepository) : ExerciseService {
    companion object {
        private final val logger: Logger = LoggerFactory.getLogger(ExerciseServiceImpl::class.java)
    }
    override fun getWorkouts(userId: String): Flux<ExerciseDto> =  exerciseRepository.findByUserId(userId).map { exerciseDtoMapper(it) }

    override fun getWorkout(id: String, userId: String): Mono<ExerciseDto> =  exerciseRepository.findByIdAndUserId(id, userId).map { exerciseDtoMapper(it) }

    override fun createWorkout(exerciseDto: ExerciseDto, userId: String): Mono<ExerciseDto> = exerciseRepository.save(exerciseModelMapper(UUID.randomUUID().toString(), exerciseDto, userId)).map { exerciseDtoMapper(it) }

    override fun updateWorkout(id: String, exerciseDto: ExerciseDto, userId: String): Mono<ExerciseDto> = exerciseRepository.save(exerciseModelMapper(id, exerciseDto, userId)).map { exerciseDtoMapper(it) }

    override fun deleteWorkout(id: String, userId: String): Mono<Void> = exerciseRepository.deleteByIdAndUserId(id, userId)

    override fun getExerciseTypes(): Flux<ExerciseType> = Flux.fromArray(ExerciseType.values())

    override fun getExerciseCategories(): Flux<ExerciseCategory> = Flux.fromArray(ExerciseCategory.values())


    private fun exerciseDtoMapper(exercise: Exercise) : ExerciseDto = ExerciseDto(exercise.id, exercise.exerciseName, ExerciseCategory.valueOf(exercise.exerciseCategory), ExerciseType.valueOf(exercise.exerciseType), exercise.exerciseNotes, exercise.userId)
    private fun exerciseModelMapper(id: String, exerciseDto: ExerciseDto, userId: String) = Exercise(id, exerciseDto.exerciseName, exerciseDto.exerciseCategory.name, exerciseDto.exerciseType.name, exerciseDto.exerciseNotes, userId)

}