package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentMyPostBinding
import com.example.hearo2.mypage.adapter.MyPostAdapter
import com.example.hearo2.mypage.viewmodel.MyPostViewModel

class MyPostFragment : Fragment() {

    private var _binding: FragmentMyPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = MyPostAdapter { post ->
            val bundle = Bundle().apply {
                putInt("postId", post.id.toInt())
            }
            findNavController().navigate(
                R.id.communityDetailFragment,
                bundle
            )
        }

        binding.recyclerMyPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadMyPosts()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
