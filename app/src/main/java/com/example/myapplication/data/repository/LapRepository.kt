package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.GroupPersonDto
import com.example.myapplication.data.dto.LapDto
import com.example.myapplication.data.dto.LapGroupDto
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.data.dto.validation.accessors.date
import com.example.myapplication.data.dto.validation.accessors.name
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.LapEntity
import com.example.myapplication.data.entity.LapGroupEntity
import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.constraints.builders.isMatching
import dev.nesk.akkurate.constraints.builders.isNotBlank
import dev.nesk.akkurate.constraints.builders.isNotEmpty
import dev.nesk.akkurate.constraints.builders.isNotNull
import dev.nesk.akkurate.constraints.otherwise

interface LapRepository {
    fun findByUid(uid: Int): LapDto?
    fun findByUidWithGroup(uid: Int): LapGroupDto
    fun getAll(): List<LapDto>
    fun getAllWithGroup(): List<LapGroupDto>
    fun add(lapDto: LapDto,groups : List<GroupDto>, validationState: ValidationState)
    fun update(lapDto: LapDto,groups : List<GroupDto>, validationState: ValidationState)
    fun delete(lapDto: LapDto)
}

class LapRepositoryImpl(private val database: AppDatabase) : LapRepository {
    override fun findByUid(uid: Int): LapDto {
        return database.lapDao().findByUid(uid)
    }

    override fun findByUidWithGroup(uid: Int): LapGroupDto {
       return database.lapDao().findByUidWithGroup(uid)
    }

    override fun getAll(): List<LapDto> {
        return database.lapDao().getAll()
    }

    override fun getAllWithGroup(): List<LapGroupDto> {
        return database.lapDao().getAllLapWithGroup()
    }

    override fun add(lapDto: LapDto, groups: List<GroupDto>, validationState: ValidationState) {
        val validateGroup = Validator<LapDto> {
            name.isNotEmpty()
            name.isNotNull()
            name.isNotBlank()

            val regex = Regex("^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])\\d{4}\$")

            date.isNotEmpty()
            date.isNotNull()
            date.isNotBlank()
            date.isMatching(regex)
        }

        when (val result = validateGroup(lapDto)) {
            is ValidationResult.Success -> {
                val lap_id = database.lapDao().add(LapEntity(lap_id = null, lapDto.name,lapDto.date))

                val lapGroups = groups.map {
                    LapGroupEntity(lap_id = lap_id.toInt(), group_id = it.group_id)
                }.toTypedArray()

                database.lapDao().addGroupToLap(*lapGroups)

                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }

            else -> {}
        }
    }

    override fun update(lapDto: LapDto,groups : List<GroupDto>, validationState: ValidationState) {
        val validatePerson = Validator<LapDto> {
            name.isNotEmpty()
            name.isNotNull()
            name.isNotBlank()

            val regex = Regex("^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])\\d{4}\$")

            date.isNotEmpty()
            date.isNotNull()
            date.isNotBlank()
            date.isMatching(regex)
        }

        when (val result = validatePerson(lapDto)) {
            is ValidationResult.Success -> {
                database.lapDao().update(LapEntity(lap_id = lapDto.lap_id, lapDto.name,lapDto.date))

                database.lapDao().findByUidWithGroup(lapDto.lap_id).groups.map {
                    val lapgroup = LapGroupEntity(lap_id = lapDto.lap_id, group_id = it.group_id!!)
                    database.lapDao().deleteGroupInLap(lapgroup)
                }

                val lapGroups = groups.map {
                    LapGroupEntity(lap_id = lapDto.lap_id, group_id = it.group_id)
                }.toTypedArray()

                database.lapDao().addGroupToLap(*lapGroups)

                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }

            else -> {}
        }
    }

    override fun delete(lapDto: LapDto) {

        database.lapDao().findByUidWithGroup(lapDto.lap_id).groups.map {
            val lapgroup = LapGroupEntity(lap_id = lapDto.lap_id, group_id = it.group_id!!)
            database.lapDao().deleteGroupInLap(lapgroup)
        }

        database.lapDao().delete(
            LapEntity(lapDto.lap_id,lapDto.name,lapDto.date)
        )
    }
}