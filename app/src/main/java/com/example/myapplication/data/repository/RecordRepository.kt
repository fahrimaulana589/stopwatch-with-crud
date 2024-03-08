package com.example.myapplication.data.repository

import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.GroupPersonDto
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.data.dto.RecordDto
import com.example.myapplication.data.dto.RecordWithPersonDto
import com.example.myapplication.data.dto.validation.accessors.name
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.PersonEntity
import com.example.myapplication.data.entity.RecordEntity
import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.constraints.builders.isNotBlank
import dev.nesk.akkurate.constraints.builders.isNotEmpty
import dev.nesk.akkurate.constraints.builders.isNotNull

interface RecordRepository {
    fun getAllByLap(lap_id:Int): List<RecordWithPersonDto>
    fun add(recordDto: RecordDto, validationState: ValidationState)
    fun update(recordDto: RecordDto, validationState: ValidationState)
    fun delete(recordDto: RecordDto)
}

class RecordRepositoryImpl(private val database: AppDatabase) : RecordRepository {
    override fun getAllByLap(lap_id:Int): List<RecordWithPersonDto> {
        return database.recordDao().getAllByLap(lap_id)
    }

    override fun add(recordDto: RecordDto, validationState: ValidationState) {
        val validateRecord = Validator<RecordDto>{

        }

        when(val result = validateRecord(recordDto)) {
            is ValidationResult.Success -> {
                database.recordDao().add(
                    RecordEntity(
                        record_id = recordDto.record_id, // Properti yang sama
                        time = recordDto.time,
                        lap_id = recordDto.lap_id,
                        person_id = recordDto.person_id
                    )
                )
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }
            else -> {}
        }
    }

    override fun update(recordDto: RecordDto, validationState: ValidationState) {
        val validateRecord = Validator<RecordDto>{

        }

        when(val result = validateRecord(recordDto)) {
            is ValidationResult.Success -> {
                database.recordDao().update(
                    RecordEntity(
                        record_id = recordDto.record_id, // Properti yang sama
                        time = recordDto.time,
                        lap_id = recordDto.lap_id,
                        person_id = recordDto.person_id
                    )
                )
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }
            else -> {}
        }
    }

    override fun delete(recordDto: RecordDto) {
       database.recordDao().delete(
           RecordEntity(
               record_id = recordDto.record_id, // Properti yang sama
               time = recordDto.time,
               lap_id = recordDto.lap_id,
               person_id = recordDto.person_id
           )
       )
    }
}