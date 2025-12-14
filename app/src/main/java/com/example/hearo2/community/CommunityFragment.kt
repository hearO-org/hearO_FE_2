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
    private var selectedCategory: String? = null

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
        setupCategoryTabs()

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

    private fun setupCategoryTabs() {
        val tabs = listOf(
            binding.tabAll to null,
            binding.tabGeneral to "GENERAL",
            binding.tabQuestion to "QUESTION",
            binding.tabReview to "REVIEW",
            binding.tabSign to "SIGN_INFO",
            binding.tabSafety to "SAFETY",
            binding.tabPolicy to "POLICY",
            binding.tabJob to "JOB"
        )

        tabs.forEach { (view, category) ->
            view.setOnClickListener {
                selectedCategory = category
                updateTabUI(view)
                loadPostList()
            }
        }

        binding.tabAll.isSelected = true
    }

    private fun updateTabUI(selected: View) {
        val tabs = listOf(
            binding.tabAll,
            binding.tabGeneral,
            binding.tabQuestion,
            binding.tabReview,
            binding.tabSign,
            binding.tabSafety,
            binding.tabPolicy,
            binding.tabJob
        )

        tabs.forEach {
            it.isSelected = (it == selected)
        }
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
                val res =
                    if (selectedCategory == null)
                        repository.loadPostList(0, 20)
                    else
                        repository.searchPosts(
                            query = null,
                            category = selectedCategory,
                            tag = null,
                            page = 0,
                            size = 20
                        )

                if (res.success) {
                    adapter.submitList(res.data.content)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
