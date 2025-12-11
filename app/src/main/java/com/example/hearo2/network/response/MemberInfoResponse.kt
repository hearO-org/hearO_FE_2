package com.example.hearo2.network.response

data class MemberInfoResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: MemberInfo
)

data class MemberInfo(
    val userId: Int,
    val email: String,
    val nickname: String,
    val gender: String,
    val birthday: String,
    val interestKeywords: List<String>
)