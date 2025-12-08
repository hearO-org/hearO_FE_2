// OnboardingResponse.kt
package com.example.hearo2.network.response

data class OnboardingResponse(
    val code: String,
    val status: Int,
    val message: String,
    val data: OnboardingData?
)

data class OnboardingData(
    val userId: Int,
    val email: String,
    val nickname: String,
    val gender: String,
    val birthday: String,
    val interestKeywords: List<String>
)
