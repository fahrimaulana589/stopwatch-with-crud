package com.example.myapplication.data.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.PersonEntity
import dev.nesk.akkurate.annotations.Validate

data class GroupPersonDto(
    @Embedded
    val groupDto: GroupDto,
    @Relation(
        parentColumn = "group_id",
        entityColumn = "person_id",
        associateBy = Junction(GroupPersonEntity::class),
    )
    val persons: List<PersonEntity>
)
