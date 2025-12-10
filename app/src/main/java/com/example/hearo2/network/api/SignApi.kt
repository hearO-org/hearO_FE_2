package com.example.hearo2.network.api

import com.example.hearo2.dictionary.model.BaseResponse
import com.example.hearo2.dictionary.model.SignDetailResponse
import com.example.hearo2.dictionary.model.SignListResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("/api/v1/signs/{id}/favorite")
    suspend fun addFavorite(
        @Path("id") id: Int
    ): BaseResponse

    // 즐겨찾기 삭제 (DELETE)
    @DELETE("/api/v1/signs/{id}/favorite")
    suspend fun removeFavorite(
        @Path("id") id: Int
    ): BaseResponse

    // 내 즐겨찾기 목록 조회 (GET)
    @GET("/api/v1/signs/favorites")
    suspend fun getFavoriteSigns(): SignListResponse
}
