package com.example.hearo2.home

data class RecentSoundData(
    val title: String,
    val accuracy: String,
    val time: String,
    val iconRes: Int,
    val tag: String? = null
)
