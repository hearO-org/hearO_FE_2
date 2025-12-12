package com.example.hearo2.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.community.repository.PostRepository
import com.example.hearo2.databinding.FragmentCommunityBinding
import kotlinx.coroutines.launch

class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var adapter: PostAdapter
    private val repository = PostRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadPostList()

        binding.fabWrite.setOnClickListener {
            findNavController().navigate(
                CommunityFragmentDirections.actionCommunityToWrite()
            )
        }
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter(
            onPostClick = { post ->
                val action =
                    CommunityFragmentDirections.actionCommunityToDetail(post.id.toInt())
                findNavController().navigate(action)
            },
            onScrapClick = { post ->
                toggleScrap(post)
            }
        )

        binding.recyclerPost.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPost.adapter = adapter
    }

    /** ⭐ 스크랩 토글 */
    private fun toggleScrap(post: PostModel) {
        lifecycleScope.launch {
            try {
                val res =
                    if (!post.scrapped)
                        repository.scrap(post.id)
                    else
                        repository.cancelScrap(post.id)

                val newPost = post.copy(
                    scrapped = res.data.likedOrScrapped
                )

                val newList = adapter.currentList.toMutableList()
                val index = newList.indexOfFirst { it.id == post.id }

                if (index != -1) {
                    newList[index] = newPost
                    adapter.submitList(newList)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /** ⭐ 게시글 목록 조회 */
    private fun loadPostList() {
        lifecycleScope.launch {
            try {
                val response = repository.loadPostList(page = 0, size = 20)
                if (response.success) {
                    adapter.submitList(response.data.content)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
