package com.example.hearo2.network.api

import com.example.hearo2.custominfo.model.JobDetailResponse
import com.example.hearo2.custominfo.model.JobListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface JobApiService {

    // 📌 ① 실시간 구인 정보 리스트
    @GET("/api/v1/jobs/external")
    suspend fun getJobList(
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 100
    ): JobListResponse


    // 📌 ② 필터 검색 APIS
    @GET("/api/v1/jobs/external/search")
    suspend fun searchJobs(
        @QueryMap filters: Map<String, String>
    ): JobListResponse


    // 📌 ③ 상세 조회 API (rno 기준)
    @GET("/api/v1/jobs/external/{rno}")
    suspend fun getJobDetail(
        @Path("rno") rno: String
    ): JobDetailResponse
}