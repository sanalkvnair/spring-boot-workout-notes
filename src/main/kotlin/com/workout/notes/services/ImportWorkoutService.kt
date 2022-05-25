package com.workout.notes.services

import com.workout.notes.dto.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Flux
import java.util.*

interface ImportWorkoutService {
    fun importWorkFromFile(filePart: FilePart): Flux<WorkoutDto>
}

class ImportWorkoutServiceImpl(private val workoutService: WorkoutService) : ImportWorkoutService {
    override fun importWorkFromFile(filePart: FilePart): Flux<WorkoutDto> {
        return filePart.content().map { dataBuffer ->
            val importWorkoutDtoList: MutableList<ImportWorkoutDto> = mutableListOf()
            val workoutDtoList: MutableList<WorkoutDto> = mutableListOf()
            dataBuffer.asInputStream().bufferedReader()
                .use {
                    val csvParser = CSVParser(
                        it,
                        CSVFormat.DEFAULT.builder().setSkipHeaderRecord(true)
                            .setHeader(
                            "Date",
                            "Exercise",
                            "Category",
                            "Weight (kgs)",
                            "Reps",
                            "Distance",
                            "Distance Unit",
                            "Time"
                        ).setTrim(true).build()
                    )
                    val csvRecords = csvParser.records
                    for (csvRecord: CSVRecord in csvRecords) {
                        val importWorkoutDto = ImportWorkoutDto(
                            csvRecord.get("Date"),
                            csvRecord.get("Exercise"),
                            csvRecord.get("Category"),
                            csvRecord.get("Weight (kgs)").toDouble(),
                            csvRecord.get("Reps").toInt()
                        )
                        importWorkoutDtoList += importWorkoutDto
                    }
                }
            importWorkoutDtoList.groupBy { it.date }.map { groupedImportWorkoutDtoList ->
                val workoutDto = WorkoutDto(groupedImportWorkoutDtoList.key)
                groupedImportWorkoutDtoList.value.groupBy { it.toKey() }
                    .map { workoutRoutines ->
                        val workoutRoutineDto = WorkoutRoutineDto(
                            workoutRoutines.key.exercise,
                            ExerciseCategory.valueOf(workoutRoutines.key.category.uppercase(Locale.getDefault())),
                            ExerciseType.WEIGHT_AND_REPS
                        )
                        workoutRoutines.value.forEach { workoutRoutine ->
                            val workoutTrackDto = WorkoutTrackDto(
                                workoutRoutine.weight,
                                "kgs",
                                workoutRoutine.reps
                            )
                            workoutRoutineDto.workoutTracks.add(workoutTrackDto)
                        }
                        workoutDto.workoutRoutines.add(workoutRoutineDto)
                }
                workoutDtoList.add(workoutDto)
            }
            Flux.fromIterable(workoutDtoList)
        }.flatMap {it}
            .flatMap { workoutDto ->
            workoutService.createAndUpdateWorkout(workoutDto)
        }

    }

}

data class ImportWorkoutRoutineKey(val exercise: String, val category: String)
fun ImportWorkoutDto.toKey() = ImportWorkoutRoutineKey(exercise, category)