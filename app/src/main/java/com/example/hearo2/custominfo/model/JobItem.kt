package com.example.hearo2.custominfo.model

data class JobItem(
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

    // UI용 필드 (찜 기능 등)
    var isLiked: Boolean = false
)
