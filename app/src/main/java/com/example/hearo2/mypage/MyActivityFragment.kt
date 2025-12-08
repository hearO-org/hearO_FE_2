package com.example.hearo2.mypage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentMyActivityBinding

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

        /** 👉 기본 화면: 북마크 */
        showBookmark()

        /** 👉 탭 클릭 이벤트 */
        binding.tabBookmark.setOnClickListener { showBookmark() }
        binding.tabLike.setOnClickListener { showLike() }
        binding.tabComment.setOnClickListener { showComment() }
    }

    /** 🔹 탭 선택 UI 변경 */
    private fun selectTab(selected: TextView, others: List<TextView>) {
        selected.setBackgroundResource(R.drawable.tab_selected_bg)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))

        others.forEach {
            it.setBackgroundResource(R.drawable.tab_unselected_bg)
            it.setTextColor(Color.parseColor("#666666"))
        }
    }

    /** 🔹 Fragment 전환 (Fade 애니메이션 포함) */
    private fun fadeChange(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.contentContainer, fragment)
            .commit()
    }

    /** 🔹 북마크 탭 */
    private fun showBookmark() {
        selectTab(binding.tabBookmark, listOf(binding.tabLike, binding.tabComment))
        fadeChange(BookmarkFragment())
    }

    /** 🔹 좋아요 탭 */
    private fun showLike() {
        selectTab(binding.tabLike, listOf(binding.tabBookmark, binding.tabComment))
        fadeChange(LikeFragment())
    }

    /** 🔹 댓글 탭 */
    private fun showComment() {
        selectTab(binding.tabComment, listOf(binding.tabBookmark, binding.tabLike))
        fadeChange(CommentFragment())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
