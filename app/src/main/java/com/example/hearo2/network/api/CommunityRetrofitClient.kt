package com.example.hearo2.network.api

import com.example.hearo2.AppGlobals
import com.example.hearo2.auth.AuthPrefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CommunityRetrofitClient {

    private const val BASE_URL = "https://hearo.my/"

    private val authInterceptor = Interceptor { chain ->
        val token = AuthPrefs.getToken(AppGlobals.context)

        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val communityApi: CommunityApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommunityApiService::class.java)
    }
}
