package com.example.hearo2.mypage.model

data class Post(
    val id: Int,
    val category: String,
    val title: String,
    val content: String,
    val time: String,
    val views: Int,
    val likes: Int,
    val comments: Int
)
