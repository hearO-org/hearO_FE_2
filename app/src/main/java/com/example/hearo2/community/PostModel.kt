package com.example.hearo2.community

data class PostModel(
    val id: Int,
    val category: String,
    val title: String,
    val content: String,
    val writer: String,
    val views: Int = 0,
    val likes: Int = 0,
    val comments: Int = 0
)
