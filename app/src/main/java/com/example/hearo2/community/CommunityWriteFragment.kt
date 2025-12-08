package com.example.hearo2.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hearo2.databinding.FragmentCommunityWriteBinding

class CommunityWriteFragment : Fragment() {

    private var _binding: FragmentCommunityWriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSubmitPost.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            if (title.isBlank() || content.isBlank()) return@setOnClickListener

            PostRepository.addPost(
                category = "자유",
                title = title,
                content = content
            )

            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
