package com.example.hearo2.network.response

data class MyInfoResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: MyInfoData
)

data class MyInfoData(
    val userId: Int,
    val email: String,
    val nickname: String,
    val gender: String,
    val birthday: String,
    val interestKeywords: List<String>
)
