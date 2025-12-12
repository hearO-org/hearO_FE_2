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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.community.repository.PostRepository
import com.example.hearo2.databinding.FragmentCommunityDetailBinding
import kotlinx.coroutines.launch

class CommunityDetailFragment : Fragment() {

    private lateinit var binding: FragmentCommunityDetailBinding
    private lateinit var commentAdapter: CommentAdapter

    private val repository = PostRepository()
    private val commentList = mutableListOf<CommentModel>()

    private var postId: Int = 0
    private var isScrapped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = arguments?.getInt("postId") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupCommentAdapter()
        setupCommentInput()
        setupScrapButton()
        setupPostMenu()

        loadPostDetail()
        loadComments()
    }

    // -------------------------
    // 게시글 상세
    // -------------------------
    private fun loadPostDetail() {
        lifecycleScope.launch {
            try {
                val res = repository.loadPostDetail(postId.toLong())
                val post = res.data

                binding.postTitle.text = post.title
                binding.postContent.text = post.content
                binding.postUserName.text = post.authorNickname

                isScrapped = post.scrapped
                updateScrapIcon(isScrapped)

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "게시글 로딩 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // -------------------------
    // 스크랩
    // -------------------------
    private fun setupScrapButton() {
        binding.btnScrap.setOnClickListener {
            val prev = isScrapped
            val next = !prev

            isScrapped = next
            updateScrapIcon(isScrapped)

            lifecycleScope.launch {
                try {
                    val res =
                        if (next) repository.scrap(postId.toLong())
                        else repository.cancelScrap(postId.toLong())

                    isScrapped = res.data.likedOrScrapped
                    updateScrapIcon(isScrapped)

                } catch (e: Exception) {
                    isScrapped = prev
                    updateScrapIcon(isScrapped)
                    Toast.makeText(requireContext(), "스크랩 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateScrapIcon(scrapped: Boolean) {
        binding.btnScrap.setImageResource(
            if (scrapped) R.drawable.ic_scrap_filled
            else R.drawable.ic_scrap_outline
        )
    }

    // -------------------------
    // 게시글 메뉴
    // -------------------------
    private fun setupPostMenu() {
        binding.postMenuButton.setOnClickListener {
            val popup = androidx.appcompat.widget.PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.menu_post_options, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        val action =
                            CommunityDetailFragmentDirections
                                .actionCommunityDetailFragmentToCommunityWriteFragment(
                                    postId = postId,
                                    isEditMode = true
                                )
                        findNavController().navigate(action)
                    }
                    R.id.menu_delete -> confirmDeletePost()
                }
                true
            }
            popup.show()
        }
    }

    private fun confirmDeletePost() {
        AlertDialog.Builder(requireContext())
            .setTitle("게시글 삭제")
            .setPositiveButton("삭제") { _, _ -> deletePost() }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun deletePost() {
        lifecycleScope.launch {
            try {
                repository.deletePost(postId.toLong())
                findNavController().popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "삭제 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // -------------------------
    // 댓글
    // -------------------------
    private fun setupCommentAdapter() {
        commentAdapter = CommentAdapter(
            onEdit = { editComment(it) },
            onDelete = { deleteComment(it) },
            onReply = { goToReplyThread(it) },
            onLike = { toggleLike(it) },
            onShowReplies = { goToReplyThread(it) }
        )

        binding.recyclerComment.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerComment.adapter = commentAdapter
    }

    private fun loadComments() {
        lifecycleScope.launch {
            try {
                val res = repository.loadComments(postId.toLong(), 0, 20)
                commentList.clear()

                res.data.content.forEach {
                    commentList.add(
                        CommentModel(
                            id = it.id.toInt(),
                            writer = it.authorNickname,
                            content = it.content,
                            time = it.createdAt,
                            parentId = it.parentId ?: 0,
                            likeCount = it.likeCount,
                            isLiked = it.liked
                        )
                    )
                }

                commentAdapter.submitList(commentList.toList())
                binding.tvReplyTitle.text = "댓글 ${commentList.size}개"

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "댓글 로딩 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCommentInput() {
        binding.btnSendComment.setOnClickListener {
            val text = binding.etComment.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            lifecycleScope.launch {
                try {
                    repository.createComment(postId.toLong(), text)
                    binding.etComment.text.clear()
                    loadComments()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "댓글 작성 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editComment(comment: CommentModel) {
        val et = EditText(requireContext())
        et.setText(comment.content)

        AlertDialog.Builder(requireContext())
            .setTitle("댓글 수정")
            .setView(et)
            .setPositiveButton("수정") { _, _ ->
                lifecycleScope.launch {
                    repository.updateComment(comment.id.toLong(), et.text.toString())
                    loadComments()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun deleteComment(comment: CommentModel) {
        lifecycleScope.launch {
            repository.deleteComment(comment.id.toLong())
            loadComments()
        }
    }

    private fun toggleLike(comment: CommentModel) {

        if (comment.id < 0) return

        val index = commentList.indexOfFirst { it.id == comment.id }
        if (index == -1) return

        val prev = commentList[index]

        // ⭐ 1. optimistic update
        val updated = prev.copy(
            isLiked = !prev.isLiked,
            likeCount = if (!prev.isLiked)
                prev.likeCount + 1
            else
                prev.likeCount - 1
        )

        commentList[index] = updated
        commentAdapter.submitList(commentList.toList())

        // ⭐ 2. 서버 요청
        lifecycleScope.launch {
            try {
                if (!prev.isLiked) {
                    repository.likeComment(prev.id.toLong())
                } else {
                    repository.cancelLikeComment(prev.id.toLong())
                }
            } catch (e: Exception) {
                // ⭐ 3. 실패 시 롤백
                commentList[index] = prev
                commentAdapter.submitList(commentList.toList())

                Toast.makeText(requireContext(), "좋아요 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToReplyThread(comment: CommentModel) {
        val action =
            CommunityDetailFragmentDirections
                .actionCommunityDetailFragmentToReplyThreadFragment(
                    postId = postId,
                    commentId = comment.id
                )
        findNavController().navigate(action)
    }
}
