package com.example.hearo2.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.MainActivity
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.signup.SignUpProfileActivity

class LoginRedirectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent?.data
        val token = uri?.getQueryParameter("token") ?: ""
        val onboardingRequired = uri?.getQueryParameter("onboardingRequired") == "true"

        AuthPrefs.saveToken(this, token, null)

        val already = AuthPrefs.isOnboarded(this)

        if (onboardingRequired && !already) {
            startActivity(Intent(this, SignUpProfileActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
