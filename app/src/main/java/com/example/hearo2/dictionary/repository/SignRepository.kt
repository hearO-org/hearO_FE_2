package com.example.hearo2.dictionary.repository

import android.content.Context
import com.example.hearo2.network.api.RetrofitClient
import com.example.hearo2.network.api.SignApi

class SignRepository(context: Context) {

    private val api = RetrofitClient.getInstance(context).create(SignApi::class.java)

    suspend fun getSigns(page: Int, size: Int) =
        api.getSigns(page, size)

    suspend fun searchSigns(keyword: String, page: Int, rows: Int) =
        api.searchSigns(keyword, page, rows)

    suspend fun getSignDetail(id: Int) =
        api.getSignDetail(id)
}
