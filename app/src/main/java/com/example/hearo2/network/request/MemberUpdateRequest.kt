package com.example.hearo2.network.request

data class MemberUpdateRequest(
    val nickname: String,
    val gender: String,
    val birthday: String,
    val interestKeywords: List<String>?
)
