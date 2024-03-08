package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["group_id","person_id"])
data class GroupPersonEntity(
    val group_id: Int,
    val person_id: Int
)
