package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.GroupPersonDto
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.GroupPersonEntity

@Dao
interface GroupDao {
    @Query("SELECT * FROM groupentity ORDER BY group_id desc")
    fun getAll(): List<GroupDto>

    @Query("SELECT * FROM groupentity")
    fun getAllGroupWithPerson(): List<GroupPersonDto>

    @Query("SELECT * FROM groupentity WHERE group_id = :uid")
    fun findByUid(uid: Int): GroupDto

    @Query("SELECT * FROM groupentity WHERE group_id = :uid")
    fun findByUidWithPerson(uid: Int): GroupPersonDto

    @Query("SELECT * FROM groupentity WHERE name LIKE :name")
    fun findByName(name: String): GroupDto

    @Insert()
    fun add(vararg groupentity: GroupEntity)

    @Insert()
    fun addPersonToGroup(vararg groupPersonEntity: GroupPersonEntity)

    @Update
    fun update(groupentity: GroupEntity)

    @Delete
    fun delete(groupentity: GroupEntity)

    @Delete
    fun deletePersonIntGroup(groupPersonEntity: GroupPersonEntity)
}