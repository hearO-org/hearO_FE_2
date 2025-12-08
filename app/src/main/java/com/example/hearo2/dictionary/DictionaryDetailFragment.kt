package com.example.hearo2.dictionary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentDictionaryDetailBinding

class DictionaryDetailFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryDetailBinding
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDictionaryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        initListeners()
    }

    private fun loadData() {
        val title = arguments?.getString("title") ?: ""
        val desc = arguments?.getString("description") ?: ""
        val category = arguments?.getString("category") ?: ""
        val views = arguments?.getInt("views") ?: 0
        val imageRes = arguments?.getInt("imageRes") ?: R.drawable.sample_reference

        binding.tvWord.text = title
        binding.tvDescription.text = desc
        binding.tvTag.text = category
        binding.tvViews.text = "조회수: ${views}회"

        // 카드 내 이미지/영상 썸네일에 동일 이미지 사용
        binding.imgReference.setImageResource(imageRes)
        binding.imgVideoThumbnail.setImageResource(imageRes)
    }

    private fun initListeners() {

        // 뒤로가기
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 즐겨찾기 토글 (상세 화면에서만 UI용)
        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon()
        }

        // 영상 다시보기 (현재는 간단한 UI용 – 나중에 ExoPlayer 연결)
        binding.btnReplay.setOnClickListener {
            // TODO: ExoPlayer 연결 (지금은 UI만)
        }

        // 한국 수어 사전 사이트 열기
        binding.btnOpenDictionary.setOnClickListener {
            val url = "https://sldict.korean.go.kr/"   // 한국수어사전
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun updateFavoriteIcon() {
        val icon = if (isFavorite) {
            R.drawable.ic_heart_filled
        } else {
            R.drawable.ic_heart_empty
        }
        binding.btnFavorite.setImageResource(icon)
    }

    companion object {
        fun newInstance(
            title: String,
            description: String,
            category: String,
            views: Int,
            imageRes: Int
        ): DictionaryDetailFragment {

            val fragment = DictionaryDetailFragment()
            val bundle = Bundle().apply {
                putString("title", title)
                putString("description", description)
                putString("category", category)
                putInt("views", views)
                putInt("imageRes", imageRes)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
