package com.example.myapplication.data.dto

import dev.nesk.akkurate.annotations.Validate

@Validate
data class PersonDto(
    val person_id: Int = 0,
    val name: String = ""
)
