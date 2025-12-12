package com.example.hearo2.mypage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentMyActivityBinding
import com.example.hearo2.mypage.MyCommentFragment


class MyActivityFragment : Fragment() {

    private var _binding: FragmentMyActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 🔙 뒤로가기
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 기본 탭
        showBookmark()

        binding.tabBookmark.setOnClickListener { showBookmark() }
        binding.tabLike.setOnClickListener { showLike() }
        binding.tabComment.setOnClickListener { showComment() }
        binding.tabMyPost.setOnClickListener { showMyPost() }
    }

    private fun selectTab(selected: TextView, others: List<TextView>) {
        selected.setBackgroundResource(R.drawable.tab_selected_bg)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))

        others.forEach {
            it.setBackgroundResource(R.drawable.tab_unselected_bg)
            it.setTextColor(Color.parseColor("#666666"))
        }
    }

    private fun fadeChange(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.contentContainer, fragment)
            .commit()
    }

    private fun showBookmark() {
        selectTab(
            binding.tabBookmark,
            listOf(binding.tabLike, binding.tabComment, binding.tabMyPost)
        )
        fadeChange(BookmarkFragment())
    }

    private fun showLike() {
        selectTab(
            binding.tabLike,
            listOf(binding.tabBookmark, binding.tabComment, binding.tabMyPost)
        )
        fadeChange(SignLikeFragment())
    }

    private fun showComment() {
        selectTab(
            binding.tabComment,
            listOf(binding.tabBookmark, binding.tabLike, binding.tabMyPost)
        )
        fadeChange(MyCommentFragment())
    }

    private fun showMyPost() {
        selectTab(
            binding.tabMyPost,
            listOf(binding.tabBookmark, binding.tabLike, binding.tabComment)
        )
        fadeChange(MyPostFragment())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
