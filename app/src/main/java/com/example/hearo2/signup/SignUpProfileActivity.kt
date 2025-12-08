package com.example.hearo2.signup

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hearo2.R
import com.example.hearo2.databinding.ActivitySignupProfileBinding

class SignUpProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupProfileBinding

    private var selectedGender: String? = null    // "MALE" / "FEMALE"
    private val profileKeywords = mutableSetOf<String>() // 가족/보호자, 청각장애인, 사회복지사

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGenderButtons()
        setupBirthSpinners()
        setupProfileKeywordButtons()
        setupNextButton()
    }

    private fun setupGenderButtons() {
        val male = binding.btnMale
        val female = binding.btnFemale

        male.setOnClickListener {
            setGenderSelected(male, female)
            selectedGender = "MALE"
        }

        female.setOnClickListener {
            setGenderSelected(female, male)
            selectedGender = "FEMALE"
        }
    }

    private fun setGenderSelected(selected: TextView, other: TextView) {
        selected.setBackgroundResource(R.drawable.keyword_selected)
        selected.setTextColor(resources.getColor(android.R.color.white, null))

        other.setBackgroundResource(R.drawable.gender_unselected)
        other.setTextColor(resources.getColor(R.color.gray_600, null))
    }

    private fun setupBirthSpinners() {
        val years = (1950..2024).map { it.toString() }
        val months = (1..12).map { it.toString().padStart(2, '0') }
        val days = (1..31).map { it.toString().padStart(2, '0') }

        binding.spinnerYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerMonth.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerDay.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun setupProfileKeywordButtons() {
        setKeywordToggle(binding.keyword1)
        setKeywordToggle(binding.keyword2)
        setKeywordToggle(binding.keyword3)
    }

    private fun setKeywordToggle(view: TextView) {
        view.setOnClickListener {
            val text = view.text.toString()

            if (profileKeywords.contains(text)) {
                profileKeywords.remove(text)
                view.setBackgroundResource(R.drawable.keyword_unselected)
                view.setTextColor(resources.getColor(R.color.gray_600, null))
            } else {
                profileKeywords.add(text)
                view.setBackgroundResource(R.drawable.keyword_selected)
                view.setTextColor(resources.getColor(android.R.color.white, null))
            }
        }
    }

    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {

            val nickname = binding.etNickname.text.toString().trim()
            val gender = selectedGender
            val year = binding.spinnerYear.selectedItem?.toString()
            val month = binding.spinnerMonth.selectedItem?.toString()
            val day = binding.spinnerDay.selectedItem?.toString()

            if (nickname.isEmpty()) return@setOnClickListener toast("닉네임을 입력해주세요")
            if (gender == null) return@setOnClickListener toast("성별을 선택해주세요")
            if (year == null || month == null || day == null)
                return@setOnClickListener toast("생년월일을 선택해주세요")

            val birthday = "$year-$month-$day"

            val intent = Intent(this, SignUpKeywordActivity::class.java)
            intent.putExtra("nickname", nickname)
            intent.putExtra("gender", gender)
            intent.putExtra("birthday", birthday)
            intent.putStringArrayListExtra(
                "profileKeywords",
                ArrayList(profileKeywords.toList())
            )
            startActivity(intent)
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
