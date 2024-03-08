package com.example.myapplication.data.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.PersonEntity
import dev.nesk.akkurate.annotations.Validate

data class RecordWithPersonDto(
    @Embedded
    val recordDto: RecordDto = RecordDto(time = 0L),
    @Relation(
        parentColumn = "person_id",
        entityColumn = "person_id"
    )
    val person: PersonEntity? = PersonEntity(person_id = null, name = "")
)

