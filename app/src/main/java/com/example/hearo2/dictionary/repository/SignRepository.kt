package com.example.hearo2.dictionary.repository

import android.content.Context
import com.example.hearo2.network.api.RetrofitClient
import com.example.hearo2.network.api.SignApi

class SignRepository(context: Context) {

    private val api = RetrofitClient.getInstance(context).create(SignApi::class.java)

    // -------------------------
    // ⭐ 기존 기능
    // -------------------------

    suspend fun getSigns(page: Int, size: Int) =
        api.getSigns(page, size)

    suspend fun searchSigns(keyword: String, page: Int, rows: Int) =
        api.searchSigns(keyword, page, rows)

    suspend fun getSignDetail(id: Int) =
        api.getSignDetail(id)


    // -------------------------
    // ⭐ 즐겨찾기 기능 추가
    // -------------------------

    // 즐겨찾기 추가
    suspend fun addFavorite(id: Int) =
        api.addFavorite(id)

    // 즐겨찾기 삭제
    suspend fun removeFavorite(id: Int) =
        api.removeFavorite(id)

    // 즐겨찾기 목록 조회  ←🔥 이름 반드시 이걸로!
    suspend fun getFavoriteSigns() =
        api.getFavoriteSigns()
}
