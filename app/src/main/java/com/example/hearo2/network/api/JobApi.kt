package com.example.hearo2.network.api

import android.content.Context

object JobApi {
    fun getService(context: Context): JobApiService {
        return RetrofitClient.getInstance(context).create(JobApiService::class.java)
    }
}
