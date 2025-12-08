package com.example.hearo2.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hearo2.databinding.FragmentReplyThreadBinding

class ReplyThreadFragment : Fragment() {

    private var _binding: FragmentReplyThreadBinding? = null
    private val binding get() = _binding!!
    private var commentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentId = arguments?.getInt("commentId") ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReplyThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val comment = CommentRepository.comments.find { it.id == commentId }

        binding.tvReplyContent.text = comment?.comment ?: "삭제된 댓글입니다."
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
