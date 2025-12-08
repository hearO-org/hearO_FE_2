package com.example.hearo2.network.response

data class LoginResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val access: String,
    val refresh: String,
    val tokenType: String,
    val expiresIn: Long,
    val onboardingRequired: Boolean
)
