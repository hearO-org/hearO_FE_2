package com.example.hearo2.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 🔥 Adapter 생성 + 클릭 시 상세보기 이동 구현
        val adapter = SignLikeAdapter { signItem ->

            // 🔥 DictionaryDetailFragment 로 이동 (id 전달)
            val action = MyActivityFragmentDirections
                .actionMyActivityToDictionaryDetailFragment(signItem.id)

            findNavController().navigate(action)
        }

        binding.recyclerLikeSigns.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerLikeSigns.adapter = adapter

        // LiveData 관찰
        viewModel.favoriteSigns.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.loadFavoriteSigns()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
