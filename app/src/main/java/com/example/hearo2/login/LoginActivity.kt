package com.example.hearo2.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.databinding.ActivityLoginBinding
import com.example.hearo2.signup.SignUpInfoActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEmailLogin.setOnClickListener {
            startActivity(Intent(this, LoginEmailActivity::class.java))
        }

        binding.tvGoSignup.setOnClickListener {
            startActivity(Intent(this, SignUpInfoActivity::class.java))
        }

        binding.btnKakaoLogin.setOnClickListener {
            startKakaoLogin()
        }
    }

    private fun startKakaoLogin() {
        val kakaoUrl = "https://hearo.my/oauth2/authorization/kakao"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoUrl))
        startActivity(intent)
    }
}
