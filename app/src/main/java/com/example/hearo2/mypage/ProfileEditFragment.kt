package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentProfileEditBinding
import com.example.hearo2.network.request.MemberUpdateRequest

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by activityViewModels {
        MyPageViewModelFactory(requireContext())
    }

    private var selectedUserType: String? = null

    // 🔥 키워드 버튼 목록 (한국어 그대로 처리)
    private val keywordViews by lazy {
        listOf(
            binding.keyword1,
            binding.keyword2,
            binding.keyword3,
            binding.keyword4,
            binding.keyword5,
            binding.keyword6,
            binding.keyword7,
            binding.keyword8
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 🔙 뒤로가기
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 탭 UI
        showBasicInfo()
        binding.tabBasic.setOnClickListener { showBasicInfo() }
        binding.tabPrivacy.setOnClickListener { showPrivacySettings() }
        binding.tabAlarm.setOnClickListener { showAlarmSettings() }

        // 성별 선택
        setupUserTypeSelector()

        // 키워드 선택 UI
        setupKeywordSelector()

        // 서버 데이터 UI 반영
        observeUserInfo()

        // 저장 버튼
        binding.btnSave.setOnClickListener {
            updateMemberInfo()
        }
    }

    // -----------------------------------------
    // ⭐ 서버 데이터 화면에 반영
    // -----------------------------------------
    private fun observeUserInfo() {
        viewModel.userInfo.observe(viewLifecycleOwner) { info ->

            binding.etName.setText(info.nickname)
            binding.etBirth.setText(info.birthday)

            applyUserType(info.gender)

            // 🔥 이미 저장된 한국어 키워드를 UI에 그대로 매핑
            applySavedKeywords(info.interestKeywords)
        }
    }

    // -----------------------------------------
    // ⭐ 성별 선택
    // -----------------------------------------
    private fun setupUserTypeSelector() {
        val types = listOf(binding.userType1, binding.userType2, binding.userType3, binding.userType4)

        types.forEach { view ->
            view.setOnClickListener {
                selectedUserType = view.text.toString()
                updateUserTypeUI(types, view)
            }
        }
    }

    private fun updateUserTypeUI(all: List<TextView>, selected: TextView) {
        all.forEach {
            it.setBackgroundResource(R.drawable.user_type_unselected)
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_600))
        }
        selected.setBackgroundResource(R.drawable.user_type_selected)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
    }

    private fun applyUserType(gender: String) {
        val types = listOf(binding.userType1, binding.userType2, binding.userType3, binding.userType4)
        types.find { it.text.toString() == gender }?.let {
            updateUserTypeUI(types, it)
            selectedUserType = gender
        }
    }

    // -----------------------------------------
    // ⭐ 키워드 선택 UI
    // -----------------------------------------
    private fun setupKeywordSelector() {
        keywordViews.forEach { view ->
            view.setOnClickListener {
                view.isSelected = !view.isSelected
                updateKeywordUI(view)
            }
        }
    }

    private fun updateKeywordUI(view: TextView) {
        if (view.isSelected) {
            view.setBackgroundResource(R.drawable.keyword_selected)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            view.setBackgroundResource(R.drawable.keyword_unselected)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
        }
    }

    private fun applySavedKeywords(keywords: List<String>) {
        keywordViews.forEach { view ->
            view.isSelected = keywords.contains(view.text.toString())
            updateKeywordUI(view)
        }
    }

    // ⭐ 선택된 한국어 키워드 리스트 반환 (ENUM 변환 절대 ❌)
    private fun getSelectedKeywords(): List<String> {
        return keywordViews
            .filter { it.isSelected }
            .map { it.text.toString() }  // 🔥 한국어 문자열 그대로
    }

    // -----------------------------------------
    // ⭐ 회원 정보 수정 API 호출
    // -----------------------------------------
    private fun updateMemberInfo() {

        // 🔥 기존 gender 가져오기
        val currentGender = viewModel.userInfo.value?.gender

        // 🔥 성별 선택 안 했으면 기존 gender 유지
        val finalGender = selectedUserType ?: currentGender ?: ""

        val request = MemberUpdateRequest(
            nickname = binding.etName.text.toString(),
            gender = finalGender,
            birthday = binding.etBirth.text.toString(),
            interestKeywords = getSelectedKeywords()
        )

        viewModel.updateMyInfo(request)

        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "수정 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // -----------------------------------------
    // 탭 UI
    // -----------------------------------------
    private fun selectTab(selected: TextView, others: List<TextView>) {
        selected.setBackgroundResource(R.drawable.keyword_selected)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        others.forEach {
            it.setBackgroundResource(R.drawable.keyword_unselected)
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
        }
    }

    private fun showBasicInfo() {
        selectTab(binding.tabBasic, listOf(binding.tabPrivacy, binding.tabAlarm))
        binding.boxBasicInfo.visibility = View.VISIBLE
        binding.boxPrivacy.visibility = View.GONE
        binding.boxAlarm.visibility = View.GONE
    }

    private fun showPrivacySettings() {
        selectTab(binding.tabPrivacy, listOf(binding.tabBasic, binding.tabAlarm))
        binding.boxBasicInfo.visibility = View.GONE
        binding.boxPrivacy.visibility = View.VISIBLE
        binding.boxAlarm.visibility = View.GONE
    }

    private fun showAlarmSettings() {
        selectTab(binding.tabAlarm, listOf(binding.tabBasic, binding.tabPrivacy))
        binding.boxBasicInfo.visibility = View.GONE
        binding.boxPrivacy.visibility = View.GONE
        binding.boxAlarm.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
