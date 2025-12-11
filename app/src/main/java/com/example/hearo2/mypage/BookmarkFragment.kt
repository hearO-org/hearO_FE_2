package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.databinding.FragmentBookmarkBinding
import com.example.hearo2.mypage.adapter.BookmarkAdapter
import com.example.hearo2.mypage.model.PostBookmark

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        val dummy = listOf(
            PostBookmark(1, "지원 정보", "청각장애인 고용지원", "2일 전", 23),
            PostBookmark(2, "교육 정보", "수어 통역사 시험 안내", "5일 전", 45)
        )

        binding.recyclerBookmark.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = BookmarkAdapter(dummy)
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
