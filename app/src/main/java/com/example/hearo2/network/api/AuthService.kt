package com.example.hearo2.network.api

import com.example.hearo2.network.request.LoginRequest
import com.example.hearo2.network.request.SignUpRequest
import com.example.hearo2.network.request.OnboardingRequest
import com.example.hearo2.network.response.LoginResponse
import com.example.hearo2.network.response.SignUpResponse
import com.example.hearo2.network.response.OnboardingResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/v1/auth/login")
    fun login(@Body body: LoginRequest): Call<LoginResponse>


    @POST("api/v1/auth/signup")
    fun signup(@Body body: SignUpRequest): Call<SignUpResponse>

    @POST("api/v1/members/onboarding")
    fun onboarding(@Body body: OnboardingRequest): Call<OnboardingResponse>


}
