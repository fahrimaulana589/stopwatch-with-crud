package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val person_id: Int?,
    @ColumnInfo(name = "name") val name: String
)
