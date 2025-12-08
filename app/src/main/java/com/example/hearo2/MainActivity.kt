package com.example.hearo2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.databinding.ActivityMainBinding
import com.example.hearo2.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ 1) 로그인 상태 확인
        if (!isLoggedIn()) {
            goToLogin()
            return
        }

        // ⭐ 2) 로그인 O → 메인 레이아웃 세팅
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ⭐ NavHostFragment 가져오기
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // ⭐ NavController 생성
        val navController = navHostFragment.navController

        // ⭐ BottomNavigationView와 NavController 연결
        binding.bottomNav.setupWithNavController(navController)

        // ⭐ 옵션: 재선택 시 프래그먼트 새로고침 방지
        binding.bottomNav.setOnItemReselectedListener {
            // 아무것도 하지 않음
        }
    }

    // =============================================================
    // 🔹 로그인 여부 확인
    // =============================================================
    private fun isLoggedIn(): Boolean {
        return AuthPrefs.isLoggedIn(this)
    }

    // =============================================================
    // 🔹 로그인 화면으로 이동
    // =============================================================
    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
