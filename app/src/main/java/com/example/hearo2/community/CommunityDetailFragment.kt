package com.example.hearo2.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.databinding.FragmentCommunityDetailBinding

class CommunityDetailFragment : Fragment() {

    private var _binding: FragmentCommunityDetailBinding? = null
    private val binding get() = _binding!!
    private var postId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = arguments?.getInt("postId") ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCommunityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val post = PostRepository.getPostById(postId) ?: return

        binding.postTitle.text = post.title
        binding.postContent.text = post.content

        val comments = CommentRepository.getComments(postId)

        val adapter = CommentAdapter(comments) { comment ->
            val action =
                CommunityDetailFragmentDirections.actionCommunityDetailFragmentToReplyThreadFragment(
                    comment.id
                )
            findNavController().navigate(action)
        }

        binding.recyclerComment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        binding.btnSendComment.setOnClickListener {
            val text = binding.etComment.text.toString()

            if (text.isNotBlank()) {
                CommentRepository.addComment(postId, text)
                binding.etComment.setText("")
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
