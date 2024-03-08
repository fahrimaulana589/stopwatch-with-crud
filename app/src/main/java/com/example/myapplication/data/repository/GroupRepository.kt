package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.GroupPersonDto
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.data.dto.validation.accessors.group_id
import com.example.myapplication.data.dto.validation.accessors.name
import com.example.myapplication.data.dto.validation.accessors.person_id
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.PersonEntity
import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.constraints.builders.isNotBlank
import dev.nesk.akkurate.constraints.builders.isNotEmpty
import dev.nesk.akkurate.constraints.builders.isNotNull
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.constraints.otherwise

interface GroupRepository {
    fun findByUid(uid: Int): GroupDto?
    fun getAllGroupWithPerson(): List<GroupPersonDto>
    fun getAll(): List<GroupDto>
    fun add(groupDto: GroupDto, validationState: ValidationState)
    fun addPersonToGroup(group_id: Int, person_id: Int)
    fun update(groupDto: GroupDto, validationState: ValidationState)
    fun delete(groupDto: GroupDto, validationState: ValidationState)
    fun deletePersonInGroup(group_id: Int, person_id: Int)
}

class GroupRepositoryImpl(private val database: AppDatabase) : GroupRepository {
    override fun findByUid(uid: Int): GroupDto {
        return database.groupDao().findByUid(uid)
    }

    override fun getAllGroupWithPerson(): List<GroupPersonDto> {
        return database.groupDao().getAllGroupWithPerson()
    }

    override fun getAll(): List<GroupDto> {
        return database.groupDao().getAll()
    }

    override fun add(groupDto: GroupDto, validationState: ValidationState) {
        val validateGroup = Validator<GroupDto> {
            name.isNotEmpty()
            name.isNotNull()
            name.isNotBlank()
        }

        when (val result = validateGroup(groupDto)) {
            is ValidationResult.Success -> {
                database.groupDao().add(GroupEntity(group_id = null, groupDto.name))
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }

            else -> {}
        }
    }

    override fun addPersonToGroup(group_id: Int, person_id: Int) {
        database.groupDao().addPersonToGroup(
            GroupPersonEntity(group_id = group_id, person_id = person_id)
        )
    }

    override fun update(groupDto: GroupDto, validationState: ValidationState) {
        val validatePerson = Validator<GroupDto> {
            name.isNotEmpty()
            name.isNotNull()
            name.isNotBlank()
        }

        when (val result = validatePerson(groupDto)) {
            is ValidationResult.Success -> {
                database.groupDao().update(
                    GroupEntity(group_id = groupDto.group_id, groupDto.name)
                )
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }

            else -> {}
        }
    }

    override fun delete(groupDto: GroupDto, validationState: ValidationState) {
        val validatePerson = Validator<GroupDto>{
            group_id.constrain {
                val result = !database.groupDao().isGroupConnectedToLap(groupDto.group_id)
                Log.e("Group", "delete isGroupConnectedToLap: "+result, )
                result
            } otherwise {"sudah tergabung ke lap"}
        }

        when (val result = validatePerson(groupDto)){
            is ValidationResult.Success -> {
                database.groupDao().findByUidWithPerson(groupDto.group_id).persons.forEach {
                    deletePersonInGroup(groupDto.group_id,it.person_id!!)
                }
                database.groupDao().delete(
                    GroupEntity(group_id = groupDto.group_id, groupDto.name)
                )
                Log.e("Group", "delete Sucses: "+result, )
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                Log.e("Group", "delete Fail: "+result.violations, )
                validationState.onFailure(result.violations)
            }
            else -> {

            }
        }
    }

    override fun deletePersonInGroup(group_id: Int, person_id: Int) {
        database.groupDao().deletePersonIntGroup(
            GroupPersonEntity(group_id = group_id, person_id = person_id)
        )
    }
}