package com.example.hearo2.custominfo.model

data class JobDetailResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: JobDetailData
)

data class JobDetailData(
    val rno: String,
    val rnum: String,
    val jobNm: String,
    val busplaName: String,
    val compAddr: String,
    val cntctNo: String?,
    val empType: String,
    val enterType: String,
    val termDate: String?,
    val salary: String?,
    val salaryType: String,
    val reqCareer: String?,
    val reqEduc: String?,

    val regganName: String?,
    val offerregDt: String?,
    val regDt: String?,

    // 아래 필드는 상세에만 존재 (필요하면 사용)
    val envBothHands: String?,
    val envEyesight: String?,
    val envHandwork: String?,
    val envLiftPower: String?,
    val envLstnTalk: String?,
    val envStndWalk: String?
)
