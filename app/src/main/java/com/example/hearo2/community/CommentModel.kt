package com.example.hearo2.community

data class CommentModel(
    val id: Int,
    val postId: Int,
    val writer: String,
    val comment: String,
    val time: String
)
