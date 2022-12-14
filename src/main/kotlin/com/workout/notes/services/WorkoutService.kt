package com.workout.notes.services

import com.workout.notes.dto.*
import com.workout.notes.models.Workout
import com.workout.notes.models.WorkoutRoutine
import com.workout.notes.models.WorkoutTrack
import com.workout.notes.repositories.WorkoutRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

interface WorkoutService {
    fun createAndUpdateWorkout(workoutDto: WorkoutDto, userId: String): Mono<WorkoutDto>
    fun getWorkout(date: String, userId: String): Mono<WorkoutDto>
}

class WorkoutServiceImpl(private val workoutRepository: WorkoutRepository): WorkoutService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WorkoutServiceImpl::class.java)
    }
    override fun createAndUpdateWorkout(workoutDto: WorkoutDto, userId: String): Mono<WorkoutDto> {
        logger.info("Creating/Updating workout for date: ${workoutDto.date} begin for user: $userId.")
        return workoutRepository.save(workoutModelMapper(workoutDto, userId)).map { workoutDtoMapper(it) }.doOnSuccess {
            logger.info("Creating/Updating workout for date: ${workoutDto.date} successful for user: $userId.")
        }
    }

    override fun getWorkout(date: String, userId: String): Mono<WorkoutDto> {
        return workoutRepository.findByDateAndUserId(date, userId).map { workoutDtoMapper(it) }
    }

    private fun workoutDtoMapper(workout: Workout): WorkoutDto {
        val workoutDto = WorkoutDto(workout.date, workout.userId)
        workout.workoutRoutines.map { workoutRoutine ->
            val workoutRoutineDto = WorkoutRoutineDto(
                workoutRoutine.exerciseName,
                ExerciseCategory.valueOf(workoutRoutine.exerciseCategory),
                workoutRoutine.exerciseType?.let { ExerciseType.valueOf(workoutRoutine.exerciseType) }
            )
            workoutRoutine.workoutTracks.map { workoutTrack ->
                val workoutTrackDto = WorkoutTrackDto(
                    workoutTrack.weight,
                    workoutTrack.weightUnit,
                    workoutTrack.reps,
                    workoutTrack.distance,
                    workoutTrack.distanceUnit,
                    workoutTrack.time
                )
                workoutRoutineDto.workoutTracks.add(workoutTrackDto)
            }
            workoutDto.workoutRoutines.add(workoutRoutineDto)
        }

        return workoutDto
    }

    private fun workoutModelMapper(workoutDto: WorkoutDto, userId: String): Workout {
        val workout = Workout(workoutDto.date, userId)
        workoutDto.workoutRoutines.map { workoutRoutineDto ->
            val workoutRoutine = WorkoutRoutine(
                workoutRoutineDto.exerciseName,
                workoutRoutineDto.exerciseCategory.name,
                workoutRoutineDto.exerciseType?.name
            )
            workoutRoutineDto.workoutTracks.map { workoutTrackDto ->
                val workoutTrack = WorkoutTrack(
                    workoutTrackDto.weight,
                    workoutTrackDto.weightUnit,
                    workoutTrackDto.reps,
                    workoutTrackDto.distance,
                    workoutTrackDto.distanceUnit,
                    workoutTrackDto.time
                )
                workoutRoutine.workoutTracks.add(workoutTrack)
            }
            workout.workoutRoutines.add(workoutRoutine)
        }

        return workout
    }

}
