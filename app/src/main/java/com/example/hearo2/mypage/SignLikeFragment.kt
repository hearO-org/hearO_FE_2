package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentSignLikeBinding
import com.example.hearo2.dictionary.repository.SignRepository
import com.example.hearo2.mypage.adapter.SignLikeAdapter

class SignLikeFragment : Fragment() {

    private var _binding: FragmentSignLikeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignLikeViewModel by viewModels {
        SignLikeViewModel.Factory(SignRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ✅ 이름을 signLikeAdapter 로 변경
        val signLikeAdapter = SignLikeAdapter { signItem ->
            findNavController().navigate(
                R.id.dictionaryDetailFragment,
                bundleOf("id" to signItem.id)
            )
        }

        binding.recyclerLikeSigns.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerLikeSigns.adapter = signLikeAdapter

        viewModel.favoriteSigns.observe(viewLifecycleOwner) { list ->
            signLikeAdapter.submitList(list)
        }

        viewModel.loadFavoriteSigns()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
