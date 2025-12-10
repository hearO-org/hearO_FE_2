package com.example.hearo2.sound

import android.util.Log
import com.example.hearo2.AppGlobals
import com.example.hearo2.auth.AuthPrefs
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File

object SoundRepository {

    private const val BASE_URL = "https://hearo.my/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: SoundApi by lazy {
        retrofit.create(SoundApi::class.java)
    }

    private val historyApi: SoundHistoryApi by lazy {
        retrofit.create(SoundHistoryApi::class.java)
    }

    // ✅ 실시간 소리 분석
    suspend fun detectSound(file: File): SoundResultModel? {
        return try {
            val token = AuthPrefs.getToken(AppGlobals.context) ?: ""
            val requestFile = file.asRequestBody("audio/wav".toMediaTypeOrNull())
            val multipartFile = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestFile
            )

            val response = api.detectSound("Bearer $token", multipartFile)

            if (response.success) response.data else null
        } catch (e: Exception) {
            Log.e("SoundRepository", "detectSound 오류", e)
            null
        }
    }

    // ✅ ⭐ 과거 인식 기록 조회 (이게 핵심이었음)
    suspend fun getMySoundHistory(): List<SoundHistoryItem> {
        return try {
            val token = AuthPrefs.getToken(AppGlobals.context) ?: ""
            val response = historyApi.getMySoundLogs("Bearer $token")
            response.data.content
        } catch (e: Exception) {
            Log.e("SoundRepository", "getMySoundHistory 오류", e)
            emptyList()
        }
    }
}

/* ---------------- API ---------------- */

interface SoundApi {
    @Multipart
    @POST("api/v1/sound/detect")
    suspend fun detectSound(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): SoundDetectResponse
}

interface SoundHistoryApi {
    @GET("api/v1/sound/logs/me")
    suspend fun getMySoundLogs(
        @Header("Authorization") token: String
    ): SoundHistoryResponse
}
