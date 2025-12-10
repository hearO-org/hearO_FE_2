package com.example.hearo2.sound

/**
 * --------------- 서버 전체 응답 ----------------
 */
data class SoundDetectResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: SoundResultModel?
)

/**
 * --------------- AI 분석 결과만 뽑은 데이터 ----------------
 */
data class SoundResultModel(
    val label: String,
    val confidence: Double,
    val alert: Boolean,
    val probs: Map<String, Double>?
)

data class SoundHistoryResponse(
    val success: Boolean,
    val data: SoundHistoryData
)

data class SoundHistoryData(
    val content: List<SoundHistoryItem>
)

data class SoundHistoryItem(
    val id: Int,
    val label: String?,
    val alert: Boolean,
    val confidence: Double,
    val detectedAt: String?
)


