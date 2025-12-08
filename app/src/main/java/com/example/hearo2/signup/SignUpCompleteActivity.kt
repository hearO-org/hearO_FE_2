package com.example.hearo2.signup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.databinding.ActivitySignupCompleteBinding
import com.example.hearo2.login.LoginEmailActivity

class SignUpCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupCompleteBinding
    private var userData: UserSignUpData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userData") as? UserSignUpData
        binding.tvWelcomeMessage.text = "${userData?.nickname}님,\n환영합니다!"

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginEmailActivity::class.java))
            finish()
        }, 1500)
    }
}
