package com.example.hearo2.mypage.model

data class MyActivityItem(

    val id: Int,
    val title: String,
    val category: String,
    val date: String,        // 날짜(=time 동일)
    val thumbnailRes: Int,
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val comment: String? = null, // 댓글일 때만 사용

    val type: Type           // ⭐ 북마크 / 좋아요 / 댓글 구분용
) {
    enum class Type {
        BOOKMARK, LIKE, COMMENT
    }
}
