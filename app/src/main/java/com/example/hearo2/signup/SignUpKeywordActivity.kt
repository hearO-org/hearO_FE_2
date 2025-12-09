package com.example.hearo2.signup

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.MainActivity
import com.example.hearo2.R
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.databinding.ActivitySignupKeywordsBinding
import com.example.hearo2.network.api.RetrofitClient
import com.example.hearo2.network.api.AuthService
import com.example.hearo2.network.request.OnboardingRequest
import com.example.hearo2.network.response.OnboardingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpKeywordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupKeywordsBinding
    private val authService by lazy {
        RetrofitClient.getInstance(this).create(AuthService::class.java)
    }

    private val selectedKeywords = mutableSetOf<String>()   // 선택된 한국어 키워드
    private val profileKeywords = mutableListOf<String>()   // 이전 화면에서 가져온 한국어 키워드

    // 🔥 한국어 → 서버 ENUM 매핑 테이블
    private val keywordMap = mapOf(
        "취업" to "EMPLOYMENT",
        "복지" to "WELFARE",
        "교육" to "EDUCATION",
        "상담" to "COUNSELING",
        "보조공학·기술" to "ACCESSIBILITY_TECH",
        "자막·영상콘텐츠" to "SUBTITLE_MEDIA",
        "뮤지컬" to "CULTURE_MUSICAL",
        "공연·콘서트" to "CULTURE_CONCERT",
        "영화·상영회" to "CULTURE_MOVIE",
        "모임·커뮤니티" to "COMMUNITY_MEETUP",
        "생활정보" to "DAILY_LIFE",
        "정책·권익" to "POLICY_RIGHTS",
        "스포츠·운동" to "SPORTS",
        "취미·여가" to "HOBBY"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupKeywordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 이전 화면에서 받은 한국어 키워드 저장
        profileKeywords.addAll(intent.getStringArrayListExtra("profileKeywords") ?: emptyList())

        setupKeywordButtons()
        setupFinishButton()
    }

    // -------------------------------------------------------------
    // 🔸 키워드 버튼 클릭 → 토글 선택
    // -------------------------------------------------------------
    private fun setupKeywordButtons() {
        val btns = listOf(
            binding.btnKeyword1,
            binding.btnKeyword2,
            binding.btnKeyword3,
            binding.btnKeyword4,
            binding.btnKeyword5,
            binding.btnKeyword6,
            binding.btnKeyword7,
            binding.btnKeyword8
        )

        btns.forEach { btn ->
            btn.setOnClickListener { toggleKeyword(btn) }
        }
    }

    private fun toggleKeyword(btn: TextView) {
        val keyword = btn.text.toString()

        if (selectedKeywords.contains(keyword)) {
            selectedKeywords.remove(keyword)
            btn.setBackgroundResource(R.drawable.keyword_unselected)
            btn.setTextColor(getColor(R.color.gray_600))
        } else {
            selectedKeywords.add(keyword)
            btn.setBackgroundResource(R.drawable.keyword_selected)
            btn.setTextColor(getColor(android.R.color.white))
        }
    }

    // -------------------------------------------------------------
    // 🔸 완료 버튼 클릭 → 온보딩 제출
    // -------------------------------------------------------------
    private fun setupFinishButton() {
        binding.btnFinish.setOnClickListener {
            submitOnboarding()
        }
    }

    // -------------------------------------------------------------
    // 🔸 온보딩 API 호출 (ENUM 변환 포함)
    // -------------------------------------------------------------
    private fun submitOnboarding() {

        val nickname = intent.getStringExtra("nickname") ?: ""
        val gender = intent.getStringExtra("gender") ?: ""
        val birthday = intent.getStringExtra("birthday") ?: ""

        // 🔹 전체 키워드 (한국어)
        val allKoreanKeywords = profileKeywords + selectedKeywords.toList()

        if (allKoreanKeywords.isEmpty()) {
            toast("관심 키워드를 1개 이상 선택해주세요")
            return
        }

        // 🔥 한국어 → ENUM 변환 (서버로 보내는 값)
        val enumKeywords = allKoreanKeywords.mapNotNull { keywordMap[it] }

        val request = OnboardingRequest(
            nickname = nickname,
            gender = gender,
            birthday = birthday,
            interestKeywords = enumKeywords  // 🔥 ENUM 값 사용!
        )

        authService.onboarding(request).enqueue(object : Callback<OnboardingResponse> {
            override fun onResponse(
                call: Call<OnboardingResponse>,
                response: Response<OnboardingResponse>
            ) {
                if (!response.isSuccessful) {
                    toast("온보딩 실패: ${response.code()}")
                    return
                }

                // 저장
                AuthPrefs.saveOnboarded(this@SignUpKeywordActivity)

                toast("온보딩이 완료되었습니다!")

                startActivity(Intent(this@SignUpKeywordActivity, MainActivity::class.java))
                finish()
            }

            override fun onFailure(call: Call<OnboardingResponse>, t: Throwable) {
                toast("서버 오류: ${t.message}")
            }
        })
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
