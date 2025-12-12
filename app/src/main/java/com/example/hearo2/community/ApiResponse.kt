package com.example.hearo2.community


data class ApiResponse<T>(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: T
)
