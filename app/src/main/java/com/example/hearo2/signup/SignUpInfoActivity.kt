package com.example.hearo2.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.databinding.ActivitySignupInfoBinding

class SignUpInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {

            val nickname = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val pwd = binding.etPassword.text.toString()
            val pwdCheck = binding.etPasswordCheck.text.toString()

            if (!validate(nickname, email, pwd, pwdCheck)) return@setOnClickListener

            val user = UserSignUpData(
                nickname = nickname,
                email = email,
                password = pwd
            )

            Log.d("SignUpInfo", "Sending userData = $user")

            val intent = Intent(this, SignUpTermsActivity::class.java)
            intent.putExtra("userData", user)

            startActivity(intent)
        }
    }

    private fun validate(nickname: String, email: String, pwd: String, pwdCheck: String): Boolean {
        if (nickname.isEmpty()) return toast("닉네임을 입력해주세요").let { false }
        if (email.isEmpty()) return toast("이메일을 입력해주세요").let { false }
        if (pwd.isEmpty()) return toast("비밀번호를 입력해주세요").let { false }
        if (pwd != pwdCheck) return toast("비밀번호가 일치하지 않습니다").let { false }
        if (pwd.length < 8) return toast("비밀번호는 8자 이상 필요합니다").let { false }
        return true
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
