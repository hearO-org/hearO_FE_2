package com.example.hearo2.auth

import android.content.Context
import android.content.SharedPreferences

object AuthPrefs {

    private const val PREF_NAME = "hearo_auth"
    private const val KEY_TOKEN = "access_token"
    private const val KEY_REFRESH = "refresh_token"   // 추가
    private const val KEY_ONBOARDED = "onboarded"
    private const val KEY_LOGGED_IN = "logged_in"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // 토큰 저장 (기존 기능 유지 + refresh 저장 기능만 추가!)
    fun saveToken(context: Context, token: String?, refreshToken: String?) {
        prefs(context).edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_REFRESH, refreshToken)   // refreshToken 저장 추가
            apply()
        }
    }

    // AccessToken 가져오기 (기존 그대로)
    fun getToken(context: Context): String? {
        return prefs(context).getString(KEY_TOKEN, null)
    }

    // RefreshToken 가져오기 (로그아웃 API에서 필요)
    fun getRefresh(context: Context): String? {
        return prefs(context).getString(KEY_REFRESH, null)
    }

    // 온보딩 여부 저장
    fun saveOnboarded(context: Context) {
        prefs(context).edit().apply {
            putBoolean(KEY_ONBOARDED, true)
            apply()
        }
    }

    // 온보딩 여부 가져오기
    fun isOnboarded(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_ONBOARDED, false)
    }

    fun saveLoggedIn(context: Context) {
        prefs(context).edit().apply {
            putBoolean(KEY_LOGGED_IN, true)
            apply()
        }
    }

    // 로그인 여부 가져오기
    fun isLoggedIn(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_LOGGED_IN, false)
    }

    fun clearOnboarded(context: Context) {
        prefs(context).edit().apply {
            remove(KEY_ONBOARDED)
            apply()
        }
    }

    // 로그아웃 시 필요한 전체 토큰 삭제 기능 추가
    fun clearTokens(context: Context) {
        prefs(context).edit().apply {
            remove(KEY_TOKEN)
            remove(KEY_REFRESH)
            remove(KEY_LOGGED_IN)
            apply()
        }
    }
}