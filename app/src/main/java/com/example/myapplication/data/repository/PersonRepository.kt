package com.example.myapplication.data.repository

import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.data.dto.validation.accessors.name
import com.example.myapplication.data.dto.validation.accessors.person_id
import com.example.myapplication.data.entity.PersonEntity
import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.constraints.builders.isNotBlank
import dev.nesk.akkurate.constraints.builders.isNotEmpty
import dev.nesk.akkurate.constraints.builders.isNotNull
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.constraints.otherwise

interface PersonRepository {
    fun findByUid(uid : Int): PersonDto?
    fun getAll(): List<PersonDto>
    fun getAllPersonInGroup(groupId: Int, name: String): List<PersonDto>
    fun getAllPersonLapSelect(lap_id: Int): List<PersonDto>
    fun getAllPersonLapUnSelect(lap_id: Int): List<PersonDto>
    fun getAllPersonNotInGroup(groupId: Int, name: String): List<PersonDto>
    fun add(personDto: PersonDto,validationState: ValidationState)
    fun update(personDto: PersonDto,validationState: ValidationState)
    fun delete(personDto: PersonDto,validationState: ValidationState)
    fun getPersonsLinkedToLapWithoutRecords(lapId: Int): List<PersonDto>
}

class PersonRepositoryImpl(private val database: AppDatabase) : PersonRepository {
    override fun findByUid(uid: Int): PersonDto {
        return database.personDao().findByUid(uid)
    }

    override fun getAll(): List<PersonDto> {
        return database.personDao().getAll()
    }

    override fun getAllPersonInGroup(groupId: Int, name: String): List<PersonDto> {
        return database.personDao().getAllPersonInGroup(groupId=groupId,name = "%$name%")
    }

    override fun getAllPersonLapSelect(lap_id: Int): List<PersonDto> {
        return database.personDao().getPersonsLinkedToLap(lap_id)
    }

    override fun getAllPersonLapUnSelect(lap_id: Int): List<PersonDto> {
        TODO("Not yet implemented")
    }

    override fun getAllPersonNotInGroup(groupId: Int, name: String): List<PersonDto> {
        return database.personDao().getAllPersonNotInGroup(groupId=groupId,name = "%$name%")
    }

    override fun add(personDto: PersonDto,validationState: ValidationState){
        val validatePerson = Validator<PersonDto>{
            name.isNotEmpty() otherwise { "Tidak Boleh Kosong" }
            name.isNotNull() otherwise { "Tidak Boleh Null" }
            name.isNotBlank() otherwise { "Tidak Boleh Blank" }
        }

        when (val result = validatePerson(personDto)){
            is ValidationResult.Success -> {
                database.personDao().add(PersonEntity(person_id = null,personDto.name))
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }
            else -> {}
        }
    }
    override fun update(personDto: PersonDto,validationState: ValidationState) {
        val validatePerson = Validator<PersonDto>{
            name.isNotEmpty() otherwise { "Tidak Boleh Kosong" }
            name.isNotNull() otherwise { "Tidak Boleh Null" }
            name.isNotBlank() otherwise { "Tidak Boleh Blank" }
        }

        when (val result = validatePerson(personDto)){
            is ValidationResult.Success -> {
                database.personDao().update(PersonEntity(person_id = personDto.person_id,personDto.name))
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }
            else -> {}
        }
    }

    override fun delete(personDto: PersonDto,validationState: ValidationState) {
        val validatePerson = Validator<PersonDto>{
            person_id.constrain {
                database.personDao().hasPersonJoinedGroup(personDto.person_id)
            } otherwise {"sudah tergabung ke group"}
            person_id.constrain {
                database.personDao().hasPersonJoinedLap(personDto.person_id)
            } otherwise {"sudah tergabung ke lap"}
            person_id.constrain {
                database.personDao().hasPersonRecord(personDto.person_id)
            } otherwise {"sudah tergabung ke record"}
        }

        when (val result = validatePerson(personDto)){
            is ValidationResult.Success -> {
                database.personDao().update(PersonEntity(person_id = personDto.person_id,personDto.name))
                validationState.onSuccess()
            }

            is ValidationResult.Failure -> {
                validationState.onFailure(result.violations)
            }
            else -> {}
        }
        database.personDao().delete(PersonEntity(person_id = personDto.person_id,personDto.name))
    }

    override fun getPersonsLinkedToLapWithoutRecords(lapId: Int): List<PersonDto> {
        return database.personDao().getPersonsLinkedToLapWithoutRecords(lapId)
    }
}