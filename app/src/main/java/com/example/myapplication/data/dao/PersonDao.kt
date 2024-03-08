package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.data.entity.PersonEntity

@Dao
interface PersonDao {
    @Query("SELECT * FROM personentity ORDER BY person_id desc")
    fun getAll(): List<PersonDto>

    @Query(
        "SELECT PersonEntity.person_id, PersonEntity.name " +
                "FROM PersonEntity " +
                "LEFT JOIN GroupPersonEntity ON PersonEntity.person_id = GroupPersonEntity.person_id AND GroupPersonEntity.group_id = :groupId " +
                "WHERE GroupPersonEntity.person_id IS NULL " +
                "AND PersonEntity.name LIKE :name"
    )
    fun getAllPersonNotInGroup(groupId: Int, name: String): List<PersonDto>

    @Query(
        "SELECT PersonEntity.person_id, PersonEntity.name " +
                "FROM PersonEntity " +
                "INNER JOIN GroupPersonEntity ON PersonEntity.person_id = GroupPersonEntity.person_id " +
                "INNER JOIN GroupEntity ON GroupPersonEntity.group_id = GroupEntity.group_id " +
                "WHERE GroupEntity.group_id = :groupId "+
                "AND PersonEntity.name LIKE :name"
    )
    fun getAllPersonInGroup(groupId: Int, name: String): List<PersonDto>

    @Query("SELECT * FROM PersonEntity WHERE person_id IN (SELECT person_id FROM GroupPersonEntity WHERE group_id IN (SELECT group_id FROM LapGroupEntity WHERE lap_id = :lapId))")
    fun getPersonsLinkedToLap(lapId: Int): List<PersonDto>

    @Query("SELECT PersonEntity.* FROM PersonEntity " +
            "INNER JOIN GroupPersonEntity ON PersonEntity.person_id = GroupPersonEntity.person_id " +
            "INNER JOIN LapGroupEntity ON GroupPersonEntity.group_id = LapGroupEntity.group_id " +
            "WHERE LapGroupEntity.lap_id = :lapId " +
            "AND PersonEntity.person_id NOT IN " +
            "(SELECT person_id FROM RecordEntity WHERE lap_id = :lapId AND person_id NOT NULL)")
    fun getPersonsLinkedToLapWithoutRecords(lapId: Int): List<PersonDto>

    @Query("SELECT * FROM personentity WHERE person_id = :uid")
    fun findByUid(uid: Int): PersonDto

    @Query("SELECT * FROM personentity WHERE person_id LIKE :name")
    fun findByName(name: String): PersonDto

    @Insert()
    fun add(vararg personEntity: PersonEntity)

    @Update
    fun update(personEntity: PersonEntity)

    @Delete
    fun delete(personEntity: PersonEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM GroupPersonEntity WHERE person_id = :personId)")
    fun hasPersonJoinedGroup(personId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM LapGroupEntity WHERE group_id IN (SELECT group_id FROM GroupPersonEntity WHERE person_id = :personId))")
    fun hasPersonJoinedLap(personId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM RecordEntity WHERE person_id = :personId)")
    fun hasPersonRecord(personId: Int): Boolean
}