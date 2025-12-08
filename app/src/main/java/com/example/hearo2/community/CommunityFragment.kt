package com.example.hearo2.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // RecyclerView 연결
        val adapter = PostAdapter(PostRepository.posts) { selectedPost ->
            val action =
                CommunityFragmentDirections.actionCommunityFragmentToCommunityDetailFragment(
                    selectedPost.id
                )
            findNavController().navigate(action)
        }

        binding.recyclerPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        // 글쓰기 버튼 → WriteFragment 이동
        binding.fabWrite.setOnClickListener {
            findNavController().navigate(
                CommunityFragmentDirections.actionCommunityFragmentToCommunityWriteFragment()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerPost.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
