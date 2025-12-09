package com.example.hearo2.dictionary.model

data class SignListResponse(
    val data: SignListData
)

data class SignListData(
    val content: List<SignItem>
)

data class SignItem(
    val id: Int,
    val localId: String?,
    val title: String,
    val videoUrl: String?,
    val thumbnailUrl: String?,
    val signDescription: String?,
    val imagesCsv: String?,   // 전체조회는 CSV 하나만 옴
    val sourceUrl: String?,
    val collectionDb: String?,
    val categoryType: String?,
    val viewCount: Int?,
    val createdAt: String?,
    val modifiedAt: String?,
    var favorite: Boolean? = false   // 서버에 없기 때문에 nullable + default 필수
)
