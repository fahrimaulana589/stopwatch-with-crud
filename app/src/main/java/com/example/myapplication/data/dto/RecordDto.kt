package com.example.myapplication.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.nesk.akkurate.annotations.Validate

@Validate
data class RecordDto(
    val record_id: Int? = null,
    val time: Long,
    val lap_id: Int? = null,
    val person_id: Int? = null
)
