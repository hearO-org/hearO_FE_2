//BookmarkFragment
package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.databinding.FragmentBookmarkBinding
import com.example.hearo2.mypage.adapter.PostAdapter
import com.example.hearo2.mypage.model.Post

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        val dummy = listOf(
            Post(
                id = 1,
                category = "지원 정보",
                title = "청각장애인 고용지원 신청 마감 안내",
                content = "지원 내용...",
                time = "2일 전",
                views = 23,
                likes = 10,
                comments = 5
            ),
            Post(
                id = 2,
                category = "교육 정보",
                title = "수어 통역사 시험 일정 안내",
                content = "시험 관련 정보...",
                time = "5일 전",
                views = 45,
                likes = 12,
                comments = 3
            )
        )

        binding.recyclerBookmark.apply {
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
