package com.example.hearo2.community

data class PostListResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: PostListData
)

data class PostListData(
    val totalElements: Int,
    val totalPages: Int,
    val page: Int,
    val size: Int,
    val content: List<PostModel>
)

data class PostDetailResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: PostModel
)

data class PostModel(
    val id: Long,
    val authorId: Long,
    val authorNickname: String,
    val title: String,
    val content: String,
    val category: String,
    val visibility: String,
    val images: List<String>?,
    val tags: List<String>?,
    val createdAt: String,
    val modifiedAt: String,
    val likeCount: Int,
    val liked: Boolean,
    val scrapped: Boolean
)

data class ImageUploadResponse(
    val imageUrls: List<String>
)

data class PostRequest(
    val title: String,
    val content: String,
    val category: String,
    val visibility: String,
    val imageUrls: List<String> = emptyList(),
    val tags: List<String>
)

data class ScrapResponse(
    val likedOrScrapped: Boolean,
    val count: Int
)

