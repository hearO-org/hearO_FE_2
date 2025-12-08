package com.example.hearo2.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.databinding.ActivitySignupTermsBinding
import com.example.hearo2.network.api.AuthService
import com.example.hearo2.network.RetrofitClient
import com.example.hearo2.network.request.SignUpRequest
import com.example.hearo2.network.response.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpTermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupTermsBinding
    private var userData: UserSignUpData? = null
    private val authService by lazy {
        RetrofitClient.getInstance(this).create(AuthService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userData") as? UserSignUpData

        if (userData == null) {
            toast("회원 정보가 전달되지 않았습니다.")
            finish()
            return
        }

        initListeners()
    }

    private fun initListeners() {

        // 전체동의 → 나머지 체크박스 자동 체크
        binding.chkAll.setOnCheckedChangeListener { _, isChecked ->
            binding.chkTerms.isChecked = isChecked
            binding.chkPrivacy.isChecked = isChecked
        }

        binding.btnNext.setOnClickListener {

            if (!binding.chkTerms.isChecked || !binding.chkPrivacy.isChecked) {
                toast("필수 약관에 동의해주세요.")
                return@setOnClickListener
            }

            val user = userData!!
            val request = SignUpRequest(
                email = user.email,
                password = user.password,
                nickname = user.nickname
            )

            signUp(request, user)
        }
    }

    private fun signUp(request: SignUpRequest, user: UserSignUpData) {

        authService.signup(request).enqueue(object : Callback<SignUpResponse> {

            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                if (response.isSuccessful) {
                    toast("회원가입이 완료되었습니다.")
                    val intent = Intent(this@SignUpTermsActivity, SignUpCompleteActivity::class.java)
                    intent.putExtra("userData", user)
                    startActivity(intent)
                    finish()
                } else {
                    toast("회원가입 실패 (${response.code()})")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                toast("서버 통신 오류: ${t.message}")
            }
        })
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
