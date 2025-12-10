package com.example.hearo2.dictionary.model

data class BaseResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String
)