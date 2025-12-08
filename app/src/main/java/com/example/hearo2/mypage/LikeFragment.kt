//LikeFragment
package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.databinding.FragmentLikeBinding
import com.example.hearo2.mypage.adapter.PostAdapter
import com.example.hearo2.mypage.model.Post

class LikeFragment : Fragment() {

    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikeBinding.inflate(inflater, container, false)

        val dummy = listOf(
            Post(
                id = 1,
                category = "커뮤니티",
                title = "직장에서 수어 통역 요청하는 방법",
                content = "내용...",
                time = "3일 전",
                views = 445,
                likes = 18,
                comments = 9
            ),
            Post(
                id = 2,
                category = "문화 정보",
                title = "청각장애인 지원 프로그램 소개",
                content = "내용...",
                time = "1일 전",
                views = 364,
                likes = 12,
                comments = 4
            )
        )

        binding.recyclerLike.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = PostAdapter(dummy)
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
