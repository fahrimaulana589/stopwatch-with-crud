package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LapEntity(
    @PrimaryKey(autoGenerate = true) val lap_id: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String
)
