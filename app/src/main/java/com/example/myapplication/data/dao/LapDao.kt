package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.GroupPersonDto
import com.example.myapplication.data.dto.LapDto
import com.example.myapplication.data.dto.LapGroupDto
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.LapEntity
import com.example.myapplication.data.entity.LapGroupEntity

@Dao
interface LapDao {
    @Query("SELECT * FROM lapentity ORDER BY lap_id desc")
    fun getAll(): List<LapDto>

    @Query("SELECT * FROM lapentity")
    fun getAllLapWithGroup(): List<LapGroupDto>

    @Query("SELECT * FROM lapentity WHERE lap_id = :uid")
    fun findByUid(uid: Int): LapDto

    @Query("SELECT * FROM lapentity WHERE lap_id = :uid")
    fun findByUidWithGroup(uid: Int): LapGroupDto

    @Query("SELECT * FROM lapentity WHERE name LIKE :name")
    fun findByName(name: String): LapDto

    @Insert()
    fun add(lapEntity: LapEntity) : Long

    @Insert()
    fun addGroupToLap(vararg lapGroupEntity: LapGroupEntity)

    @Update
    fun update(lapEntity: LapEntity)

    @Delete
    fun delete(lapEntity: LapEntity)

    @Delete
    fun deleteGroupInLap(lapGroupEntity: LapGroupEntity)
}