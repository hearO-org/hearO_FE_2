package com.example.hearo2.mypage

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.databinding.FragmentCommentBinding
import com.example.hearo2.mypage.adapter.MyCommentAdapter
import com.example.hearo2.mypage.MyCommentViewModel

class MyCommentFragment : Fragment() {

    private lateinit var binding: FragmentCommentBinding
    private val viewModel: MyCommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = MyCommentAdapter()

        binding.recyclerComment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.comments.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadMyComments()
    }
}
