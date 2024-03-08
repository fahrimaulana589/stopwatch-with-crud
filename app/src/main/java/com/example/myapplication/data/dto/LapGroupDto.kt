package com.example.myapplication.data.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.LapGroupEntity
import com.example.myapplication.data.entity.PersonEntity
import dev.nesk.akkurate.annotations.Validate

@Validate
data class LapGroupDto(
    @Embedded
    val lapDto: LapDto,
    @Relation(
        parentColumn = "lap_id",
        entityColumn = "group_id",
        associateBy = Junction(LapGroupEntity::class),
    )
    val groups: List<GroupEntity>
)
