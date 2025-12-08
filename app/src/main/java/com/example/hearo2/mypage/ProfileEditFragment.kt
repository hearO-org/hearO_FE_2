package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.chip.Chip
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentProfileEditBinding

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private var selectedUserType: String? = null
    private val interestTags = mutableListOf<String>()  // ChipGroup 태그 목록 저장

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 기본탭 활성화
        showBasicInfo()

        // 탭 클릭 이벤트
        binding.tabBasic.setOnClickListener { showBasicInfo() }
        binding.tabPrivacy.setOnClickListener { showPrivacySettings() }
        binding.tabAlarm.setOnClickListener { showAlarmSettings() }

        // 사용자 유형 선택 기능
        setupUserTypeSelector()

        // 태그 추가 버튼
        binding.btnAddTag.setOnClickListener {
            val input = binding.etAddTag.text.toString().trim()
            if (input.isNotEmpty()) {
                addTag(input)
                binding.etAddTag.text.clear()
            }
        }
    }

    // -----------------------------------------
    // 사용자 유형 선택 (4개 버튼)
    // -----------------------------------------
    private fun setupUserTypeSelector() {
        val typeList = listOf(
            binding.userType1,
            binding.userType2,
            binding.userType3,
            binding.userType4
        )

        typeList.forEach { view ->
            view.setOnClickListener {
                selectedUserType = view.text.toString()
                updateUserTypeUI(typeList, view)
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

    // -----------------------------------------
    // 태그 추가 (ChipGroup)
    // -----------------------------------------
    private fun addTag(text: String) {
        if (interestTags.contains(text)) return
        interestTags.add(text)

        val chip = Chip(requireContext()).apply {
            this.text = text
            isCloseIconVisible = true

            // 디자인
            setChipBackgroundColorResource(R.color.tag_bg)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_700))
            setCloseIconTintResource(R.color.purple_700)

            // 삭제 기능
            setOnCloseIconClickListener {
                binding.tagGroup.removeView(this)
                interestTags.remove(text)
            }
        }

        binding.tagGroup.addView(chip)
    }

    // -----------------------------------------
    // 탭 UI 전환
    // -----------------------------------------
    private fun selectTab(selected: TextView, others: List<TextView>) {
        selected.setBackgroundResource(R.drawable.keyword_unselected)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))

        others.forEach {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
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
