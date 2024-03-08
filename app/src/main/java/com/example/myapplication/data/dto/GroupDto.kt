package com.example.myapplication.data.dto

import dev.nesk.akkurate.annotations.Validate

@Validate
data class GroupDto(
    val group_id: Int = 0,
    val name: String = ""
)
