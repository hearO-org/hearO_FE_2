//JobData
package com.example.hearo2.custominfo

data class JobData(
    val title: String,
    val company: String,
    val location: String,
    val condition: String,
    val pay: String,
    val type: String,        // 계약직 / 상용직
    var isLiked: Boolean = false
)
