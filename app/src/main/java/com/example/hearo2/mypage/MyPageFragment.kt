package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // -------------------------------------
        // ✔ 프로필 수정
        // -------------------------------------
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_myPage_to_profileEdit)
        }

        // -------------------------------------
        // ✔ 내 활동 전체 보기
        // -------------------------------------
        binding.menuMyActivity.setOnClickListener {
            findNavController().navigate(R.id.action_myPage_to_myActivity)
        }

        // -------------------------------------
        // ✔ 알림 설정
        // -------------------------------------


        // -------------------------------------
        // ✔ 다크 모드 (추후 구현)
        // -------------------------------------
        binding.menuDarkmode.setOnClickListener {
            // TODO: 다크 모드 전환 기능 추가 예정
        }

        // -------------------------------------
        // ✔ 공지사항
        // -------------------------------------

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
