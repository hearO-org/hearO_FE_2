package com.example.hearo2.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentMypageBinding
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.hearo2.login.LoginActivity

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MyPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ⭐ ViewModel 초기화
        viewModel = MyPageViewModel(requireContext())

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
        // ✔ 로그아웃
        // -------------------------------------
        binding.menuLogout.setOnClickListener {
            viewModel.logout()
        }

        // ⭐ 로그아웃 결과 처리
        viewModel.logoutState.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "로그아웃 완료!", Toast.LENGTH_SHORT).show()

                // ⭐ LoginActivity로 이동 + 모든 백스택 제거
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
