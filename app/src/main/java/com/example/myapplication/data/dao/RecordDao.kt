package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.GroupPersonDto
import com.example.myapplication.data.dto.RecordDto
import com.example.myapplication.data.dto.RecordWithPersonDto
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.RecordEntity

@Dao
interface RecordDao {
    @Query("SELECT * FROM recordentity WHERE lap_id = :lap_id ORDER BY record_id desc")
    fun getAllByLap(lap_id : Int): List<RecordWithPersonDto>

    @Insert()
    fun add(vararg recordEntity: RecordEntity)

    @Update
    fun update(recordEntity: RecordEntity)

    @Delete
    fun delete(recordEntity: RecordEntity)
    @Query("DELETE FROM recordentity WHERE lap_id = :lapId")
    fun deleteBylap(lapId: Int)
}