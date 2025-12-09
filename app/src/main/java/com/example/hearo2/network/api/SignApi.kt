package com.example.hearo2.network.api

import com.example.hearo2.dictionary.model.SignDetailResponse
import com.example.hearo2.dictionary.model.SignListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SignApi {

    @GET("/api/v1/signs")
    suspend fun getSigns(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): SignListResponse

    @GET("/api/v1/signs/search")
    suspend fun searchSigns(
        @Query("keyword") keyword: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int
    ): SignListResponse

    @GET("/api/v1/signs/{id}")
    suspend fun getSignDetail(
        @Path("id") id: Int
    ): SignDetailResponse
}
