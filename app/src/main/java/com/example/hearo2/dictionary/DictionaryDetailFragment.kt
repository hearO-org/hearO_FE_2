package com.example.hearo2.dictionary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentDictionaryDetailBinding
import com.example.hearo2.dictionary.repository.SignRepository
import com.example.hearo2.dictionary.viewmodel.DictionaryDetailViewModel
import com.example.hearo2.dictionary.viewmodel.DictionaryDetailViewModelFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class DictionaryDetailFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryDetailBinding
    private lateinit var viewModel: DictionaryDetailViewModel

    private var player: ExoPlayer? = null
    private var isFavorite = false
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

        val repository = SignRepository(requireContext())
        val factory = DictionaryDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DictionaryDetailViewModel::class.java]

        val id = arguments?.getInt("id") ?: return

        observeViewModel()
        viewModel.loadDetail(id)
        initListeners()
    }

    private fun observeViewModel() {
        viewModel.detail.observe(viewLifecycleOwner) { data ->

            binding.tvWord.text = data.title
            binding.tvDescription.text = data.signDescription
            binding.tvTag.text = data.categoryType ?: "수어"
            binding.tvViews.text = "조회수: ${data.viewCount ?: 0}회"
            binding.tvCategory.text = "카테고리: ${data.categoryType ?: "정보 없음"}"

            Glide.with(this)
                .load(data.thumbnailUrl)
                .placeholder(R.drawable.sample_reference)
                .into(binding.imgVideoThumbnail)

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

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon()
        }

        binding.imgVideoThumbnail.setOnClickListener { startVideo() }
        binding.btnReplay.setOnClickListener { startVideo() }

        binding.btnOpenDictionary.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://sldict.korean.go.kr/")))
        }
    }

    private fun startVideo() {
        val url = currentVideoUrl ?: return

        binding.imgVideoThumbnail.visibility = View.GONE
        binding.playerView.visibility = View.VISIBLE

        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            exoPlayer.setMediaItem(MediaItem.fromUri(url))
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    private fun updateFavoriteIcon() {
        val icon = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_empty
        binding.btnFavorite.setImageResource(icon)
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
    }
}
