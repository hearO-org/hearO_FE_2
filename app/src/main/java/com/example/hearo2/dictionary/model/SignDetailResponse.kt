package com.example.hearo2.dictionary.model

data class SignDetailResponse(
    val data: SignDetailData
)

data class SignDetailData(
    val id: Int,
    val localId: String?,
    val title: String?,
    val videoUrl: String?,
    val thumbnailUrl: String?,
    val signDescription: String?,
    val images: List<String>?,
    val categoryType: String?,
    val viewCount: Int?,
    val createdAt: String?,
    val modifiedAt: String?
)
