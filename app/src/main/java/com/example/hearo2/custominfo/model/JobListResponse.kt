package com.example.hearo2.custominfo.model

data class JobListResponse(
    val status: Int,
    val success: Boolean,
    val code: String,
    val message: String,
    val data: JobListData
)

data class JobListData(
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int,
    val items: List<JobItem>
)
