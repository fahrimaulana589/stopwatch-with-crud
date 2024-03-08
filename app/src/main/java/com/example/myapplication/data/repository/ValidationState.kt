package com.example.myapplication.data.repository

import dev.nesk.akkurate.constraints.ConstraintViolationSet

data class ValidationState(
    var onSuccess:() -> Unit = {},
    var onFailure:(violations: ConstraintViolationSet) -> Unit = {},
)
