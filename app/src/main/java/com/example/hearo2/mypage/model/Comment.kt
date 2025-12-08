package com.example.hearo2.mypage.model

data class Comment(
    val id: Int,
    val category: String,
    val content: String,
    val time: String,
    val reply: String?
)