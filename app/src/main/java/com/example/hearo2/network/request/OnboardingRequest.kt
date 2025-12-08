// OnboardingRequest.kt
package com.example.hearo2.network.request

data class OnboardingRequest(
    val nickname: String,
    val gender: String,
    val birthday: String,
    val interestKeywords: List<String>
)
