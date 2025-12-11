package com.example.hearo2.network.api

import com.example.hearo2.network.request.MemberUpdateRequest
import com.example.hearo2.network.response.MemberInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface MemberApi {

    // ⭐ 내 정보 조회
    @GET("/api/v1/members/me")
    suspend fun getMyInfo(): Response<MemberInfoResponse>

    // ⭐ 내 정보 수정
    @PUT("/api/v1/members/me")
    suspend fun updateMyInfo(
        @Body request: MemberUpdateRequest
    ): Response<MemberInfoResponse>
}
