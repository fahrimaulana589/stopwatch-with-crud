package com.example.myapplication.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["group_id","lap_id"])
data class LapGroupEntity(
    val lap_id: Int,
    val group_id: Int,
)
