package com.example.myapplication.data.dto

import dev.nesk.akkurate.annotations.Validate

@Validate
data class LapDto(
    val lap_id: Int = 0,
    val name: String = "",
    val date: String = "",
)
