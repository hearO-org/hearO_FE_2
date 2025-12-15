package com.example.hearo2.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.hearo2.MainActivity
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.signup.SignUpInfoActivity
import com.example.hearo2.signup.SignUpProfileActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            navigate()
        }, 1200)
    }

    private fun navigate() {

        val access = AuthPrefs.getToken(this)
        val refresh = AuthPrefs.getRefresh(this)
        val loggedIn = AuthPrefs.isLoggedIn(this)
        val onboarded = AuthPrefs.isOnboarded(this)

        // ================================================
        // 1️⃣ 로그인 정보 없음 → LoginActivity 이동
        // ================================================
        if (access.isNullOrEmpty() || refresh.isNullOrEmpty() || !loggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // ================================================
        // 2️⃣ 로그인은 했지만 온보딩 미완료 → 온보딩 시작
        // ================================================
        if (!onboarded) {
            startActivity(Intent(this, SignUpProfileActivity::class.java))
            finish()
            return
        }

        // ================================================
        // 3️⃣ 로그인 + 온보딩 완료 → MainActivity 이동
        // ================================================
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
