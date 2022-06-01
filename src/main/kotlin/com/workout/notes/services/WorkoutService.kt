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
    fun createAndUpdateWorkout(workoutDto: WorkoutDto): Mono<WorkoutDto>
    fun getWorkout(date: String): Mono<WorkoutDto>
}

class WorkoutServiceImpl(private val workoutRepository: WorkoutRepository): WorkoutService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WorkoutServiceImpl::class.java)
    }
    override fun createAndUpdateWorkout(workoutDto: WorkoutDto): Mono<WorkoutDto> {
        logger.info("Creating/Updating workout for date: ${workoutDto.date} begin.")
        return workoutRepository.save(workoutModelMapper(workoutDto)).map { workoutDtoMapper(it) }.doOnSuccess {
            logger.info("Creating/Updating workout for date: ${workoutDto.date} successful.")
        }
    }

    override fun getWorkout(date: String): Mono<WorkoutDto> {
        return workoutRepository.findById(date).map { workoutDtoMapper(it) }
    }

    private fun workoutDtoMapper(workout: Workout): WorkoutDto {
        val workoutDto = WorkoutDto(workout.date)
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

    private fun workoutModelMapper(workoutDto: WorkoutDto): Workout {
        val workout = Workout(workoutDto.date)
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
