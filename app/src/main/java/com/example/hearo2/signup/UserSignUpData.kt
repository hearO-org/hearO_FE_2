package com.example.hearo2.signup

import java.io.Serializable

data class UserSignUpData(
    val nickname: String,
    val email: String,
    val password: String
) : Serializable
