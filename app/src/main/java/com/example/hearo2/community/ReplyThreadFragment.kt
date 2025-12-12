package com.example.hearo2.community

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.community.repository.PostRepository
import com.example.hearo2.databinding.FragmentReplyThreadBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReplyThreadFragment : Fragment() {

    private lateinit var binding: FragmentReplyThreadBinding
    private lateinit var replyAdapter: CommentAdapter

    private val repository = PostRepository()

    private var postId: Int = 0
    private var commentId: Int = 0

    private val replyList = mutableListOf<CommentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            postId = it.getInt("postId")
            commentId = it.getInt("commentId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReplyThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupReplyInput()
        loadReplies()
    }

    // -------------------------------------------------------------
    // RecyclerView
    // -------------------------------------------------------------
    private fun setupRecycler() {
        replyAdapter = CommentAdapter(
            onEdit = { editReply(it) },
            onDelete = { deleteReply(it) },
            onReply = { /* 대댓글의 대댓글 금지 */ },
            onLike = { toggleLikeReply(it) },
            onShowReplies = { /* 이미 대댓글 화면 */ }
        )

        binding.recyclerReplies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReplies.adapter = replyAdapter
    }

    // -------------------------------------------------------------
    // 대댓글 목록 조회 (서버 기준)
    // -------------------------------------------------------------
    private fun loadReplies() {
        lifecycleScope.launch {
            try {
                val response = repository.loadReplies(
                    postId = postId.toLong(),
                    parentCommentId = commentId.toLong()
                )

                val serverData = response.data.data

                // ⭐ 서버가 비정상 응답이면 UI 유지 (중요!)
                if (serverData == null || serverData.content.isNullOrEmpty()) {
                    return@launch
                }

                replyList.clear()

                val items = serverData.content.map { dto ->
                    CommentModel(
                        id = dto.id.toInt(),
                        writer = dto.authorNickname,
                        content = dto.content,
                        time = dto.createdAt,
                        parentId = dto.parentId ?: 0L,
                        likeCount = dto.likeCount,
                        isLiked = dto.liked
                    )
                }

                replyList.addAll(items)
                binding.replyCount.text = "답글 ${replyList.size}개"
                replyAdapter.submitList(replyList.toList())

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "대댓글 조회 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // -------------------------------------------------------------
    // 🔥 대댓글 작성 (Optimistic Update + 서버 동기화)
    // -------------------------------------------------------------
    private fun setupReplyInput() {
        binding.btnSendReply.setOnClickListener {

            val text = binding.etReply.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ⭐ 1. 임시 댓글 즉시 추가
            val tempReply = CommentModel(
                id = -System.currentTimeMillis().toInt(), // 임시 ID
                writer = "나",
                content = text,
                time = now(),
                parentId = commentId.toLong(),
                likeCount = 0,
                isLiked = false
            )

            replyList.add(0, tempReply)
            replyAdapter.submitList(replyList.toList())
            binding.replyCount.text = "답글 ${replyList.size}개"
            binding.etReply.text.clear()

            // ⭐ 2. 서버 요청
            lifecycleScope.launch {
                try {
                    repository.createReply(
                        postId = postId.toLong(),
                        parentCommentId = commentId.toLong(),
                        content = text
                    )

                    // ⭐ 3. 서버 반영 대기 후 재조회 (중요!)
                    delay(300)
                    loadReplies()

                } catch (e: Exception) {
                    // 실패 시 롤백
                    replyList.remove(tempReply)
                    replyAdapter.submitList(replyList.toList())
                    binding.replyCount.text = "답글 ${replyList.size}개"
                    Toast.makeText(requireContext(), "대댓글 작성 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // -------------------------------------------------------------
    // 수정
    // -------------------------------------------------------------
    private fun editReply(reply: CommentModel) {

        // 임시 댓글은 수정 불가
        if (reply.id < 0) {
            Toast.makeText(
                requireContext(),
                "업로드 중인 댓글은 수정할 수 없습니다",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val editText = EditText(requireContext())
        editText.setText(reply.content)

        AlertDialog.Builder(requireContext())
            .setTitle("대댓글 수정")
            .setView(editText)
            .setPositiveButton("수정") { _, _ ->
                lifecycleScope.launch {
                    try {
                        repository.updateReply(
                            reply.id.toLong(),
                            editText.text.toString()
                        )
                        loadReplies()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // -------------------------------------------------------------
    // 삭제
    // -------------------------------------------------------------
    private fun deleteReply(reply: CommentModel) {

        // 임시 댓글은 로컬 삭제만
        if (reply.id < 0) {
            replyList.remove(reply)
            replyAdapter.submitList(replyList.toList())
            binding.replyCount.text = "답글 ${replyList.size}개"
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("대댓글 삭제")
            .setPositiveButton("삭제") { _, _ ->
                lifecycleScope.launch {
                    try {
                        repository.deleteReply(reply.id.toLong())
                        loadReplies()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "삭제 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // -------------------------------------------------------------
    // 좋아요
    // -------------------------------------------------------------
    private fun toggleLikeReply(reply: CommentModel) {

        // 임시 댓글은 서버 요청 X
        if (reply.id < 0) return

        lifecycleScope.launch {
            try {
                if (!reply.isLiked) {
                    repository.likeReply(reply.id.toLong())
                } else {
                    repository.cancelLikeReply(reply.id.toLong())
                }

                // ⭐ 반드시 재조회
                loadReplies()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "좋아요 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun now(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).format(Date())
}
