package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val record_id: Int?,
    @ColumnInfo(name = "time") val time: Long,
    val lap_id: Int?,
    val person_id: Int?
)
