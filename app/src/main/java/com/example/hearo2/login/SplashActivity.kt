package com.example.hearo2.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.hearo2.MainActivity
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.signup.SignUpProfileActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            navigate()
        }, 1200)
    }

    private fun navigate() {

        val token = AuthPrefs.getToken(this)
        val onboarded = AuthPrefs.isOnboarded(this)

        when {
            token.isNullOrEmpty() -> {
                // 로그인 한 적 없음
                startActivity(Intent(this, LoginActivity::class.java))
            }
            !onboarded -> {
                // 로그인했지만 온보딩 미완료
                startActivity(Intent(this, SignUpProfileActivity::class.java))
            }
            else -> {
                // 로그인 + 온보딩 완료
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        finish()
    }
}
