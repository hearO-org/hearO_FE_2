package com.example.hearo2.custominfo

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.hearo2.R
import com.example.hearo2.custominfo.adapter.JobAdapter
import com.example.hearo2.custominfo.model.JobItem
import com.example.hearo2.custominfo.viewmodel.JobViewModel
import com.example.hearo2.custominfo.viewmodel.UiState
import com.example.hearo2.databinding.FragmentCustomInfoBinding

class CustomInfoFragment : Fragment() {

    private lateinit var binding: FragmentCustomInfoBinding
    private val viewModel: JobViewModel by viewModels()
    private lateinit var adapter: JobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupObservers()
        setupSearch()
        setupJobFilter()

        // 첫 화면 로딩
        viewModel.loadJobList(requireContext())
    }

    // ------------------------------
    // RecyclerView
    // ------------------------------
    private fun setupRecycler() {
        adapter = JobAdapter(emptyList()) { item -> openDetail(item) }
        binding.rvJobs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvJobs.adapter = adapter
    }

    // ------------------------------
    // ViewModel Observers
    // ------------------------------
    private fun setupObservers() {
        viewModel.jobListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoading(true)

                is UiState.Success -> {
                    showLoading(false)
                    adapter.updateList(state.data)
                }

                is UiState.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvJobs.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    // ------------------------------
    // Detail 이동
    // ------------------------------
    private fun openDetail(item: JobItem) {
        val action = CustomInfoFragmentDirections.actionCustomInfoToJobDetail(item.rno)
        findNavController().navigate(action)
    }

    // ------------------------------
    // 검색
    // ------------------------------
    private fun setupSearch() {
        binding.etJobSearch.setOnEditorActionListener { _, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch()
                true
            } else false
        }
    }

    private fun performSearch() {
        val query = binding.etJobSearch.text.toString().trim()

        if (query.isEmpty()) {
            viewModel.loadJobList(requireContext())
            return
        }

        val filters = mapOf("keyword" to query)
        viewModel.searchJobs(requireContext(), filters)
    }

    // ------------------------------
    // 필터 탭
    // ------------------------------
    private fun setupJobFilter() {

        val tabs = listOf(
            binding.tabAll,       // 전체
            binding.tabContract,  // 계약직
            binding.tabPermanent, // 상용직
            binding.tabPartTime   // 시간제
        )

        var selectedTab: TextView = binding.tabAll

        fun updateTabUI(selected: TextView) {
            tabs.forEach { tab ->
                if (tab == selected) {
                    tab.setBackgroundResource(R.drawable.keyword_selected)
                    tab.setTextColor(resources.getColor(android.R.color.white))
                } else {
                    tab.setBackgroundResource(R.drawable.keyword_unselected)
                    tab.setTextColor(resources.getColor(R.color.gray_700))
                }
            }
        }

        fun callFilterAPI(category: String) {
            if (category == "전체") {
                viewModel.loadJobList(requireContext())
            } else {
                val filterMap = mapOf("empType" to category)
                viewModel.searchJobs(requireContext(), filterMap)
            }
        }

        tabs.forEach { tab ->
            tab.setOnClickListener {
                selectedTab = tab
                updateTabUI(tab)
                callFilterAPI(tab.text.toString())
            }
        }

        updateTabUI(selectedTab)
    }
}
