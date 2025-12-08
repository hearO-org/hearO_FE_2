package com.example.hearo2

data class NotificationItem(
    val id: Int,
    val type: String,          // "중요", "시스템", "커뮤니티"
    val category: String,      // ex: "소리 감지", "커뮤니티", "시스템"
    val title: String,         // 알림 제목
    val content: String,       // 본문
    val time: String,          // 예: "5분 전"
    val isImportant: Boolean = false,
    val isNew: Boolean = true
)