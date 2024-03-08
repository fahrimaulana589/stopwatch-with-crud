package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val group_id: Int?,
    @ColumnInfo(name = "name") val name: String,
)
