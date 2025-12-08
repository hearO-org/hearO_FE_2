package com.example.hearo2.auth

import android.content.Context
import android.content.SharedPreferences

object AuthPrefs {

    private const val PREF_NAME = "hearo_auth"
    private const val KEY_TOKEN = "access_token"
    private const val KEY_ONBOARDED = "onboarded"
    private const val KEY_LOGGED_IN = "logged_in"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ⭐ 토큰 저장
    fun saveToken(context: Context, token: String?, refreshToken: String?) {
        prefs(context).edit().apply {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    // ⭐ 토큰 가져오기 (여기가 없어서 오류 난 거야!!)
    fun getToken(context: Context): String? {
        return prefs(context).getString(KEY_TOKEN, null)
    }

    // ⭐ 온보딩 여부 저장
    fun saveOnboarded(context: Context) {
        prefs(context).edit().apply {
            putBoolean(KEY_ONBOARDED, true)
            apply()
        }
    }

    // ⭐ 온보딩 여부 가져오기
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
}
