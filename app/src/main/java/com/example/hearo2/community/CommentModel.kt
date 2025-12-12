package com.example.hearo2.community

data class CommentModel(
    val id: Int,
    val writer: String,
    val content: String,
    val time: String,
    val parentId: Long = 0L,
    var likeCount: Int = 0,
    var isLiked: Boolean = false

)

data class CommentRequest(
    val content: String
)

data class CommentListResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: CommentPageData
)

data class CommentPageData(
    val content: List<CommentDto>
)

data class CommentDto(
    val id: Long,
    val authorId: Long,
    val authorNickname: String,
    val content: String,
    val deleted: Boolean,
    val parentId: Long?,
    val createdAt: String,
    val modifiedAt: String,
    val likeCount: Int,
    val liked: Boolean

)


data class LikeResponse(
    val likedOrScrapped: Boolean,
    val count: Int
)

