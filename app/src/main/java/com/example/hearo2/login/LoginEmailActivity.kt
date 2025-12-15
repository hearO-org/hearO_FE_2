package com.example.hearo2.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.MainActivity
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.databinding.ActivityLoginEmailBinding
import com.example.hearo2.network.api.RetrofitClient
import com.example.hearo2.network.api.AuthService
import com.example.hearo2.network.request.LoginRequest
import com.example.hearo2.network.response.LoginResponse
import com.example.hearo2.signup.SignUpProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginEmailBinding
    private val authService by lazy {
        RetrofitClient.getInstance(this).create(AuthService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDoLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val pw = binding.etPasswordLogin.text.toString()

            if (email.isEmpty() || pw.isEmpty()) {
                toast("이메일과 비밀번호를 입력하세요")
                return@setOnClickListener
            }

            login(email, pw)
        }
    }

    private fun login(email: String, pw: String) {

        val request = LoginRequest(email = email, password = pw)

        authService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (!response.isSuccessful || response.body() == null) {
                    toast("로그인 실패: ${response.code()}")
                    return
                }

                val body = response.body()?.data
                if (body == null) {
                    toast("서버 오류: 데이터 없음")
                    return
                }

                // ⭐ 온보딩 여부: 앱 내부 저장을 기준으로만 판단
                val alreadyOnboarded = AuthPrefs.isOnboarded(this@LoginEmailActivity)

                // ⭐ Access + Refresh 둘 다 저장하는 올바른 코드!!
                AuthPrefs.saveToken(
                    this@LoginEmailActivity,
                    body.access,
                    body.refresh
                )

                AuthPrefs.saveLoggedIn(this@LoginEmailActivity)

                AuthPrefs.clearOnboarded(this@LoginEmailActivity)

                if (!alreadyOnboarded) {
                    startActivity(Intent(this@LoginEmailActivity, SignUpProfileActivity::class.java))
                } else {
                    startActivity(Intent(this@LoginEmailActivity, MainActivity::class.java))
                }

                finish()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast("서버 통신 오류: ${t.message}")
            }
        })
    }

    private fun saveToken(token: String) {
        AuthPrefs.saveToken(this, token, null)
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
