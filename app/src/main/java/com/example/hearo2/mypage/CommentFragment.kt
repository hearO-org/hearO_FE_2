//CommentFragment
package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.databinding.FragmentCommentBinding
import com.example.hearo2.mypage.adapter.CommentAdapter
import com.example.hearo2.mypage.model.Comment

class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)

        val dummy = listOf(
            Comment(
                id = 1,
                category = "커뮤니티",
                content = "새로운 보청기 추천 부탁드려요",
                time = "2시간 전",
                reply = "저도 비슷한 경험 있어서 댓글 남겨요~"
            ),
            Comment(
                id = 2,
                category = "수어 정보",
                content = "수어 학습 앱 추천해주세요",
                time = "1일 전",
                reply = null
            )
        )

        binding.recyclerComment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CommentAdapter(dummy)
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
