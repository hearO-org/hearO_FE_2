package com.example.hearo2.dictionary

data class DictionaryData(
    val title: String,
    val description: String,
    val category: String,    // 전문용어/수어 등
    val viewCount: Int,      // 조회수
    val imageRes: Int,       // 참고 이미지 or 썸네일
    val likes: Int = 0,      // 좋아요 수 (옵션)
    val tag: String? = null,  // Adapter에서 불러오는 Tag (옵션)
    var isFavorite: Boolean = false
)







