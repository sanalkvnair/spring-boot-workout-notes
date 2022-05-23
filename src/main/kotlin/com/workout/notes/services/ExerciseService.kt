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
    fun getWorkouts(): Flux<ExerciseDto>
    fun getWorkout(id: String): Mono<ExerciseDto>
    fun createWorkout(exerciseDto: ExerciseDto): Mono<ExerciseDto>
    fun updateWorkout(id: String, exerciseDto: ExerciseDto): Mono<ExerciseDto>
    fun deleteWorkout(id: String): Mono<Void>

    fun getExerciseTypes(): Flux<ExerciseType>
    fun getExerciseCategories(): Flux<ExerciseCategory>
}

class ExerciseServiceImpl(private val exerciseRepository: ExerciseRepository) : ExerciseService {
    companion object {
        private final val logger: Logger = LoggerFactory.getLogger(ExerciseServiceImpl::class.java)
    }
    override fun getWorkouts(): Flux<ExerciseDto> =  exerciseRepository.findAll().map { exerciseDtoMapper(it) }

    override fun getWorkout(id: String): Mono<ExerciseDto> =  exerciseRepository.findById(id).map { exerciseDtoMapper(it) }

    override fun createWorkout(exerciseDto: ExerciseDto): Mono<ExerciseDto> = exerciseRepository.save(exerciseModelMapper(UUID.randomUUID().toString(), exerciseDto)).map { exerciseDtoMapper(it) }

    override fun updateWorkout(id: String, exerciseDto: ExerciseDto): Mono<ExerciseDto> = exerciseRepository.save(exerciseModelMapper(id, exerciseDto)).map { exerciseDtoMapper(it) }

    override fun deleteWorkout(id: String): Mono<Void> = exerciseRepository.deleteById(id)

    override fun getExerciseTypes(): Flux<ExerciseType> = Flux.fromArray(ExerciseType.values())

    override fun getExerciseCategories(): Flux<ExerciseCategory> = Flux.fromArray(ExerciseCategory.values())


    private fun exerciseDtoMapper(exercise: Exercise) : ExerciseDto = ExerciseDto(exercise.id, exercise.exerciseName, ExerciseCategory.valueOf(exercise.exerciseCategory), ExerciseType.valueOf(exercise.exerciseType), exercise.exerciseNotes)
    private fun exerciseModelMapper(id: String, exerciseDto: ExerciseDto) = Exercise(id, exerciseDto.exerciseName, exerciseDto.exerciseCategory.name, exerciseDto.exerciseType.name, exerciseDto.exerciseNotes)

}