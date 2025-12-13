package com.example.hearo2.dictionary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentDictionaryDetailBinding
import com.example.hearo2.dictionary.repository.SignRepository
import com.example.hearo2.dictionary.viewmodel.DictionaryDetailViewModel
import com.example.hearo2.dictionary.viewmodel.DictionaryDetailViewModelFactory
import com.example.hearo2.dictionary.viewmodel.DictionaryViewModel
import com.example.hearo2.dictionary.viewmodel.DictionaryViewModelFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class DictionaryDetailFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryDetailBinding
    private lateinit var viewModel: DictionaryDetailViewModel

    // ⭐ 목록 ViewModel 공유 (즐겨찾기 동기화용)
    private val listViewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory(requireActivity())
    }

    private var player: ExoPlayer? = null
    private var isFavorite = false
    private var signId: Int = -1
    private var currentVideoUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDictionaryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signId = arguments?.getInt("id") ?: return

        val repository = SignRepository(requireContext())
        val factory = DictionaryDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DictionaryDetailViewModel::class.java]

        observeViewModel()
        viewModel.loadDetail(signId)
        initListeners()
    }

    // --------------------------------------------------
    // ViewModel Observe
    // --------------------------------------------------
    private fun observeViewModel() {
        viewModel.detail.observe(viewLifecycleOwner) { data ->

            binding.tvWord.text = data.title
            binding.tvDescription.text = data.signDescription
            binding.tvTag.text = data.categoryType ?: "수어"
            binding.tvViews.text = "조회수: ${data.viewCount ?: 0}회"
            binding.tvCategory.text = "카테고리: ${data.categoryType ?: "정보 없음"}"

            // 즐겨찾기 상태
            isFavorite = data.favorite == true
            updateFavoriteIcon()

            // 영상 썸네일
            Glide.with(this)
                .load(data.thumbnailUrl)
                .placeholder(R.drawable.sample_reference)
                .into(binding.imgVideoThumbnail)

            // 참고 이미지 (첫 번째만)
            data.images?.firstOrNull()?.let {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.sample_reference)
                    .into(binding.imgReference)
            }

            currentVideoUrl = data.videoUrl
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "오류: $it", Toast.LENGTH_SHORT).show()
        }
    }

    // --------------------------------------------------
    // Listener
    // --------------------------------------------------
    private fun initListeners() {

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 즐겨찾기 토글
        binding.btnFavorite.setOnClickListener {
            val newState = !isFavorite

            viewModel.toggleFavorite(signId, newState) { success ->
                if (success) {
                    isFavorite = newState
                    updateFavoriteIcon()

                    // ⭐ 목록에도 즉시 반영
                    listViewModel.updateFavoriteStateFromDetail(signId, isFavorite)
                }
            }
        }

        // 영상 재생
        binding.imgVideoThumbnail.setOnClickListener { startVideo() }
        binding.btnReplay.setOnClickListener { startVideo() }

        // 외부 사전 열기
        binding.btnOpenDictionary.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://sldict.korean.go.kr/")
                )
            )
        }
    }

    // --------------------------------------------------
    // 🎬 영상 재생 (화면 안 보이던 문제 해결)
    // --------------------------------------------------
    private fun startVideo() {
        val url = currentVideoUrl ?: return

        binding.playerView.visibility = View.VISIBLE
        binding.imgVideoThumbnail.visibility = View.GONE
        binding.playerView.requestLayout()   // ⭐ 핵심

        if (player == null) {
            player = ExoPlayer.Builder(requireContext()).build()
            binding.playerView.player = player
        }

        player?.apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
    }

    private fun updateFavoriteIcon() {
        val icon =
            if (isFavorite) R.drawable.ic_heart_filled
            else R.drawable.ic_heart_empty
        binding.btnFavorite.setImageResource(icon)
    }

    // --------------------------------------------------
    // Lifecycle
    // --------------------------------------------------
    override fun onStop() {
        super.onStop()
        player?.pause()
        player?.clearVideoSurface()   // surface 꼬임 방지
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.playerView.player = null
        player?.release()
        player = null
    }
}
