package com.example.hearo2.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentMypageBinding
import com.example.hearo2.login.LoginActivity

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    // ⭐ ViewModelFactory 적용
    private val viewModel: MyPageViewModel by viewModels {
        MyPageViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ⭐ 내 정보 로드
        viewModel.loadMyInfo()

        // ⭐ UI 적용
        viewModel.userInfo.observe(viewLifecycleOwner) { info ->
            binding.tvNickname.text = info.nickname
            binding.tvEmail.text = info.email
        }

        // ⭐ 프로필 수정으로 이동
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_myPage_to_profileEdit)
        }

        // ⭐ 내 활동
        binding.menuMyActivity.setOnClickListener {
            findNavController().navigate(R.id.action_myPage_to_myActivity)
        }

        // ⭐ 로그아웃 요청
        binding.menuLogout.setOnClickListener {
            viewModel.logout()
        }

        // ⭐ 로그아웃 결과 UI 처리
        viewModel.logoutState.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "로그아웃 완료!", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "로그아웃 실패…", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
