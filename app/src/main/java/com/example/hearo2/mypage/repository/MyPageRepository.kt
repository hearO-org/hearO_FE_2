package com.example.hearo2.mypage.repository

import android.content.Context
import com.example.hearo2.network.api.MemberApi
import com.example.hearo2.network.api.RetrofitClient

class MyPageRepository(context: Context) {

    private val api = RetrofitClient.getInstance(context).create(MemberApi::class.java)

    suspend fun getMyInfo() = api.getMyInfo()
}
